package view.coreUI;

import controller.UserController;
import services.FileServices;
import view.components.HeaderPanel;
import view.components.NavigationPanel;
import view.components.UIBase;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NotificationsUI extends UIBase {

    private UserController userController;

    /**
     * Constructor for NotificationsUI.
     *
     * This constructor initializes the NotificationsUI components containing
     * the header panel, navigation panel and the content panel containing the
     * actual notifications.
     * */
    public NotificationsUI() {
        setTitle("Notifications");
        this.userController = new UserController();
        buildUI();
    }

    /*
     * Builds the user interface for the Notifications screen.
     *
     * This method sets up the header panel, navigation panel, and content panel
     * for displaying notifications. It reads notifications from a file and
     * formats them for display.
     */
    private void buildUI() {
        JPanel headerPanel = new HeaderPanel("Notifications");
        JPanel navigationPanel = new NavigationPanel(this);

        // Content Panel for notifications
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/data", "notifications.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].trim().equals(userController.getLoggedInUsername())) {
                    // Formats the notification message
                    String userWhoInteracted = parts[1].trim();
                    String imageId = parts[2].trim();
                    String timestamp = parts[3].trim();
                    String type = parts[4].trim();

                    String notificationMessage;
                    switch (type) {
                        case "like":
                           notificationMessage = userWhoInteracted + " liked your post";
                           break;
                        case "comment":
                            notificationMessage = userWhoInteracted + " commented on your post";
                            break;
                        default:
                            notificationMessage = userWhoInteracted + " interacted with your post";
                            break;
                    }

                    // Adds the notification to the panel
                    JPanel notificationPanel = new JPanel(new BorderLayout());
                    notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                    // Adds profile icon (if available) and timestamp
                    ImageIcon profileIcon = new ImageIcon("resources/img/storage/profile/" + userWhoInteracted + ".png");
                    profileIcon.setImage(profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
                    JLabel scaledIcon = new JLabel(profileIcon);

                    String timestampMessage = FileServices.getElapsedTimestamp(timestamp);
                    if(!timestampMessage.equals("Just now")) {
                        timestampMessage = FileServices.getElapsedTimestamp(timestamp) + " ago";
                    }

                    JLabel timestampLabel = new JLabel(timestampMessage);
                    timestampLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                    notificationPanel.add(scaledIcon, BorderLayout.WEST);
                    notificationPanel.add(timestampLabel, BorderLayout.EAST);

                    JLabel notificationLabel = new JLabel(notificationMessage);
                    notificationLabel.setFont(new Font("Arial", Font.BOLD, 12));
                    notificationPanel.add(notificationLabel, BorderLayout.CENTER);

                    contentPanel.add(notificationPanel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

    }
}