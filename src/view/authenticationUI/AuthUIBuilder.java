package view.authenticationUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class AuthUIBuilder{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    private final JFrame FRAME;
    private final ArrayList<JButton> BUTTONS;
    private final ArrayList<JPanel> FIELD_PANELS;

    public AuthUIBuilder(JFrame frame, String title){
        this.FRAME = frame;
        FRAME.setTitle(title);
        FRAME.setResizable(false);
        FRAME.setSize(WIDTH, HEIGHT);
        FRAME.setLocationRelativeTo(null);
        FRAME.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        FRAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FRAME.setLayout(new BorderLayout());

        BUTTONS = new ArrayList<>();
        FIELD_PANELS = new ArrayList<>();
    }

    public AuthUIBuilder addTextFieldPanel(String label, JTextField textField){
        JPanel panel = ComponentFactory.createFieldPanel(label, textField);
        FIELD_PANELS.add(panel);
        return this;
    }

    public AuthUIBuilder addButton(String label, Color color, ActionListener action){
        JButton button = ComponentFactory.createButton(label, color);
        button.addActionListener(action);
        BUTTONS.add(button);
        return this;
    }

    public void buildUI() {
        // Create headerPanel
        JPanel headerPanel = ComponentFactory.createHeader(WIDTH);

        // Create bodyPanel to hold logo and FIELD_PANELS
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

        // Create buttonPanel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Create and add logoPanel to bodyPanel
        JPanel logoPanel = ComponentFactory.createLogoPanel();
        bodyPanel.add(logoPanel);

        // Add fields to bodyPanel
        for(JPanel fieldPanel : FIELD_PANELS){
            bodyPanel.add(fieldPanel);
        }

        // Add buttons to buttonPanel
        for(JButton button : BUTTONS){
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(button);
        }

        // Add panels to the root frame
        FRAME.add(headerPanel, BorderLayout.NORTH);
        FRAME.add(bodyPanel, BorderLayout.CENTER);
        FRAME.add(buttonPanel, BorderLayout.SOUTH);
    }
}
