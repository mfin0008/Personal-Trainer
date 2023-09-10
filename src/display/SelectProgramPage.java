package display;

import programs.Program;
import programs.ProgramManager;
import users.Client;
import users.Trainer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.util.ArrayList;

/**
 * Page used by the user to select a program from one of their connected trainers
 */
public class SelectProgramPage extends Page {
    /**
     * The client who is currently logged in
     */
    private final Client client;
    /**
     * The trainer whose programs are being selected from
     */
    private final Trainer trainer;
    /**
     * A list of the trainer's programs to select from
     */
    private final ArrayList<Program> programs;
    /**
     * The horizontal margin for the page
     */
    private final int marginX = 50;
    /**
     * The vertical margin for the program cards
     */
    private final int marginY = 50;
    /**
     * The height of the training cards
     */
    private final int trainerCardHeight = 200;

    SelectProgramPage(DisplayController displayController, Client client, Trainer trainer) {
        super(displayController);
        this.client = client;
        this.trainer = trainer;
        // fetch the programs from the database
        this.programs = ProgramManager.getInstance().getTrainersProgramsFromID(trainer.getTrainerID());
    }

    /**
     * Initialises the program panels and adds them to the page
     */
    private void initialiseProgramPanels() {
        int runningProgramCount = 0;
        for (Program program : programs) {
            // create the program panel
            JPanel programPanel = new JPanel();
            programPanel.setLayout(new BorderLayout());
            programPanel.setBounds(marginX, getContentStartHeight() + marginY + runningProgramCount*(trainerCardHeight+marginY), getContentWidth() - 2*marginX, trainerCardHeight);
            programPanel.setBorder(getLineBorder());

            // add title
            JLabel titleLabel = new JLabel(program.getTitle(), SwingConstants.CENTER);
            titleLabel.setFont(getHeaderFont());
            programPanel.add(titleLabel, BorderLayout.NORTH);

            // add exercise count
            JLabel exerciseCountLabel = new JLabel("This program contains " + program.getExercises().size() + " exercises.", SwingConstants.CENTER);
            exerciseCountLabel.setFont(getBasicFont());
            programPanel.add(exerciseCountLabel, BorderLayout.CENTER);

            // add purchase / view button
            if (ProgramManager.getInstance().hasClientPurchasedProgram(program.getProgramID(), client.getClientID())) {
                // if the client has already purchased the program, give them the option to open it
                JButton openProgramButton = new JButton("Open " + program.getTitle());
                openProgramButton.setFont(getButtonFont());
                openProgramButton.addActionListener(e->getDisplayController().openViewProgram(program, trainer));
                openProgramButton.setFocusPainted(false);
                programPanel.add(openProgramButton, BorderLayout.EAST);
            }
            else {
                // if the client hasn't purchased the program, give them the option to
                JButton purchaseProgramButton = new JButton("Purchase " + program.getTitle());
                purchaseProgramButton.setFont(getButtonFont());
                purchaseProgramButton.addActionListener(e->purchaseProgram(program));
                purchaseProgramButton.setFocusPainted(false);
                programPanel.add(purchaseProgramButton, BorderLayout.EAST);
            }

            // add the program panel to the page
            this.add(programPanel);
            runningProgramCount++;
        }
    }

    /**
     * Overriding abstract class in Page.
     * Called by DisplayController when this page is loaded to initialise the page's components.
     */
    @Override
    public void initialisePanels() {
        initialiseTitlePanel("<html>" + trainer.getFirstName() + " has " + programs.size() + " program(s) to choose from:</html>", this);
        initialiseProgramPanels();
    }

    /**
     * Called by the purchase program button. Starts the database call chain to simulate a program's purchase by the logged-in user
     * @param program: the program being purchased
     */
    private void purchaseProgram(Program program) {
        JOptionPane.showMessageDialog(this, "Your purchase has been successful!");
        ProgramManager.getInstance().purchaseProgram(program.getProgramID(), client.getClientID());
        getDisplayController().openSelectProgram(trainer);
    }
}
