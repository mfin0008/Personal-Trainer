package programs;

/**
 * Stores the data for an exercise in a trainer's program
 */
public class Exercise {
    /**
     * The exercise's title
     */
    private final String title;
    /**
     * The number of repetitions the exercise suggests
     */
    private final int numReps;
    /**
     * The number of sets of repetitions the exercise suggests
     */
    private final int numSets;
    /**
     * The rest time in between sets the exercise suggests
     */
    private final int restTime;
    /**
     * Rating of Perceived Exertion (measures the intensity of the exercise)
     */
    private final int rpe;
    /**
     * The path of the exercise's example video in the file system
     */
    private final String videoPath;

    public Exercise(String title, int numReps, int numSets, int restTime, int rpe, String videoPath) {
        this.title = title;
        this.numReps = numReps;
        this.numSets = numSets;
        this.restTime = restTime;
        this.rpe = rpe;
        this.videoPath = videoPath;
    }

    /**
     * Getter method for the exercise's title
     * @return the exercise's title
     */
    private String getTitle() {
        return title;
    }

    /**
     * Getter method for the exercise's number of reps
     * @return the exercise's number of reps
     */
    private int getNumReps() {
        return numReps;
    }

    /**
     * Getter method for the exercise's number of sets
     * @return the exercise's number of sets
     */
    private int getNumSets() {
        return numSets;
    }

    /**
     * Getter method for the exercise's rest time
     * @return the exercise's rest time
     */
    private int getRestTime() {
        return restTime;
    }

    /**
     * Getter method for the exercise's rpe value
     * @return the exercise's rpe value
     */
    private int getRpe() {
        return rpe;
    }

    /**
     * Getter method for the exercise's example video path
     * @return the exercise's example video path
     */
    public String getVideoPath() {
        return videoPath;
    }

    /**
     * Formats the exercise's information into a single string
     * @param exerciseNumber: The exercise's number in the displayed program
     * @return a formatted string containing all the exercise's data
     */
    public String getInfoString(int exerciseNumber) {
        return
                "<html>Exercise " + exerciseNumber + ": " + getTitle() +
                        "<br>" + getNumSets() + " sets X " + getNumReps() + " reps" +
                        "<br>" + getRestTime() + " seconds rest time between sets" +
                        "<br>RPE: " + getRpe() + "</html>";
    }
}
