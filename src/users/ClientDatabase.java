package users;

import database.DBConnection;
import java.sql.ResultSet;

/**
 * Connects the Client Manager to the database connection
 * Passes the raw query results back to the manager, whose job it is to parse them
 */
public class ClientDatabase {

    /**
     * Queries the database to return all the clients in the system
     * @return the ResultSet containing all the clients in the database
     */
    public ResultSet getAllClients() {
        String query = "select * from client;";
        return DBConnection.processQuery(query);
    }

    /**
     * Queries the database to get all the connections for the inputted client
     * @param clientID: the client whose connections are being queried
     * @return the ResultSet containing all the inputted client's connections
     */
    public ResultSet getAllConnectionsFromID(int clientID) {
        String query = "select trainer_id from connect where client_id = " + clientID + ";";
        return DBConnection.processQuery(query);
    }

    /**
     * Queries the database to get all the trainers who are not connected with the inputted client
     * @param clientID: the client who is not connected to the trainers
     * @return the ResultSet containing all the trainers not connected with the inputted client
     */
    public ResultSet getAllUnconnectedTrainersFromID(int clientID) {
        String query = "select trainer_id from trainer where trainer_id not in (select trainer_id from connect where client_id = " + clientID + ");";
        return DBConnection.processQuery(query);
    }

    /**
     * Queries the database to get the information for the client whose username has been inputted
     * @param username: The client's username being queried
     * @return the ResultSet containing the client data with the inputted username
     */
    public ResultSet getClientFromUsername(String username) {
        String query = "select * from client where username = '" + username + "';";
        return DBConnection.processQuery(query);
    }

    /**
     * Updates the database to insert a new connection between the inputted client and the inputted trainer
     * @param clientID: The id of the client making the connection
     * @param trainerID: The id of the trainer being connected with
     */
    public void createNewConnect(int clientID, int trainerID) {
        String update = "insert into connect (client_id, trainer_id) values (" + clientID + ", " + trainerID + ");";
        DBConnection.processUpdate(update);
    }

    /**
     * Updates the database to delete the connection between the inputted client and the inputted trainer
     * @param clientID: the id of the client who made the connection
     * @param trainerID: the id of the trainer who was connected with
     */
    public void removeConnect(int clientID, int trainerID) {
        String update = "delete from connect where client_id = " + clientID + " and trainer_id = " + trainerID + ";";
        DBConnection.processUpdate(update);
    }

    /**
     * Updates the database to insert a new booking between the inputted client and trainer at the inputted day and time
     * @param clientID: The id of the client making the booking
     * @param trainerID: The id of the trainer being booked
     * @param dayName: The name of the day for the booking (must be uppercase, i.e., Monday, etc)
     * @param timeIndex: The index of the half-hour slot in the day. (i.e., 12am has index 0, 12:30am 1, etc)
     */
    public void createBooking(int clientID, int trainerID, String dayName, int timeIndex) {
        String update = "insert into booking(connect_id, timeslot_id) values (" +
                "(select connect_id from connect where client_id = " + clientID + " and trainer_id = " + trainerID + "), " +
                "(select timeslot_id from timeslot where trainer_id = " + trainerID +  " and day_name = '" + dayName + "' and time_index = " + timeIndex + ")" +
                ");";
        DBConnection.processUpdate(update);
    }

    /**
     * Queries the database to see how many times the inputted user has rated the inputted trainer
     * @param clientID: The id of the client being queried
     * @param trainerID: The id of the trainer being queried
     * @return how many times the inputted user has rated the inputted trainer
     */
    public ResultSet hasRatedTrainer(int clientID, int trainerID) {
        String query = "select count(*) from rating where connect_id = " +
                "(select connect_id from connect where trainer_id = " + trainerID + " and client_id = " + clientID + ");";
        return DBConnection.processQuery(query);
    }

}

