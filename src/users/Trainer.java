package users;

/**
 * Stores the data for a trainer
 */
public class Trainer {
    /**
     * The id of the trainer in the database
     */
    private final int trainerID;
    /**
     * The trainer's first name
     */
    private final String firstName;
    /**
     * The trainer's last name
     */
    private final String lastName;
    /**
     * The trainer's city
     */
    private final String city;
    /**
     * The trainer's hourly rate
     */
    private final Float hourlyRate;
    /**
     * The trainer's number of years experience
     */
    private final int yearsExperience;
    /**
     * The file path of the trainer's profile picture
     */
    private final String picturePath;

    Trainer(int trainerID, String firstName, String lastName, String city, Float hourlyRate, int yearsExperience, String picturePath) {
        this.trainerID = trainerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.hourlyRate = hourlyRate;
        this.yearsExperience = yearsExperience;
        this.picturePath = picturePath;
    }

    /**
     * Getter method for the trainer's id
     * @return the trainer's id
     */
    public int getTrainerID() {
        return trainerID;
    }

    /**
     * Getter method for the trainer's first name
     * @return the trainer's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Getter method for the trainer's last name
     * @return the trainer's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Getter method for the trainer's city
     * @return the trainer's city
     */
    public String getCity() {
        return city;
    }

    /**
     * Getter method for the trainer's hourly rate
     * @return the trainer's hourly rate
     */
    public Float getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Getter method for the trainer's number of years experience
     * @return the trainer's number of years experience
     */
    public int getYearsExperience() {
        return yearsExperience;
    }

    /**
     * Getter method for the file path of the trainer's profile picture in the system
     * @return the file path of the trainer's profile picture in the system
     */
    public String getPicturePath() {
        return picturePath;
    }
}



