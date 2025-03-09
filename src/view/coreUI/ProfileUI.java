package view.coreUI;

import model.User;
import services.UserServices;
import view.Components.NavigationPanel;
import view.Components.UIBase;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.*;
import java.nio.file.*;
import java.util.stream.Stream;

public class ProfileUI extends UIBase {

    private final int WIDTH = this.getWidth();

    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images

    private JPanel contentPanel; // Panel to display the image grid or the clicked image
    private JPanel headerPanel; // Panel for the header
    private UserServices userService;
    private String username;

    public ProfileUI(String username) {
        this.username = username;
        setTitle("DACS Profile");
        
        userService = new UserServices(username);

        contentPanel = new JPanel();
        headerPanel = createHeaderPanel(); // Initialize header panel

        initializeUI();
    }

    // TODO FIND PURPOSE OF THIS
    public ProfileUI() {
        setTitle("DACS Profile");

        contentPanel = new JPanel();
        headerPanel = createHeaderPanel(); // Initialize header panel

        initializeUI();
    }

    private void initializeUI() {
        getContentPane().removeAll(); // Clear existing components

        // Re-add the header and navigation panels
        add(headerPanel, BorderLayout.NORTH);
        add(new NavigationPanel(this), BorderLayout.SOUTH);

        // Initialize the image grid
        initializeImageGrid();

        revalidate();
        repaint();
    }

    private JPanel createHeaderPanel() {
        String loggedInUsername = userService.getLoggedInUsername();
        boolean isCurrentUser = loggedInUsername.equals(username);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        // Top Part of the Header (Profile Image, Stats, Follow Button)
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));

        // Profile image
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("resources/img/storage/profile/" + username + ".png").getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topHeaderPanel.add(profileImage, BorderLayout.WEST);

        // Stats Panel
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));

        // TODO MAKE statsLabel use Userservices for getter functions
        statsPanel.add(createStatLabel(Integer.toString(userService.getPostCount(username)), "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(userService.getFollowerCount(username)), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(userService.getFollowingCount(username)), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding

        JButton profileButton = createProfileButton(isCurrentUser, loggedInUsername);

        // Add Stats and Follow Button to a combined Panel
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(statsPanel);
        statsFollowPanel.add(profileButton);
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);

        headerPanel.add(topHeaderPanel);

        // Profile Name and Bio Panel
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));

        JLabel profileNameLabel = new JLabel(username);
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides

        JTextArea profileBio = new JTextArea(username);
        System.out.println("This is the bio " + username);
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides

        profileNameAndBioPanel.add(profileNameLabel, BorderLayout.NORTH);
        profileNameAndBioPanel.add(profileBio, BorderLayout.CENTER);

        headerPanel.add(profileNameAndBioPanel);

        return headerPanel;

    }

    // TODO MAKE HEADER UPDATE WHEN ACTION IS PRESSED
    // TODO FIGURE THIS OUT AFTER profileUI optimization is Done
    private JButton createProfileButton(boolean isCurrentUser, String loggedInUsername) {
        JButton profileButton = new JButton("Edit Profile");
        if (!isCurrentUser) {
            String buttonText = userService.isFollowing(loggedInUsername, username) ? "Unfollow" : "Follow";
            profileButton.setText(buttonText);
            profileButton.addActionListener(e -> {
                if(userService.isFollowing(loggedInUsername, username)){
                    userService.unFollowUser(loggedInUsername, username);
                    profileButton.setText("Follow");
                } else {
                    userService.followUser(loggedInUsername, username);
                    profileButton.setText("Unfollow");
                }
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

    private void initializeImageGrid() {
        contentPanel.removeAll(); // Clear existing content
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

        // TODO CREATE A initializeUserPosts method in UserServices
        // TODO EXTRACT DATA HANDLING TO readUserPosts in UserRepository
        // TODO USE List<Post>.getImagePath instead of reading from file
        Path imageDir = Paths.get("resources/img", "uploaded");
        try (Stream < Path > paths = Files.list(imageDir)) {
            paths.filter(path -> path.getFileName().toString().startsWith(username + "_"))
                    .forEach(path -> {
                        ImageIcon imageIcon = new ImageIcon(new ImageIcon(path.toString()).getImage().getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
                        JLabel imageLabel = new JLabel(imageIcon);
                        imageLabel.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                displayImage(imageIcon); // Call method to display the clicked image
                            }
                        });
                        contentPanel.add(imageLabel);
                    });
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle exception (e.g., show a message or log)
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

        revalidate();
        repaint();
    }

    private void displayImage(ImageIcon imageIcon) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            getContentPane().removeAll(); // Remove all components from the frame
            initializeUI(); // Re-initialize the UI
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }

}