import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;

import javax.swing.JPopupMenu;

import javax.swing.SwingUtilities;

public class Room extends CanvasPanel{
    private int initialX;
    private int initialY;
    public boolean selection = false;
    public static boolean selection11;
    public int xP;
    public int yP;
    private int doorxp, dooryp;
    private int newX;
    private int newY;
    // private int doorx, doory;
    public int roomtype;
    public String roomtype_str;
    private boolean isDragging; // Track dragging state
    public ArrayList<furniture> furnList = new ArrayList<>();
    private int wallWidth = 6;
    private MatteBorder border;
    private ArrayList<JLayeredPane> doors = new ArrayList<>();
    // private ArrayList<Room> adjacentRooms = new ArrayList<>(); // Stores
    // neighboring rooms

    public Room(int roomtype) {
        this.roomtype = roomtype;
        this.setSize(App.getSize(this));
        this.setOpaque(false); // Make the room non-opaque

        border = new MatteBorder(wallWidth, wallWidth, wallWidth, wallWidth, Color.BLACK);
        this.setBorder(border);
        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    showOptionDialog(e, e.getX(), e.getY());
                }
                xP = e.getX();
                yP = e.getY();
                initialX = getX();
                initialY = getY();

                if (!selection) {
                    select();
                    deselectAllRooms();

                } else if (selection) {
                    deselect();

                }
                selection11 = selection;

