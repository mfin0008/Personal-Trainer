package programs;

import database.DBConnection;

import java.sql.ResultSet;

/**
 * Connects the Program Manager to the database connection
 * Passes the raw query results back to the manager, whose job it is to parse them
 */
public class ProgramDatabase {
    /**
     * Queries the database for a list of all programs in the database
     * @return the ResultSet containing all the programs in the database
     */
    public ResultSet getAllPrograms() {
        String query = "select * from program;";
        return DBConnection.processQuery(query);
    }

    /**
     * Queries the database for a list of all the exercises contained within the inputted program
     * @param programID: The id of the program that contains the desired exercises
     * @return the ResultSet containing all the exercises in the inputted program
     */
    public ResultSet getAllExcercisesForProgramFromID(int programID) {
        String query = "select * from exercise where program_id = " + programID + ";";
        return DBConnection.processQuery(query);
    }

    /**
     * Queries the database for the number of times the inputted client has purchased the inputted program
     * @param programID: The id of the program being queried
     * @param clientID: The id of the client being queried
     * @return the number of times the inputted client has purchased the inputted program
     */
    public ResultSet hasClientPurchasedProgram(int programID, int clientID) {
        String query = "select count(*) from programPurchase where program_id = " + programID + " and client_id = " + clientID + ";";
        return DBConnection.processQuery(query);
    }

    /**
     * Updates the database to insert a purchase of the inputted program by the inputted client
     * @param programID: The id of the program being purchased
     * @param clientID: The id of the client purchasing the program
     */
    public void puchaseProgram(int programID, int clientID) {
        String update = "insert into programPurchase (program_id, client_id) values (" + programID + ", " + clientID + ");";
        DBConnection.processUpdate(update);
    }
}
