package view.components;

import controller.NavigationController;
import view.authenticationUI.SignInUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UIBase extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    // Sets the size of the frame and centers it on the screen
    public UIBase() {
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                onExit();
            }
        });
        setLayout(new BorderLayout());
    }

    public void onExit(){
        NavigationController.getInstance().navigate(this, new SignInUI());
    }
}
