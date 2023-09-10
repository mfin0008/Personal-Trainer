package users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Stores all the clients in the database.
 * Connects the other files with the database for client-related queries and updates.
 */
public class ClientManager {
    /**
     * The single instance of the ClientManager class
     */
    private static ClientManager instance;
    /**
     * The list of all the clients in the database
     */
    private final ArrayList<Client> clients = new ArrayList<>();
    /**
     * A copy of the client database instance
     */
    private final ClientDatabase database = new ClientDatabase();

    private ClientManager() {
        updateClientList();
    }

    /**
     * Singleton getInstance method to ensure that only one ClientManager instance can exist
     * @return the single ClientManager instance
     */
    public static  synchronized ClientManager getInstance() {
        if (instance == null) {
            instance = new ClientManager();
        }
        return instance;
    }

    /**
     * Adds the inputted client to the client list
     * @param client: The client to be added
     */
    private void addClient(Client client) {
        clients.add(client);
    }

    /**
     * Gets the client from the list which matches the inputted id
     * @param clientID: The id of the client to be found
     * @return the found client instance
     */
    public Client getClientFromID(int clientID) {
        Client foundClient = null;
        for (Client client : clients) {
            if (client.getClientID() == clientID) {
                foundClient = client;
                break;
            }
        }
        return foundClient;
    }

    /**
     * Fetches the client corresponding to the inputted username from the database
     * @param username: The username of the client to find
     * @return the fetched client
     */
    public Client getClientFromUsername(String username) {
        Client foundClient = null;
        ResultSet result = database.getClientFromUsername(username);
        try {
            while (result.next()) {
                // search the client list for the client
                foundClient = getClientFromID(result.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return foundClient;
    }

    /**
     * Checks the client list to see if the inputted client is already in it
     * @param clientID: The id of the client to check
     * @return whether the inputted client is already in the client list
     */
    private boolean isAlreadyClient(int clientID) {
        boolean result = false;
        for (Client client : clients) {
            if (client.getClientID() == clientID) {
                // if the current client matches the inputted id, return true
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Parses and creates a list of all Clients in the database
     */
    private void updateClientList() {
        // fetch the list of clients from the database
        ResultSet result = database.getAllClients();
        try {
            while (result.next()) {
                if (!isAlreadyClient(result.getInt(1))) {
                    // if the client is not already in the list, create a new Client and add it
                    addClient(new Client(
                            result.getInt(1),
                            result.getString(2)
                        )
                    );
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Parses a list of the inputted client's connections from the database
     * @param clientID: The id of the client to check
     * @return a list of the inputted client's connections
     */
    public ArrayList<Trainer> getConnectionsFromID(int clientID) {
        ArrayList<Trainer> trainers = new ArrayList<>();
        // fetch the client's connections from the database
        ResultSet result = database.getAllConnectionsFromID(clientID);
        try {
            while (result.next()) {
                // for each trainer returned, add their Trainer instance to the trainers list
                trainers.add(TrainerManager.getInstance().getTrainerFromID(result.getInt(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trainers;
    }

    /**
     * Parses a list of the trainers the inputted client is not connected with from the database
     * @param clientID: the id of the client to check
     * @return a list of all the trainers the client is not connected with
     */
    public ArrayList<Trainer> getUnconnectedTrainersFromID(int clientID) {
        ArrayList<Trainer> trainers = new ArrayList<>();
        // fetch the trainers the client is not connected with from the database
        ResultSet result = database.getAllUnconnectedTrainersFromID(clientID);
        try {
            while (result.next()) {
                // for each trainer returned, add their Trainer instance to the trainers list
                trainers.add(TrainerManager.getInstance().getTrainerFromID(result.getInt(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return trainers;
    }

    /**
     * Sends an update to the database to insert a new connection between the inputted client and the inputted traienr
     * @param client_id: the id of the client making the connection
     * @param trainer_id: the id of the trainer being connected with
     */
    public void createNewConnect (int client_id, int trainer_id) {
        try  {
            database.createNewConnect(client_id, trainer_id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an update to the database to delete the connection between the inputted client and the inputted trainerS
     * @param client_id: the id of the client who made the connection
     * @param trainer_id: the id of the trainer who was connected with
     */
    public void removeConnect (int client_id, int trainer_id) {
        try  {
            database.removeConnect(client_id, trainer_id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends an update to the database to insert a booking for the inputted client and trainer at the inputted day and time
     * @param clientID: The id of the client making the booking
     * @param trainerID: The id of the trainer being booked
     * @param dayName: The name of the day for the booking (must be uppercase, i.e., Monday, etc)
     * @param timeIndex: The index of the half-hour slot in the day. (i.e., 12am has index 0, 12:30am 1, etc)
     */
    public void createBooking(int clientID, int trainerID, String dayName, int timeIndex) {
        try {
            database.createBooking(clientID, trainerID, dayName, timeIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a query to the database to see whether the inputted client has already rated the inputted trainer
     * @param clientID: The id of the client being queried
     * @param trainerID: The id of the trainer being queried
     * @return Whether the client has already reviewed the trainer
     */
    public boolean hasRatedTrainer(int clientID, int trainerID) {
        // fetch the number of times the client has rated the trainer from the database
        ResultSet result = database.hasRatedTrainer(clientID, trainerID);
        boolean answer;
        try {
            result.next();
            // if the client has rated them 1 time, they have rated them, otherwise they haven't
            // ... because of how the database is configured, the count must always be 0 or 1
            answer = result.getInt(1) == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return answer;
    }

}
