package display;

import main.App;
import programs.Program;
import users.ClientManager;
import users.Trainer;

/**
 * Class used to control the navigation of the display
 */
public class DisplayController {
    /**
     * Stores which Page is currently being displayed
     */
    private Page currentPage = null;
    /**
     * Stores the App instance, which is sometimes needed for pages being loaded.
     */
    private final App app;

    public DisplayController(App app) {
        this.app = app;
        // initialise the landing page
        openLoginDialogue(true);
    }

    /**
     * Called by all the public open methods.
     * Replaces the currentPage attribute.
     * Initialises the page's panels and then makes it visible on the display.
     * @param newPage: the page being opened
     */
    private void openNewPage(Page newPage) {
        if (currentPage != null) {
            currentPage.dispose();
        }
        currentPage = newPage;
        currentPage.initialisePanels();
        currentPage.setVisible(true);
    }

    /**
     * Called when the user is attempting to log in
     * @param firstLogin: boolean representing whether this is the first time the user is logging in for this session.
     */
    public void openLoginDialogue(boolean firstLogin) {
        new LoginDialogue(app, firstLogin);
        // once login is successful, navigate to that client's dashboard
        openClientDashboard();
    }

    /**
     * Called when the user is attempting to rate a connected trainer
     * @param trainer: the trainer the user is attempting to rate
     */
    public void openRatingDialogue(Trainer trainer) {
        new RatingDialogue(trainer, app.getCurrentUserID());
        // once the rating is successful, reload the connection dashboard (since this is the only page that can call this method)
        openConnectionDashboard(trainer);
    }

    /**
     * Called when the user navigates to their dashboard
     */
    public void openClientDashboard() {
        openNewPage(new ClientDashboardPage(ClientManager.getInstance().getClientFromID(app.getCurrentUserID()), this));
    }

    /**
     * Called when the user navigates to find new connections
     */
    public void openFindNewTrainers() {
        openNewPage(new FindNewTrainersPage(ClientManager.getInstance().getClientFromID(app.getCurrentUserID()), this));
    }

    /**
     * Called when the user clicks "view programs" for a connected trainer
     * @param trainer: The connected trainer
     */
    public void openSelectProgram(Trainer trainer) {
        openNewPage(new SelectProgramPage(this, ClientManager.getInstance().getClientFromID(app.getCurrentUserID()), trainer));
    }

    /**
     * Called when the user clicks "open workout" for a connected trainer
     * @param program: the selected program
     * @param trainer: the connected trainer
     */
    public void openViewProgram(Program program, Trainer trainer) {
        openNewPage(new ViewProgramPage(this, program, trainer));
    }

    /**
     * Called when the user clicks "open connection" on a connected trainer
     * @param trainer: the connected trainer
     */
    public void openConnectionDashboard(Trainer trainer) {
        openNewPage(new ConnectionDashboardPage(ClientManager.getInstance().getClientFromID(app.getCurrentUserID()), trainer, this));
    }
}


