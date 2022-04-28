package Code;

import javax.swing.*;
import java.awt.*;

public class ColorConfiguration {

    public String name;
    public String text;
    public Color color;
    public ImageIcon icon;

    public ColorConfiguration(String name, String text, Color color, ImageIcon icon) {
        this.name = name;
        this.text = text;
        this.color = color;
        this.icon = icon;
    }
}
