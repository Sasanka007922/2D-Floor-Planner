import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import javax.swing.*;




public class App {
    private static App instance;
    public static JTextField width;
    public static JTextField height;
    public static int roomtype;
    public static trash bin;
    CanvasPanel canvas;
    ControlPanel control;
    private boolean savestatus;
    

    @SuppressWarnings("unused")
    public App() {
        instance = this;
        JFrame frame = new JFrame();
        frame.setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("2D Floor Planner");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setMinimumSize(new Dimension(1920, 1080));
        frame.setLocation(-7, -7);
        frame.setResizable(false);
        canvas = new CanvasPanel();
        control = new ControlPanel();
        AdaptiveNormalButtons addroom = new AdaptiveNormalButtons(20, " Add Room                ");
        AdaptiveNormalButtons addfurniture = new AdaptiveNormalButtons(20, " Add Furniture/Fixtures  ");
        ArrayList<AdaptiveRoundButtons> Roombuttons = new ArrayList<>();
        AdaptiveRoundButtons bedroom = new AdaptiveRoundButtons(Roombuttons, "Bedroom");
        AdaptiveRoundButtons kitchen = new AdaptiveRoundButtons(Roombuttons, "Kitchen");
        AdaptiveRoundButtons living = new AdaptiveRoundButtons(Roombuttons, "Living Room");
        AdaptiveRoundButtons dining = new AdaptiveRoundButtons(Roombuttons, "Dining Room");
        AdaptiveRoundButtons bathroom = new AdaptiveRoundButtons(Roombuttons, "Bathroom");
        ButtonPopup Roommenu = new ButtonPopup(Roombuttons, 6, 1);

        for (int i = 0; i < Roombuttons.size(); i++) {
            AdaptiveRoundButtons btn = Roombuttons.get(i);
            System.out.println("Button " + i + ": " + btn.getText());
        }

        ArrayList<AdaptiveRoundButtons> FurnButtons = new ArrayList<>();
        AdaptiveRoundButtons bed = new AdaptiveRoundButtons(FurnButtons, "Bed");
        AdaptiveRoundButtons commode = new AdaptiveRoundButtons(FurnButtons, "Commode");
        AdaptiveRoundButtons chair = new AdaptiveRoundButtons(FurnButtons, "Chair");
        AdaptiveRoundButtons washbasin = new AdaptiveRoundButtons(FurnButtons, "Washbasin");
        AdaptiveRoundButtons table = new AdaptiveRoundButtons(FurnButtons, "Table");
        AdaptiveRoundButtons shower = new AdaptiveRoundButtons(FurnButtons, "Shower");
        AdaptiveRoundButtons sofa = new AdaptiveRoundButtons(FurnButtons, "Sofa");
        AdaptiveRoundButtons sink = new AdaptiveRoundButtons(FurnButtons, "Sink");
        AdaptiveRoundButtons diningset = new AdaptiveRoundButtons(FurnButtons, "Dining Set");
        AdaptiveRoundButtons stove = new AdaptiveRoundButtons(FurnButtons, "Stove");
        ButtonPopup Furnmenu = new ButtonPopup(FurnButtons, 5, 2);

        AdaptiveNormalButtons New = new AdaptiveNormalButtons(15, "New");
        AdaptiveNormalButtons Save = new AdaptiveNormalButtons(15, "Save");
        AdaptiveNormalButtons Load = new AdaptiveNormalButtons(15, "Load");
        AdaptiveNormalButtons Snap = new AdaptiveNormalButtons(15, "Snap");
        JPanel menu = new JPanel();
        menu.setBackground(Color.DARK_GRAY);
        menu.setLayout(new GridLayout(1, 4));
        menu.add(New);
        menu.add(Save);
        menu.add(Load);
        menu.add(Snap);
        JPopupMenu snapmenu = new JPopupMenu();
        snapmenu.setBackground(Color.DARK_GRAY);
        snapmenu.setBorderPainted(false);
        AdaptiveNormalButtons wall = new AdaptiveNormalButtons(15, "   Wall");
        AdaptiveNormalButtons grid = new AdaptiveNormalButtons(15, "   Grid");
        snapmenu.add(wall);
        snapmenu.add(grid);
        snapmenu.setLayout(new GridLayout(2, 1));

        width = new JTextField("100", 7);
        height = new JTextField("100", 7);
        width.setBackground(Color.black);
        width.setForeground(Color.WHITE);
        height.setBackground(Color.black);
        height.setForeground(Color.white);
        width.setFont(new Font("Monospaced", Font.BOLD, 15));
        height.setFont(new Font("Monospaced", Font.BOLD, 15));

        JLabel w = new JLabel(" Width= ");
        JLabel h = new JLabel("Height=   ");
        JLabel wh = new JLabel();
        w.setBackground(Color.black);
        w.setLayout(new FlowLayout(FlowLayout.RIGHT));
        h.setBackground(Color.black);
        h.setLayout(new FlowLayout(FlowLayout.RIGHT));
        wh.setBackground(Color.black);
        w.setForeground(Color.white);
        h.setForeground(Color.white);
        wh.setForeground(Color.white);
        w.setFont(new Font("Monospaced", Font.BOLD, 20));
        h.setFont(new Font("Monospaced", Font.BOLD, 20));
        w.add(width);
        h.add(height);
        wh.setLayout(new GridLayout(1, 2));
        wh.add(w);
        wh.add(h);
        Roommenu.add(wh);

        // Create circular buttons
        Circularbuttons help = new Circularbuttons(40, "?");
        Circularbuttons G = new Circularbuttons(40, "G");

        // Create the label
        JLabel helppp = new JLabel("Help", JLabel.CENTER);
        helppp.setForeground(Color.WHITE);
        helppp.setFont(new Font("Monospaced", Font.BOLD, 20));

        // Create the panel
        JPanel helpbar = new JPanel(null); // null layout for absolute positioning
        helpbar.setBackground(Color.BLACK);

        // Set bounds (assume x and y are screen width and height)

        // Add components
        helpbar.add(help);
        helpbar.add(helppp);
        helpbar.add(G);

        // Add to control
        control.add(helpbar);

        JPopupMenu relative = new JPopupMenu();
        relative.setBackground(Color.DARK_GRAY);
        relative.setBorderPainted(false);
        AdaptiveNormalButtons Add = new AdaptiveNormalButtons(15, "Add         ");
        AdaptiveNormalButtons RelAdd = new AdaptiveNormalButtons(15, "Relative Add");
        relative.add(Add);
        relative.add(RelAdd);
        relative.setLayout(new GridLayout(2, 1));

        bin = new trash();
        canvas.add(bin);
        bin.setVisible(false);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                frame.setLocation(-7, -7);
                int x = frame.getWidth();
                int y = frame.getHeight();
                canvas.setBounds(x / 4, 0, 3 * x / 4, y);
                control.setBounds(0, 0, x / 4, y);
                helpbar.setBounds(0, 9 * y / 10 - 20, x / 4, y / 10);
                help.setBounds(5, 5, 40, 40);
                helppp.setBounds(helpbar.getWidth() / 2 - 110, 5, 60, 40); // centered
                G.setBounds(helpbar.getWidth() - 45 - 5, 5, 40, 40); // right aligned
                menu.setBounds(0, 0, x / 4, y / 30);
                addroom.setBounds(0, y / 8, x / 4, y / 15);
                if (!Roommenu.isDisplayable()) {
                    addfurniture.setBounds(0, 23 * y / 120, x / 4, y / 15);
                    Furnmenu.setBounds(0, addfurniture.getY() + addfurniture.getHeight(), x / 4, y / 4);
                } else {
                    addfurniture.setLocation(0, addroom.getY() + addroom.getHeight() + Roommenu.getHeight());
                    Furnmenu.setBounds(0, addfurniture.getY() + addfurniture.getHeight(), x / 4, y / 4);
                }
                Roommenu.setBounds(0, 23 * y / 120, x / 4, 3 * y / 10);
                snapmenu.setPopupSize(x / 16, y / 15);
                relative.setPopupSize(x / 8, 3 * y / 25);
                int binSize = x / 8;
                int binX = (3 * x / 4) - binSize;
                int binY = 0;
                bin.setBounds(binX, binY, binSize, binSize);
                control.repaint();

            }
        });
        New.addActionListener(e -> {
            if(savestatus||CanvasPanel.RoomsList.isEmpty()){
                control.remove(Roommenu);
            control.remove(Furnmenu);
            addfurniture.setBounds(0, addroom.getY() + addroom.getHeight(), addroom.getWidth(), addroom.getHeight());
            control.repaint();
            for (Room room : CanvasPanel.RoomsList) {
                canvas.remove(room);
                room.furnList.clear();
            }
            CanvasPanel.RoomsList.clear();
            canvas.repaint();
            Room.selection11 = false;
            } else {
                canvas.showWarningDialog("You left some Unsaved Progress", 300);
            }
        });
        Snap.addActionListener(e -> {
            snapmenu.show(Snap, 0, Snap.getHeight());
            Snap.repaint();
            menu.repaint();
            menu.revalidate();
            control.repaint();
            control.revalidate();
        });

        Save.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Application State");
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                saveAppState(file); // Save the state to the selected file
            }
            savestatus = true;
        });

        Load.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Application State");
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                loadAppState(file); // Load the state from the selected file
            }
        });

        addroom.addActionListener(e -> {
            if (!Roommenu.isDisplayable()) {
                control.add(Roommenu);
                control.repaint();
                control.revalidate();
                addfurniture.setLocation(0, addroom.getY() + addroom.getHeight() + Roommenu.getHeight());
                Furnmenu.setLocation(0, addfurniture.getY() + addfurniture.getHeight());

            } else {
                control.remove(Roommenu);
                control.repaint();
                control.revalidate();
                addfurniture.setLocation(0, addroom.getY() + addroom.getHeight());
                Furnmenu.setLocation(0, addfurniture.getY() + addfurniture.getHeight());
            }
        });

        for (AdaptiveRoundButtons b : Roombuttons) {
            for (ActionListener al : b.getActionListeners()) {
                b.removeActionListener(al);
            }

            b.addActionListener(e -> {
                if (!Room.selection11) {
                    canvas.addRoom(Roombuttons.indexOf(b));
                } else {
                    relative.show(b, b.getX() + b.getWidth(), 0);
                    roomtype = Roombuttons.indexOf(b);
                }
            });
        }

        for (AdaptiveRoundButtons f : FurnButtons) {
            f.addActionListener(e -> {
                // Use a separate method to find the selected room
                Room selectedRoom = canvas.getSelectedRoom();

                // Check if a room is selected
                if (selectedRoom == null) {
                    canvas.showWarningDialog("Please select a room before adding furniture.", 350);
                    return; // Exit if no room is selected
                }

                // Use SwingWorker for adding furniture in the background
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        // Add furniture to the selected room
                        selectedRoom.addFurn(FurnButtons.indexOf(f), selectedRoom);
                        return null; // No result to return
                    }

                    @Override
                    protected void done() {
                        // Update UI if needed
                        selectedRoom.revalidate();
                        selectedRoom.repaint();
                    }
                };

                worker.execute(); // Start the background task
            });
        }

        // Helper method to get the selected room

        Add.addActionListener(e -> {
            canvas.addRoom(roomtype);
        });

        RelAdd.addActionListener(e -> {
            canvas.PosOptions();
        });

        G.addActionListener(e -> {
            canvas.gridOn = !canvas.gridOn;
            canvas.repaint();
            canvas.revalidate();
        });

        wall.addActionListener(e -> {
            CanvasPanel.wallSnap = !CanvasPanel.wallSnap;
            CanvasPanel.gridSnap = false;
            canvas.repaint();
        });
        grid.addActionListener(e -> {
            CanvasPanel.gridSnap = !CanvasPanel.gridSnap;
            CanvasPanel.wallSnap = false;
            canvas.repaint();
        });

        addfurniture.addActionListener(e -> {
            if (!Furnmenu.isDisplayable()) {
                control.add(Furnmenu);
                control.repaint();
                control.revalidate();

            } else {
                control.remove(Furnmenu);
                control.repaint();
                control.revalidate();
            }
        });

        control.add(menu);
        control.add(addroom);
        control.add(addfurniture);
        control.repaint();
        canvas.repaint();
        frame.repaint();
        frame.add(control);
        frame.add(canvas);
        frame.setVisible(true);

    }

    public static Dimension getSize(CanvasPanel canvas) {
        int w = 0;
        int h = 0;
        try {
            w = Integer.parseInt(width.getText());
            h = Integer.parseInt(height.getText());
            if (w <= 0 || h <= 0) {
                canvas.showWarningDialog("    Bro, Seriously. Come on!", 300);
                width.setText("100");
                height.setText("100");
            }
        } catch (NumberFormatException e) {
            canvas.showWarningDialog(" Are you from Another Planet!!", 300);
            width.setText("100");
            height.setText("100");
        }
        return new Dimension(w, h);
    }

    public void saveAppState(File file) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            // Save number of rooms
            out.writeInt(CanvasPanel.RoomsList.size());

            for (Room room : CanvasPanel.RoomsList) {
                out.writeInt(room.getRoomType()); // Save room type
                out.writeObject(room.getLocation());
                out.writeObject(room.getSize());

                // Save furniture list
                out.writeInt(room.furnList.size());
                for (furniture f : room.furnList) {
                    out.writeInt(f.furntype);
                    out.writeObject(f.getLocation());
                    out.writeInt(f.rotation);
                }
            }

            out.writeObject(bin);
            JOptionPane.showMessageDialog(null, "App state successfully saved.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the app state from a file
    public void loadAppState(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            int roomCount = in.readInt();
            CanvasPanel.RoomsList.clear();
            canvas.removeAll();

            for (int i = 0; i < roomCount; i++) {
                int type = in.readInt();
                Point loc = (Point) in.readObject();
                Dimension size = (Dimension) in.readObject();

                Room room = new Room(type);
                switch (type) {
                    case 0 -> room.roomtype_str = "Bedroom";
                    case 1 -> room.roomtype_str = "Kitchen";
                    case 2 -> room.roomtype_str = "Living Room";
                    case 3 -> room.roomtype_str = "Dining Room";
                    case 4 -> room.roomtype_str = "Bathroom";
                }

                room.setLocation(loc);
                room.setSize(size);

                int furnitureCount = in.readInt();
                for (int j = 0; j < furnitureCount; j++) {
                    int furnType = in.readInt();
                    Point furnLoc = (Point) in.readObject();
                    int rotation = in.readInt();

                    furniture furn = new furniture(furnType);
                    furn.setLocation(furnLoc);
                    furn.rotation = rotation;
                    room.furnList.add(furn);
                    room.add(furn);
                }

                CanvasPanel.RoomsList.add(room);
                canvas.add(room);
            }

            bin = (trash) in.readObject();
            canvas.add(bin);
            bin.setVisible(false);

            canvas.revalidate();
            canvas.repaint();
            JOptionPane.showMessageDialog(null, "App state successfully loaded.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static App getInstance(){
        return instance;
    }

    public boolean getsavestatus(){
        return savestatus;
    }

    public void setsavestatus(boolean newone){
        savestatus = newone;
    }


    public static void main(String[] args) {
        @SuppressWarnings("unused")
        App app = new App();

    }

}