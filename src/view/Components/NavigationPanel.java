package view.Components;

import controller.NavigationController;
import model.User;
import view.coreUI.*;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NavigationPanel extends JPanel {

    private static final int NAV_ICON_SIZE = 20; // Size for navigation icons
    private final JFrame currFrame;

    // TODO CHANGE
    public NavigationPanel(JFrame currFrame) {
        this.currFrame = currFrame;
        buildNavigationPanel();
    }

    public void buildNavigationPanel() {
        setBackground(new Color(249, 249, 249));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(createIconButton("resources/img/icons/home.png", "home"));
        add(Box.createHorizontalGlue());
        add(createIconButton("resources/img/icons/search.png", "explore"));
        add(Box.createHorizontalGlue());
        add(createIconButton("resources/img/icons/add.png", "add"));
        add(Box.createHorizontalGlue());
        add(createIconButton("resources/img/icons/notification.png", "notification"));
        add(Box.createHorizontalGlue());
        add(createIconButton("resources/img/icons/profile.png", "profile"));
    }

    private JButton createIconButton(String iconPath, String buttonType) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // Define actions based on button type
        if ("home".equals(buttonType)) {
            button.addActionListener(e -> openHomeUI());
        } else if ("profile".equals(buttonType)) {
            button.addActionListener(e -> openProfileUI());
        } else if ("notification".equals(buttonType)) {
            button.addActionListener(e -> openNotificationsUI());
        } else if ("explore".equals(buttonType)) {
            button.addActionListener(e -> openExploreUI());
        } else if ("add".equals(buttonType)) {
            button.addActionListener(e -> openImageUploadUI());
        }
        return button;

    }

    private void openProfileUI() {
        String loggedInUsername = "";

        // TODO HANDLE SERVICE OUTSIDE VIEW LAYER
        // TODO SERVICE IS READING THE loggedIN USER FROM DATA FILE
        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = new User(loggedInUsername);
        // Navigate to profile UI
        NavigationController.getInstance().navigate(currFrame, new ProfileUI(user));
    }

    private void openImageUploadUI() { NavigationController.getInstance().navigate(currFrame, new ImageUploadUI()); }

    private void openNotificationsUI() { NavigationController.getInstance().navigate(currFrame, new NotificationsUI()); }

    private void openHomeUI() { NavigationController.getInstance().navigate(currFrame, new HomeUI()); }

    private void openExploreUI() {NavigationController.getInstance().navigate(currFrame, new ExploreUI()); }
}
