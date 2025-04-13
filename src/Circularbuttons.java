import javax.swing.*;
import java.awt.*;

public class Circularbuttons extends JButton {
    private final int diameter;

    public Circularbuttons(int diameter, String text) {
        super(text);
        this.diameter = diameter;
        setFocusable(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, diameter / 3));
    }

    @Override
protected void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(getModel().isPressed() ? Color.GRAY : Color.DARK_GRAY);
    g2.fillOval(0, 0, diameter, diameter);

    g2.setColor(getForeground());
    FontMetrics fm = g2.getFontMetrics();
    int x = (diameter - fm.stringWidth(getText())) / 2;
    int y = (diameter + fm.getAscent()) / 2 - 3;
    g2.drawString(getText(), x, y);

    g2.dispose();
}


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(diameter, diameter);
    }
}
