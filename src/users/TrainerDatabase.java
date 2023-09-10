package users;

import database.DBConnection;

import java.sql.ResultSet;

/**
 * Connects the Trainer Manager to the database connection
 * Passes the raw query results back to the manager, whose job it is to parse them
 */
public class TrainerDatabase {

    /**
     * Queries the database to return all the trainers in the system
     * @return the ResultSet containing all the trainers in the database
     */
    public ResultSet getAllTrainers() {
        String query = "select * from trainer;";
        return DBConnection.processQuery(query);
    }

    /**
     * Queries the database to see how many programs the inputted trainer has
     * @param trainerID: the id of the trainer who is being queried
     * @return the ResultSet containing how many programs the trainer has
     */
    public ResultSet getNumProgramsFromID(int trainerID) {
        String query = "select count(*) from trainer natural join program where trainer_id = " + trainerID + ";";
        return DBConnection.processQuery(query);
    }

    /**
     * Queries the database to get the average rating of the inputted trainer
     * @param trainerID: the id of the trainer being queried
     * @return the ResultSet containing the average rating of the inputted trainer
     */
    public ResultSet getAverageRatingFromID(int trainerID) {
        String query = "select avg(rating_value) from rating natural join connect where trainer_id = " + trainerID + ";";
        return DBConnection.processQuery(query);
    }

    /**
     * Queries the database to see how many times the inputted time slot has been booked
     * @param trainerID: the id of the trainer who owns the timeslot
     * @param dayName: The name of the day for the booking (must be uppercase, i.e., Monday, etc)
     * @param timeIndex: The index of the half-hour slot in the day. (i.e., 12am has index 0, 12:30am 1, etc)
     * @return the ResultSet containing the number of times the inputted timeslot has been booked
     */
    public ResultSet isTimeslotAvailable(int trainerID, String dayName, int timeIndex) {
        String query = "select count(*) from booking natural join connect where timeslot_id = " +
                "(select timeslot_id from timeslot where trainer_id = " + trainerID +  " and day_name = '" + dayName + "' and time_index = "+ timeIndex +");";
        return DBConnection.processQuery(query);
    }

    /**
     * Queries the database to find the id of the client who booked the inputted time slot
     * @param trainerID: the id of the trainer who owns the timeslot
     * @param dayName: The name of the day for the booking (must be uppercase, i.e., Monday, etc)
     * @param timeIndex: The index of the half-hour slot in the day. (i.e., 12am has index 0, 12:30am 1, etc)
     * @return the ResultSet containing the id of the client who booked the inputted time slot
     */
    public ResultSet whichClientBookedThisTimeslot(int trainerID, String dayName, int timeIndex) {
        String query = "select client_id from booking natural join connect where timeslot_id = " +
                "(select timeslot_id from timeslot where trainer_id = " + trainerID +  " and day_name = '" + dayName + "' and time_index = "+ timeIndex +");";
        return DBConnection.processQuery(query);
    }

    /**
     * Updates the database to insert a new rating for the inputted trainer and client
     * @param trainerID: The id of the trainer who is being rated
     * @param clientID: The id of the client making the rating
     * @param ratingValue: The value of the rating (will be an int between 1 and 5)
     */
    public void addRatingByClient(int trainerID, int clientID, int ratingValue) {
        String update = "insert into rating (connect_id, rating_value) values (" +
                "    (select connect_id from connect where client_id = " + clientID + " and trainer_id = " + trainerID + ")," +
                ratingValue +
                ");";
        DBConnection.processUpdate(update);
    }

}