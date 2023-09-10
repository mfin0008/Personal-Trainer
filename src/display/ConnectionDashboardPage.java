package display;

import users.Client;
import users.ClientManager;
import users.Trainer;
import users.TrainerManager;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;

/**
 * Page where a client can view information about their connection with a trainer
 * This includes the trainer's schedule, their info, their programs, and the option to give the trainer a rating
 */
public class ConnectionDashboardPage extends Page{
    /**
     * The client who is currently logged in
     */
    private final Client client;
    /**
     * The connected trainer
     */
    private final Trainer trainer;
    /**
     * The horizontal margin for the page
     */
    private final int marginX = 50;
    /**
     * The length of the schedule
     */
    private final int scheduleLength = 700;
    /**
     * The width of the trainer content section
     */
    private final int trainerContentWidth = getContentWidth()-4*marginX-scheduleLength;
    /**
     * The height of the trainer content section
     */
    private final int trainerInfoHeight = 250;
    /**
     * The vertical margin for the trainer content section
     */
    private final int marginY = 50;
    /**
     * The height of the trainer's image
     */
    private final int imageHeight = 300;

    ConnectionDashboardPage(Client client, Trainer trainer, DisplayController displayController) {
        super(displayController);
        this.client = client;
        this.trainer = trainer;
    }

    /**
     * Creates the schedule panel, fills in the time slots and other labels, and adds them to the page
     */
    private void initialiseSchedulePanel() {
        int scheduleMarginX = 10;
        int scheduleRowMarginY = 70;
        int timeSlotMarginX = 2;
        int dayLabelHeight = 30;
        int scheduleTitleHeight = 55;
        int timeLabelHeight = 20;

        // create the panel
        JPanel schedulePanel = new JPanel();
        schedulePanel.setLayout(null);
        schedulePanel.setBounds(marginX, getContentStartHeight(), scheduleLength, scheduleLength);
        schedulePanel.setBorder(getLineBorder());

        // add title
        JLabel title = new JLabel("<html>" + trainer.getFirstName() + "'s Schedule</html>", SwingConstants.CENTER);
        title.setFont(getHeaderFont());
        title.setBounds(0, 0, scheduleLength, scheduleTitleHeight);
        schedulePanel.add(title);

        // add time slot squares
        int squareLength = 12;
        int numTimeSlotsPerDay = 48;
        int numDays = 7;
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] times = {"12am", "1am", "2am", "3am", "4am", "5am", "6am", "7am", "8am", "9am", "10am", "11am", "12pm", "1pm", "2pm", "3pm", "4pm", "5pm", "6pm", "7pm", "8pm", "9pm", "10pm", "11pm"};
        Color available = new Color(110, 198, 124); // available to be booked
        Color unavailable = new Color(156,156,156); // booked by a different client
        Color booked = new Color(30,143,255); // booked by the current client

        // add day labels
        for (int i=0; i<numDays; i++) {
            String dayName = days[i];
            JLabel dayLabel = new JLabel(dayName, SwingConstants.CENTER);
            dayLabel.setFont(getBasicFont());
            dayLabel.setBounds(0, scheduleRowMarginY +i*(scheduleRowMarginY +squareLength), schedulePanel.getWidth(), dayLabelHeight);
            schedulePanel.add(dayLabel);

            // add the time slots for the ith day
            for (int j=0; j<numTimeSlotsPerDay; j++) {
                int timeIndex = j;

                // create the timeslot button
                JButton timeslot = new JButton();
                timeslot.setFocusPainted(false);
                timeslot.setBounds(scheduleMarginX +(squareLength + timeSlotMarginX)*j, 2*scheduleTitleHeight + i*(scheduleRowMarginY+squareLength), squareLength, squareLength);

                // check timeslot availability
                if (TrainerManager.getInstance().isTimeslotAvailable(trainer.getTrainerID(), dayName, timeIndex)) {
                    timeslot.setBackground(available);
                }
                else if (TrainerManager.getInstance().isTimeslotBookedByThisClient(trainer.getTrainerID(), client.getClientID(), dayName, timeIndex)){
                    timeslot.setBackground(booked);
                }
                else {
                    timeslot.setBackground(unavailable);
                }
                timeslot.addActionListener(e-> createBooking(dayName, timeIndex));
                schedulePanel.add(timeslot);
                // add time labels
                if (j%2 == 0) {
                    JLabel timeLabel = new JLabel(times[j/2], SwingConstants.LEFT);
                    timeLabel.setFont(new Font("verdana", Font.PLAIN, 9));
                    timeLabel.setBounds(scheduleMarginX+(squareLength + timeSlotMarginX)*j, squareLength + 2*scheduleTitleHeight+i*(scheduleRowMarginY+squareLength), 3*squareLength, timeLabelHeight);
                    schedulePanel.add(timeLabel);
                }
            }
        }
        // create the schedule legend panel
        JPanel legendPanel = new JPanel();
        legendPanel.setBounds(2, scheduleRowMarginY + numDays*(scheduleRowMarginY + squareLength), schedulePanel.getWidth()-4, timeLabelHeight);
        // add the available legend entry
        JPanel availablePanel = new JPanel();
        availablePanel.setBackground(available);
        legendPanel.add(availablePanel);
        legendPanel.add(new JLabel(": Available to book  | "));
        // add the unavailable legend entry
        JPanel unavailablePanel = new JPanel();
        unavailablePanel.setBackground(unavailable);
        legendPanel.add(unavailablePanel);
        legendPanel.add(new JLabel(": Booked by another client  | "));
        // add the booked legend entry
        JPanel bookedPanel = new JPanel();
        bookedPanel.setBackground(booked);
        legendPanel.add(bookedPanel);
        legendPanel.add(new JLabel(": You have already booked"));
        schedulePanel.add(legendPanel);

