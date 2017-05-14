/**
 * Author: Min Hoo Lee
 * Date: March 20, 2015 - May 19, 2015 (start to end)
 * 
 * Description: 
 * ColorShooter.java is the program containing the game "Arcus Coelestis".
 * This game was created and designed for the Game-In-5-Weeks Project.
 * It involves the user controlling a cannon to shoot colored balls
 * (specified by HSV sliders) at randomly colored balls
 * located in the top of the screen. If the ball hits a ball of the same color,
 * the user scores a point. Otherwise, the user loses a life (one out of five).
 * The user has 5 lives and every two points that the user scores,
 * the amount of balls decreases by 4.
 */

/**
 * TESTING PLAN
 * should work: click on buttons, change values of sliders, mouse click
 * should not work: keys typed, keys pressed, clicking outside of box
 */

// import libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.AffineTransform;

public class ColorShooter extends JFrame { // ColorShooter class header; holds the game 'Arcus Coelestis'

    private CardHolder ch; // CardHolder panel

    public static void main(String[]args) { // main method header, instantiate constructor
        try {
            new ColorShooter();
        } catch (NullPointerException npe) { // catch null pointer exception meaning an error in nested classes
            System.out.println("Error: " + npe);
        }
    }


    public ColorShooter() { // constructor header that instantiates the JFrame
        super("Arcus Coelestis");// title JFrame 'Arcus Coelestis'
        setSize(1350, 700);// JFrame size = 1350 (w) by 700 (h)
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // respond to 'X' on JFrame
        setLocation(30, 130); // set location on monitor to (30, 130)
        setResizable(false); // cannot be resized

        ch = new CardHolder(); // CardHolder panel holds the instructions panel and game panel
        add(ch);

        setVisible(true); // make JFrame show up
    }

    public class CardHolder extends JPanel { // CardHolder class header; panel that holds the instructions and game panels

        private MainPanel main; // Main Panel
        private InstructionsPanel1 instruct1; // Instructions Panel about gameplay (Part 1)
        private InstructionsPanel2 instruct2; // Instructions Panel about gameplay (Part 2)
        private InstructionsPanel3 instruct3; // Instructions Panel about HSV
        private SeeMore1 more1; // see more about hue
        private SeeMore2 more2; // see more about saturation
        private SeeMore3 more3; // see more about value
        private GameMode mode; // Panel for user to select game mode in
        private GamePanel game; // Game Panel
        private GameLose lose; // Panel to display if user wins or loses

        private JButton instructButton; // button that redirects to first instructions panel
        private JButton modeButton; // button that redirects to game mode panel
        private JButton back; // back button

        private CardLayout cards; // card layout

        // create some colors to use for layout
        private Color lightBlue = new Color(100, 255, 255); // light blue color for the backgrounds
        private Color lighterBlue = new Color(200, 255, 255); // lighter blue for the box inside the backgrounds (border)
        private Color lightGray = new Color (220, 220, 220);  // light gray
        private Color lightestGray = new Color(240, 240, 240); // lighter gray than light gray
        private Color darkGray = new Color(100, 100, 100); // dark gray
        private Color darkestGray = new Color(75, 75, 75); // darker gray; best gray shade for titles
        private Color top = new Color(120, 120, 120); // same gray as one surrounding cannon (for top border)

        private final int FAR_LEFT = 220; // furthest left that any graphics will go
        private final int FAR_TOP = 150; // furthest top that any graphics will go (aside from title)
        private final int TEXT_LEFT = 535; // furthest left that text instructions will go

        private int topButton = 200; // highest place any button will go
        private int topText = 220; // highest place any text will go

        private boolean fromMain = false; // boolean that determines if skipped instructions and went to game mode panel (i.e. need to go back now)

        // booleans for determining which game mode user selected
        private boolean hueMode = true; // default mode is hueMode
        private boolean satMode = false;
        private boolean valMode = false;
        private boolean allMode = false;

        public CardHolder() {// constructor header
            // set card layout
            cards = new CardLayout();
            setLayout(cards);

            // add the panels to the card layout
            main = new MainPanel(); // landing panel for user to see name of game clearly
            instruct1 = new InstructionsPanel1(); // instruct user how to play
            instruct2 = new InstructionsPanel2(); // instruct user about how to play (cont.)
            instruct3 = new InstructionsPanel3(); // instruct user about HSV
            more1 = new SeeMore1(); // see more about hue
            more2 = new SeeMore2(); // see more about saturation
            more3 = new SeeMore3(); // see more about value
            mode = new GameMode(); // user selects game mode first
            game = new GamePanel(); // user actually plays game here
            lose = new GameLose(); // depending on if user wins or loses, redirect to this panel

            // add panels to card layout in order to swap
            add(main, "main");
            add(instruct1, "instruct1");
            add(instruct2, "instruct2");
            add(instruct3, "instruct3");
            add(more1, "see more1");
            add(more2, "see more2");
            add(more3, "see more3");
            add(mode, "mode");
            add(game, "game");
            add(lose, "lose");
        }

        public class MainPanel extends JPanel implements ActionListener { // MainPanel class header, implements Action Listener; Landing page for user, contains title

            public MainPanel() {
                setLayout(null); // set the layout as null (need to draw two buttons, but have the background remain the picture)

                modeButton = new JButton("Game Mode"); // JButton with predefined text
                modeButton.addActionListener(this); // add action listener
                modeButton.setSize(675, 30); // position sizes and location in the same way as a panel with two column gridlayout would be
                modeButton.setLocation(0, 648);
                add(modeButton); // add button to first column

                instructButton = new JButton("How To Play"); // JButton that says "How To Play"
                instructButton.addActionListener(this); // add action listener and button to panel (South)
                instructButton.setSize(675, 30); // position sizes and location in the same way as a panel with two column gridlayout would be
                instructButton.setLocation(0, 648);
                instructButton.setLocation(675, 648);
                add(instructButton); // add button to second column
            }

            public void paintComponent(Graphics g) { // paintComponent header
                setBackground(lightBlue); // set background color as light blue
                super.paintComponent(g);

                Image blurredGame = Toolkit.getDefaultToolkit().getImage("background.png"); // import background image
                g.drawImage(blurredGame, 0, 0, 1350, 700, this);

                // title = Bolded, Italicized, Helvetica, 48, white
                Font title = new Font("helvetica", Font.BOLD, 140);
                g.setFont(title);
                g.setColor(darkGray);
                g.drawString("ARCUS COELISTIS", 57, 337);
                g.drawString("RAINBOW", 347, 487);

                g.setColor(Color.WHITE);
                g.drawString("ARCUS COELISTIS", 50, 330);
                g.drawString("RAINBOW", 340, 480);

            }

            public void actionPerformed(ActionEvent e) { // actionPerformed header
                JButton source = (JButton) e.getSource(); // find source of the button being clicked
                if (source == instructButton)
                    cards.show(ch, "instruct1"); // show the instructions panel when button is pressed
                else if (source == modeButton) {
                    fromMain = true;
                    cards.show(ch, "mode"); // show the game mode panel when button is pressed
                }
            }
        }

        public class InstructionsPanel1 extends JPanel { // instructions panel header; Teaches Gameplay Part 1

            public InstructionsPanel1() { // constructor header
                setLayout(new BorderLayout()); // set the layout as Border Layout

                TwoButtonPanel buttons = new TwoButtonPanel("Back", "main", "How To Play (Part 2)", "instruct2"); // two buttons: one to move back, one to move on
                add(buttons, BorderLayout.SOUTH);
            }

            public void paintComponent(Graphics g) { // paintComponent header
                setBackground(lightBlue); // set background color as light blue
                super.paintComponent(g);

                // draw one big rectangle
                g.setColor(Color.GRAY);
                g.fillRect(180, 98, 992, 532); // give 40 by 50 border in which no graphics exist (text will be inside, however)

                Image background_text = Toolkit.getDefaultToolkit().getImage("background_text.png"); // import image of game screen with descriptions

                // draw images
                g.drawImage(background_text, 180, 100, 990, 530, this); // instruct how to score points

                // title = Bolded, Palatino, 48, gray
                g.setColor(darkestGray);
                Font title = new Font("palatino", Font.BOLD, 48);
                g.setFont(title);
                g.drawString("How To Play", 535, 70);
            }
        }

        public class InstructionsPanel2 extends JPanel { // instructions panel header; Teaches Gameplay Part 2

            public InstructionsPanel2() { // constructor header
                setLayout(new BorderLayout()); // set the layout as Border Layout

                TwoButtonPanel buttons = new TwoButtonPanel("Back", "instruct1", "HSV Explanation", "instruct3"); // two buttons: one to move back, one to move on
                add(buttons, BorderLayout.SOUTH);
            }

