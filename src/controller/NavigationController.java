package controller;

import javax.swing.*;

public class NavigationController {

    private static NavigationController instance;

    private NavigationController() {}

    public static NavigationController getInstance() {
        if (instance == null) {
            instance = new NavigationController();
        }
        return instance;
    }

    public void navigate(JFrame currentFrame, JFrame nextFrame) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        SwingUtilities.invokeLater(() -> nextFrame.setVisible(true));
    }

}
