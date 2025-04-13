import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;
// import java.io.Serializable;

public class CanvasPanel extends JPanel {
    
    public static ArrayList<Room> RoomsList = new ArrayList<>();
    public boolean gridOn = true;
    int gridSize;
    int gridSpacing;
    public static boolean gridSnap = false;
    public static boolean wallSnap = false;
    public CanvasPanel() {
        this.setBackground(Color.LIGHT_GRAY);
        this.setLayout(null);
        this.setVisible(true);
        this.setOpaque(true);
    }

    public void addRoom(int roomtype) {
        Room room = new Room(roomtype);
    
        int canvasWidth = (int) (7 * this.getWidth() / 8);
        int canvasHeight = this.getHeight() - 60;
    
        // Check if room exceeds canvas dimensions
        if (room.getWidth() > canvasWidth || room.getHeight() > canvasHeight) {
            showWarningDialog("No space for rooms of this size.", 350);
            return; 
        }
    
        int x = 10;
        int y = 10;
        boolean placed = false;
    
        while (!placed) {
            for (x = 10; x + room.getWidth() <= canvasWidth; x += 15) {
                room.setLocation(x, y);
    
                if (!Validator(room)) {
                    placed = true;
                    break;
                }
            }
    
            if (!placed) {
                y += 15;
    
                if (y + room.getHeight() >= canvasHeight) {
                    break;
                }
            }
        }
    
        if (placed) {
            RoomsList.add(room);
            this.add(room, JLayeredPane.DEFAULT_LAYER);
            this.repaint();
            this.revalidate();
        } else {
            showWarningDialog("No space for rooms of this size.", 350);
        }
        switch (room.roomtype) {
            case 0 -> room.roomtype_str = "Bedroom";
              
            case 1 -> room.roomtype_str = "Kitchen";
              
            case 2 -> room.roomtype_str = "Living Room";
                
            case 3 -> room.roomtype_str = "Dining Room";
               
            case 4 -> room.roomtype_str = "Bathroom";
              
        }
    }
    

    public void addRoomRelative(String direction, String alignment, int roomType) {
        System.out.println("Adding room in direction: " + direction + " with alignment: " + alignment);
        
        int newRoomWidth = (int) (App.getSize(this).getWidth()); 
        int newRoomHeight = (int) (App.getSize(this).getHeight()); 
    
        if (direction == null || alignment == null) {
            System.out.println("Direction or alignment is null.");
            return;
        }
        
        // Ensure room dimensions are valid
        if (newRoomWidth <= 0 || newRoomHeight <= 0) {
            showWarningDialog("Invalid Room Dimension", 300);
            return; // Exit early if dimensions are invalid
        }
    
        // Create the new room with the specified room type
        Room newRoom = new Room(roomType);
        System.out.println("New room created: " + newRoom);
    
        int selectedRoomX = 0; 
        int selectedRoomY = 0;
        int selectedRoomWidth = 0;
        int selectedRoomHeight = 0;
        // boolean roomSelected = false;
    
        for (Room room : RoomsList) { 
            if (room.selection) {
                selectedRoomX = room.getX();
                selectedRoomY = room.getY();
                selectedRoomWidth = room.getWidth();
                selectedRoomHeight = room.getHeight();
                // roomSelected = true;
                break;
            }
        }
    
        
    
        // Set the location of the new room based on the direction and alignment
        switch (alignment) {
            case "Left":
                if (direction.equals("North")) {
                    newRoom.setLocation(selectedRoomX, selectedRoomY - newRoomHeight);
                } else if (direction.equals("South")) {
                    newRoom.setLocation(selectedRoomX, selectedRoomY + selectedRoomHeight);
                }
                break;
    
            case "Center":
                if (direction.equals("North")) {
                    newRoom.setLocation(selectedRoomX + (selectedRoomWidth / 2) - (newRoomWidth / 2), selectedRoomY - newRoomHeight);
                } else if (direction.equals("South")) {
                    newRoom.setLocation(selectedRoomX + (selectedRoomWidth / 2) - (newRoomWidth / 2), selectedRoomY + selectedRoomHeight);
                } else if (direction.equals("East")) {
                    newRoom.setLocation(selectedRoomX + selectedRoomWidth, selectedRoomY + (selectedRoomHeight / 2) - (newRoomHeight / 2));
                } else if (direction.equals("West")) {
                    newRoom.setLocation(selectedRoomX - newRoomWidth, selectedRoomY + (selectedRoomHeight / 2) - (newRoomHeight / 2));
                }
                break;
    
            case "Right":
                if (direction.equals("North")) {
                    newRoom.setLocation(selectedRoomX + selectedRoomWidth-newRoomWidth, selectedRoomY - newRoomHeight);
                } else if (direction.equals("South")) {
                    newRoom.setLocation(selectedRoomX + selectedRoomWidth-newRoomWidth, selectedRoomY + selectedRoomHeight);
                }
                break;
    
            case "Top":
                if (direction.equals("East")) {
                    newRoom.setLocation(selectedRoomX + selectedRoomWidth, selectedRoomY);
                } else if (direction.equals("West")) {
                    newRoom.setLocation(selectedRoomX - newRoomWidth, selectedRoomY);
                }
                break;
    
            case "Bottom":
                if (direction.equals("East")) {
                    newRoom.setLocation(selectedRoomX + selectedRoomWidth, selectedRoomY + selectedRoomHeight-newRoomHeight);
                } else if (direction.equals("West")) {
                    newRoom.setLocation(selectedRoomX - newRoomWidth, selectedRoomY + selectedRoomHeight-newRoomHeight);
                }
                break;
        }
    
        // Check if the room location is valid
        if (newRoom.getX() < 0 || newRoom.getY() < 0) {
            System.out.println(0);
            showWarningDialog("Invalid Room Position", 300);
            return;
        }
    
        // Add the room to the list and the pane
       if(!Validator(newRoom)
       &&(newRoom.getWidth()+newRoom.getX() <=0.75*(this.getWidth())+20)
       &&(newRoom.getX()>=5)
       &&(newRoom.getY()+newRoom.getHeight()<=this.getHeight()-60)
       &&(newRoom.getY()>=5)
       ){ 
        RoomsList.add(newRoom);
        this.add(newRoom, JLayeredPane.DEFAULT_LAYER);
        this.revalidate();
        this.repaint();}
        else{
            showWarningDialog("Invalid Room Position", 300);
            
        }
    }
    





