package controller;

import javax.swing.*;

public class NavigationController {

    private static NavigationController instance;

    private NavigationController() {}

    /*
    * Singleton class to ensure only one instance of NavigationController is created.
    * */
    public static NavigationController getInstance() {
        if (instance == null) {
            instance = new NavigationController();
        }
        return instance;
    }

    /*
    * Disposes the current frame and makes the next frame visible.
    * */
    public void navigate(JFrame currentFrame, JFrame nextFrame) {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
        SwingUtilities.invokeLater(() -> nextFrame.setVisible(true));
    }

}