            public void paintComponent(Graphics g) { // paintComponent header
                setBackground(lightBlue); // set background color as light blue
                super.paintComponent(g);

                // draw one big rectangle
                g.setColor(lighterBlue);
                g.fillRect(FAR_LEFT - 40, FAR_TOP - 50, 1350 - (2 * (FAR_LEFT - 40)), 530); // give 40 by 50 border in which no graphics exist (text will be inside, however)

                // extract images
                Image aim = Toolkit.getDefaultToolkit().getImage("screenshot_aim.png"); // import image of aiming screenshot
                Image sliders = Toolkit.getDefaultToolkit().getImage("screenshot_sliders.png"); // import image of arrow

                // draw images
                g.drawImage(aim, FAR_LEFT, FAR_TOP + 55, 280, 230, this); // instruct how to score points
                g.drawImage(sliders, 220, 470, 910, 130, this);

                // draw 1 pixel sized borders around images
                g.setColor(Color.GRAY);
                g.drawRect(FAR_LEFT - 1, FAR_TOP + 54, 282, 232);
                g.drawRect(219, 469, 912, 132);

                // title = Bolded, Palatino, 48
                g.setColor(darkestGray);
                Font title = new Font("palatino", Font.BOLD, 48);
                g.setFont(title);
                g.drawString("How To Play (Part 2)", 445, 70);

                // subtitle = Bolded, Italicised, Serif, 14
                g.setColor(Color.BLACK);
                Font subtitle = new Font("serif", Font.BOLD | Font.ITALIC, 24);
                g.setFont(subtitle);
                g.drawString("Game Elements:", FAR_LEFT, FAR_TOP + 10);
                g.drawLine(FAR_LEFT, FAR_TOP + 15, FAR_LEFT + 166, FAR_TOP + 15); // hack way of underlining

                // text instructions to right of pictures = unbolded, Sans Serif, 18
                Font instruct = new Font("sansserif", Font.PLAIN, 18);
                g.setFont(instruct);
                // basic summary of gameplay
                g.drawString("In order to win, you must use your spectacular knowledge of HSV.", TEXT_LEFT, FAR_TOP + 100);
                g.drawString("Control the HSV Sliders and use the shooter to fire away!", TEXT_LEFT, FAR_TOP + 140);
                g.drawString("Hit a ball (same color) with your shooter ball, and your score will go up!", TEXT_LEFT, FAR_TOP + 180);
                g.drawString("Score 300 points to move on until you hit the final round.", TEXT_LEFT, FAR_TOP + 220);
                g.drawString("WARNING!! If you miss a shot, you will lose a life!", TEXT_LEFT, FAR_TOP + 260);
            }
        }

        public class InstructionsPanel3 extends JPanel implements ActionListener { // InstructionsPanel3 class header, implements ActionListener; Teaches HSV

            // see more buttons
            private JButton seeMore1;
            private JButton seeMore2;
            private JButton seeMore3;

            public InstructionsPanel3() { // constructor header
                setLayout(null); // null layout

                // create buttons that link to other panels
                seeMore1 = new JButton("See More (Hue)");
                seeMore2 = new JButton("See More (Saturation)");
                seeMore3 = new JButton("See More (Value)");

                // set sizes of buttons
                int width = 270; // width and height of butttons
                int height = 50;
                seeMore1.setSize(width, height);
                seeMore2.setSize(width, height);
                seeMore3.setSize(width, height);

                // set color of buttons to be transparent
                Color transparent = new Color (200, 255, 255, 0); // alpha value (last param) is 0 = transparent
                seeMore1.setBackground(transparent);
                seeMore2.setBackground(transparent);
                seeMore3.setBackground(transparent);

                // do not color border
                seeMore1.setBorderPainted(false);
                seeMore2.setBorderPainted(false);
                seeMore3.setBorderPainted(false);

                // set the font of the texts on the buttons as sansserif, plain, 18, white
                Font instruct = new Font("sansserif", Font.PLAIN, 18);
                seeMore1.setForeground(Color.BLUE);
                seeMore2.setForeground(Color.BLUE);
                seeMore3.setForeground(Color.BLUE);
                seeMore1.setFont(instruct);
                seeMore2.setFont(instruct);
                seeMore3.setFont(instruct);

                // set locations of buttons
                seeMore1.setLocation(TEXT_LEFT + 224, FAR_TOP + 320);
                seeMore2.setLocation(TEXT_LEFT + 250, FAR_TOP + 355);
                seeMore3.setLocation(TEXT_LEFT + 230, FAR_TOP + 390);

                // add action listeners
                seeMore1.addActionListener(this);
                seeMore2.addActionListener(this);
                seeMore3.addActionListener(this);

                // add buttons
                add(seeMore1);
                add(seeMore2);
                add(seeMore3);

                setLayout(new BorderLayout()); // set the layout as Border Layout

                TwoButtonPanel buttons = new TwoButtonPanel("Back", "instruct2", "Game Modes", "mode"); // two buttons: one to move back, one to move on
                add(buttons, BorderLayout.SOUTH);
            }

            public void paintComponent(Graphics g) { // paintComponent header
                setBackground(lightBlue); // set background color as light blue
                super.paintComponent(g);

                // draw one big rectangle
                g.setColor(lighterBlue);
                g.fillRect(180, 100, 990, 530); // give 40 by 50 border in which no graphics exist (text will be inside, however)

                // draw rectangle around the buttons for visualization
                Color lightRed = new Color (255, 150, 150);
                g.setColor(lightRed);
                g.fillRect(800, 460, 240, 140);

                // extract images
                Image flat_hsv = Toolkit.getDefaultToolkit().getImage("2D_hsv.gif"); // import image of aiming screenshot
                Image cylinder_hsv = Toolkit.getDefaultToolkit().getImage("3D_hsv.png"); // import image of five hearts screenshot

                // draw images
                g.drawImage(flat_hsv, FAR_LEFT + 120, FAR_TOP + 35, 330, 220, this); // demonstrate how hsv looks in a 2-D shape
                g.drawImage(cylinder_hsv, FAR_LEFT + 570, FAR_TOP + 35, 260, 220, this); // demonstrate how hsv looks in a 3-D shape

                // draw 1 pixel sized borders around images
                g.setColor(Color.GRAY);
                g.drawRect(FAR_LEFT + 119, FAR_TOP + 34, 332, 222); // border around flat_hsv
                g.drawRect(FAR_LEFT + 569, FAR_TOP + 34, 262, 222); // border around cylinder_hsv

                // subtitle = Bolded, Italicised, Serif, 14
                g.setColor(Color.BLACK);
                Font subtitle = new Font("serif", Font.BOLD | Font.ITALIC, 24);
                g.setFont(subtitle);
                g.drawString("Hue, Saturation, Value:", FAR_LEFT + 60, FAR_TOP);
                g.drawLine(FAR_LEFT + 60, FAR_TOP + 5, FAR_LEFT + 300, FAR_TOP + 5); // hack way of underlining

                // text to explain what HSV means = bolded, Sans Serif, 18, black
                Font define = new Font("sansserif", Font.BOLD, 18);
                g.setFont(define);
                g.setColor(Color.BLACK);
                g.drawString("HSV stands for hue, saturation, and value.", FAR_LEFT + 60, FAR_TOP + 315); // HSV summary

                // label above the buttons for identification
                g.setColor(Color.RED);
                g.drawString("Click below for more information!", 765, 450);

                // text for hue, Sat, Val summary pictures = unbolded, Sans Serif, 18
                Font instruct = new Font("sansserif", Font.PLAIN, 18);
                g.setFont(instruct);
                g.setColor(Color.BLACK);
                g.drawString("Hue refers to a pure color - one without tint or shade. ", FAR_LEFT + 60, FAR_TOP + 350); // 35 spacing apart
                g.drawString("Saturation refers to how how tinted a color is (faded).", FAR_LEFT + 60, FAR_TOP + 385);
                g.drawString("Value refers to how shaded a color is (dark).", FAR_LEFT + 60, FAR_TOP + 420);

                // title = Bolded, Palatino, 48, gray
                g.setColor(darkestGray);
                Font title = new Font("palatino", Font.BOLD, 48);
                g.setFont(title);
                g.drawString("What is HSV?", 535, 70);
            }

            public void actionPerformed(ActionEvent e) { // actionPerformed header
                JButton source = (JButton) e.getSource(); // find source of the button being clicked
                if (source == seeMore1) {
                    cards.show(ch, "see more1");
                } else if (source == seeMore2) {
                    cards.show(ch, "see more2");
                } else if (source == seeMore3) {
                    cards.show(ch, "see more3");
                }
            }
        }

        public class SeeMore1 extends JPanel implements ActionListener { // SeeMore1 class header, implements Action Listener; See more for hue

            private JButton back;

            public SeeMore1() {
                setLayout(new BorderLayout());

                back = new JButton("Back"); // button to go back to previous panel
                back.addActionListener(this); // add action listener
                add(back, BorderLayout.SOUTH); // add button
            }