    private boolean Validator(Room room) {
        Rectangle r1 = new Rectangle(room.getBounds());
        Rectangle r2 ;
        for (Room room2 : RoomsList) {
            if (room == room2)
                {continue;}
            r2 = new Rectangle(room2.getBounds());
            if (r1.intersects(r2)) {
                return true;

            }
        }
        return false;
    }
    

    @SuppressWarnings("unused")
    public void showWarningDialog(String Message, int length) {

        JDialog dialog = new JDialog();
        dialog.setTitle("Warning");
        dialog.setSize(length, 150);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);

        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        titlePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Warning", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.BLACK);
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JLabel messageLabel = new JLabel(Message);
        messageLabel.setForeground(Color.WHITE);
        messageLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        contentPanel.add(messageLabel);

        AdaptiveNormalButtons okButton = new AdaptiveNormalButtons(15, "                 OK");
        okButton.addActionListener(e -> dialog.dispose());

        contentPanel.add(okButton, BorderLayout.SOUTH);

        dialog.getContentPane().add(titlePanel, BorderLayout.NORTH);
        dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
        dialog.setUndecorated(true);
        dialog.setModal(true);
        dialog.isAlwaysOnTop();
        // Timer timer = new Timer(2000, e -> dialog.dispose());
        // timer.setRepeats(false);  // Run only once
        // timer.start();
    
