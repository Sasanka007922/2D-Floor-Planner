import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButtonPopup extends JPanel {

    public ButtonPopup(ArrayList<AdaptiveRoundButtons> buttons, int rows, int cols) {
        setLayout(new GridLayout(rows, cols)); // rows x cols grid with spacing
        setBackground(Color.BLACK); // Background for dark look

        for (AdaptiveRoundButtons button : buttons) {
            button.setFocusable(false);
            button.setBackground(Color.DARK_GRAY); // Button background
            button.setForeground(Color.WHITE);     // Text color
            button.setBorderPainted(false);        // No border
            add(button);                           // Add button to panel
        }
    }
}
