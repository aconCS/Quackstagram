package view.authenticationUI;

import view.Components.HeaderPanel;

import javax.swing.*;
import java.awt.*;

public class ComponentFactory {
    static JPanel createHeader(){
        return new HeaderPanel();
    }

    static JButton createButton(String text){
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        return button;
    }

    static JPanel createFieldPanel(String label, JTextField textField){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create fieldLabel and style
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Style textField
        textField.setPreferredSize(new Dimension(200, 30));

        panel.add(fieldLabel);
        panel.add(textField);

        return panel;
    }

    static JPanel createLogoPanel(){
        JLabel lblPhoto = new JLabel();

        // Set alignment and sizing of Photo
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setPreferredSize(new Dimension(80, 80));

        // Set icon
        lblPhoto.setIcon(new ImageIcon(new ImageIcon("resources/img/logos/DACS.png")
                .getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH)));

        // Add to photoPanel
        JPanel photoPanel = new JPanel();
        photoPanel.add(lblPhoto);

        return photoPanel;
    }
}
