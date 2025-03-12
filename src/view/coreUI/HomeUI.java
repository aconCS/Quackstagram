package view.coreUI;

import controller.NavigationController;
import controller.PostController;
import controller.UserController;
import services.FileServices;
import view.components.*;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class HomeUI extends UIBase {
    private final int width = this.getWidth();

    private final int imageWidth = width - 100; // Width for the image posts
    private final int imageHeight = imageWidth; // Height for the image posts

    private final JPanel homePanel;
    private final UserController userController;

    public HomeUI() {
        setTitle("Quakstagram Home");

        userController = new UserController();
        homePanel = new JPanel(new BorderLayout());

        buildUI();

        add(homePanel, BorderLayout.CENTER);
        add(new HeaderPanel("Home"), BorderLayout.NORTH);
        add(new NavigationPanel(this), BorderLayout.SOUTH);
    }

    private void buildUI() {
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
            itemPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            itemPanel.setAlignmentX(CENTER_ALIGNMENT);
            JLabel nameLabel = new JLabel(postData[0]);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel imageLabel = new JLabel();
            imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            imageLabel.setPreferredSize(new Dimension(imageWidth, imageHeight));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to image label
            ImageIcon imageIcon = FileServices.createScaledIcon(postData[3], imageWidth, imageHeight);
            if (imageIcon != null) {
                imageLabel.setIcon(imageIcon);
            }else{
                imageLabel.setText("Image not found");
            }

            JLabel descriptionLabel = new JLabel(postData[1]);
            descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            String imageId = new File(postData[3]).getName().split("\\.")[0];
            PostController postController = new PostController(imageId);

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
                    JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(imageLabel);
                    NavigationController.getInstance().navigate(currFrame, new PostUI(postController)); // Call a method to switch to the image view
                }
            });

            // Grey spacing panel
            JPanel spacingPanel = new JPanel();
            spacingPanel.setPreferredSize(new Dimension(width - 10, 5)); // Set the height for spacing
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

}