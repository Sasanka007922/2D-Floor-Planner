import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class AdaptiveRoundButtons extends JButton {
    private Color normalColor = Color.BLACK;
    private Color hoverColor = new Color(30, 30, 30);
    private Color currentColor = normalColor;

    public AdaptiveRoundButtons(ArrayList<AdaptiveRoundButtons> group, String text) {
        super(text);

        setFocusable(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Monospaced", Font.BOLD, 15)); // ✅ Monospaced
        setPreferredSize(new Dimension(100, 40));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (group != null) {
            group.add(this);
        }

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                currentColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                currentColor = normalColor;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2.setColor(currentColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

       

        // Draw text centered
        FontMetrics fm = g2.getFontMetrics();
        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getAscent();
        int x = (getWidth() - stringWidth) / 2;
        int y = (getHeight() + stringHeight) / 2 - 3;

        g2.setColor(getForeground());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}