        dialog.setVisible(true);

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.BLACK);

        int xC = getWidth();
        int yC = getHeight();
        if (gridSnap) {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 25));
            g2d.setColor(Color.BLACK);
            g2d.drawString("GRID SNAP: ON", getWidth()*5/6-50, getHeight() - 70);
            repaint();
        } else if (wallSnap) {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 25));
            g2d.setColor(Color.BLACK);
            g2d.drawString("WALL SNAP: ON", getWidth() *5/6-50, getHeight() - 70);
            repaint();
        }

        for (int x = 21; x < xC; x += 20) {
            for (int y = 21; y < yC; y += 20) {
                if (gridOn) {
                    g2d.fillOval(x, y, 2, 2);
                }
            }
        }
        
    }

    @SuppressWarnings("unused")
    public void PosOptions() {
        JDialog dialog = new JDialog();
    
        // Set dialog properties
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setUndecorated(true);
        dialog.setModal(true);
        dialog.getContentPane().setBackground(Color.BLACK); // Set background color of the content pane
        dialog.setLayout(null);
    
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.BLACK);
        titlePanel.setForeground(Color.BLACK);
        titlePanel.setFont(new Font("Monospaced", Font.BOLD, 20));
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        JLabel titleLabel = new JLabel("Select Direction and Alignment", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        titlePanel.add(titleLabel);
        dialog.add(titlePanel);
        titlePanel.setBounds(0,0,dialog.getWidth(),35);
    
        // Direction Buttons
        JRadioButton north = createRadioButton("North");
        JRadioButton south = createRadioButton("South");
        JRadioButton east = createRadioButton("East");
        JRadioButton west = createRadioButton("West");
    
        ButtonGroup directionGroup = new ButtonGroup();
        directionGroup.add(north);
        directionGroup.add(south);
        directionGroup.add(east);
        directionGroup.add(west);
    
        JPanel directionPanel = createPanel(new GridLayout(1, 4), north, south, east, west);
        directionPanel.setBackground(Color.BLACK);
        dialog.add(directionPanel);
        directionPanel.setBounds(0,50,dialog.getWidth(),100);
    
        // Alignment Buttons (Left, Center, Right for North/South)
        JRadioButton left = createRadioButton("Left");
        JRadioButton centerNS = createRadioButton("Center");
        JRadioButton right = createRadioButton("Right");
        
        // Alignment Buttons (Top, Center, Bottom for East/West)
        JRadioButton top = createRadioButton("Top");
        JRadioButton centerEW = createRadioButton("Center");
        JRadioButton bottom = createRadioButton("Bottom");
    
        ButtonGroup alignmentGroup = new ButtonGroup();
        alignmentGroup.add(left);
        alignmentGroup.add(centerNS);
        alignmentGroup.add(right);
        alignmentGroup.add(top);
        alignmentGroup.add(centerEW);
        alignmentGroup.add(bottom);
    
        JPanel nsAlignPanel = createPanel(new GridLayout(1, 3), left, centerNS, right);
        nsAlignPanel.setBackground(Color.BLACK);
        nsAlignPanel.setVisible(false); // Initially hidden
        dialog.add(nsAlignPanel); // Add to SOUTH
        nsAlignPanel.setBounds(0,150,dialog.getWidth(),100);
    
        JPanel ewAlignPanel = createPanel(new GridLayout(1, 3), top, centerEW, bottom);
        ewAlignPanel.setBackground(Color.BLACK);
        ewAlignPanel.setVisible(false); // Initially hidden
        dialog.add(ewAlignPanel); // Add to SOUTH, will be replaced
        ewAlignPanel.setBounds(0,150,dialog.getWidth(),100);

        AdaptiveNormalButtons cancelButton = new AdaptiveNormalButtons(15, "                    Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.add(cancelButton);
        cancelButton.setBounds(0,250,dialog.getWidth(),50);
    
        // Action Listeners for direction selection
        north.addActionListener(e -> {
            ewAlignPanel.setVisible(false); // Hide East/West panel
            nsAlignPanel.setVisible(true);   // Show North/South panel
        });
    
        south.addActionListener(e -> {
            ewAlignPanel.setVisible(false); // Hide East/West panel
            nsAlignPanel.setVisible(true);   // Show North/South panel
        });
    
        east.addActionListener(e -> {
            nsAlignPanel.setVisible(false); // Hide North/South panel
            ewAlignPanel.setVisible(true);   // Show East/West panel
        });
    
        west.addActionListener(e -> {
            nsAlignPanel.setVisible(false); // Hide North/South panel
            ewAlignPanel.setVisible(true);   // Show East/West panel
        });
    
        // Action Listeners for alignments
        left.addActionListener(e -> {
            if (north.isSelected()) {
                addRoomRelative("North", "Left", App.roomtype);
            } else if (south.isSelected()) {
                addRoomRelative("South", "Left", App.roomtype);
            }
            dialog.dispose();
        });
    
        centerNS.addActionListener(e -> {
            if (north.isSelected()) {
                addRoomRelative("North", "Center", App.roomtype);
            } else if (south.isSelected()) {
                addRoomRelative("South", "Center", App.roomtype);
            }
            dialog.dispose();
        });
    
        right.addActionListener(e -> {
            if (north.isSelected()) {
                addRoomRelative("North", "Right", App.roomtype);
            } else if (south.isSelected()) {
                addRoomRelative("South", "Right", App.roomtype);
            }
            dialog.dispose();
        });
    
        top.addActionListener(e -> {
            if (east.isSelected()) {
                addRoomRelative("East", "Top", App.roomtype);
            } else if (west.isSelected()) {
                addRoomRelative("West", "Top", App.roomtype);
            }
            dialog.dispose();
        });
    
        centerEW.addActionListener(e -> {
            if (east.isSelected()) {
                addRoomRelative("East", "Center", App.roomtype);
            } else if (west.isSelected()) {
                addRoomRelative("West", "Center", App.roomtype);
            }
            dialog.dispose();
        });
    
        bottom.addActionListener(e -> {
            if (east.isSelected()) {
                addRoomRelative("East", "Bottom", App.roomtype);
            } else if (west.isSelected()) {
                addRoomRelative("West", "Bottom", App.roomtype);
            }
            dialog.dispose();
        });
    
        dialog.setVisible(true); // Show the dialog
    }
    
    // Helper method to create radio buttons
    private JRadioButton createRadioButton(String text) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setBackground(Color.BLACK);
        radioButton.setForeground(Color.WHITE);
        radioButton.setFont(new Font("Monospaced", Font.BOLD, 15));
        radioButton.setFocusable(false);
        return radioButton;
    }
    
    // Helper method to create panels
    private JPanel createPanel(LayoutManager layout, JComponent... components) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(Color.BLACK);
        for (JComponent component : components) {
            panel.add(component);
        }
        return panel;
    }
    public Room getSelectedRoom() {
        for (Room room : CanvasPanel.RoomsList) {
            if (room.isSelected()) { // Assuming you have isSelected() method
                return room; // Return the first selected room found
            }
        }
        return null; // No room selected
    }
    
    
    
    
    

}
