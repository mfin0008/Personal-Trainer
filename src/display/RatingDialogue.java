package display;

import users.Trainer;
import users.TrainerManager;

import javax.swing.JOptionPane;

/**
 * Dialogue used to prompt the user for a rating of one of their connected trainers
 */
public class RatingDialogue {
    RatingDialogue(Trainer trainer, int client_id) {
        boolean successful = false;
        int ratingValue = -1;
        while (!successful) {
            // keep prompting until the user either enters a valid number, or exits the dialogue
            try {
                String inputString = JOptionPane.showInputDialog("Give " + trainer.getFirstName() + " a rating! (Must be an integer value between 1 and 5)");
                if (inputString == null) {
                    // if the user exits the dialogue, close it
                    return;
                }
                ratingValue = Integer.parseInt(inputString);
            }
            catch (NumberFormatException e) {
                // if the input cannot be converted to an integer, reopen the dialogue
                System.out.println(e);
                continue;
            }
            if (ratingValue >= 1 && ratingValue <= 5) {
                // if the rating is not between 1 and 5, reopen the dialogue
                successful = true;
            }
        }
        // if the rating is valid, add it to the database
        TrainerManager.getInstance().addRatingByClient(trainer.getTrainerID(), client_id, ratingValue);
    }
}