            public void paintComponent(Graphics g) {
                setBackground(lightBlue); // set background color as light blue
                super.paintComponent(g);

                // draw one big rectangle
                g.setColor(lighterBlue);
                g.fillRect(180, 100, 990, 530); // give 40 by 50 border in which no graphics exist (text will be inside, however)

                // extract images
                Image hue_sat = Toolkit.getDefaultToolkit().getImage("hue_sat.png"); // import chart that has hue as x axis and sat as y axis
                Image hue_val = Toolkit.getDefaultToolkit().getImage("hue_val.png"); // import chart that has hue as x axis and val as y axis
                Image flat_hsv = Toolkit.getDefaultToolkit().getImage("2D_hsv.gif"); // import image of aiming screenshot
                Image cylinder_hsv = Toolkit.getDefaultToolkit().getImage("3D_hsv.png"); // import image of five hearts screenshot

                // draw images
                g.drawImage(hue_sat, FAR_LEFT + 10, FAR_TOP, 200, 200, this); // chart that has hue as x axis and sat as y axis
                g.drawImage(hue_val, FAR_LEFT + 10, FAR_TOP + 230, 200, 200, this); // chart that has hue as x axis and val as y axis
                g.drawImage(flat_hsv, FAR_LEFT + 260, FAR_TOP, 330, 220, this); // demonstrate how hsv looks in a 2-D shape
                g.drawImage(cylinder_hsv, FAR_LEFT + 640, FAR_TOP, 260, 220, this); // demonstrate how hsv looks in a 3-D shape

                // draw 1 pixel sized borders around images
                g.setColor(Color.GRAY);
                g.drawRect(FAR_LEFT + 9, FAR_TOP - 1, 202, 202); // border around hue_sat
                g.drawRect(FAR_LEFT + 9, FAR_TOP + 229, 202, 202); // border around hue_val
                g.drawRect(FAR_LEFT + 259, FAR_TOP - 1, 332, 222); // border around flat_hsv
                g.drawRect(FAR_LEFT + 639, FAR_TOP - 1, 262, 222); // border around cylinder_hsv

                // text instructions to right of pictures = unbolded, Sans Serif, 18
                Font instruct = new Font("sansserif", Font.PLAIN, 18);
                g.setFont(instruct);

                int init_pos = FAR_LEFT + 245; // initial position of the line
                g.setColor(Color.BLACK);
                g.drawString("There are 6 main hues:", init_pos, FAR_TOP + 270);
                // shift between the colors of the words
                g.setColor(Color.RED);
                g.drawString("'red, ", init_pos + 206, FAR_TOP + 270);
                g.setColor(Color.ORANGE);
                g.drawString("orange, ", init_pos + 250, FAR_TOP + 270);
                g.setColor(Color.YELLOW);
                g.drawString("yellow, ", init_pos + 325, FAR_TOP + 270);
                g.setColor(Color.GREEN);
                g.drawString("green, ", init_pos + 390, FAR_TOP + 270);
                g.setColor(Color.BLUE);
                g.drawString("blue, ", init_pos + 450, FAR_TOP + 270);
                g.setColor(new Color(127, 0, 255)); // Violet Color
                g.drawString("violet.'", init_pos + 498, FAR_TOP + 270);

                g.setColor(Color.BLACK);
                g.drawString("Hue ranges from 0 - 360 because it is represented by a circle.", init_pos, FAR_TOP + 315); // 45 spacing
                g.setColor(Color.RED); // important for gameplay
                g.drawString("This means that red is approximately 0-20 and also 340-360!", init_pos, FAR_TOP + 360);
                g.setColor(Color.BLACK);
                g.drawString("Hue is always referred to as 'hue'.", init_pos, FAR_TOP + 405);

                // title = Bolded, Palatino, 48, gray
                g.setColor(darkestGray);
                Font title = new Font("palatino", Font.BOLD, 48);
                g.setFont(title);
                g.drawString("Hue", 625, 70);
            }

            public void actionPerformed(ActionEvent e) { // actionPerformed header
                cards.show(ch, "instruct3"); // redirect to game after selecting choice
            }
        }

        public class SeeMore2 extends JPanel implements ActionListener  { // SeeMore2 class header, implements Action Listener; See more for Sat

            private JButton back;

            public SeeMore2() {
                setLayout(new BorderLayout());

                back = new JButton("Back"); // button to go back to previous panel
                back.addActionListener(this); // add action listener
                add(back, BorderLayout.SOUTH); // add button
            }

            public void paintComponent(Graphics g) {
                setBackground(lightBlue); // set background color as light blue
                super.paintComponent(g);

                // draw one big rectangle
                g.setColor(lighterBlue);
                g.fillRect(180, 100, 990, 530); // give 40 by 50 border in which no graphics exist (text will be inside, however)

                // extract images
                Image hue_sat = Toolkit.getDefaultToolkit().getImage("hue_sat.png"); // import chart that has hue as x axis and sat as y axis
                Image sat_val = Toolkit.getDefaultToolkit().getImage("sat_val.png"); // import chart that has hue as x axis and val as y axis
                Image flat_hsv = Toolkit.getDefaultToolkit().getImage("2D_hsv.gif"); // import image of aiming screenshot
                Image cylinder_hsv = Toolkit.getDefaultToolkit().getImage("3D_hsv.png"); // import image of five hearts screenshot

                // draw images
                g.drawImage(hue_sat, FAR_LEFT + 10, FAR_TOP, 200, 200, this); // chart that has hue as x axis and sat as y axis
                g.drawImage(sat_val, FAR_LEFT + 10, FAR_TOP + 230, 200, 200, this); // chart that has hue as x axis and val as y axis
                g.drawImage(flat_hsv, FAR_LEFT + 260, FAR_TOP, 330, 220, this); // demonstrate how hsv looks in a 2-D shape
                g.drawImage(cylinder_hsv, FAR_LEFT + 640, FAR_TOP, 260, 220, this); // demonstrate how hsv looks in a 3-D shape

                // draw 1 pixel sized borders around images
                g.setColor(Color.GRAY);
                g.drawRect(FAR_LEFT + 9, FAR_TOP - 1, 202, 202); // border around hue_sat
                g.drawRect(FAR_LEFT + 9, FAR_TOP + 229, 202, 202); // border around hue_val
                g.drawRect(FAR_LEFT + 259, FAR_TOP - 1, 332, 222); // border around flat_hsv
                g.drawRect(FAR_LEFT + 639, FAR_TOP - 1, 262, 222); // border around cylinder_hsv

                // text instructions to right of pictures = unbolded, Sans Serif, 18
                Font instruct = new Font("sansserif", Font.PLAIN, 18);
                g.setFont(instruct);

                int init_pos = FAR_LEFT + 245; // left most side of text
                g.setColor(Color.BLACK);
                g.drawString("Saturation ranges from 0 - 100 because it is a percentage of the hue.", init_pos, FAR_TOP + 270); // 45 spacing
                g.drawString("It goes from a shade of gray to the most pure form of the hue.", init_pos, FAR_TOP + 315);
                g.setColor(Color.RED); // important for gameplay
                g.drawString("When saturation is 0, the color is always 0 (for all hues)!", init_pos, FAR_TOP + 360);
                g.setColor(Color.BLACK);
                g.drawString("Saturation is also referred to (not completely the same) as chroma.", init_pos, FAR_TOP + 405);

                // title = Bolded, Palatino, 48, gray
                g.setColor(darkestGray);
                Font title = new Font("palatino", Font.BOLD, 48);
                g.setFont(title);
                g.drawString("Saturation", 560, 70);
            }

            public void actionPerformed(ActionEvent e) { // actionPerformed header
                cards.show(ch, "instruct3"); // redirect to game after selecting choice
            }
        }

        public class SeeMore3 extends JPanel implements ActionListener  { // SeeMore3 class header, implements Action Listener; See more for Val

            private JButton back;

            public SeeMore3() {
                setLayout(new BorderLayout());

                back = new JButton("Back"); // button to go back to previous panel
                back.addActionListener(this); // add action listener
                add(back, BorderLayout.SOUTH); // add button
            }

