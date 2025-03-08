package view.authenticationUI;

import javax.swing.*;
import java.awt.*;

public class ComponentFactory {
    static JPanel createHeader(int WIDTH){
        // Header with the Register label
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel headerLabel = new JLabel("Quackstagram");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE); // Set the text color to white
        headerLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        headerPanel.add(headerLabel);
        headerPanel.setPreferredSize(new Dimension(WIDTH, 50)); // Give the header a fixed height

        return headerPanel;
    }

    static JButton createButton(String text, Color color){
        JButton button = new JButton(text);
        button.setBackground(color); // Use a red color that matches the mockup
        button.setForeground(Color.BLACK); // Set the text color to black
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    static JPanel createFieldPanel(String label, JTextField textField){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Create fieldLabel and style
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Arial", Font.BOLD, 14));

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