                isDragging = true; // Set dragging state
                toFront(); // Bring this room to the front
                repaint();
                App.getInstance().setsavestatus(false);
            }

            public void mouseReleased(MouseEvent e) {
                revalidate();
                repaint(); // Keep the selection color on repaint

                if (CanvasPanel.wallSnap) {
                    WallSnap();
                    repaint();
                    getParent().repaint();
                } else if (CanvasPanel.gridSnap) {
                    GridSnap();
                    repaint();
                    getParent().repaint();
                }

                if (Validator(Room.this)) {

                    showWarningDialog("   Room already Exists here..", 300);
                    setLocation(initialX, initialY);

                }
                // else if (!Validator(Room.this)&&
                // (Room.this.getWidth() + newX <= 0.75 * Room.this.getParent().getWidth() + 20)
                // &&
                // (newX >= 5) &&
                // (newY + Room.this.getHeight() <= Room.this.getParent().getHeight() - 60) &&
                // (newY >= 5))
                // {
                // // If valid, update the position
                // setLocation(newX, newY);
                // }

                isDragging = false; // Reset dragging state
                App.bin.setVisible(false);
                repaint(); // Repaint to restore transparency
                // Do NOT deselect here; maintain selection state

                if (Room.this.getBounds().intersects(App.bin.getBounds())) {
                    Room.this.getParent().remove(Room.this);
                    Room.this.furnList.clear();
                    Room.this.doors.clear();
                    RoomsList.remove(Room.this);
                    selection = false;
                    selection11 = selection;
                    App.bin.setVisible(false);
                    App.bin.getParent().repaint();
                }
                App.getInstance().setsavestatus(false);
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int parentWidth = Room.this.getParent().getWidth();
                int parentHeight = Room.this.getParent().getHeight();
        
                int roomWidth = Room.this.getWidth();
                int roomHeight = Room.this.getHeight();
        
                int rawX = getX() + e.getX() - xP;
                int rawY = getY() + e.getY() - yP;
        
                // Clamp values to keep the room within bounds
                newX = Math.max(5, Math.min(rawX, parentWidth - roomWidth - 20));
                newY = Math.max(5, Math.min(rawY, parentHeight - roomHeight - 60));
        
                setLocation(newX, newY);
                Room.this.getParent().repaint();
                App.bin.setVisible(true);
        
                select();
                deselectAllRooms();
                selection11 = selection;
                App.getInstance().setsavestatus(false);
            }
        });

        App.getInstance().setsavestatus(false);
        

    }

    public int getRoomType() {
        return this.roomtype;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Selection effect
        if (selection) {
            g2.setColor(new Color(0, 0, 200, 50));
            g2.setStroke(new BasicStroke(8));
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        }

        // Set transparency if dragging
        if (isDragging) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 50% opaque
        } else {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Fully opaque
        }

        // Set up gradient based on room type
        GradientPaint gradient = null;
        String roomLabel = ""; // TegetX()t label for room type
        switch (roomtype) {
            case 0: // Bedroom
                gradient = new GradientPaint(0, 0, new Color(34, 139, 34), getWidth(), getHeight(),
                        new Color(85, 107, 47));

                roomLabel = "Bedroom";
                break;
            case 1: // Kitchen
                gradient = new GradientPaint(0, 0, new Color(139, 0, 0), getWidth(), getHeight(),
                        new Color(178, 34, 34));
                roomLabel = "Kitchen";
                break;
            case 2: // Living Room
                gradient = new GradientPaint(0, 0, new Color(255, 69, 0), getWidth(), getHeight(),
                        new Color(255, 140, 0));
                roomLabel = "Living Room";
                break;
            case 3: // Dining Room
                gradient = new GradientPaint(0, 0, new Color(184, 134, 11), getWidth(), getHeight(),
                        new Color(218, 165, 32));
                roomLabel = "Dining Room";
                break;
            case 4: // Bathroom
                gradient = new GradientPaint(0, 0, new Color(0, 0, 120), getWidth(), getHeight(),
                        new Color(25, 25, 100));
                roomLabel = "Bathroom";
                break;
        }

        // Apply gradient if defined
        if (gradient != null) {
            g2.setPaint(gradient);
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        // Draw selection border if selected
        if (selection) {
            g2.setColor(new Color(0, 0, 200, 100));
            g2.fillRect(0, 0, this.getWidth(), this.getHeight());
            g2.setColor(Color.BLUE);
            // g2.setStroke(new BasicStroke(5));
            // g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        } else {
            // Draw black border for unselected rooms
            // g2.setColor(Color.BLACK);
            // g2.setStroke(new BasicStroke(8));
            // g2.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        }

        // Draw the room type label
        if (!roomLabel.isEmpty()) {
            // Calculate font size to fit within the room bounds
            int fontSize = Math.min(25, Math.min(getWidth() / roomLabel.length(), getHeight() / 2));
            g2.setFont(new Font("Monospaced", Font.BOLD, fontSize));
            g2.setColor(new Color(0, 0, 0, 128)); // Text color for visibility on gradient

            // Calculate position for centered text
            FontMetrics metrics = g2.getFontMetrics(g2.getFont());
            int x = (getWidth() - metrics.stringWidth(roomLabel)) / 2;
            int y = (getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();

            g2.drawString(roomLabel, x, y);

        }
    }

    @Override
    public boolean isOpaque() {
        return false; // Ensure the room is non-opaque
    }

    private boolean Validator(Room room) {
        Rectangle r1 = new Rectangle(room.getBounds());
        for (Room room2 : RoomsList) {
            if (room == room2)
                continue;

            Rectangle r2 = new Rectangle(room2.getBounds()); // Debug bounds

            if (r1.intersects(r2)) {
                return true;
            }
        }
        return false;
    }

    // Bring this component to the front of its parent
    private void toFront() {
        Container parent = getParent();
        if (parent != null) {
            parent.setComponentZOrder(this, 0); // Move this component to the top
            parent.revalidate(); // Revalidate the parent to update layout
            parent.repaint(); // Repaint the parent to reflect changes
        }
    }

    private void select() {
        selection = true;

        repaint(); // Ensure the room is repainted when selected
        App.getInstance().setsavestatus(false);
    }

    private void deselect() {
        selection = false;
        repaint(); // Ensure the room is repainted when deselected
        App.getInstance().setsavestatus(false);
    }

    private void deselectAllRooms() {
        for (Room room : RoomsList) {
            if (room != this) {
                room.selection = false;
                room.repaint();

            }
        }
    }

    private Point getSnapPosition(Room room) {
        int originalX = room.getX();
        int originalY = room.getY();

        int roomWidth = room.getWidth();
        int roomHeight = room.getHeight();

        for (Room room2 : RoomsList) {
            if (room == room2)
                continue;

            int room2X = room2.getX();
            int room2Y = room2.getY();
            int room2Width = room2.getWidth();
            int room2Height = room2.getHeight();

            // right
            if (Math.abs(room2X + room2Width - originalX) <= 20
                    && Math.abs(room2Y - originalY) <= room2Height) {
                return new Point(room2X + room2Width, originalY);
            }

            // left
            if (Math.abs(room2X - (originalX + roomWidth)) <= 20
                    && Math.abs(room2Y - originalY) <= room2Height) {
                return new Point(room2X - roomWidth, originalY);
            }

            // below
            if (Math.abs(room2Y + room2Height - originalY) <= 20
                    && Math.abs(room2X - originalX) <= room2Width) {
                return new Point(originalX, room2Y + room2Height);
            }

            // above
            if (Math.abs(room2Y - (originalY + roomHeight)) <= 20
                    && Math.abs(room2X - originalX) <= room2Width) {
                return new Point(originalX, room2Y - roomHeight);
            }
        }

        return new Point(originalX, originalY);
    }

    private void WallSnap() {
        Point snapPosition = this.getSnapPosition(this);
        if (!snapPosition.equals(new Point(getX(), getY()))) {
            setLocation(snapPosition.x, snapPosition.y);
        }
        this.repaint();
        getParent().repaint();
    }

    private void GridSnap() {
        int snappedX = (this.getX() / 20) * 20;
        int snappedY = (this.getY() / 20) * 20;

        if (!Validator(this)) {
            this.setLocation(snappedX + 2, snappedY + 2);
        }
        this.repaint();
        getParent().repaint();
    }

    public void addFurn(int furntype, Room room) {
        // Check if the room is selected
        if (!room.isSelected()) {
            showWarningDialog("Please select a room.", 350);
            return; // Exit the method if the room is not selected
        }

        furniture furniture = new furniture(furntype);
        int x = 0;
        int y = 0;
        boolean placed = false;

        // Start placing furniture
        while (!placed) {
            // Try placing the furniture in the current row
            for (x = 0; x + furniture.getWidth() <= room.getWidth(); x += 15) {
                furniture.setLocation(x, y);

                // Check if this position is valid within the room
                if (!furnValid(furniture, room)) { // Update the check to ensure it returns true when valid
                    placed = true; // Mark as placed
                    break; // Exit the for loop
                }
            }

            // If not placed, move to the next row
            if (!placed) {
                y += 15; // Move down to the next row

                // Stop if no more space is available within the room
                if (y + furniture.getHeight() > room.getHeight()) {
                    break; // Exit if it exceeds room height
                }
            }
        }

        // If placed, add the furniture to the room
        if (placed) {
            furnList.add(furniture);
            room.add(furniture);
            room.revalidate();
            room.repaint();
        } else {
            showWarningDialog("No space for furniture", 350);
        }

        switch (furniture.furntype) {
            case 0 -> furniture.furntype_str = "Bed";

            case 1 -> furniture.furntype_str = "Commmode";

            case 2 -> furniture.furntype_str = "Chair";

            case 3 -> furniture.furntype_str = "Sink";

            case 4 -> furniture.furntype_str = "Table";

            case 5 -> furniture.furntype_str = "Shower";

            case 6 -> furniture.furntype_str = "Sofa";

            case 7 -> furniture.furntype_str = "Wash Basin";

            case 8 -> furniture.furntype_str = "Dining Set";

            case 9 -> furniture.furntype_str = "Stove";

        }
    }
    private void setWall(int dir, boolean add) {
        Border currentBorder = this.getBorder();
        Insets insets = new Insets(wallWidth, wallWidth, wallWidth, wallWidth);
        if (currentBorder instanceof MatteBorder) {
            insets = ((MatteBorder) currentBorder).getBorderInsets();
        } else {
            showWarningDialog("There is door", 150);
        }

        insets = currentBorder.getBorderInsets(this);
        switch (dir) {
            case 0 -> insets.right = add ? wallWidth : 0;
            case 1 -> insets.left = add ? wallWidth : 0;
            case 2 -> insets.top = add ? wallWidth : 0;
            case 3 -> insets.bottom = add ? wallWidth : 0;
        }

        MatteBorder updatedBorder = new MatteBorder(insets.top, insets.left, insets.bottom, insets.right, Color.BLACK);
        this.setBorder(updatedBorder);
        this.revalidate();
        this.repaint();
    }

    private void setDoor(int dir, boolean add) {
        if (add) {
            // Create a new door panel
            JLayeredPane door = new JLayeredPane();
            door.setBackground(Room.this.getBackground());
            door.setOpaque(true);

            // Initialize variables for dragging

            // Mouse listeners for dragging functionality
            door.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    doorxp = e.getX();
                    dooryp = e.getY();
                }
            });

            door.addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    int doorx = door.getX() + e.getX() - doorxp;
                    int doory = door.getY() + e.getY() - dooryp;

                    switch (dir) {
                        case 0 -> {
                            // Right side wall - constrain movement vertically
                            if (doorx == Room.this.getWidth() - wallWidth && doory >= 0 &&
                                    doory <= Room.this.getHeight() - door.getHeight()) {
                                door.setLocation(doorx, doory);
                            }
                        }
                        case 1 -> {
                            // Left side wall - constrain movement vertically
                            if (doorx == 0 && doory >= 0 &&
                                    doory <= Room.this.getHeight() - door.getHeight()) {
                                door.setLocation(doorx, doory);
                            }
                        }
                        case 2 -> {
                            // Top wall - constrain movement horizontally
                            if (doory == 0 && doorx >= 0 &&
                                    doorx <= Room.this.getWidth() - door.getWidth()) {
                                door.setLocation(doorx, doory);
                            }
                        }
                        case 3 -> {
                            // Bottom wall - constrain movement horizontally
                            if (doory == Room.this.getHeight() - wallWidth && doorx >= 0 &&
                                    doorx <= Room.this.getWidth() - door.getWidth()) {
                                door.setLocation(doorx, doory);
                            }
                        }
                    }
                }
            });

            // Set door position and size based on direction
            switch (dir) {
                case 0 -> { // Right wall
                    door.setSize(wallWidth, 25);
                    door.setLocation(Room.this.getWidth() - wallWidth,
                            (Room.this.getHeight() / 2) - (door.getHeight() / 2));
                }
                case 1 -> { // Left wall
                    door.setSize(wallWidth, 25);
                    door.setLocation(0,
                            (Room.this.getHeight() / 2) - (door.getHeight() / 2));
                }
                case 2 -> { // Top wall
                    door.setSize(25, wallWidth);
                    door.setLocation((Room.this.getWidth() / 2) - (door.getWidth() / 2), 0);
                }
                case 3 -> { // Bottom wall
                    door.setSize(25, wallWidth);
                    door.setLocation((Room.this.getWidth() / 2) - (door.getWidth() / 2),
                            Room.this.getHeight() - door.getHeight());
                }
            }

            Room.this.setLayout(null);
            doors.add(door);
            Room.this.add(door);
            Room.this.revalidate();
            Room.this.repaint();

        } else {
            // Remove door based on direction
            JLayeredPane doorToRemove = findDoorByDirection(dir);

            if (doorToRemove != null) {
                doors.remove(doorToRemove);
                Room.this.remove(doorToRemove);
                Room.this.revalidate();
                Room.this.repaint();
            }
        }
    }

    private JLayeredPane findDoorByDirection(int dir) {
        for (JLayeredPane door : doors) {
            switch (dir) {
                case 0 -> {
                    if (door.getX() == Room.this.getWidth() - wallWidth) {
                        return door;
                    }
                }
                case 1 -> {
                    if (door.getX() == 0) {
                        return door;
                    }
                }
                case 2 -> {
                    if (door.getY() == 0) {
                        return door;
                    }
                }
                case 3 -> {
                    if (door.getY() == Room.this.getHeight() - door.getHeight()) {
                        return door;
                    }
                }
            }
        }
        return null;
    }

    private boolean furnValid(furniture furniture, Room room) {
        Rectangle furnBounds = furniture.getBounds();

        for (Component comp : room.getComponents()) {
            if (comp instanceof furniture && comp != furniture) {
                Rectangle existingBounds = comp.getBounds();
                if (furnBounds.intersects(existingBounds)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSelected() {
        return selection;
    }

  @SuppressWarnings("unused")
private void showOptionDialog(MouseEvent e, int x, int y) {
    Font font = new Font("Monospaced", Font.BOLD, 14);
    JPopupMenu mainMenu = new JPopupMenu();
    mainMenu.setLayout(new GridLayout(0, 1, 5, 5));
    mainMenu.setBackground(Color.BLACK);

    String[] actions = {"Clear", "Delete", "Copy"};
    for (String label : actions) {
        JMenuItem item = new JMenuItem(label);
        item.setFont(font);
        item.setBackground(Color.BLACK);
        item.setFocusPainted(false);
        item.setForeground(Color.WHITE);
        item.setBorderPainted(false);
        // item.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        mainMenu.add(item);

        item.addActionListener(ev -> {
            switch (label) {
                case "Clear" -> {
                    furnList.forEach(Room.this::remove);
                    furnList.clear();
                }
                case "Delete" -> {
                    Container parent = Room.this.getParent();
                    if (parent != null) parent.remove(Room.this);
                    CanvasPanel.RoomsList.remove(Room.this);
                    if (App.bin.getParent() != null) App.bin.getParent().repaint();
                }
                case "Copy" -> {
                    Room copyRoom = new Room(roomtype);
                    copyRoom.setSize(getSize());
                    copyRoom.setBorder(getBorder());

                    JPopupMenu pasteMenu = new JPopupMenu();
                    JMenuItem pasteBtn = new JMenuItem("Paste");
                    pasteBtn.setFont(font);
                    pasteBtn.setBackground(Color.BLACK);
                    pasteBtn.setForeground(Color.WHITE);
                    pasteBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE));
                    pasteMenu.add(pasteBtn);

                    CanvasPanel cp = (CanvasPanel) getParent();
                    pasteBtn.addActionListener(e1 -> {
                        Room newRoom = new Room(copyRoom.roomtype);
                        newRoom.setSize(copyRoom.getSize());
                        newRoom.setBorder(copyRoom.getBorder());

                        for (furniture furn : furnList) {
                            furniture newFurn = furn.clone(); // assume clone exists
                            newRoom.add(newFurn);
                            newRoom.furnList.add(newFurn);
                        }

                        newRoom.setLocation(SwingUtilities.convertPoint(pasteBtn, 0, 0, cp));
                        if (!Validator(newRoom)) {
                            CanvasPanel.RoomsList.add(newRoom);
                            cp.add(newRoom);
                            cp.repaint();
                        } else {
                            showWarningDialog("Invalid Room Position", 300);
                        }
                    });

                    cp.addMouseListener(new MouseAdapter() {
                        public void mousePressed(MouseEvent ev) {
                            if (SwingUtilities.isRightMouseButton(ev)) {
                                pasteMenu.show(ev.getComponent(), ev.getX(), ev.getY());
                            }
                        }
                    });
                }
            }
            mainMenu.setVisible(false);
        });
    }

    // Wall submenu
    JMenu wallMenu = new JMenu("Wall");
    wallMenu.setFont(font);
    wallMenu.setForeground(Color.WHITE);
    wallMenu.setBackground(Color.BLACK);
    wallMenu.setOpaque(true);

    String[] dirs = {"East", "West", "North", "South"};
    for (int i = 0; i < dirs.length; i++) {
        int dirIndex = i;
        JMenuItem add = new JMenuItem(dirs[i] + " Add");
        JMenuItem rem = new JMenuItem(dirs[i] + " Remove");

        for (JMenuItem item : new JMenuItem[]{add, rem}) {
            item.setFont(font);
            item.setBackground(Color.BLACK);
            item.setFocusPainted(false);
            item.setBorderPainted(false);
            item.setForeground(Color.WHITE);
            item.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }

        add.addActionListener(a -> {
            Room.this.setWall(dirIndex, true);
            mainMenu.setVisible(false);
        });
        rem.addActionListener(r -> {
            Room.this.setWall(dirIndex, false);
            mainMenu.setVisible(false);
        });

        wallMenu.add(add);
        wallMenu.add(rem);
    }
    mainMenu.add(wallMenu);

    // Door submenu
    JMenu doorMenu = new JMenu("Door");
    doorMenu.setFont(font);
    doorMenu.setForeground(Color.WHITE);
    doorMenu.setBackground(Color.BLACK);
    doorMenu.setOpaque(true);

    for (int i = 0; i < dirs.length; i++) {
        int dirIndex = i;
        JMenuItem add = new JMenuItem(dirs[i] + " Add");
        JMenuItem rem = new JMenuItem(dirs[i] + " Remove");

        for (JMenuItem item : new JMenuItem[]{add, rem}) {
            item.setFont(font);
            item.setBackground(Color.BLACK);
            item.setForeground(Color.WHITE);
            item.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        }

        add.addActionListener(a -> {
            Room.this.setDoor(dirIndex, true);
            mainMenu.setVisible(false);
        });
        rem.addActionListener(r -> {
            Room.this.setDoor(dirIndex, false);
            mainMenu.setVisible(false);
        });

        doorMenu.add(add);
        doorMenu.add(rem);
    }
    mainMenu.add(doorMenu);

    mainMenu.show(e.getComponent(), e.getX(), e.getY());
}

    
}