            public void paintComponent(Graphics g) {
                setBackground(lightBlue); // set background color as light blue
                super.paintComponent(g);

                // draw one big rectangle
                g.setColor(lighterBlue);
                g.fillRect(180, 100, 990, 530); // give 40 by 50 border in which no graphics exist (text will be inside, however)

                // extract images
                Image hue_val = Toolkit.getDefaultToolkit().getImage("hue_val.png"); // import chart that has hue as x axis and sat as y axis
                Image sat_val = Toolkit.getDefaultToolkit().getImage("sat_val.png"); // import chart that has hue as x axis and val as y axis
                Image flat_hsv = Toolkit.getDefaultToolkit().getImage("2D_hsv.gif"); // import image of aiming screenshot
                Image cylinder_hsv = Toolkit.getDefaultToolkit().getImage("3D_hsv.png"); // import image of five hearts screenshot

                // draw images
                g.drawImage(hue_val, FAR_LEFT + 10, FAR_TOP, 200, 200, this); // chart that has hue as x axis and sat as y axis
                g.drawImage(sat_val, FAR_LEFT + 10, FAR_TOP + 230, 200, 200, this); // chart that has hue as x axis and val as y axis
                g.drawImage(flat_hsv, FAR_LEFT + 260, FAR_TOP, 330, 220, this); // demonstrate how hsv looks in a 2-D shape
                g.drawImage(cylinder_hsv, FAR_LEFT + 640, FAR_TOP, 260, 220, this); // demonstrate how hsv looks in a 3-D shape

                // draw 1 pixel sized borders around images
                g.setColor(Color.GRAY);
                g.drawRect(FAR_LEFT + 9, FAR_TOP - 1, 202, 202); // border around hue_sat
                g.drawRect(FAR_LEFT + 9, FAR_TOP + 229, 202, 202); // border around hue_val
                g.drawRect(FAR_LEFT + 259, FAR_TOP - 1, 332, 222); // border around flat_hsv
                g.drawRect(FAR_LEFT + 639, FAR_TOP - 1, 262, 222); // border around cylinder_hsv

                // text instructions to right of pictures = unbolded, Sans Serif, 18
                Font instruct = new Font("sansserif", Font.PLAIN, 18);
                g.setFont(instruct);

                int init_pos = FAR_LEFT + 245; // left most side of text

                g.setColor(Color.BLACK);
                g.drawString("Value ranges from 0 - 100 because it is a percentage of the hue.", init_pos, FAR_TOP + 270); // 45 spacing
                g.drawString("Value goes from black to the most saturated form of the hue.", init_pos, FAR_TOP + 315);
                g.setColor(Color.RED); // important for gameplay
                g.drawString("When sat is 0, the color is always black to white (for all hues)!", init_pos, FAR_TOP + 360);
                g.setColor(Color.BLACK);
                g.drawString("Value is also referred to (although not completely the same) as: ", init_pos, FAR_TOP + 405);
                g.drawString("'Lightness, Intensity, Luminance, or Brightness.'", init_pos, FAR_TOP + 450);

                // title = Bolded, Palatino, 48, gray
                g.setColor(darkestGray);
                Font title = new Font("palatino", Font.BOLD, 48);
                g.setFont(title);
                g.drawString("Value", 613, 70);
            }

            public void actionPerformed(ActionEvent e) { // actionPerformed header
                cards.show(ch, "instruct3"); // redirect to game after selecting choice
            }
        }

        public class GameMode extends JPanel implements ActionListener { // GameModePanel header; panel that user arrives to and then selects game mode (after selecting, game mode just advances to next level)

            // game mode buttons
            private JButton hueButton;
            private JButton satButton;
            private JButton valButton;
            private JButton allButton;

            private int buttonWidth = 200; // dimensions of the button
            private int buttonHeight = 70;

            public GameMode() {
                setLayout(null);

                // create buttons that choose the game mode
                hueButton = new JButton("Hue Mode (easy)");
                satButton = new JButton("Saturation Mode (medium)");
                valButton = new JButton("Value Mode (medium)");
                allButton = new JButton("All Three Mode (hard)");

                // set sizes of buttons
                hueButton.setSize(buttonWidth, buttonHeight);
                hueButton.setLocation(FAR_LEFT + 150, topButton);

                satButton.setSize(buttonWidth, buttonHeight);
                satButton.setLocation(FAR_LEFT + 150, topButton + 110);

                valButton.setSize(buttonWidth, buttonHeight);
                valButton.setLocation(FAR_LEFT + 150, topButton + 220);

                allButton.setSize(buttonWidth, buttonHeight);
                allButton.setLocation(FAR_LEFT + 150, topButton + 330);

                // add action listeners
                hueButton.addActionListener(this);
                satButton.addActionListener(this);
                valButton.addActionListener(this);
                allButton.addActionListener(this);

                // add buttons
                add(hueButton);
                add(satButton);
                add(valButton);
                add(allButton);

                setLayout(new BorderLayout()); // switch from null to border layout
                TwoButtonPanel buttons = new TwoButtonPanel("Back", "instruct3", "Continue (Hue Mode)", "game"); // two buttons: one to move back, one to move on
                add(buttons, BorderLayout.SOUTH);
            }

            public void paintComponent(Graphics g) {
                setBackground(lightBlue); // set background color as light blue
                super.paintComponent(g);

                // draw one big rectangle
                g.setColor(lighterBlue);
                g.fillRect(180, 100, 990, 530); // give 40 by 50 border in which no graphics exist (text will be inside, however)

                // title = Bolded, Palatino, 48, gray
                g.setColor(darkestGray);
                Font title = new Font("palatino", Font.BOLD, 48);
                g.setFont(title);
                g.drawString("Select Game Mode", 470, 70);

                // text to describe modes = unbolded, Sans Serif, 18, black
                Font instruct = new Font("sansserif", Font.PLAIN, 18);
                g.setFont(instruct);
                g.setColor(Color.GRAY);
                g.drawString("In these game modes, the name of the mode is the only feature that is in the game.", 310, 140); // instruct to choose wisely
                g.drawString("Choose wisely, you cannot go back!", 520, 165); // instruct to choose wisely

                g.setColor(darkestGray);
                g.drawString("Hue Mode (easy): ", 650, topText); // instruct about hue only mode (i.e. what features it presents)
                g.drawString("You can only use the hue slider!", 650, topText + 25);

                g.drawString("Saturation Mode (medium): ", 650, topText + 110); // instruct about hue only mode (i.e. what features it presents)
                g.drawString("You can only use the saturation slider!", 650, topText + 135);

                g.drawString("Value Mode (medium): ", 650, topText + 220); // instruct about hue only mode (i.e. what features it presents)
                g.drawString("You can only use the value slider!", 650, topText + 245);

                g.drawString("All Mode (hard): ", 650, topText + 330); // instruct about hue only mode (i.e. what features it presents)
                g.drawString("You can use all three sliders!", 650, topText + 355);
            }

            public void actionPerformed(ActionEvent e) { // actionPerformed header
                JButton source = (JButton) e.getSource(); // find source of the button being clicked
                if (source == hueButton) { // hue only mode
                    hueMode = true; // hue mode boolean
                } else if (source == satButton) { // saturation only mode
                    hueMode = false; // set default false
                    satMode = true;
                } else if (source == valButton) { // value only mode
                    hueMode = false; // set default false
                    valMode = true;
                } else if (source == allButton) { // all three mode
                    hueMode = false; // set default false
                    allMode = true;
                }
                cards.show(ch, "game"); // redirect to game after selecting choice

                if (source == back) { // overrides the previous card show
                    if (fromMain) {
                        cards.show(ch, "main");
                        fromMain = false; // reset boolean
                    } else
                        cards.show(ch, "instruct3"); // move back to previous panel
                }
            }
        }

        public class TwoButtonPanel extends JPanel implements ActionListener { // TwoButtonPanel header; panel that holds two buttons side by side (button for going back and going forward in cards)

            private JButton button1; // left button
            private JButton button2; // right button

            // panels to redirect to
            private String panel1;
            private String panel2;

            public TwoButtonPanel(String buttonText1, String panelName1, String buttonText2, String panelName2) { // pass values of text displayed on buttons and also panels to redirect to

                panel1 = panelName1; // panels to redirect to
                panel2 = panelName2;

                setLayout(new GridLayout(1, 2)); // one row, two column layout

                button1 = new JButton(buttonText1); // JButton with predefined text
                button1.addActionListener(this); // add action listener
                add(button1); // add button to first column

                button2 = new JButton(buttonText2); // JButton with predefined text
                button2.addActionListener(this); // add action listener
                add(button2); // add button to second column
            }

            public void paintComponent(Graphics g) {
                setBackground(lightBlue); // set background color as light blue
                super.paintComponent(g);
            }

            public void actionPerformed(ActionEvent e) { // actionPerformed header
                JButton source = (JButton) e.getSource(); // find source of the button being clicked
                if (source == button1)
                    cards.show(ch, panel1); // show the instructions panel when button is pressed
                else if (source == button2)
                    cards.show(ch, panel2); // show the instructions panel when button is pressed
            }
        }

        public class GameLose extends JPanel implements ActionListener { // GameLose header, implements ActionListener; user has lost game, allow them to restart

            private JButton restart; // restart button, redirects to main panel

            public GameLose() {
                setLayout(null); // set layout as null

                restart = new JButton("Restart?");
                restart.setBackground(Color.BLACK); // black colored button
                restart.setBorderPainted(false); // do not color the border
                restart.setOpaque(true); // show background color of button
                restart.setForeground(Color.WHITE); // text is white
                restart.setSize(100, 50);
                restart.setLocation(625, 430);
                restart.addActionListener(this);
                add(restart);
            }

