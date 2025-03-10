package view.coreUI;

import controller.UserController;
import view.Components.HeaderPanel;
import view.Components.NavigationPanel;
import view.Components.UIBase;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageUploadUI extends UIBase {

    private final int WIDTH = this.getWidth();
    private final int HEIGHT = this.getHeight();

    private static final int NAV_ICON_SIZE = 20; // Size for navigation icons
    private JLabel imagePreviewLabel;
    private JTextArea captionTextArea;
    private JButton uploadButton;
    private JButton saveButton;
    private boolean imageUploaded = false;
    private final UserController userController;

    public ImageUploadUI() {
        setTitle("Upload Image");
        userController = new UserController();
        initializeUI();
    }

    private void initializeUI() {
        JPanel headerPanel = new HeaderPanel("Upload Image");

        JPanel navigationPanel = new NavigationPanel(this);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Image preview
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT / 3));

        // Set an initial empty icon to the imagePreviewLabel
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);

        contentPanel.add(imagePreviewLabel);

        // Caption text area
        captionTextArea = new JTextArea("Enter a caption");
        captionTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        captionTextArea.setLineWrap(true);
        captionTextArea.setWrapStyleWord(true);
        JScrollPane captionScrollPane = new JScrollPane(captionTextArea);
        captionScrollPane.setPreferredSize(new Dimension(WIDTH - 50, HEIGHT / 6));
        contentPanel.add(captionScrollPane);

        // Upload button
        uploadButton = new JButton("Upload Image");
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(this::uploadAction);
        contentPanel.add(uploadButton);

        // Save button (for caption)
        saveButton = new JButton("Save Caption");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.addActionListener(this::saveCaptionAction);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private void uploadAction(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an image file");
        fileChooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg");
        fileChooser.addChoosableFileFilter(filter);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String username = userController.getLoggedInUsername(); // Read username from users.txt
                int imageId = getNextImageId(username);
                String fileExtension = getFileExtension(selectedFile);
                String newFileName = username + "_" + imageId + "." + fileExtension;

                Path destPath = Paths.get("resources/img", "uploaded", newFileName);
                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                // Save the caption and image ID to a text file
                saveImageInfo(username + "_" + imageId, username, captionTextArea.getText());

                // Load the image from the saved path
                ImageIcon imageIcon = new ImageIcon(destPath.toString());

                // Check if imagePreviewLabel has a valid size
                if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
                    Image image = imageIcon.getImage();

                    // Calculate the dimensions for the image preview
                    int previewWidth = imagePreviewLabel.getWidth();
                    int previewHeight = imagePreviewLabel.getHeight();
                    int imageWidth = image.getWidth(null);
                    int imageHeight = image.getHeight(null);
                    double widthRatio = (double) previewWidth / imageWidth;
                    double heightRatio = (double) previewHeight / imageHeight;
                    double scale = Math.min(widthRatio, heightRatio);
                    int scaledWidth = (int)(scale * imageWidth);
                    int scaledHeight = (int)(scale * imageHeight);

                    // Set the image icon with the scaled image
                    imageIcon.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
                }

                imagePreviewLabel.setIcon(imageIcon);

                // Update the flag to indicate that an image has been uploaded
                imageUploaded = true;

                // Change the text of the upload button
                uploadButton.setText("Upload Another Image");

                JOptionPane.showMessageDialog(this, "Image uploaded and preview updated!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int getNextImageId(String username) throws IOException {
        Path storageDir = Paths.get("resources/img", "uploaded"); // Ensure this is the directory where images are saved
        if (!Files.exists(storageDir)) {
            Files.createDirectories(storageDir);
        }

        int maxId = 0;
        try (DirectoryStream < Path > stream = Files.newDirectoryStream(storageDir, username + "_*")) {
            for (Path path: stream) {
                String fileName = path.getFileName().toString();
                int idEndIndex = fileName.lastIndexOf('.');
                if (idEndIndex != -1) {
                    String idStr = fileName.substring(username.length() + 1, idEndIndex);
                    try {
                        int id = Integer.parseInt(idStr);
                        if (id > maxId) {
                            maxId = id;
                        }
                    } catch (NumberFormatException ex) {
                        // Ignore filenames that do not have a valid numeric ID
                    }
                }
            }
        }
        return maxId + 1; // Return the next available ID
    }

    private void saveImageInfo(String imageId, String username, String caption) throws IOException {
        Path infoFilePath = Paths.get("resources/img", "image_details.txt");
        if (!Files.exists(infoFilePath)) {
            Files.createFile(infoFilePath);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (BufferedWriter writer = Files.newBufferedWriter(infoFilePath, StandardOpenOption.APPEND)) {
            writer.write(String.format("ImageID: %s, Username: %s, Caption: %s, Timestamp: %s, Likes: 0", imageId, username, caption, timestamp));
            writer.newLine();
        }

    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    private void saveCaptionAction(ActionEvent event) {
        // Here you would handle saving the caption text
        String captionText = captionTextArea.getText();
        // For example, save the caption text to a file or database
        JOptionPane.showMessageDialog(this, "Caption saved: " + captionText);
    }
}