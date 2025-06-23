import java.awt.*;
import java.awt.event.*;


import javax.swing.*;

public class furniture extends JLabel implements KeyListener{
    private int initialX;
    private int initialY;
    public int xP;
    public int yP;
    public int furntype;
    public String furntype_str;
    public int rotation = 0;
    private transient Image image;

    public furniture(int furntype) {
        this.furntype = furntype;
        this.setSize(40, 40);
        this.setOpaque(false);

        // Load and set the image based on furniture type
        loadImageBasedOnType();

        // Set focusable and add KeyListener to capture rotation keys
        this.setFocusable(true);
        this.addKeyListener(this);

        // Request focus when clicked, so it can receive key events
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
                App.getInstance().setsavestatus(false);
            }
            public void mouseReleased(MouseEvent e) {
                if (!furnValid(furniture.this)) {

                    // CanvasPanel.showWarningDialog("Furniture already Exists here..", 300);
                    setLocation(initialX, initialY);

                }
                revalidate();
                repaint(); // Keep the selection color on repaint
                App.getInstance().setsavestatus(false);
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Room room = (Room) furniture.this.getParent();
        
                int rawX = getX() + e.getX() - xP;
                int rawY = getY() + e.getY() - yP;
        
                // Clamp values within room bounds
                int maxX = room.getWidth() - getWidth();
                int maxY = room.getHeight() - getHeight();
                int clampedX = Math.max(0, Math.min(rawX, maxX));
                int clampedY = Math.max(0, Math.min(rawY, maxY));
        
                if (room.selection && furnValid(furniture.this)) {
                    setLocation(clampedX, clampedY);
                    room.repaint();
                }
                App.getInstance().setsavestatus(false);
            }
        });
        App.getInstance().setsavestatus(false);
        
    }

    private void loadImageBasedOnType() {
        // Load images based on furniture type, replacing paths with your file locations
        ImageIcon icon = switch (furntype) {
            case 0 -> new ImageIcon("lib\\bed.png");
            case 1 -> new ImageIcon("lib\\commode.png");
            case 2 -> new ImageIcon("lib\\chair.png");
            case 3 -> new ImageIcon("lib\\sink.png");
            case 4 -> new ImageIcon("lib\\table.png");
            case 5 -> new ImageIcon("lib\\shower.png");
            case 6 -> new ImageIcon("lib\\sofa.png");
            case 7 -> new ImageIcon("lib\\sink.png");
            case 8 -> new ImageIcon("lib\\Diningset.png");
            case 9 -> new ImageIcon("lib\\stove.png");
            default -> null;
        };
        if (icon != null) {
            image = icon.getImage();
        }
    }
    private boolean furnValid(furniture furn) {
        Rectangle f1 = new Rectangle(furn.getBounds()); // Create a rectangle for the new furniture
        boolean isValid = true; // Assume valid unless an intersection is found

        for (Room room : CanvasPanel.RoomsList) {
            if (room.selection) { // Check if the room is selected
                for (furniture furn2 : room.furnList) {
                    if (furn == furn2) {
                        continue; // Skip the same furniture object
                    }

                    Rectangle f2 = new Rectangle(furn2.getBounds()); // Create a rectangle for existing furniture

                    if (f1.intersects(f2)) { // Check for intersection
                        isValid = false; // Found an intersection, set isValid to false
                        break; // No need to check further
                    }
                }
                if (!isValid) { // If an intersection was found, no need to check other rooms
                    break;
                }
            }
        }

        return isValid; // Return whether the furniture placement is valid
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Update rotation based on arrow keys
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> rotation += 90; // Rotate clockwise
            case KeyEvent.VK_LEFT -> rotation -= 90;  // Rotate counterclockwise
        }
        App.getInstance().setsavestatus(false);
        repaint(); // Repaint to apply rotation
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Clear previous painting
        super.paintComponent(g);
        
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Center of rotation
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            
            // Apply rotation around the center
            g2d.rotate(Math.toRadians(rotation), centerX, centerY);

            // Draw the rotated image centered in the JLabel
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);

            g2d.dispose();
        }
    }

    // Unused KeyListener methods
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    public furniture clone() {
        // Create a new instance with the same type and properties
        furniture newFurn = new furniture(this.furntype);
        // Set other properties as needed
        newFurn.setSize(this.getSize());
        newFurn.setLocation(this.getLocation());
        return newFurn;
    }
}