            public void paintComponent(Graphics g) {
                setBackground(Color.BLACK); // set background color as black
                super.paintComponent(g);

                // title = Bolded, Palatino, 48, gray
                Font title = new Font("sansserif", Font.BOLD, 100);
                g.setFont(title);
                g.setColor(Color.RED);
                g.drawString("GAME OVER", 370, 370);
            }

            public void actionPerformed(ActionEvent e){
                cards.show(ch, "main");
            }
        }

        public class GamePanel extends JPanel { // GamePanel header; holds the two game panels

            private GameScreen screen; // Panel that user actually plays game in
            private GameInput input; // Panel that user uses sliders to change ball colors

            private float h = 0; // hue color param
            private float s = 1; // sat color param
            private float v = 1; // val color param

            private Color shootColor = Color.getHSBColor(h, s, v); // color of the ball that is being shot by the user

            private int presetHue = 0; // come up with a random value of hue (for satMode and valMode of red, blue, or yellow

            private boolean fromInput = false; // screen repaint is called from the input panel
            private boolean ballChanged = false; // boolean for determining whether shooting ball has changed color
            private boolean firstTime = false; // boolean for whether repaint input panel once or not

            public GamePanel() { // constructor header
                setLayout(new BorderLayout()); // set layout as Border Layout

                // two panels, one is GameScreen, other is GameInput
                screen = new GameScreen();
                input = new GameInput();

                add(screen, BorderLayout.CENTER); // add GameScreen to Center
                add(input, BorderLayout.SOUTH); // add GameInput panel to South
            }

            public class GameScreen extends JPanel implements MouseListener, MouseMotionListener { // GameScreen panel header, implements MouseListener, MouseMotionListener

                private final int CENTER = 675; // center of the screen

                private int roundNum = 0; // number of the round
                private int mouse_x = 0; // x position of the mouse
                private int mouse_y = 0; // y position of the mouse
                private int start_x = CENTER - 25; // x-y starting positions of shooting ball
                private int start_y = 455;
                private int ball_x = CENTER - 25; // x-y position of shooting ball while moving
                private int ball_y = 455;
                private int cannon_x = CENTER; // point where the aiming line ends (nearest to cannon)
                private int cannon_y = 475;
                private int shoot_col = 0; // counter for the shooting ball method
                private int ballNum = 17; // number of random balls
                private int lifeNum = 5; // number of lives
                private int randCount = 0; // counter for random ball coordinates and colors
                private int correct = 0; // correct index after cycling through loop
                private int score = 0; // number of points that user earned, factor of 100
                private int presetFeedback = 0; // come up with a random value from 0-2 to decide what positive feedback to display after user scores a point

                private double theta = 0; // angle is the angle between the mouse coordinates and a certain point (uses arctan)
                private double angle = 0; // angle is the angle between the mouse coordinates and a certain point (uses arctan)
                private double speed = 30.0; // speed shooting ball is moving at

                private String currentMode; // displays current mode

                private boolean ballMoving = false; // boolean for determining whether shooting ball is moving

                private boolean cannonChanged = false; // boolean for determining whether cannon has rotated
                private boolean ballHit = false; // boolean for determining whether the shooting ball has hit another ball
                private boolean found = false; // boolean for determining if y coordinate of shooting ball matches one of random balls
                private boolean stop = false; // boolean for determining if x coordinate of shooting ball hit left or right side
                private boolean ballStop = false; // boolean for determining if ball has stopped, but cannon has moved
                private boolean firstGen = true; // first time of generating presetHue
                private boolean feedback = false; // user receives positive feedback

                private int[][] ballCoord; // 2D array of random ball x-y coordinates
                private int[][] shootingArr = new int[2][27]; // array containing x and y values of the shooting ball position
                private float[] arrayHSV = new float[3]; // array containing the randomly generated HSV values for the top balls
                private float[] randHSV = new float[3]; // check if HSV of random ball matches the shooter ball's HSV
                private float[] shootHSV = new float[3];
                private Color[] randomColor; // array holding color of random balls

                public GameScreen() { // opened up for initializing statistics
                    shootHSV[0] = 0; // initialize so that the strings are not null
                    shootHSV[1] = 100;
                    shootHSV[2] = 100;
                    randHSV[0] = 0;
                    randHSV[1] = 0;
                    randHSV[2] = 0;

                    addMouseListener(this); // add listeners for mouse and mouse motion
                    addMouseMotionListener(this);
                }

                public void paintComponent(Graphics g) { // paintComponent header
                    setBackground(lightBlue); // background color is light blue
                    super.paintComponent(g);

                    g.setColor(darkGray); // draw the boundary of where user can use mouse to change cannon angle
                    g.drawRect(300, 200, 750, 275);

                    // text to show color stats = unbolded, Sans Serif, 18, black
                    Font stats = new Font("sansserif", Font.PLAIN, 18);
                    g.setFont(stats);
                    g.setColor(Color.BLACK);
                    g.drawString("Shooter Ball Hue:", 350, 365);
                    g.drawString("Shooter Ball Saturation: ", 350, 390);
                    g.drawString("Shooter Ball Value:", 350, 415);
                    g.drawString((int) shootHSV[0] + "", 580, 365);
                    g.drawString((int) shootHSV[1] + "", 580, 390);
                    g.drawString((int) shootHSV[2] + "", 580, 415);

                    g.drawString("Target Ball Hue:", 738, 365);
                    g.drawString("Target Ball Saturation:", 738, 390);
                    g.drawString("Target Ball Value:", 738, 415);
                    g.drawString((int) randHSV[0] + "", 968, 365);
                    g.drawString((int) randHSV[1] + "", 968, 390);
                    g.drawString((int) randHSV[2] + "", 968, 415);

                    g.setColor(top); // draw a solid rectangle around the randomly drawn circles at the top
                    g.fillRect(150, 0, 1050, 85);

                    cannon(cannon_x - 50, cannon_y - 55, g); // draw the cannon at a certain position (based on height/width of cannon)
                    lifeBar(lifeNum, g); // draw the lifebars with the number of rounds
                    scoreBoard(score, g); // draw the scoreboard

                    if (lifeNum == 0) { // user has lost the game
                        cards.show(ch, "lose");
                        lifeNum = 5; // reset for after user restarts
                        allMode = false;
                        hueMode = true;
                        score = 0;
                    }

                    // change modes based on score and reset score/round number each time
                    if (!fromInput) { // changing color doesn't change round
                        if (hueMode) { // hue only mode
                            currentMode = "Hue Mode";
                            roundNum = 6; // only have 5 balls
                            firstTime = true;
                            if (score == 3) { // user has scored 3 balls (300 points), next game mode
                                hueMode = false; // set current mode false
                                satMode = true; // set next mode true
                                score = 0; // reset score
                                firstGen = true; // first time generating presetHue for sat mode and val mode
                                input.repaint(); // repaint the labels and sliders
                            }
                        } else if (satMode) { // saturation only mode
                            currentMode = "Saturation Mode";
                            roundNum = 6; // only have 5 balls
                            firstTime = true;
                            if (score == 3) { // user has scored 3 balls (300 points), next game mode
                                satMode = false; // set current mode false
                                valMode = true; // set next mode true
                                score = 0; // reset score
                                firstGen = true; // first time generating presetHue for sat mode and val mode
                                input.repaint(); // repaint the labels and sliders
                            }
                        } else if (valMode) { // value only mode
                            currentMode = "Value Mode";
                            roundNum = 6; // only have 5 balls
                            firstTime = true;
                            if (score == 3) { // user has scored 3 balls (300 points), next game mode
                                valMode = false; // set current mode false
                                allMode = true; // set next mode true
                                score = 0; // reset score
                                roundNum = 0; // 17 balls
                                input.repaint(); // repaint the labels and sliders
                            }
                        } else if (allMode) { // all three modes
                            currentMode = "All Mode";
                            if (score >= 9) { // user has scored 9 balls (900 points)
                                g.drawString("YOU WIN! CONGRATULATIONS!", 484, 160);
                                roundNum = 0; // from now on, constantly 17 balls
                            }
                        }
                    }
                    fromInput = false; // if next time is from input, fromInput = true. Otherwise, fromInput = false

                    // display current game mode, font = Bolded, SansSerif, 24, gray
                    g.setColor(darkestGray);
                    Font title = new Font("sansserif", Font.BOLD, 24);
                    g.setFont(title);
                    g.drawString("Current Mode: ", 30, 160);

                    // change color to red and output the game mode
                    Color darkRed = new Color (200, 0, 0);
                    g.setColor(darkRed);
                    g.drawString(currentMode, 220, 160);
                    if (score == 0) // beginning of round
                        g.drawString("New Round", 604, 271); // alert user that round has changed

                    if (feedback) { // present positive feedback to user
                        if (presetFeedback == 0) // returns the greatest integer less than or greater than the random values from 0 (inclusive) - 3 (exclusive)
                            g.drawString("Nice Shot!", 614, 160); // give positive feedback to user
                        else if (presetFeedback == 1) // returns the greatest integer less than or greater than the random values from 0 (inclusive) - 3 (exclusive)
                            g.drawString("Amazing!", 614, 160); // give positive feedback to user
                        else if (presetFeedback == 2) // returns the greatest integer less than or greater than the random values from 0 (inclusive) - 3 (exclusive)
                            g.drawString("Wow! You are good!", 551, 160); // give positive feedback to user
                    } else
                        g.drawString("DO NOT CLICK OUTSIDE THE BOX", 465, 160); // alert user that they must click inside the box

                    g.setColor(shootColor); // set color of the shooting ball
                    if (ballMoving) { // ball has been shot
                        ballShoot(angle, g);  // draw the shooting ball
                        redraw_randomBall(g); // redraw the random balls at the top
                    } else if ((ballChanged) || (cannonChanged && !ballStop)) { // color of ball is being changed or ball is moving and cannon is being changed
                        ballGenerate(g, start_x, start_y); // draw the shooting ball
                        redraw_randomBall(g); // redraw the random balls at the top
                        cannonChanged = false; // cannon is not being altered
                        ballChanged = false; // ball color is not being altered
                    } else { // first round, default conditions
                        ballStop = false; // necessary instead of ballMoving because needs to only happen once (i.e. right after ball has stopped, if cannon is moved, then it doesn't regenerate random balls)
                        ballGenerate(g, start_x, start_y); // draw the shooting ball
                        randomBall(roundNum, g); // draw the random balls at the top, round 0
                    }
                }

