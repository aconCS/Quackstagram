package view.Components;

import javax.swing.*;
import java.awt.*;

public class UIBase extends JFrame {
    private static final int HEIGHT = 500;
    private static final int WIDTH = 300;

    // Sets the size of the frame and centers it on the screen
    public UIBase() {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }
}
