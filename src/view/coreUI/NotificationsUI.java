package view.coreUI;

import controller.UserController;
import view.Components.HeaderPanel;
import view.Components.NavigationPanel;
import view.Components.UIBase;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class NotificationsUI extends UIBase {
    UserController userController;


    public NotificationsUI() {
        setTitle("Notifications");
        this.userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        // Reuse the header and navigation panel creation methods from the InstagramProfileUI class
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
                    // Format the notification message
                    String userWhoLiked = parts[1].trim();
                    String imageId = parts[2].trim();
                    String timestamp = parts[3].trim();
                    String notificationMessage = userWhoLiked + " liked your picture";

                    // Add the notification to the panel
                    JPanel notificationPanel = new JPanel(new BorderLayout());
                    notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                    // Add profile icon (if available) and timestamp
                    JLabel profileIcon = new JLabel(new ImageIcon("resources/img/storage/profile/" + userWhoLiked + ".png"));

                    String timestampMessage = getElapsedTime(timestamp);
                    if(!timestampMessage.equals("Just now")) {
                        timestampMessage = getElapsedTime(timestamp) + " ago";
                    }

                    JLabel timestampLabel = new JLabel(timestampMessage);

                    notificationPanel.add(profileIcon, BorderLayout.WEST);
                    notificationPanel.add(timestampLabel, BorderLayout.EAST);

                    JLabel notificationLabel = new JLabel(notificationMessage);
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

    private String getElapsedTime(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeOfNotification = LocalDateTime.parse(timestamp, formatter);
        LocalDateTime currentTime = LocalDateTime.now();

        long daysBetween = ChronoUnit.DAYS.between(timeOfNotification, currentTime);
        long minutesBetween = ChronoUnit.MINUTES.between(timeOfNotification, currentTime) % 60;

        StringBuilder timeElapsed = new StringBuilder();
        if (daysBetween > 0) {
            timeElapsed.append(daysBetween).append(" day").append(daysBetween > 1 ? "s" : "");
        }
        if (minutesBetween > 0) {
            if (daysBetween > 0) {
                timeElapsed.append(" and ");
            }
            timeElapsed.append(minutesBetween).append(" minute").append(minutesBetween > 1 ? "s" : "");
        } else {
            timeElapsed.append("Just now");
        }

        return timeElapsed.toString();
    }

}