package display;

import users.Client;
import users.ClientManager;
import users.Trainer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.util.ArrayList;

/**
 * Page that shows the logged in client a list of their connected trainers.
 * Gives them the option to open that connection or to find new ones.
 */
public class ClientDashboardPage extends Page {

    /**
     * Stores the currently logged in client
     */
    private final Client client;
    private final int buttonWidth = 200;
    private final int marginX = 50;

    ClientDashboardPage(Client client, DisplayController displayController) {
        super(displayController);
        this.client = client;
    }

    /**
     * Initialises the connected trainers cards (if any).
     */
    private void initialiseConnectionsPanel() {
        // fetch the list of connected trainers
        ArrayList<Trainer> connectedTrainers = ClientManager.getInstance().getConnectionsFromID(client.getClientID());
        // if the client has no connected trainers ...
        if (connectedTrainers == null || connectedTrainers.isEmpty()) {
            // ... add a panel to the page telling them this
            JLabel noConnectionsText = new JLabel("<html>It looks like you haven't made any connections yet.<br>Click 'Find New Trainers' to view our list of qualified trainers and make your first connection today!</html>", SwingConstants.CENTER);
            noConnectionsText.setFont(getBasicFont());
            noConnectionsText.setBounds(marginX, getContentStartHeight(), getContentWidth() - buttonWidth - 4*marginX, getContentHeight());
            this.add(noConnectionsText);
        }
        else {
            // ... otherwise create a connection card for each trainer
            int trainerRunningCount = 0;
            for (Trainer trainer : connectedTrainers) {
                createConnectionCard(trainer, trainerRunningCount);
                trainerRunningCount++;
            }
        }
    }

    /**
     * Creates the trainer's connection card and adds it to the page
     * @param trainer: The connection's trainer
     * @param trainerRunningCount: The number of the trainer
     */
    private void createConnectionCard(Trainer trainer, int trainerRunningCount) {
        int connectionCardHeight = 150;
        int imageWidth = 200;

        // create the card
        JPanel connectionCard = new JPanel();

        // adding the trainer's name
        JLabel name = new JLabel(trainer.getFirstName() + " " + trainer.getLastName());
        name.setHorizontalAlignment(JLabel.CENTER);
        name.setFont(getHeaderFont());
        connectionCard.add(name);

        // adding trainer image
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(new ImageIcon(trainer.getPicturePath()).getImage().getScaledInstance(imageWidth, connectionCardHeight - 15, -1)));
        connectionCard.add(imageLabel);

        // adding the open connection button
        JButton openConnectionButton = new JButton("Open Connection");
        openConnectionButton.setFont(getButtonFont());
        openConnectionButton.setFocusPainted(false);
        openConnectionButton.addActionListener(e->getDisplayController().openConnectionDashboard(trainer));
        connectionCard.add(openConnectionButton);

        // adding the remove connection button
        JButton removeConnectionButton = new JButton("Remove Connection");
        removeConnectionButton.setFont(getButtonFont());
        removeConnectionButton.setFocusPainted(false);
        removeConnectionButton.addActionListener(e -> removeConnection(trainer));
        connectionCard.add(removeConnectionButton);

        // add a border to the card
        connectionCard.setBorder(getLineBorder());

        // add the card to the page
        int marginY = 25;
        connectionCard.setBounds(marginX, getContentStartHeight() + marginY + trainerRunningCount*(connectionCardHeight + marginY), getContentWidth() - buttonWidth - 4*marginX, connectionCardHeight);
        this.add(connectionCard);
    }

    /**
     * Initialises the navigation button used to find new connections with unconnected trainers
     */
    private void initialiseFindTrainersButton() {
        // create the button
        JButton findTrainers = new JButton("Find New Trainers!");
        findTrainers.setFont(getButtonFont());
        findTrainers.setFocusPainted(false);
        // make it navigate to the FindNewTrainersPage
        findTrainers.addActionListener(e -> getDisplayController().openFindNewTrainers());
        // add it to the page
        int buttonHeight = 100;
        findTrainers.setBounds(getContentWidth()-buttonWidth-marginX, getContentStartHeight() + (getContentHeight()- buttonHeight)/2, buttonWidth, buttonHeight);
        this.add(findTrainers);
    }

    /**
     * Overriding abstract class in Page.
     * Called by DisplayController when this page is loaded to initialise the page's components.
     */
    @Override
    public void initialisePanels() {
        initialiseTitlePanel("<html>Welcome, " + client.getFirstName() + "! <br>Here you can view your connections<html>", this);
        initialiseConnectionsPanel();
        initialiseFindTrainersButton();
    }

    /**
     * Called by the remove connection button. Starts the database call chain to remove the connection, creates info dialogue, then reloads the page.
     * @param trainer: the trainer whose connection the user is removing
     */
    private void removeConnection(Trainer trainer) {
        ClientManager.getInstance().removeConnect(client.getClientID(), trainer.getTrainerID());
        JOptionPane.showMessageDialog(this, "Your connection has been removed!");
        getDisplayController().openClientDashboard();
    }
}
