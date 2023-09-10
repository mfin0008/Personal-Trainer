package programs;

import java.util.ArrayList;

/**
 * Stores the data for a trainer's program
 */
public class Program {
    /**
     * The id of the program in the database
     */
    private final int programID;
    /**
     * The title of the program
     */
    private final String title;
    /**
     * The id of the trainer who owns the program
     */
    private final int trainerID;
    /**
     * A list of exercises contained within the program
     */
    private final ArrayList<Exercise> exercises = new ArrayList<>();

    public Program (int programID, String title, int trainerID) {
        this.programID = programID;
        this.title = title;
        this.trainerID = trainerID;
    }

    /**
     * Adds the inputted exercise to the exercise list
     * @param exercise: The exercise to be added
     */
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    /**
     * Getter method for the program ID
     * @return the program ID
     */
    public int getProgramID() {
        return programID;
    }

    /**
     * Getter method for the program's title
     * @return the program's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter method for the program's trainer ID
     * @return the program's trainer ID
     */
    public int getTrainerID() {
        return trainerID;
    }

    /**
     * Getter method for the exercise list
     * @return the exercise list
     */
    public ArrayList<Exercise> getExercises() {
        return new ArrayList<>(exercises);
    }
}
