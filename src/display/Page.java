package display;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;

/**
 * Abstract class holding common attributes and functionalities across the app's pages
 */
public abstract class Page extends JFrame {
    /**
     * the width of the display window
     */
    private final int screenWidth = 1400;
    /**
     * the height of the display window
     */
    private final int screenHeight = 950;
    /**
     * the height of the common header across each Page child
     */
    private final int headerHeight = 80;
    /**
     * the height of the common title across each Page child
     */
    private final int titleHeight = 100;
    /**
     * The common background colour across each Page child
     */
    private final Color backgroundColor = new Color(233, 218, 196);
    /**
     * The colour of the header bar (and other secondary elements) across each Page child
     */
    private final Color headerColor = new Color(172, 142, 96);
    /**
     * Font used for the main (usually title text) in each Page child
     */
    private final Font headerFont = new Font("Verdana", Font.BOLD, 35);
    /**
     * Font used for the plain text in each Page child
     */
    private final Font basicFont = new Font("verdana", Font.PLAIN, 25);
    /**
     * Smaller font used for the buttons (and other small sections) in each Page child
     */
    private final Font buttonFont = new Font("Verdana", Font.PLAIN, 15);
    /**
     * DisplayController instance each page can access to control navigation
     */
    private final DisplayController displayController;

    Page(DisplayController displayController) {
        this.displayController = displayController;
        this.setTitle("FITMate"); // sets title of frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit out of application
        this.setResizable(false); // prevent frame from being resized
        this.setLayout(null); // allows me to manually position elements on the screen
        this.setSize(screenWidth, screenHeight); //Set the width and height of the JFrame.
        this.getContentPane().setBackground(getBackgroundColor()); // change colour of background
        // create the app header
        AppHeader header = new AppHeader(displayController, getHeaderColor(), getHeaderFont(), getButtonFont());
        header.setBounds(0, 0, screenWidth, headerHeight);
        this.add(header);
    }

    /**
     * Getter method to return the display controller instance
     * @return the display controller instance
     */
    public DisplayController getDisplayController() {
        return displayController;
    }

    /**
     * Common method to be used by the Page children which generates the title panel for their displays
     * @param labelText: The text to be used in the title
     * @param page: the page child that the title panel is being created for
     */
    public void initialiseTitlePanel(String labelText, Page page) {
        // add title
        JLabel title = new JLabel(labelText, SwingConstants.CENTER);
        title.setFont(getHeaderFont());
        title.setBounds(0, headerHeight, getContentWidth(), titleHeight);
        page.add(title);
    }

    /**
     * Abstract method implemented by all Page children.
     * Instantiates all the panels required for the page's display.
     * Called by the DisplayController method which opens the desired page.
     */
    public abstract void initialisePanels();

    /**
     * Getter method to return the common background colour
     * @return the common background colour
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Getter method to return the common header (secondary) colour
     * @return the common header (secondary) colour
     */
    public Color getHeaderColor() {
        return headerColor;
    }

    /**
     * Getter method to return the common line border used across each Page child
     * @return the common line border
     */
    public LineBorder getLineBorder() {
        return new LineBorder(getHeaderColor(), 2);
    }

    /**
     * Getter method to return the height at which the content on a Page child should start
     * @return The height at which the content should start
     */
    public int getContentStartHeight() {
        // the content should start underneath both the header and the title panels
        return headerHeight + titleHeight;
    }

    /**
     * Getter method to return how high the page's content can be
     * @return how high the page's content (exc. header and title) can be
     */
    public int getContentHeight() {
        return screenHeight - headerHeight - titleHeight;
    }

    /**
     * Getter method to return how wide the page's content can be
     * @return how wide the page's content can be
     */
    public int getContentWidth() {
        return screenWidth;
    }

    /**
     * Getter method to return the font used in a Page child's header text
     * @return the font used in a Page child's header text
     */
    public Font getHeaderFont() {
        return headerFont;
    }

    /**
     * Getter method to return the font used in a Page child's basic text
     * @return the font used in a Page child's basic text
     */
    public Font getBasicFont() {
        return basicFont;
    }

    /**
     * Getter method to return the font used in a Page child's button text
     * @return the font used in a Page child's button text
     */
    public Font getButtonFont() {
        return buttonFont;
    }
}
