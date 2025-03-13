package view.components;

import controller.NavigationController;
import view.coreUI.ProfileUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserNavPanel extends JPanel {

    private final String imageOwner;

    public UserNavPanel(String imageOwner) {
        this.imageOwner = imageOwner;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        buildUI();
    }

    private void buildUI() {
        ImageIcon profileIcon = new ImageIcon("resources/img/storage/profile/" + imageOwner + ".png");
        profileIcon.setImage(profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JLabel scaledIcon = new JLabel(profileIcon);
        add(scaledIcon); // Add profile icon

        JLabel userName = new JLabel(imageOwner);
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        add(userName); // Add usernameLabel

        // Make the image clickable
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(UserNavPanel.this);
                NavigationController.getInstance().navigate(currFrame, new ProfileUI(imageOwner)); // Call a method to switch to the image view
            }
        });

    }
}
