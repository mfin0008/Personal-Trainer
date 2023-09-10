package display;

import main.App;
import users.Client;
import users.ClientManager;

import javax.swing.JOptionPane;

/**
 * Dialogue used to prompt the user for their username when they log in.
 */
public class LoginDialogue {

    LoginDialogue(App app, boolean firstLogin) {
        Client client = null;
        boolean loginSuccessful = false;
        while (!loginSuccessful) {
            // continue prompting the user until they produce a successful login
            String username = JOptionPane.showInputDialog("Enter your username: ");
            if (username == null && !firstLogin) {
                // if the user backs out of the dialogue (and they have already logged in for the session) close the dialogue
                return;
            }
            try {
                // try and fetch the user
                client = ClientManager.getInstance().getClientFromUsername(username);
            } catch (NullPointerException e) {
                // if the user entered an invalid username, reopen the dialogue
                continue;
            }
            if (client != null) {
                // if the username is valid, set the current user in the App instance and close the dialogue
                loginSuccessful = true;
                app.setCurrentUserID(client.getClientID());
            }
        }
    }
}