        // add the schedule panel to the page
        this.add(schedulePanel);
    }

    /**
     * Called by the timeslot buttons. Starts the database call chain to create a booking, then reload the page.
     * @param dayName: a string containing the name of the booking's day in uppercase (i.e., Monday, etc)
     * @param timeIndex: the index of the half-hour slot in the day. (i.e., 12am has index 0, 12:30am 1, etc)
     */
    private void createBooking(String dayName, int timeIndex) {
        ClientManager.getInstance().createBooking(client.getClientID(), trainer.getTrainerID(), dayName, timeIndex);
        // reload the page
        getDisplayController().openConnectionDashboard(trainer);
    }

    /**
     * Initialises the trainer's photo and adds it to the page
     */
    private void initialisePhoto() {
        // create the label
        JLabel trainerPhotoLabel = new JLabel();
        trainerPhotoLabel.setIcon(new ImageIcon(new ImageIcon(trainer.getPicturePath()).getImage().getScaledInstance(trainerContentWidth, imageHeight, -1)));
        trainerPhotoLabel.setBounds(3*marginX + scheduleLength, getContentStartHeight(), trainerContentWidth, imageHeight);

        // add the photo to the page
        this.add(trainerPhotoLabel);
    }

    /**
     * Initialises the trainer's info and adds it to the page
     */
    private void initialiseTrainerInfo() {
        // create the label
        JLabel trainerInfoLabel = new JLabel(TrainerManager.getInstance().getTrainerInfoString(trainer.getTrainerID()));
        trainerInfoLabel.setFont(getBasicFont());
        trainerInfoLabel.setBounds(3*marginX + scheduleLength, getContentStartHeight() + imageHeight + marginY, trainerContentWidth, trainerInfoHeight);

        // add the info label to the page
        this.add(trainerInfoLabel);
    }

    /**
     * Initialises the view programs button and returns it
     * @return the created button
     */
    private JButton generateProgramsButton() {
        JButton programsButton = new JButton("View " + trainer.getFirstName() + "'s Programs");
        programsButton.setFont(getButtonFont());
        programsButton.setFocusPainted(false);
        programsButton.addActionListener(e -> getDisplayController().openSelectProgram(trainer));
        return programsButton;
    }

    /**
     * Initialises the rate trainer button and returns it
     * @return the created button
     */
    private JButton generateRatingButton() {
        JButton ratingButton = new JButton("Give " + trainer.getFirstName() + " A Rating");
        ratingButton.setFont(getButtonFont());
        ratingButton.setFocusPainted(false);
        ratingButton.addActionListener(e->getDisplayController().openRatingDialogue(trainer));
        return ratingButton;
    }

    /**
     * Initialises the trainer button panel.
     * Determines whether each button is necessary and if it is, calls its creation and adds them to the page.
     */
    private void initialiseTrainerButtonsPanel() {
        int trainerButtonsPanelHeight = 50;

        // create the panel
        JPanel trainerButtonsPanel = new JPanel();
        trainerButtonsPanel.setBounds(3*marginX + scheduleLength, getContentStartHeight() + imageHeight + trainerInfoHeight + 2*marginY, trainerContentWidth, trainerButtonsPanelHeight);

        boolean includePanel = false;
        if (TrainerManager.getInstance().getNumOfProgramsFromID(trainer.getTrainerID()) > 0) {
            // if the trainer has programs available, add a view programs button
            includePanel = true;
            trainerButtonsPanel.add(generateProgramsButton());
        }
        if (!ClientManager.getInstance().hasRatedTrainer(client.getClientID(), trainer.getTrainerID())) {
            // if the client has not yet rated this trainer, add a give rating button
            includePanel = true;
            trainerButtonsPanel.add(generateRatingButton());
        }
        if (includePanel) {
            // if either button is active, add the panel to the page
            trainerButtonsPanel.setBackground(getBackgroundColor());
            this.add(trainerButtonsPanel);
        }
    }

    /**
     * Overriding abstract class in Page.
     * Called by DisplayController when this page is loaded to initialise the page's components.
     */
    @Override
    public void initialisePanels() {
        initialiseTitlePanel("<html>Your connection with " + trainer.getFirstName() + "</html>", this);
        initialiseSchedulePanel();
        initialisePhoto();
        initialiseTrainerInfo();
        initialiseTrainerButtonsPanel();
    }
}
