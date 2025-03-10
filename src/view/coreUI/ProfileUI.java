package view.coreUI;

import controller.UserController;
import view.Components.ImageGrid;
import view.Components.NavigationPanel;
import view.Components.ProfileHeader;
import view.Components.UIBase;

import java.awt.*;

public class ProfileUI extends UIBase {

    private final int WIDTH = this.getWidth();
    private final int IMAGE_SIZE = WIDTH / 3; // Static size for grid images

    private final UserController userController;
    private final String username;

    public ProfileUI(String username) {
        this.username = username;
        setTitle("DACS Profile");

        userController = new UserController(username);

        buildUI();
    }

    private void buildUI() {
        getContentPane().removeAll(); // Clear existing components

        add(new ProfileHeader(username, userController), BorderLayout.NORTH);
        add(new NavigationPanel(this), BorderLayout.SOUTH);

        // Initialize the image grid
        ImageGrid imageGridPanel = new ImageGrid(username, IMAGE_SIZE, true);
        add(imageGridPanel, BorderLayout.CENTER);
    }

}