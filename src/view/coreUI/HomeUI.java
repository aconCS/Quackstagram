package view.coreUI;

import controller.ImageLikesServices;
import controller.UserController;
import view.Components.HeaderPanel;
import view.Components.NavigationPanel;
import view.Components.UIBase;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class HomeUI extends UIBase {
    private final int WIDTH = this.getWidth();
    private final int HEIGHT = this.getHeight();

    private final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    private static final int IMAGE_HEIGHT = 150; // Height for the image posts

    private static final Color LIKE_BUTTON_COLOR = new Color(255, 90, 95); // Color for the like button
    private static final Color COMENT_BUTTON_COLOR = new Color(0, 200, 200); // Color for the like button
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JPanel homePanel;
    private final JPanel imageViewPanel;
    private final UserController userController;
    private final ImageLikesServices imageLikesService;

    public HomeUI() {
        setTitle("Quakstagram Home");

        userController = new UserController();
        imageLikesService = new ImageLikesServices();

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new JPanel(new BorderLayout());
        imageViewPanel = new JPanel(new BorderLayout());

        initializeUI();

        cardPanel.add(homePanel, "Home");
        cardPanel.add(imageViewPanel, "ImageView");

        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "Home"); // Start with the home view

        add(new HeaderPanel("Home"), BorderLayout.NORTH);

        add(new NavigationPanel(this), BorderLayout.SOUTH);
    }

    private void initializeUI() {
        // Content Scroll Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Vertical box layout
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Never allow horizontal scrolling
        String[][] sampleData = createSampleData();
        populateContentPanel(contentPanel, sampleData);
        add(scrollPane, BorderLayout.CENTER);

        // Set up the home panel
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        homePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void populateContentPanel(JPanel panel, String[][] sampleData) {

        for (String[] postData: sampleData) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(Color.WHITE); // Set the background color for the item panel
            itemPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemPanel.setAlignmentX(CENTER_ALIGNMENT);
            JLabel nameLabel = new JLabel(postData[0]);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Crop the image to the fixed size
            JLabel imageLabel = new JLabel();
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            imageLabel.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label
            String imageId = new File(postData[3]).getName().split("\\.")[0];
            try {
                BufferedImage originalImage = ImageIO.read(new File(postData[3]));
                BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), IMAGE_WIDTH), Math.min(originalImage.getHeight(), IMAGE_HEIGHT));
                ImageIcon imageIcon = new ImageIcon(croppedImage);
                imageLabel.setIcon(imageIcon);
            } catch (IOException ex) {
                // Handle exception: Image file not found or reading error
                imageLabel.setText("Image not found");
            }

            JLabel descriptionLabel = new JLabel(postData[1]);
            descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel likesLabel = new JLabel(postData[2]);
            likesLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JButton likeButton = createLikeButton(postData, imageId, likesLabel);

            itemPanel.add(nameLabel);
            itemPanel.add(imageLabel);
            itemPanel.add(descriptionLabel);
            itemPanel.add(likesLabel);
            itemPanel.add(likeButton);

            panel.add(itemPanel);

            // Make the image clickable
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(postData); // Call a method to switch to the image view
                }
            });

            // Grey spacing panel
            JPanel spacingPanel = new JPanel();
            spacingPanel.setPreferredSize(new Dimension(WIDTH - 10, 5)); // Set the height for spacing
            spacingPanel.setBackground(new Color(230, 230, 230)); // Grey color for spacing
            panel.add(spacingPanel);
        }
    }

    private void handleLikeAction(String imageId, JLabel likesLabel) {
        Path detailsPath = Paths.get("resources/img", "image_details.txt");
        StringBuilder newContent = new StringBuilder();
        boolean updated = false;
        String currentUser = userController.getLoggedInUsername();
        String imageOwner = "";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Read and update image_details.txt
        try (BufferedReader reader = Files.newBufferedReader(detailsPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + imageId)) {
                    String[] parts = line.split(", ");
                    imageOwner = parts[1].split(": ")[1];
                    int likes = Integer.parseInt(parts[4].split(": ")[1]);
                    likes++; // Increment the likes count
                    parts[4] = "Likes: " + likes;
                    line = String.join(", ", parts);

                    // Update the UI
                    likesLabel.setText("Likes: " + likes);
                    updated = true;
                }
                newContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write updated likes back to image_details.txt
        if (updated) {
            try (BufferedWriter writer = Files.newBufferedWriter(detailsPath)) {
                writer.write(newContent.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Record the like in notifications.txt
            String notification = String.format("%s; %s; %s; %s\n", imageOwner, currentUser, imageId, timestamp);
            try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("resources/data", "notifications.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                notificationWriter.write(notification);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*try {
            imageLikesService.likeImage(userController.getLoggedInUsername(), imageId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    private String[][] createSampleData() {
        String currentUser = userController.getLoggedInUsername();

        ArrayList<String> followedUsers = new ArrayList<String>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/data", "following.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(currentUser + ":")) {
                    followedUsers.add(line.split(":")[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Temporary structure to hold the data
        String[][] tempData = new String[100][]; // Assuming a maximum of 100 posts for simplicity
        int count = 0;

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/img", "image_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null && count < tempData.length) {
                String[] details = line.split(", ");
                String imagePoster = details[1].split(": ")[1];
                if (followedUsers.contains(imagePoster)) {
                    String imagePath = "resources/img/uploaded/" + details[0].split(": ")[1] + ".png"; // Assuming PNG format
                    String description = details[2].split(": ")[1];
                    String likes = "Likes: " + details[4].split(": ")[1];

                    tempData[count++] = new String[] {
                            imagePoster,
                            description,
                            likes,
                            imagePath
                    };
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Transfer the data to the final array
        String[][] sampleData = new String[count][];
        System.arraycopy(tempData, 0, sampleData, 0, count);

        return sampleData;
    }

    private void displayImage(String[] postData) {
        imageViewPanel.removeAll(); // Clear previous content

        String imageId = new File(postData[3]).getName().split("\\.")[0];
        JLabel likesLabel = new JLabel(postData[2]); // Update this line

        // Display the image
        JLabel fullSizeImageLabel = new JLabel();
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);

        try {
            BufferedImage originalImage = ImageIO.read(new File(postData[3]));
            BufferedImage croppedImage = originalImage.getSubimage(0, 0, Math.min(originalImage.getWidth(), WIDTH - 20), Math.min(originalImage.getHeight(), HEIGHT - 40));
            ImageIcon imageIcon = new ImageIcon(croppedImage);
            fullSizeImageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            // Handle exception: Image file not found or reading error
            fullSizeImageLabel.setText("Image not found");
        }

        //User Info
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(postData[0]);
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        userPanel.add(userName); //User Name

        // Button panel and buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton likeButton = createLikeButton(postData, imageId, likesLabel);
        JButton commentButton = createCommentButton(postData, imageId);
        buttonPanel.add(likeButton);
        buttonPanel.add(commentButton);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Information panel at the bottom
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(postData[1])); // Description
        //infoPanel.add(new JLabel(postData[2])); // Likes
        infoPanel.add(likesLabel);
        infoPanel.add(buttonPanel);
        //infoPanel.add(likeButton);
        //infoPanel.add(commentButton);

        imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
        imageViewPanel.add(infoPanel, BorderLayout.SOUTH);
        imageViewPanel.add(userPanel, BorderLayout.NORTH);

        imageViewPanel.revalidate();
        imageViewPanel.repaint();

        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

    private JButton createCommentButton(String[] postData, String imageId) {
        JButton commentButton = new JButton("💬");
        commentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentButton.setBackground(COMENT_BUTTON_COLOR); // Set the background color for the like button
        commentButton.setOpaque(true);
        commentButton.setBorderPainted(false);
        commentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCommentAction(postData, imageId);
            }
        });

        return commentButton;
    }

    private void handleCommentAction1(String[] postData, String imageId) {
        //postObject.addComment(comment);
        /*
        * Opens textfield for user to enter comment, and upon submit button clicked, the comment is added to the postObject
        * How to access the postObject from here?
        * Need to create a userCommentPanel that has the textfield and submit button
        * Need to create another panel that displays the comments belong to the post
        * How to connect imageId to the postObject?
        * Where is postData written to text file originally so the comments can be added to text file?
        * */
    }

    private JButton createLikeButton(String[] postData, String imageId, JLabel likesLabel) {
        JButton likeButton = new JButton("❤");
        likeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        likeButton.setBackground(LIKE_BUTTON_COLOR); // Set the background color for the like button
        likeButton.setOpaque(true);
        likeButton.setBorderPainted(false); // Remove border
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if(imageLikesService.likeImage(userController.getLoggedInUsername(), imageId)){
                        handleLikeAction(imageId, likesLabel); // Update this line
                        refreshDisplayImage(postData, imageId, likesLabel); // Refresh the view
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        return likeButton;
    }

    private void refreshDisplayImage(String[] postData, String imageId, JLabel likesLabel) {
        // Read updated likes count from image_details.txt

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/img", "image_details.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + imageId)) {
                    String likes = line.split(", ")[4].split(": ")[1];//replace with big section below
                    postData[2] = "Likes: " + likes;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleCommentAction(String[] postData, String imageId) {
        // Create a dialog for user to enter comment
        JDialog commentDialog = new JDialog((Frame) null, "Add Comment", true);
        commentDialog.setLayout(new BorderLayout());
        commentDialog.setSize(300, 150);

        // Text field for entering comment
        JTextField commentField = new JTextField();
        commentDialog.add(commentField, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentField.getText();
                if (!comment.isEmpty()) {
                    // Add comment to the post object
                    addCommentToPost(imageId, comment);

                    // Close the dialog
                    commentDialog.dispose();
                }
            }
        });
        commentDialog.add(submitButton, BorderLayout.SOUTH);

        // Show the dialog
        commentDialog.setLocationRelativeTo(null);
        commentDialog.setVisible(true);
    }

    private void addCommentToPost(String imageId, String comment) {
        Path commentsPath = Paths.get("resources/data/", "comments.txt");
        StringBuilder newContent = new StringBuilder();
        boolean updated = false;

        // Read and update image_details.txt
        try (BufferedReader reader = Files.newBufferedReader(commentsPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + imageId)) {
                    String[] parts = line.split(", ");
                    String comments = parts[5].split(": ")[1];
                    comments += comment + ";;;"; // Append the new comment
                    parts[5] = "Comments: " + comments;
                    line = String.join(", ", parts);
                    updated = true;
                }
                newContent.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write updated comments back to image_details.txt
        if (updated) {
            try (BufferedWriter writer = Files.newBufferedWriter(commentsPath)) {
                writer.write(newContent.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}