import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdaptiveNormalButtons extends JButton {

    private final Color normalBackground = Color.BLACK;
    private final Color normalForeground = Color.WHITE;
    private final Font monospacedFont;
    private final Border hoverBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
    private final Border noBorder = BorderFactory.createEmptyBorder(); // effectively removes border

    public AdaptiveNormalButtons(int fontSize, String text) {
        super(text);

        monospacedFont = new Font("Monospaced", Font.BOLD, fontSize);

        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);
        setBackground(normalBackground);
        setForeground(normalForeground);
        setFont(monospacedFont);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(noBorder); // Start with no border

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBorder(hoverBorder);
            }

            public void mouseExited(MouseEvent e) {
                setBorder(noBorder);
            }

            // public void mouseReleased(MouseEvent e) {
            //     setBackground(hoverBackground);
            // }
        });
    }
}
