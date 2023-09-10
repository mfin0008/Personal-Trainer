package users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Stores all the trainers in the database.
 * Connects the other files with the database for trainer-related queries and updates
 */
public class TrainerManager {
    /**
     * The single instance of the TrainerManager class
     */
    private static TrainerManager instance = null;
    /**
     * The list of all the trainers in the database
     */
    private final ArrayList<Trainer> trainers = new ArrayList<>();
    /**
     * A copy of the trainer database instance
     */
    private final TrainerDatabase database = new TrainerDatabase();

    private TrainerManager() {
        updateTrainerList();
    }

    /**
     * Singleton getInstance method to ensure that only one TrainerManager instance can exist
     * @return the single TrainerManager instance
     */
    public static synchronized TrainerManager getInstance() {
        if (instance == null) {
            instance = new TrainerManager();
        }
        return instance;
    }

    /**
     * Adds the inputted trainer to the trainer list
     * @param trainer: The trainer to be added
     */
    private void addTrainer(Trainer trainer) {
        trainers.add(trainer);
    }

    /**
     * Gets the trainer from the list which matches the inputted id
     * @param trainerID: The id of the trainer to be found
     * @return the found trainer instance
     */
    public Trainer getTrainerFromID(int trainerID) {
        Trainer foundTrainer = null;
        for (Trainer trainer : trainers) {
            if (trainer.getTrainerID() == trainerID) {
                foundTrainer = trainer;
                break;
            }
        }
        return foundTrainer;
    }

    /**
     * Checks the trainer list to see if the inputted trainer is already in it
     * @param trainerID: The id of the trainer to check
     * @return whether the inputted trainer is already in the trainer list
     */
    private boolean isAlreadyTrainer(int trainerID) {
        boolean result = false;
        for (Trainer trainer : trainers) {
            if (trainer.getTrainerID() == trainerID) {
                // if the current trainer matches the inputted id, return true
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Parses and creates a list of all Trainers in the database
     */
    private void updateTrainerList() {
        // fetch the list of trainers from the database
        ResultSet result = database.getAllTrainers();
        try {
            while (result.next()) {
                if (!isAlreadyTrainer(result.getInt(1))) {
                    // if the trainer is not already in the list, create a new Trainer and add it
                    addTrainer(new Trainer(
                                result.getInt(1),
                                result.getString(2),
                                result.getString(3),
                                result.getString(4),
                                result.getFloat(5),
                                result.getInt(6),
                                result.getString(8)
                            )
                    );

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Sends a query to the database to see how many programs the inputted trainer is selling
     * @param trainerID: The id of the trainer being queried
     * @return how many programs the inputted trainer is selling
     */
    public int getNumOfProgramsFromID(int trainerID) {
        ResultSet result = database.getNumProgramsFromID(trainerID);
        try {
            result.next();
            return result.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a query to the database to find the average rating for the inputted trainer
     * @param trainerID: the id of the trainer being queried
     * @return the average rating for the inputted trainer
     */
    public float getAverageRatingFromID(int trainerID) {
        ResultSet result = database.getAverageRatingFromID(trainerID);
        try {
            result.next();
            return result.getFloat(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a query to the database to see whether the inputted timeslot is available
     * @param trainerID: the trainer who owns the timeslot
     * @param dayName: The name of the day for the booking (must be uppercase, i.e., Monday, etc)
     * @param timeIndex: The index of the half-hour slot in the day. (i.e., 12am has index 0, 12:30am 1, etc)
     * @return whether the inputted timeslot is available
     */
    public boolean isTimeslotAvailable(int trainerID, String dayName, int timeIndex) {
        // fetch how many times the timeslot has been booked
        ResultSet result = database.isTimeslotAvailable(trainerID, dayName, timeIndex);
        Boolean isAvailable;
        try {
            result.next();
            // if it has been booked once, it is not available, otherwise it's not.
            // ... because of how the database is configured, the count must always be 0 or 1
            isAvailable = result.getInt(1) == 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return isAvailable;
    }

    /**
     * Sends a query to the database to see whether the inputted timeslot has been booked by the inputted client
     * @param trainerID: the trainer who owns the timeslot
     * @param clientID: the client who may have booked the timeslot
     * @param dayName: The name of the day for the booking (must be uppercase, i.e., Monday, etc)
     * @param timeIndex: The index of the half-hour slot in the day. (i.e., 12am has index 0, 12:30am 1, etc)
     * @return whether the inputted client has booked the inputted timeslot
     */
    public boolean isTimeslotBookedByThisClient(int trainerID, int clientID, String dayName, int timeIndex) {
        boolean isBooked;
        // fetch the id of the client who booked the timeslot from the database
        ResultSet result = database.whichClientBookedThisTimeslot(trainerID, dayName, timeIndex);
        try {
            result.next();
            // if the inputted client matches the client who booked the timeslot, return true
            isBooked = result.getInt(1) == clientID;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return isBooked;
    }

    /**
     * Gets the inputted trainer's info string
     * @param trainerID: The trainer to get the info for
     * @return the inputted trainer's info string
     */
    public String getTrainerInfoString(int trainerID) {
        // get the Trainer instance from the trainer list
        Trainer trainer = getTrainerFromID(trainerID);

        // format the trainer's info string
        String infoString =
                "<html><br>" + trainer.getFirstName() + " charges $" + trainer.getHourlyRate() + " per hour" +
                        "<br>" + trainer.getFirstName() + " is located in " + trainer.getCity() +
                        "<br>" + trainer.getFirstName() + " has been training for " + trainer.getYearsExperience() + " years " +
                        "<br>" + trainer.getFirstName() + " has " + TrainerManager.getInstance().getNumOfProgramsFromID(trainer.getTrainerID()) + " programs available to purchase";
        if (TrainerManager.getInstance().getAverageRatingFromID(trainer.getTrainerID()) > 0) {
            infoString += "<br>" + trainer.getFirstName() + " has been rated " + TrainerManager.getInstance().getAverageRatingFromID(trainer.getTrainerID()) + "/5 by their clients";
        }
        infoString += "</html>";

        return infoString;
    }

    /**
     * Sends an update to the database to insert a rating for the inputted trainer by the inputted client
     * @param trainerID: The id of the trainer being rated
     * @param clientID: The id of the client being rated
     * @param ratingValue: The value of the rating (will be an integer between 1 and 5)
     */
    public void addRatingByClient(int trainerID, int clientID, int ratingValue) {
        database.addRatingByClient(trainerID, clientID, ratingValue);
    }

}
