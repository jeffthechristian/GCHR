package Code;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class MainPanel implements ActionListener {
	private static final String RESOURCE_PATH = "resources/";
	private static final String ICON_PATH = RESOURCE_PATH + "Icons/";
	private static final String TEXT_PATH = RESOURCE_PATH + "Texts/";
	private static final String FILE_LOCATION = "C:\\Users\\" + System.getProperty("user.name")
			+ "\\AppData\\Local\\Google\\Chrome\\User Data\\Default";
	private final HashMap<Object, ColorConfiguration> colorMenu = new HashMap<>();
	private ColorConfiguration currentConfiguration;

	private final JFrame mainFrame;
	private JButton mButtons;
	private final JLabel textLabel;
	private final JLabel imageLabel;
	private final JTextArea textConsole;

	private final static Logger infoLogger = Logger.getLogger("LogFileInfo.log");
	private final static Logger warningLogger = Logger.getLogger("LogFileWarning.log");

	MainPanel() throws SecurityException, IOException {

		// Adding loggers
		LogManager.getLogManager().reset();

		FileHandler iLog = new FileHandler("LogFileInfo.log");
		iLog.setLevel(Level.INFO);
		infoLogger.addHandler(iLog);

		FileHandler wLog = new FileHandler("LogFileWarning.log");
		wLog.setLevel(Level.WARNING);
		warningLogger.addHandler(wLog);

		// Adding icons
		ImageIcon imageIcon16 = new ImageIcon(ICON_PATH + "iconLogo16.png");
		ImageIcon imageIcon94 = new ImageIcon(ICON_PATH + "iconLogo94.png");
		ImageIcon imageIconSad94 = new ImageIcon(ICON_PATH + "iconLogoSad94.png");
		ImageIcon imageIconHot94 = new ImageIcon(ICON_PATH + "iconLogoHot94.png");
		ImageIcon imageIconBanana94 = new ImageIcon(ICON_PATH + "iconLogoBanana94.png");

		// Making main frame
		this.mainFrame = new JFrame("Google Chrome History Remover");
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setSize(600, 450);
		this.mainFrame.setResizable(false);
		this.mainFrame.setLayout(null);
		this.mainFrame.getContentPane().setBackground(Color.MAGENTA);
		this.mainFrame.setIconImage(imageIcon16.getImage());

		this.textLabel = new JLabel("Welcome to GCHR !", JLabel.CENTER);
		this.textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 21));
		this.textLabel.setBounds(73, 0, 200, 50);
		this.textLabel.setForeground(Color.BLACK);
		this.mainFrame.add(this.textLabel);

		this.imageLabel = new JLabel();
		this.imageLabel.setBounds(5, 5, 68, 68);
		this.imageLabel.setIcon(imageIcon94);

		this.mainFrame.add(this.imageLabel);

		// Background color
		List<ColorConfiguration> colorConfigurations = new ArrayList<>();
		colorConfigurations.add(new ColorConfiguration("Orange", "BANANA COLOR !", Color.ORANGE, imageIconBanana94));
		colorConfigurations.add(new ColorConfiguration("Cyan", "GCHR", Color.CYAN, imageIcon94));
		colorConfigurations.add(new ColorConfiguration("Light Gray", "Monkey sad :(", Color.lightGray, imageIconSad94));
		colorConfigurations.add(new ColorConfiguration("Blue", "GCHR", Color.BLUE, imageIcon94));
		colorConfigurations.add(new ColorConfiguration("Red", "Monkey feels hot !", Color.RED, imageIconHot94));
		colorConfigurations.add(new ColorConfiguration("Green", "GCHR", Color.GREEN, imageIcon94));
		colorConfigurations.add(new ColorConfiguration("Magenta", "GCHR", Color.MAGENTA, imageIcon94));

		JMenu colorMenu = new JMenu("Background Color");

		for (ColorConfiguration configuration : colorConfigurations) {
			JMenuItem item = new JMenuItem(configuration.name);
			item.addActionListener(e -> colorChange(e));
			colorMenu.add(item);
			this.colorMenu.put(item, configuration);
		}
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(colorMenu);
		mainFrame.setJMenuBar(menuBar);

		// Info
		JTextArea textArea = new JTextArea();
		textArea.setText(loadTextFromFile(new File(TEXT_PATH + "explanation.txt")));
		textArea.setFont(new Font("ARIEL", Font.PLAIN, 12));
		textArea.setBorder(BorderFactory.createEmptyBorder());
		textArea.setForeground(Color.BLACK);
		textArea.setBackground(Color.PINK);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(257, 137));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel infoText = new JPanel();
		infoText.setBounds(300, 20, 260, 140);
		infoText.setBackground(Color.PINK);
		infoText.setBorder(BorderFactory.createEmptyBorder());
		infoText.add(scrollPane);
		this.mainFrame.add(infoText);

		// Console
		this.textConsole = new JTextArea();
		this.textConsole.setLineWrap(true);
		this.textConsole.setWrapStyleWord(true);
		this.textConsole.setBackground(Color.PINK);
		this.textConsole.setBorder(BorderFactory.createBevelBorder(1));
		this.textConsole.setForeground(Color.BLACK);
		this.textConsole.setFont(new Font("ARIEL", Font.PLAIN, 12));
		this.textConsole.setEditable(false);

		JScrollPane scrollConsole = new JScrollPane(textConsole);
		scrollConsole.setPreferredSize(new Dimension(257, 200));
		scrollConsole.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel console = new JPanel();
		console.setBounds(300, 180, 257, 200);
		console.add(scrollConsole);
		this.mainFrame.add(console);

		// Buttons
		String[] buttons = { "Delete History", "Delete Cache", "Monkey", "Exit" };
		int garums = 100;
		int starpa = 50;
		int amount = 0;
		for (int i = 0; i < buttons.length; i++) {
			mButtons = new JButton(buttons[i]);
			mButtons.setFont(new Font("Times New Roman", Font.BOLD, 19));
			mButtons.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			mButtons.setBounds(60, 20 + (garums + starpa * i), 200, 40);
			mButtons.setForeground(Color.BLACK);
			mButtons.setBackground(Color.WHITE);
			mButtons.addActionListener(this);
			mainFrame.add(mButtons);
			amount++;
			infoLogger.log(Level.INFO, "Button: " + amount + " = successful");
		}

		this.mainFrame.setVisible(true);
	}

	// Background color
	public void colorChange(ActionEvent e) {
		ColorConfiguration configuration = this.currentConfiguration = this.colorMenu.get(e.getSource());
		this.mainFrame.getContentPane().setBackground(configuration.color);
		this.textLabel.setText(configuration.text);
		this.imageLabel.setIcon(configuration.icon);
	}

	// Removing history/cache files
	public void removeFilesInDirectory(File directory, String name) {
		try {
			if (!directory.isDirectory()) {
				this.textConsole.append("There is nothing to remove!\n");
				warningLogger.log(Level.WARNING, "Nothing to remove (history button/cache button)!");
			} else {
				File[] listFiles = directory.listFiles();
				for (File file : listFiles) {
					this.textConsole.append("Deleting:\n" + file.getName());
					file.delete();
				}
				this.textConsole.append("\n\n Successfully removed your " + name + " files!\n");
			}
		} catch (Exception e) {
			warningLogger.log(Level.WARNING, "removeFilesInDirectory is not working", e);
		}
	}

	// Loading texts from file
	public String loadTextFromFile(File file) {
		StringBuilder builder = new StringBuilder();
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				builder.append(scanner.nextLine()).append('\n');
			}
		} catch (FileNotFoundException e) {
			warningLogger.log(Level.WARNING, "loadTextFromFile is not working", e);
		}
		return builder.toString();
	}

	// Buttons
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			String buttonText = ((JButton) e.getSource()).getText();
			switch (buttonText) {
				case "Delete History":
					removeFilesInDirectory(new File(FILE_LOCATION), "history");
					infoLogger.log(Level.INFO, "Delete History button works just fine like a wine!");
					break;
				case "Delete Cache":
					removeFilesInDirectory(new File(FILE_LOCATION + "\\Cache"), "cache");
					infoLogger.log(Level.INFO, "Delete Cache button works just fine like a wine!");
					break;
				case "Monkey":
					try {
						new ChatPanel(mainFrame);
						// do not touch 233, 234, 235
					} catch (SecurityException | IOException e1) {
						e1.printStackTrace();
					}
					infoLogger.log(Level.INFO, "Monkey button works just fine like a wine!");
					break;
				case "Exit":
					System.exit(0);
					infoLogger.log(Level.INFO, "Exit button works just fine like a wine!");
					break;
			}
		}
	}
}
// it works :O