                /**
                 * randomBall method
                 * parameters are the round number and graphics
                 * starts with 17, goes down by 4 every 2 points
                 * until 8 points is scored, and then stays at 1
                 */
                public void randomBall(int roundNum, Graphics g) {
                    if (roundNum % 2 != 0) { // round number is odd
                        roundNum -= 1;
                    }
                    if (roundNum <= 8) { // round number until 8
                        ballNum = 17 - (roundNum * 2); // number of balls starts by 17 and goes down by 4 each round
                    } else {
                        ballNum = 1;
                    }

                    int x = 165 + (roundNum * 60); // starting x and y coordinates depends on round number
                    int y = 20;

                    ballCoord = new int[2][ballNum]; // array with 17 indexes for the 17 ball coordinates
                    randomColor = new Color[ballNum];

                    for (int col = 0; col < ballNum; col++) { // 17 columns
                        for (int row = 0; row < 2; row++) { // 2 rows
                            if (row == 0) { // x coordinates
                                ballCoord[0][col] = x;
                                x += 60; // width of the ball
                            } else if (row == 1) { // y coordinates
                                ballCoord[1][col] = y;
                            }
                        }
                    }

                    // generate random hsv colors and store them in an array
                    for (int index = 0; index < ballNum; index++) {
                        randomHSV(); // randomHSV generates random H, S, and V values and stores them in arrayHSV
                        Color randomBall = Color.getHSBColor(arrayHSV[0], arrayHSV[1], arrayHSV[2]); // generate a new color that is based on completely random RGB values
                        randomColor[index] = randomBall;
                    }

                    // draw the balls with the correct color and x-y positions
                    for (int counter = 0; counter < ballNum; counter++) {
                        x = ballCoord[0][counter]; // set the x and y coordinates of the balls so that they are placed in a line
                        y = ballCoord[1][counter];

                        g.setColor(randomColor[counter]);
                        ballGenerate(g, x, y); // draw the ball with the right colors and coordinates
                    }
                }

                /**
                 * redraw_randomBall method
                 * parameters are the round number and Graphics
                 * necessary when ball is moving because the balls need to possess the same colors
                 */
                public void redraw_randomBall(Graphics g) {
                    int redraw_x = 0;
                    int redraw_y = 0;

                    for (int col = 0; col < ballNum; col++) { // 17 columns
                        Color redrawColor;
                        redrawColor = randomColor[col]; // take the color of the ball from the array used to construct the colors

                        for (int row = 0; row < 2; row++) { // 2 rows
                            if (row == 0) {
                                redraw_x = ballCoord[0][col];
                            } else if (row == 1) {
                                redraw_y = ballCoord[1][col];
                            }
                        }
                        g.setColor(redrawColor);
                        ballGenerate(g, redraw_x, redraw_y);
                    }
                }

                /**
                 * randomHSV method
                 * no parameters
                 * generates an array of HSV values
                 * 0th index = hue, 1th index = saturation, 2th = value
                 */
                public void randomHSV() {
                    if (satMode || valMode) { // only generate the hue if satMode or valMode
                        if (firstGen) { // only make presetHue once
                            firstGen = false;

                            // cycle through random ints 1 - 3 to determine if hue will be red, yellow, or blue
                            presetHue = (int) (Math.floor(Math.random() * 3)); // returns the greatest integer less than or greater than the random values from 0 (inclusive) - 3 (exclusive)

                            if (presetHue == 0)
                                arrayHSV[0] = 0; // hue value of red
                            else if (presetHue == 1)
                                arrayHSV[0] = (float)(60) / 360; // hue value of yellow
                            else if (presetHue == 2)
                                arrayHSV[0] = (float)(240) / 360; // hue value of blue

                            shootColor = Color.getHSBColor(arrayHSV[0], 1, 1); // repaint the shooter ball so that it has the correct hue values
                            repaint(); // repaint screen so that the shooter ball has correct color
                        }
                    }

                    if (hueMode) { // user can only change hue. sat and val = 1 (max)
                        arrayHSV[0] = (float)(Math.random());  // returns a random integer from 0 (inclusive) - 1 (exclusive)
                        arrayHSV[1] = 1; // saturation and value are constant (max)
                        arrayHSV[2] = 1;
                    } else if (satMode) { // user can only change sat. hue and val are constants
                        arrayHSV[1] = (float)(Math.random());  // returns a random integer from 0 (inclusive) - 1 (exclusive)
                        arrayHSV[2] = 1; // value is constant (max)
                    } else if (valMode) { // user can only change val. hue and sat are constants
                        arrayHSV[1] = 1; // saturation is constant (max)
                        arrayHSV[2] = (float)(Math.random());  // returns a random integer from 0 (inclusive) - 1 (exclusive)
                    } else if (allMode) { // user can change all three values
                        arrayHSV[0] = (float)(Math.random());  // returns a random integer from 0 (inclusive) - 1 (exclusive)
                        arrayHSV[1] = (float)(Math.random() * (1 - 0.15f) + 0.15f);  // returns a random integer from 0.15 (inclusive) - 1 (exclusive). Necessary because 0-0.15 looks too white
                        arrayHSV[2] = (float)(Math.random() * (1 - 0.2f) + 0.2f);  // returns a random integer from 0.2 (inclusive) - 1 (exclusive). Necessary because 0-0.2 looks too black
                    }
                }

                /**
                 * ballShoot method
                 * parameters are angle, x, y starting coord, and Graphics
                 */
                public void ballShoot(double angle, Graphics g) {
                    // put the x-y coordinates into an array
                    shootingArr[0][shoot_col] = ball_x;
                    shootingArr[1][shoot_col] = ball_y;

                    double shoot_width = speed * Math.sin(angle); // the x distance that the ball travels is cosine of the angle multiplied by the speed
                    double shoot_height = speed * Math.cos(angle); // the y distance that the ball travels is sine of the angle multiplied by the speed

                    if (angle > Math.PI / 2) { // mouse is in second quadrant
                        shoot_width = -1 * shoot_width; // in second quadrant, sine is positive, but should be negative
                        shoot_height = Math.abs(shoot_height); // in second quadrant, cosine is negative, but should be positive
                    }

                    // change the coordinates of the ball appropriately
                    ball_x += shoot_width;
                    ball_y -= shoot_height;

                    ballGenerate(g, ball_x, ball_y); // draw the ball

                    try { // slow down the rate that the balls move by 50 milliseconds
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        System.out.println("Error: " + ex);
                    }

                    if (shoot_col < 26) { // array (possible movement of ball) is only 50 spaces big
                        shoot_col++; // iterate through the 2D array
                        repaint(); // stops repainting when loop has ended
                    } else { // ball has stopped
                        ball_x = start_x; // reset shooting ball positions
                        ball_y = start_y;

                        ballCheck(g); // check if the shooting ball has hit a random ball
                        shoot_col = 0; // reset loop counter
                        ballMoving = false; // ball has stopped moving
                        ballStop = true; // ball has stopped
                        found = false; // ball has stopped, reset boolean that determines if ball hit top
                        stop = false; // ball has stopped, reset boolean that determines if ball has hit sides
                        ballHit = false; // set boolean to false for next shot
                        repaint();
                    }
                }

                /**
                 * ballGenerate method
                 * parameters are Graphics, and x-y positions
                 */
                public void ballGenerate(Graphics g, int x, int y) {
                    g.fillOval(x, y, 50, 50); // draw the 50 by 50 ball
                    Color outline = new Color(150, 150, 150);
                    g.setColor(outline); // draw a gray outline
                    g.drawOval(x, y, 50, 50);
                }

