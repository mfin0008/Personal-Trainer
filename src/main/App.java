package main;

import display.DisplayController;

/**
 * Contains session data for the user
 * Instantiates the display controller
 */
public class App {

    /**
     * The id of the client who is currently logged in
     */
    private int currentUserID;
    App() {
        new DisplayController(this);
    }

    /**
     * Setter method for the user who is currently logged in
     * @param userID: The user who just logged in
     */
    public void setCurrentUserID(int userID) {
        currentUserID = userID;
    }

    /**
     * Getter method for the user who is currently logged in
     * @return the user who is currently logged in
     */
    public int getCurrentUserID() {
        return currentUserID;
    }

}
