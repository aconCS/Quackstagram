package view.components;

import controller.NavigationController;
import controller.UserController;
import view.coreUI.EditProfileUI;

import javax.swing.*;
import java.awt.*;

public class ProfileHeader extends JPanel {
    private static final int PROFILE_IMAGE_SIZE = 80;
    private final String username;
    private final UserController userController;
    private JPanel statsPanel;

    public ProfileHeader(String username, UserController userController) {
        this.username = username;
        this.userController = userController;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.GRAY);

        buildHeaderPanel();
    }

    private void refresh() {
        int postCount = userController.getPostCount();
        int followerCount = userController.getFollowerCount();
        int followingCount = userController.getFollowingCount();

        statsPanel.removeAll();
        statsPanel.add(createStatLabel(Integer.toString(postCount), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(followerCount), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(followingCount), "Following"));
        statsPanel.revalidate();
        statsPanel.repaint();

        revalidate();
        repaint();
    }

    private void buildHeaderPanel() {
        String loggedInUsername = userController.getLoggedInUsername();
        boolean isCurrentUser = loggedInUsername.equals(username);

        // Top Part of the Header (Profile Image, Stats, Follow Button)
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Profile image
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("resources/img/storage/profile/" + username + ".png").getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);

        // Stats Panel
        statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));

        statsPanel.add(createStatLabel(Integer.toString(userController.getPostCount()), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(userController.getFollowerCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(userController.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding

        JButton profileButton = createProfileButton(isCurrentUser, loggedInUsername);

        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(profileButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        add(topHeaderPanel);

        // Profile Name and Bio Panel
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

        JLabel profileNameLabel = new JLabel(username);
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

        JTextArea profileBio = new JTextArea(userController.getBio());
        System.out.println("This is the bio " + username);
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        add(profileNameAndBioPanel);
    }

    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

    private JButton createProfileButton(boolean isCurrentUser, String loggedInUsername) {
        JButton profileButton = new JButton("null");

        if (!isCurrentUser) {
            String buttonText = userController.isFollowing(loggedInUsername, username) ? "Unfollow" : "Follow";
            profileButton.setText(buttonText);
            profileButton.addActionListener(e -> {
                if(userController.isFollowing(loggedInUsername, username)){
                    userController.unfollowUser(loggedInUsername, username);
                    profileButton.setText("Follow");
                    refresh();
                } else {
                    userController.followUser(loggedInUsername, username);
                    profileButton.setText("Unfollow");
                    refresh();
                }
            });
        }else{
            profileButton.setText("Edit Profile");
            profileButton.addActionListener(e -> {
                JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                NavigationController.getInstance().navigate(currFrame, new EditProfileUI(userController));
            });
        }

        profileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileButton.setFont(new Font("Arial", Font.BOLD, 12));
        profileButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, profileButton.getMinimumSize().height)); // Make the button fill the horizontal space
        profileButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
        profileButton.setForeground(Color.BLACK);
        profileButton.setOpaque(true);
        profileButton.setBorderPainted(false);
        profileButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding

        return profileButton;
    }
}