                /**
                 * ballCheck method
                 * parameter is Graphics
                 */
                public void ballCheck(Graphics g) {
                    for (int col = 0; col < shoot_col; col++) { // all the coordinates of the shot ball
                        if ((shootingArr[1][col] > 0 && shootingArr[1][col] < 40) || (found)) { // y coordinate of shooting ball must match the random ball y coordinate (20)
                            if (!found) {
                                correct = col; // correct index is col
                                found = true; // found the index containing the correct y coordinate
                            }
                            for (randCount = 0; randCount < ballNum; randCount++) {
                                if ((shootingArr[0][correct] > ballCoord[0][randCount] - 25) && (shootingArr[0][correct] < ballCoord[0][randCount] + 25)) { // check if shooting ball matches x coordinates of each random ball
                                    getHSVComponents(randomColor[randCount], randHSV); // get the HSV values of the shooting ball color and the random ball color
                                    getHSVComponents(shootColor, shootHSV);

                                    if ((randHSV[0] >= 340) && (randHSV[0] <= 360))
                                        randHSV[0] = 360 - randHSV[0]; // scale of 20 - 0 from 340 - 360, necessary because 340-360 look same as 0-20

                                    // check if ball that was hit and the shooting ball have similar colors (depends on mode)
                                    if (hueMode) {
                                        if (shootHSV[0] > randHSV[0] - 10 && shootHSV[0] < randHSV[0] + 10) { // hue threshold of plus or minus 10
                                            ballHit = true;
                                        }
                                    } else if (satMode) {
                                        if (shootHSV[1] > randHSV[1] - 10 && shootHSV[1] < randHSV[1] + 10) { // sat threshold of plus or minus 10
                                            ballHit = true;
                                        }
                                    } else if (valMode) {
                                        if (shootHSV[2] > randHSV[2] - 10 && shootHSV[2] < randHSV[2] + 10) { // val threshold of plus or minus 10
                                            ballHit = true;
                                        }
                                    } else if (allMode) {
                                        if (shootHSV[0] > randHSV[0] - 15 && shootHSV[0] < randHSV[0] + 15) { // hue threshold of plus or minus 15
                                            if (shootHSV[1] > randHSV[1] - 25 && shootHSV[1] < randHSV[1] + 25) { // sat threshold of plus or minus 25
                                                if (shootHSV[2] > randHSV[2] - 25 && shootHSV[2] < randHSV[2] + 25) { // val threshold of plus or minus 25
                                                    ballHit = true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (ballHit && (!stop)) { // ball has hit and has same color as the one it hit
                                stop = true; // stop checking if ball has missed
                                presetFeedback = (int) (Math.floor(Math.random() * 3)); // set the presetFeedback here
                                feedback = true; // user receives positive feedback
                                score++; // increase score
                                if (allMode) // other modes have constant number of balls
                                    roundNum++; // increase the round to next round
                            }
                        }
                        if ((col == (shoot_col - 1)) && (!ballHit) && (!stop)) { // end of loop, shooting ball has not hit other balls
                            stop = true; // stop checking if ball has missed
                            lifeNum--; // decrease the amount of lives by one because user missed
                        }
                    }
                }

                /**
                 * getHSVComponents method
                 * parameters are the color and the float array with 3 indices that will hold the hsv values
                 */
                public void getHSVComponents(Color input, float[] array) {
                    int valueR;
                    int valueG;
                    int valueB;

                    valueR = input.getRed(); // get the RGB values of the random ball
                    valueG = input.getGreen();
                    valueB = input.getBlue();

                    Color.RGBtoHSB(valueR, valueG, valueB, array); // turn the RGB values into an array with HSV values

                    // scale them appropriately
                    array[0] *= 360; // hue: 0 to 360
                    array[1] *= 100; // sat: 0 to 100
                    array[2] *= 100; // val: 0 to 100
                }

                /**
                 * cannon method
                 * parameters are x, y coordinates and Graphics
                 * includes the line of sight projecting from shooter
                 */
                public void cannon(int can_x, int can_y, Graphics g) {
                    Image cannon = Toolkit.getDefaultToolkit().getImage("cannon_pointer.png"); // import image of cannon

                    // use AffineTransform to rotate images
                    Graphics2D g2 = (Graphics2D) g; // cast previous graphics into a new graphics
                    AffineTransform saveG = g2.getTransform();
                    AffineTransform at = AffineTransform.getRotateInstance(theta - Math.PI / 2, cannon_x, cannon_y); // last two parameters control position of anchor point
                    g2.setTransform(at); // set the graphic as the rotated instance
                    g2.drawImage(cannon, can_x, can_y, 1000, 100, this); // draw the cannon image at specified coordinates w:133, h:110 1.21 ratio
                    g2.setTransform(saveG); // reset the graphic as the one before rotating
                }

                /**
                 * lifeBar method
                 * parameters are lifeNum (# of lives) and graphics
                 * lives do not replenish, user starts with 5
                 * loses 1 life per miss
                 */
                public void lifeBar(int lifeNum, Graphics g) {
                    Image full = Toolkit.getDefaultToolkit().getImage("heart_full.png"); // import image of full heart
                    Image empty = Toolkit.getDefaultToolkit().getImage("heart_empty.png"); // import image of empty heart
                    lifeNum -= 5; // range of -5 to 0

                    int fullHeart = 0; // number of full hearts

                    for (int xpos = 812; xpos < 912 + lifeNum * 20; xpos += 20) { // 5 hearts, number of hearts goes down when lifeNum goes down
                        g.drawImage(full, xpos, 250, 20, 20, this); // draw the hearts to depict lifebars
                        fullHeart++;
                    }

                    for (int xpos = 892; xpos > 792 + fullHeart * 20; xpos -= 20) {
                        g.drawImage(empty, xpos, 250, 20, 20, this); // draw the hearts to depict lifebars
                    }
                }

                /**
                 * scoreBoard method
                 * parameters are the score and graphics
                 */
                public void scoreBoard(int score, Graphics g) {
                    score *= 100; // make score multiples of 100

                    // score font = Dialog, Bold, 24, gray
                    Font scoreFont = new Font("dialog", Font.BOLD, 24);
                    g.setFont(scoreFont);
                    g.setColor(darkestGray);
                    g.drawString("Score: " + score, 417, 271); // draw the score
                    if (score == 0) // score is default 0
                        g.drawRect(397, 240, 140, 45); // smaller rectangle otherwise it looks awkward
                    else if (score < 10000) // score is 3-4 digits
                        g.drawRect(397, 240, 185, 45);
                    else // score is 5 digits
                        g.drawRect(397, 240, 195, 45);
                }

                // mouse motion methods for when user controls the angle of the cannon
                public void mouseDragged(MouseEvent e) { // allow user to shoot in mouse drag as well to make it faster
                    requestFocus();

                    mouse_x = e.getX(); // get the mouse x-y coordinates
                    mouse_y = e.getY();

                    if (!ballMoving) { // ball should not be moving while clicking, otherwise ball will change direction
                        if ((mouse_x >= 300) && (mouse_x <= 1050)) { // do not do anything unless the mouse is inside these coordinates
                            if ((mouse_y >= 200) && (mouse_y <= 475)) {
                                double shoot_x = mouse_x - cannon_x; // x distance between mouse point and cannon rotating point
                                double shoot_y = cannon_y - mouse_y; // y distance between mouse point and cannon rotating point

                                angle = Math.atan(shoot_x / shoot_y); // find the angle needed for shooting the ball. Add 0.09 radians because cannon is always offset by that amount

                                if (angle < 0) {
                                    angle += Math.PI; // add pi radians (180 deg) to make angle positive
                                }

                                ballMoving = true;
                                shoot_col = 0; // reset loop counter
                                repaint();
                            }
                        }
                    }
                }

                public void mouseMoved(MouseEvent e) {
                    requestFocus();

                    mouse_x = e.getX(); // get the mouse x-y coordinates
                    mouse_y = e.getY();

                    if ((mouse_x >= 300) && (mouse_x <= 1050)) { // do not do anything unless the mouse is inside these coordinates
                        if ((mouse_y >= 200) && (mouse_y <= 475)) {
                            double shoot_x = mouse_x - cannon_x; // x distance between mouse point and cannon rotating point
                            double shoot_y = cannon_y - mouse_y; // y distance between mouse point and cannon rotating point

                            theta = Math.atan(shoot_x / shoot_y); // find the angle needed to rotate cannon.

                            cannonChanged = true;

                            repaint();
                        }
                    }
                }

                // mouse() methods for when user clicks to shoot the ball
                public void mousePressed(MouseEvent e) {} // mousePressed method

                public void mouseReleased(MouseEvent e) {} // mouseReleased method

                public void mouseClicked(MouseEvent e) { // mouseClicked method
                    requestFocus();

                    feedback = false; // make the positive feedback from the previous shot disappear

                    mouse_x = e.getX(); // get the mouse x-y coordinates
                    mouse_y = e.getY();

                    if (!ballMoving) { // ball should not be moving while clicking, otherwise ball will change direction
                        if ((mouse_x >= 300) && (mouse_x <= 1050)) { // do not do anything unless the mouse is inside these coordinates
                            if ((mouse_y >= 200) && (mouse_y <= 475)) {
                                double shoot_x = mouse_x - cannon_x; // x distance between mouse point and cannon rotating point
                                double shoot_y = cannon_y - mouse_y; // y distance between mouse point and cannon rotating point

                                angle = Math.atan(shoot_x / shoot_y); // find the angle needed for shooting the ball. Add 0.09 radians because cannon is always offset by that amount

                                if (angle < 0) {
                                    angle += Math.PI; // add pi radians (180 deg) to make angle positive
                                }

                                ballMoving = true;

                                repaint();
                            }
                        }
                    }
                }

                public void mouseEntered(MouseEvent e) {} // mouseEntered method

                public void mouseExited(MouseEvent e) {} // mouseExited method
            }

            public class GameInput extends JPanel implements ChangeListener { // GameInput panel header, implements ChangeListener

                // three JSliders that user can use to change color (HSV)
                private JSlider hue;
                private JSlider sat;
                private JSlider val;

                // three JLabels so that user can identify which slider is which
                private JLabel hueLabel;
                private JLabel satLabel;
                private JLabel valLabel;

                // placeholder buttons that just color the backgrounds for the input section
                private JButton labelPlaceH;
                private JButton labelPlaceS;
                private JButton labelPlaceV;
                private JButton sliderPlaceH;
                private JButton sliderPlaceS;
                private JButton sliderPlaceV;

                public GameInput() { // constructor header
                    setLayout(new GridLayout(3, 2, 0, 5)); // set layout as Grid Layout

                    // create 3 JSliders that control HSV input
                    hue = new JSlider(JSlider.HORIZONTAL, 0, 360, 0); // hue is scale 0-255
                    sat = new JSlider(JSlider.HORIZONTAL, 0, 100, 100); // sat is scale 0-100
                    val = new JSlider(JSlider.HORIZONTAL, 0, 100, 100); // val is scale 0-100

                    // sliders have big ticks and small ticks
                    hue.setMajorTickSpacing(25); // ticks are at multiples of 25
                    sat.setMajorTickSpacing(10); // ticks are at multiples of 10
                    val.setMajorTickSpacing(10);
                    hue.setMinorTickSpacing(5); // ticks are at multiples of 5
                    sat.setMinorTickSpacing(1); // ticks are at multiples of 1
                    val.setMinorTickSpacing(1);

                    // knob snaps to nearest tick
                    hue.setSnapToTicks(true);
                    sat.setSnapToTicks(true);
                    val.setSnapToTicks(true);

                    // paints the ticks onto the slider
                    hue.setPaintTicks(true);
                    sat.setPaintTicks(true);
                    val.setPaintTicks(true);

                    // paints the number labels onto the slider
                    hue.setPaintLabels(true);
                    sat.setPaintLabels(true);
                    val.setPaintLabels(true);

                    // create 6 placeholder buttons that color the backgrounds for the input section
                    labelPlaceH = new JButton();
                    labelPlaceS = new JButton();
                    labelPlaceV = new JButton();
                    sliderPlaceH = new JButton();
                    sliderPlaceS = new JButton();
                    sliderPlaceV = new JButton();

                    // do not paint the borders of the buttons
                    labelPlaceH.setBorderPainted(false);
                    labelPlaceS.setBorderPainted(false);
                    labelPlaceV.setBorderPainted(false);
                    sliderPlaceH.setBorderPainted(false);
                    sliderPlaceS.setBorderPainted(false);
                    sliderPlaceV.setBorderPainted(false);

                    // set the colors of the buttons in order to make them look like placeholders
                    labelPlaceH.setBackground(lightGray);
                    labelPlaceS.setBackground(lightGray);
                    labelPlaceV.setBackground(lightGray);
                    sliderPlaceH.setBackground(lightGray);
                    sliderPlaceS.setBackground(lightGray);
                    sliderPlaceV.setBackground(lightGray);

                    // set placeholders opaque so that the background colors are visible
                    labelPlaceH.setOpaque(true);
                    labelPlaceS.setOpaque(true);
                    labelPlaceV.setOpaque(true);
                    sliderPlaceH.setOpaque(true);
                    sliderPlaceS.setOpaque(true);
                    sliderPlaceV.setOpaque(true);

                    // create 3 JLabels that label each slider
                    hueLabel = new JLabel("Hue: " + (int) h);
                    satLabel = new JLabel("Saturation: " + (int) s * 100);
                    valLabel = new JLabel("Value: " + (int) v * 100);

                    // set horizontal alignment for the 3 labels
                    hueLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    satLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    valLabel.setHorizontalAlignment(SwingConstants.CENTER);

                    // add sliders and labels to panel
                    add(hueLabel);
                    add(hue);
                    add(satLabel);
                    add(sat);
                    add(valLabel);
                    add(val);

                    // add change listeners to sliders
                    hue.addChangeListener(this);
                    sat.addChangeListener(this);
                    val.addChangeListener(this);
                }

                public void paintComponent(Graphics g) { // paintComponent header
                    setBackground(lightestGray);
                    super.paintComponent(g);

                    if (firstTime) {
                        firstTime = false;
                        if (hueMode) { // hue only mode, show hue label and slider in center
                            updateUI(); // need to update the UI before moving on
                            removeAll(); // remove all the components

                            // add the components
                            add(labelPlaceH);
                            add(sliderPlaceH);
                            add(hueLabel); // ones that the user sees
                            add(hue);
                            add(labelPlaceV);
                            add(sliderPlaceV);
                        } else if (satMode) { // saturation only mode, show sat label and slider in center
                            updateUI(); // need to update the UI before moving on
                            removeAll(); // remove all the components

                            // add the components
                            add(labelPlaceH);
                            add(sliderPlaceH);
                            add(satLabel); // ones that the user sees
                            add(sat);
                            add(labelPlaceV);
                            add(sliderPlaceV);
                        } else if (valMode) { // value only mode, show value label and slider in center
                            updateUI(); // need to update the UI before moving on
                            removeAll(); // remove all the component

                            // add the components
                            add(labelPlaceH);
                            add(sliderPlaceH);
                            add(valLabel); // one that the user sees
                            add(val);
                            add(labelPlaceV);
                            add(sliderPlaceV);
                        } else if (allMode) { // show all three sliders and labels
                            updateUI(); // need to update the UI before moving on
                            removeAll(); // remove all the components

                            // add the components
                            add(hueLabel);
                            add(hue);
                            add(satLabel);
                            add(sat);
                            add(valLabel);
                            add(val);
                        }
                    }
                }

                public void stateChanged(ChangeEvent e) { // stateChanged header; change the color of the ball being shot in the cannon by using the three HSV sliders
                    ballChanged = true; // shooting ball is being changed
                    fromInput = true; // dont change the round if screen is being repainted from this class

                    JSlider source = (JSlider) e.getSource(); // find source of the slider being changed, change color based on source
                    if (source == hue) { // hue slider
                        h = hue.getValue(); // h is the value of hue slider

                        if (hueMode) { // if hue mode, set saturation and value to max (least influenced)
                            s = 1;
                            v = 1;
                        }

                        hueLabel.setText("hue: " + (int) h); // rewrite the label
                        h = h / 360; // h must be between 0-1 not 0-360
                    } else if (source == sat) { // sat slider
                        s = sat.getValue(); // s is the value of sat slider

                        if (satMode) { // if saturation mode, need to match the hue value to the ones of the random balls
                            if (presetHue == 0)
                                h = 0; // hue value of red
                            else if (presetHue == 1)
                                h = (float) (60) / 360; // hue value of yellow
                            else if (presetHue == 2)
                                h = (float) (240) / 360; // hue value of blue
                        }

                        satLabel.setText("Sat: " + (int) s); // rewrite the label
                        s = s / 100; // s must be between 0-1 not 0-100
                    } else if (source == val) { // val slider
                        v = val.getValue(); // v is the value of val slider

                        if (valMode) { // if value mode, need to match the hue value to the ones of the random balls
                            if (presetHue == 0)
                                h = 0; // hue value of red
                            else if (presetHue == 1)
                                h = (float)(60) / 360; // hue value of yellow
                            else if (presetHue == 2)
                                h = (float)(240) / 360; // hue value of blue
                        }

                        valLabel.setText("Val: " + (int) v); // rewrite the label
                        v = v / 100; // v must be between 0-1 not 0-100
                    }

                    shootColor = Color.getHSBColor(h, s, v); // change color of the ball based on sliders
                    screen.repaint(); // repaint the game screen in order to change color of the shooter ball
                }
            }
        }
    }
}
