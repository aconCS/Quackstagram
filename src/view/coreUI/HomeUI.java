package view.coreUI;

import controller.ImageLikesServices;
import controller.PostController;
import controller.UserController;
import view.Components.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HomeUI extends UIBase {
    private final int WIDTH = this.getWidth();
    private final int HEIGHT = this.getHeight();

    private final int IMAGE_WIDTH = WIDTH - 100; // Width for the image posts
    private static final int IMAGE_HEIGHT = 150; // Height for the image posts

    private static final Color COMENT_BUTTON_COLOR = new Color(0, 200, 200); // Color for the like button
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JPanel homePanel;
    private final JPanel imageViewPanel;
    private final UserController userController;
    private final ImageLikesServices imageLikesService;
    private PostController postController;
    private CommentPanel commentPanel;

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
            ImageIcon imageIcon = createScaledIcon(postData[3], IMAGE_WIDTH, IMAGE_HEIGHT);
            if (imageIcon != null) {
                imageLabel.setIcon(imageIcon);
            }else{
                imageLabel.setText("Image not found");
            }

            JLabel descriptionLabel = new JLabel(postData[1]);
            descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            String imageId = new File(postData[3]).getName().split("\\.")[0];
            postController = new PostController(imageId);
            JPanel likePanel = new LikeButton(postController);

            itemPanel.add(nameLabel);
            itemPanel.add(imageLabel);
            itemPanel.add(descriptionLabel);
            itemPanel.add(likePanel);

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



    private String[][] createSampleData() {
        String currentUser = userController.getLoggedInUsername();

        ArrayList<String> followedUsers = new ArrayList<>();
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
        postController = new PostController(imageId);
        commentPanel = new CommentPanel(imageId);

        // Display the image
        JLabel fullSizeImageLabel = new JLabel();
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        fullSizeImageLabel.setIcon(createScaledIcon(postData[3], WIDTH, HEIGHT));

        // User Info
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(postData[0]);
        userName.setFont(new Font("Arial", Font.BOLD, 18));
        userPanel.add(userName); // User Name

        // Button panel and buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel likeButton = new LikeButton(postController);
        JButton commentButton = createCommentButton(imageId);
        buttonPanel.add(likeButton);
        buttonPanel.add(commentButton);

        // Information panel at the bottom
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel caption = new JLabel(postData[1]);
        infoPanel.add(caption); // Description
        infoPanel.add(buttonPanel);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(infoPanel, BorderLayout.NORTH);
        bottomPanel.add(commentPanel, BorderLayout.CENTER);

        imageViewPanel.add(fullSizeImageLabel, BorderLayout.CENTER);
        imageViewPanel.add(userPanel, BorderLayout.NORTH);
        imageViewPanel.add(bottomPanel, BorderLayout.SOUTH);

        imageViewPanel.revalidate();
        imageViewPanel.repaint();

        cardLayout.show(cardPanel, "ImageView"); // Switch to the image view
    }

    private ImageIcon createScaledIcon(String path, int width, int height) {
        ImageIcon imageIcon;
        try {
            BufferedImage originalImage = ImageIO.read(new File(path));
            Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            BufferedImage bufferedScaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedScaledImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            BufferedImage croppedImage = bufferedScaledImage.getSubimage(0, 0, Math.min(bufferedScaledImage.getWidth(), width), Math.min(bufferedScaledImage.getHeight(), height - 20));
             imageIcon = new ImageIcon(croppedImage);
        } catch (IOException ex) {
            // Handle exception: Image file not found or reading error
            return null;
        }
        return imageIcon;
    }

    private JButton createCommentButton(String imageId) {
        JButton commentButton = new JButton("💬");
        commentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentButton.setBackground(COMENT_BUTTON_COLOR); // Set the background color for the like button
        commentButton.setOpaque(true);
        commentButton.setBorderPainted(false);
        commentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCommentAction(imageId);
            }
        });

        return commentButton;
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

        likesLabel.setText(postData[2]);
    }

    private void handleCommentAction(String imageId) {
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
                    postController.addCommentToPost(comment);
                    // Close the dialog
                    commentDialog.dispose();
                    // Refresh comments
                    commentPanel.refreshCommentsPanel();

                }
            }
        });
        commentDialog.add(submitButton, BorderLayout.SOUTH);

        // Show the dialog
        commentDialog.setLocationRelativeTo(null);
        commentDialog.setVisible(true);
    }



}