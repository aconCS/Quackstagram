package view.Components;

import controller.NavigationController;
import controller.PostController;
import view.coreUI.ProfileUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserNavPanel extends JPanel {

    private final PostController postController;

    public UserNavPanel(PostController postController) {
        this.postController = postController;
        setLayout(new FlowLayout(FlowLayout.LEFT));

        initializeUI();
    }

    private void initializeUI() {
        ImageIcon profileIcon = new ImageIcon("resources/img/storage/profile/" + postController.getImageOwner() + ".png");
        profileIcon.setImage(profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JLabel scaledIcon = new JLabel(profileIcon);
        add(scaledIcon); // Add profile icon

        JLabel userName = new JLabel(postController.getImageOwner());
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        add(userName); // Add usernameLabel

        // Make the image clickable
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String imageOwner = postController.getImageOwner();
                JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(UserNavPanel.this);
                NavigationController.getInstance().navigate(currFrame, new ProfileUI(imageOwner)); // Call a method to switch to the image view
            }
        });

    }
}
