package display;

import programs.Exercise;
import programs.Program;
import users.Trainer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Page used to view the exercises contained in a trainer's program
 */
public class ViewProgramPage extends Page{
    /**
     * The program currently being viewed
     */
    private final Program program;
    /**
     * The trainer who owns the program
     */
    private final Trainer trainer;

    ViewProgramPage(DisplayController displayController, Program program, Trainer trainer) {
        super(displayController);
        this.program = program;
        this.trainer = trainer;
    }

    /**
     * Creates the exercise panels and adds them to the page
     */
    private void initialiseExercisePanels() {
        int marginX = 100;
        int marginY = 25;
        int exerciseCardHeight = 150;
        // fetch the program's exercises
        ArrayList<Exercise> exercises = program.getExercises();
        int runningExerciseCount = 0;
        for (Exercise exercise : exercises) {
            // create the exercise panel
            JPanel exercisePanel = new JPanel();
            exercisePanel.setBorder(getLineBorder());
            exercisePanel.setBounds(marginX, getContentStartHeight() + runningExerciseCount*(exerciseCardHeight + marginY), getContentWidth() - 2* marginX, exerciseCardHeight);
            // add information label
            JLabel exerciseInfoLabel = new JLabel(exercise.getInfoString(runningExerciseCount+1), SwingConstants.CENTER);
            exerciseInfoLabel.setFont(getBasicFont());
            exercisePanel.add(exerciseInfoLabel);
            // add open example video button
            JButton openExampleVideoButton = new JButton("Open Example Video");
            openExampleVideoButton.setFont(getButtonFont());
            openExampleVideoButton.setFocusPainted(false);
            openExampleVideoButton.addActionListener(e->openVideo(exercise.getVideoPath()));
            exercisePanel.add(openExampleVideoButton);
            // add the exercise panel to the page
            this.add(exercisePanel);
            runningExerciseCount++;
        }
    }

    /**
     * Opens the video in the specified path in the system's default media player
     * @param videoPath: The path to the video to be opened
     */
    private void openVideo(String videoPath) {
        try {
            Desktop.getDesktop().open(new File(videoPath));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Overriding abstract class in Page.
     * Called by DisplayController when this page is loaded to initialise the page's components.
     */
    @Override
    public void initialisePanels() {
        initialiseTitlePanel("<html>" + trainer.getFirstName() + "'s Program: " + program.getTitle() + "</html>", this);
        initialiseExercisePanels();
    }
}
