import javax.swing.*;
import java.awt.*;

public class trash extends JLabel {

    public trash() {
        setPreferredSize(new Dimension(60, 60));
        setOpaque(true);
        setBackground(new Color(30, 30, 30));
        setForeground(Color.WHITE);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setText("Trash"); // Unicode trash can emoji

        setFont(new Font("Segoe UI", Font.PLAIN, 28));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // You can customize this if you want to draw a custom trash icon
    }

    public boolean contains(Point point) {
        return getBounds().contains(point);
    }
}
