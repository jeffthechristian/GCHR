package Code;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatPanel implements ActionListener {
	private List<Question> questions;
	private String[] replies = new String[] { "Sorry, but I do not know the answer to that question",
			"I don't understand", "What do you mean?", "Can you please ask me another question?", "I can't answer that",
			"Please ask me something else" };
	private JTextArea chatBox;
	private JTextField messageField;
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private LocalDateTime now = LocalDateTime.now();

	private final static Logger warningLogger = Logger.getLogger("LogFileMonkey.log");

	public ChatPanel(JFrame mainFrame) throws SecurityException, IOException {

		// Adding logger
		FileHandler wLog = new FileHandler("LogFileMonkey.log");
		wLog.setLevel(Level.INFO);
		warningLogger.addHandler(wLog);

		// Adding Chat With Monkey frame
		questions = loadQuestions("resources/texts/chat.txt");
		JFrame frame = new JFrame();
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setTitle("Chat With Monkey");
		frame.setSize(new Dimension(500, 450));
		frame.setResizable(false);

		JPanel titlePanel = new JPanel();
		titlePanel.setSize(new Dimension(500, 50));
		JLabel titleLabel = new JLabel("Monkey Chat", new ImageIcon("resources/icons/iconLogo94.png"), JLabel.CENTER);
		titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		titlePanel.add(titleLabel);

		// Chat panel
		JPanel chatPanel = new JPanel();
		chatPanel.setSize(new Dimension(500, 50));
		chatBox = new JTextArea(14, 40);
		chatBox.append(dtf.format(now) + " Monkey: Hello\n");
		chatBox.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(chatBox);
		chatPanel.add(scrollPane);

		// Ability to scroll chat history
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
		});

		// Send panel
		JPanel sendPanel = new JPanel();
		messageField = new JTextField(20);
		messageField.setSize(new Dimension(50, 50));
		messageField.setPreferredSize(new Dimension(30, 26));
		messageField.addActionListener(this);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(this);
		sendPanel.add(messageField);
		sendPanel.add(sendButton);

		frame.add(titlePanel);
		frame.add(chatPanel);
		frame.add(sendPanel);

		// Closing reopens main frame
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				mainFrame.setVisible(true);
			}
		});
		// disables main frame when monkey frame is opened
		mainFrame.setVisible(false);
		// monkey frame is set to visible
		frame.setVisible(true);
	}

	// Load questions from file
	private static List<Question> loadQuestions(String filename) {
		List<Question> questions = new ArrayList<Question>();
		try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] data = line.split(";");
				questions.add(new Question(data[0], data[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			warningLogger.log(Level.WARNING, "Unable to load questions!", e);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return questions;
	}

	// Chat
	@Override
	public void actionPerformed(ActionEvent e) {
		String question = messageField.getText();

		if (question.equals("")) {
			chatBox.append(dtf.format(now) + " Monkey: Please ask me question\n");
			warningLogger.log(Level.INFO, "No question detected!", e);
		} else {
			messageField.setText("");
			chatBox.append(dtf.format(now) + " You: " + question + "\n");
			for (int i = 0; i < questions.size(); i++) {
				if (questions.get(i).getQuestion().equalsIgnoreCase(question)) {
					chatBox.append(dtf.format(now) + " Monkey: " + questions.get(i).getAnswer() + "\n");
					return;
				}
			}
			chatBox.append(dtf.format(now) + " Monkey: " + replies[(int) (Math.random() * replies.length)] + "\n");
		}

	}
}
