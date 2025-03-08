package view.authenticationUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class AuthUIBuilder{
    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    private final JFrame frame;
    private final ArrayList<JButton> buttons;
    private final ArrayList<JPanel> fieldPanels;

    public AuthUIBuilder(JFrame frame, String title){
        this.frame = frame;
        frame.setTitle(title);
        frame.setResizable(false);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        buttons = new ArrayList<>();
        fieldPanels = new ArrayList<>();
    }

    public AuthUIBuilder addTextFieldPanel(String label, JTextField textField){
        JPanel panel = ComponentFactory.createFieldPanel(label, textField);
        fieldPanels.add(panel);
        return this;
    }

    public AuthUIBuilder addButton(String label, Color color, ActionListener action){
        JButton button = ComponentFactory.createButton(label, color);
        button.addActionListener(action);
        buttons.add(button);
        return this;
    }

    public void buildUI() {
        // Create headerPanel
        JPanel headerPanel = ComponentFactory.createHeader(WIDTH);

        // Create bodyPanel to hold logo and fieldPanels
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

        // Create buttonPanel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Create and add logoPanel to bodyPanel
        JPanel logoPanel = ComponentFactory.createLogoPanel();
        bodyPanel.add(logoPanel);

        // Add fields to bodyPanel
        for(JPanel fieldPanel : fieldPanels){
            bodyPanel.add(fieldPanel);
        }

        // Add buttons to buttonPanel
        for(JButton button : buttons){
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(button);
        }

        // Add panels to the root frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(bodyPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }
}
