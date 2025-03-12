package view.authenticationUI;

import view.components.HeaderPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class AuthUIBuilder{

    private final JFrame frame;
    private final ArrayList<JButton> buttons;
    private final ArrayList<JPanel> fieldPanels;

    public AuthUIBuilder(JFrame frame){
        this.frame = frame;

        buttons = new ArrayList<>();
        fieldPanels = new ArrayList<>();
    }

    /*
    * Adds a panel created by the ComponentFactory using the given label and textField to the fieldPanels list.
    * */
    public AuthUIBuilder addTextFieldPanel(String label, JTextField textField){
        JPanel panel = ComponentFactory.createFieldPanel(label, textField);
        fieldPanels.add(panel);
        return this;
    }

    /*
    * Adds a button created by the ComponentFactory using the given label and actionListener to the buttons list.
    * */
    public AuthUIBuilder addButton(String label, ActionListener action){
        JButton button = ComponentFactory.createButton(label);
        button.addActionListener(action);
        buttons.add(button);
        return this;
    }

    public AuthUIBuilder addHeaderPanel(String title){
        JPanel headerPanel = new HeaderPanel(title);
        frame.add(headerPanel, BorderLayout.NORTH);
        return this;
    }

    /*
    * Builds the general UI structure for the authentication screens (shared by SignInUI and SignUpUI).
    * */
    public void buildUI() {

        // Create bodyPanel to hold logo and fieldPanels
        JPanel bodyPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // GridBagLayout settings
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create buttonPanel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Create and add logoPanel to bodyPanel
        JPanel logoPanel = ComponentFactory.createLogoPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across all columns
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment
        bodyPanel.add(logoPanel, gbc);


        // Add fields to bodyPanel
        gbc.gridwidth = 1; // Reset to default
        gbc.anchor = GridBagConstraints.WEST; // Left alignment
        for(JPanel fieldPanel : fieldPanels){
            gbc.gridy++;
            bodyPanel.add(fieldPanel, gbc);
        }

        // Add buttons to buttonPanel
        for(JButton button : buttons){
            buttonPanel.add(button);
            buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Add panels to the root frame
        frame.add(bodyPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
    }
}
