package view.components;

import controller.NavigationController;
import controller.UserController;
import view.coreUI.*;

import javax.swing.*;
import java.awt.*;

public class NavigationPanel extends JPanel {

    private static final int NAV_ICON_SIZE = 20; // Size for navigation icons
    private final UserController userController;
    private final JFrame currFrame;

    // TODO CHANGE
    public NavigationPanel(JFrame currFrame) {
        this.userController = new UserController();
        this.currFrame = currFrame;
        buildNavigationPanel();
    }

    /*
     * Builds the navigation bar that every UI frame will have at the bottom
     * of the window by calling createIconButton for each icon.
     * */
    private void buildNavigationPanel() {
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

    /*
    * Creates a JButton with an icon that has an actionListener based on the button type
    * which navigates to the corresponding UI frame.
    * */
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

    /*
    * Reads the logged-in user's username from users.txt and navigates to the profile UI.
    * */
    private void openProfileUI() {
        // Navigate to profile UI
        NavigationController.getInstance().navigate(currFrame, new ProfileUI(userController.getLoggedInUsername()));
    }

    private void openImageUploadUI() { NavigationController.getInstance().navigate(currFrame, new ImageUploadUI()); }

    private void openNotificationsUI() { NavigationController.getInstance().navigate(currFrame, new NotificationsUI()); }

    private void openHomeUI() { NavigationController.getInstance().navigate(currFrame, new HomeUI()); }

    private void openExploreUI() {NavigationController.getInstance().navigate(currFrame, new ExploreUI()); }
}
