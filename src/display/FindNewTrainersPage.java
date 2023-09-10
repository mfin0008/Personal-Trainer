package display;

import users.Client;
import users.ClientManager;
import users.Trainer;
import users.TrainerManager;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.util.ArrayList;

/**
 * Page that displays unconnected trainers to the user
 */
public class FindNewTrainersPage extends Page {
    /**
     * The currently logged in client
     */
    private final Client client;
    /**
     * The horizontal margin for the page
     */
    private final int marginX = 50;
    /**
     * The vertical margin for the trainer cards
     */
    private final int marginY = 15;
    /**
     * The width of the trainers' images
     */
    private final int imageWidth = 150;
    /**
     * The height of the trainers' images
     */
    private final int imageHeight = 100;
    /**
     * The width of the trainer cards
     */
    private final int trainerCardWidth = 600;
    /**
     * The height of the trainer cards
     */
    private final int trainerCardHeight = 230;

    FindNewTrainersPage(Client client, DisplayController displayController) {
        super(displayController);
        this.client = client;
    }

    /**
     * Initialises the unconnected trainer cards (if any)
     */
    private void initialiseUnconnectedTrainersPanel() {
        // fetch a list of unconnected trainers from the database
        ArrayList<Trainer> unconnectedTrainers = ClientManager.getInstance().getUnconnectedTrainersFromID(client.getClientID());

        // create the unconnected trainers panel
        JPanel unconnectedTrainersPanel = new JPanel();
        unconnectedTrainersPanel.setLayout(null);
        unconnectedTrainersPanel.setBounds(0, getContentStartHeight(), getContentWidth(), getContentHeight());
        unconnectedTrainersPanel.setBackground(getBackgroundColor());

        // filling the connections
        if (unconnectedTrainers.isEmpty()) {
            JLabel noConnectionsText = new JLabel("<html>It looks like we don't have any new trainers for you. Check back soon!</html>", SwingConstants.CENTER);
            noConnectionsText.setFont(getBasicFont());
            unconnectedTrainersPanel.add(noConnectionsText);
        }
        else {
            int runningTrainerCount = 0;
            // creating a 2x3 grid of trainers
            // ^ is not extensible for >6 trainers so would rather have had a scrolling panel but couldn't get that to work
            int[] xPos = new int[] {0, 1};
            int[] yPos = new int[] {0, 1, 2};
            for (int i = 0; i<yPos.length && runningTrainerCount < unconnectedTrainers.size(); i++) {
                for (int j = 0; j<xPos.length && runningTrainerCount < unconnectedTrainers.size(); j++) {
                    // create card for position (i, j) in grid
                    JPanel trainerCard = createTrainerCard(unconnectedTrainers.get(runningTrainerCount), xPos[j], yPos[i]);
                    unconnectedTrainersPanel.add(trainerCard);
                    runningTrainerCount++;
                }
            }
        }

        // add the unconnected trainers panel to the page
        this.add(unconnectedTrainersPanel);
    }

    /**
     * Creates the trainer's card, and sets it to the specified position on the grid
     * @param trainer: the unconnected trainer
     * @param xPos: the x position in the trainer grid
     * @param yPos: the y position in the trainer grid
     * @return the created trainer card
     */
    private JPanel createTrainerCard(Trainer trainer, int xPos, int yPos) {
        JPanel trainerCard = new JPanel();
        trainerCard.setLayout(new BorderLayout());

        // adding the trainer's name
        JLabel name = new JLabel(trainer.getFirstName() + " " + trainer.getLastName());
        name.setHorizontalAlignment(JLabel.CENTER);
        name.setFont(getHeaderFont());
        trainerCard.add(name, BorderLayout.NORTH);

        // adding trainer info
        JLabel info = new JLabel(TrainerManager.getInstance().getTrainerInfoString(trainer.getTrainerID()));
        info.setFont(getButtonFont());
        trainerCard.add(info, BorderLayout.CENTER);

        // adding trainer image
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(new ImageIcon(trainer.getPicturePath()).getImage().getScaledInstance(imageWidth, imageHeight, -1)));
        trainerCard.add(imageLabel, BorderLayout.WEST);

        // adding request connection button
        JButton requestConnectButton = new JButton("Request Connection");
        requestConnectButton.setFont(getButtonFont());
        requestConnectButton.setFocusPainted(false);
        requestConnectButton.addActionListener(e -> requestConnection(trainer));
        trainerCard.add(requestConnectButton, BorderLayout.EAST);

        // add a border
        trainerCard.setBorder(getLineBorder());

        // add the card to the page
        trainerCard.setBounds(marginX + xPos * (trainerCardWidth + 2*marginX), yPos * (trainerCardHeight + marginY), trainerCardWidth, trainerCardHeight);
        return trainerCard;
    }

    /**
     * Overriding abstract class in Page.
     * Called by DisplayController when this page is loaded to initialise the page's components.
     */
    @Override
    public void initialisePanels() {
        initialiseTitlePanel("<html>You haven't connected with these trainers<html>", this);
        initialiseUnconnectedTrainersPanel();
    }

    /**
     * Called by the request connection button. Starts the database call chain to add the connection, creates success dialogue, then reloads the page.
     * NB: All "requests" are automatically accepted. The request is just a simulation of a functioning app.
     * @param trainer: the trainer whose connection the user is requesting
     */
    private void requestConnection(Trainer trainer) {
        ClientManager.getInstance().createNewConnect(client.getClientID(), trainer.getTrainerID());
        JOptionPane.showMessageDialog(this, "Your connection has been approved!");
        getDisplayController().openFindNewTrainers();
    }
}
