package programs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Stores all the programs in the database.
 * Connects the other files with the database for program related queries and updates.
 */
public class ProgramManager {
    /**
     * The single instance of the ProgramManager class
     */
    private static ProgramManager instance;
    /**
     * The list of all programs in the database
     */
    private final ArrayList<Program> programs = new ArrayList<>();
    /**
     * A copy of the program database instance
     */
    private final ProgramDatabase database = new ProgramDatabase();

    private ProgramManager() {
        // fetch all of the programs from the database
        updateProgramList();
    }

    /**
     * Singleton getInstance method to ensure that only one ProgramManager instance can exist
     * @return the single ProgramManager instance
     */
    public static synchronized ProgramManager getInstance() {
        if (instance == null) {
            instance = new ProgramManager();
        }
        return instance;
    }

    /**
     * Adds the inputted program to the program list
     * @param program: The program to be added
     */
    private void addProgram(Program program) {
        programs.add(program);
    }

    /**
     * Generates a list of all programs being sold by the inputted trainer
     * @param trainerID: The id of the trainer whose programs are wanted
     * @return a list of all programs being sold by the inputted trainer
     */
    public ArrayList<Program> getTrainersProgramsFromID(int trainerID) {
        ArrayList<Program> trainersPrograms = new ArrayList<>();
        for (Program program : programs) {
            if (program.getTrainerID() == trainerID) {
                // if the current program is sold by the inputted trainer, add it to the list
                trainersPrograms.add(program);
            }
        }
        return trainersPrograms;
    }

    /**
     * Checks whether the inputted program already exists in the list
     * @param programID: The id of the program to be checked
     * @return whether the inputted program already exists in the list
     */
    private boolean isAlreadyProgram(int programID) {
        boolean result = false;
        for (Program program : programs) {
            if (program.getProgramID() == programID) {
                // if the program is in the list already, return true
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Parses and creates a list of all Programs in the database
     */
    private void updateProgramList() {
        // fetch the list of programs from the database
        ResultSet result = database.getAllPrograms();
        try {
            while (result.next()) {
                if (!isAlreadyProgram(result.getInt(1))) {
                    // if the current program is not already in the list, create a new Program and add it
                    Program program = new Program(
                            result.getInt(1),
                            result.getString(3),
                            result.getInt(2)
                    );
                    addProgram(program);
                    // fetch all of the new program's exercises and add them to the program
                    addExercisesToProgram(program);
                }
            }
        } catch (Exception e) {
            System.out.println(e);}
    }

    /**
     * Fetches a list of all a program's exercises, parses them into Exercise instances and adds them to the Program
     * @param program: The Program that contains the exercises
     */
    private void addExercisesToProgram(Program program) {
        // fetch a list of exercises contained by the program
        ResultSet result = database.getAllExcercisesForProgramFromID(program.getProgramID());
        try {
            while (result.next()) {
                // create a new exercise and add it to the program
                program.addExercise(new Exercise(
                        result.getString(3),
                        result.getInt(4),
                        result.getInt(5),
                        result.getInt(6),
                        result.getInt(7),
                        result.getString(8)
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks whether the inputted client has purchased the inputted program
     * @param programID: The id of the program that may have been purchased
     * @param clientID: The id of the client who may have purchased the program
     * @return whether the client has purchased the program
     */
    public boolean hasClientPurchasedProgram(int programID, int clientID) {
        // fetch the number of times the client has purchased the program
        ResultSet result = database.hasClientPurchasedProgram(programID, clientID);
        boolean answer;
        try {
            result.next();
            // if the number of times is 1, the client has purchased the program
            // ... because of how the database is configured, the count must always be 0 or 1
            answer = result.getInt(1) == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return answer;
    }

    /**
     * Passes on to the database the update command for the purchase of the inputted program by the inputted client
     * @param programID: the id of the program being purchased
     * @param clientID: the id of the client making the purchase
     */
    public void purchaseProgram(int programID, int clientID) {
        database.puchaseProgram(programID, clientID);
    }

}
