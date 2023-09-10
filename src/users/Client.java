package users;

/**
 * Stores the data for a client
 */
public class Client {
    /**
     * The id of the client in the database
     */
    private final int clientID;
    /**
     * The client's first name
     */
    private final String firstName;

    Client(int clientID, String firstName) {
        this.clientID = clientID;
        this.firstName = firstName;
    }

    /**
     * Getter method for the client's id
     * @return the client's id
     */
    public int getClientID() {
        return clientID;
    }

    /**
     * Getter method for the client's first name
     * @return the client's first name
     */
    public String getFirstName() { return firstName; }
}
