package display;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * Component class used by Page to add a header to all its child classes.
 */
public class AppHeader extends JPanel {
    AppHeader(DisplayController displayController, Color headerColor, Font headerFont, Font buttonFont) {
        setBackground(headerColor);
        setLayout(new BorderLayout());
        // initialising the title
        JLabel headerLabel = new JLabel("FITMate", SwingConstants.CENTER);
        headerLabel.setFont(headerFont);
        this.add(headerLabel, BorderLayout.CENTER);
        // initialising the return to dashboard button
        JButton returnToDashboardButton = new JButton("Return to your dashboard");
        returnToDashboardButton.setFont(buttonFont);
        returnToDashboardButton.setFocusPainted(false);
        returnToDashboardButton.addActionListener(e->displayController.openClientDashboard());
        this.add(returnToDashboardButton, BorderLayout.WEST);
        // initialising the change account button
        JButton changeAccountButton = new JButton("Switch account");
        changeAccountButton.setFont(buttonFont);
        changeAccountButton.setFocusPainted(false);
        changeAccountButton.addActionListener(e->displayController.openLoginDialogue(false));
        this.add(changeAccountButton, BorderLayout.EAST);
    }
}
