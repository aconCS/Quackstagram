package view.coreUI;

import controller.UserController;
import view.components.HeaderPanel;
import view.components.NavigationPanel;
import view.components.UIBase;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ImageUploadUI extends UIBase {

    private final int width = this.getWidth();
    private final int height = this.getHeight();

    private JLabel imagePreviewLabel;
    private JTextArea captionTextArea;
    private JButton uploadButton;
    private JButton saveButton;
    private final UserController userController;

    /**
     * Constructor for ImageUploadUI.
     * <p>
     * This constructor initializes the ImageUploadUI components, including the header panel,
     * navigation panel, image preview label, caption text area, upload button, and save button.
     */
    public ImageUploadUI() {
        setTitle("Upload Image");
        userController = new UserController();
        buildUI();
    }

    /**
     * Initializes the user interface components for the ImageUploadUI.
     * <p>
     * This method sets up the header panel, navigation panel, image preview label,
     * caption text area, upload button, and save button.
     */
    private void buildUI() {
        JPanel headerPanel = new HeaderPanel("Upload Image");

        JPanel navigationPanel = new NavigationPanel(this);

        // Main content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Image preview
        imagePreviewLabel = new JLabel();
        imagePreviewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imagePreviewLabel.setPreferredSize(new Dimension(width, height / 3));

        // Sets an initial empty icon to the imagePreviewLabel
        ImageIcon emptyImageIcon = new ImageIcon();
        imagePreviewLabel.setIcon(emptyImageIcon);

        contentPanel.add(imagePreviewLabel);

        // Caption-text area
        captionTextArea = new JTextArea("Enter a caption");
        captionTextArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        captionTextArea.setLineWrap(true);
        captionTextArea.setWrapStyleWord(true);
        JScrollPane captionScrollPane = new JScrollPane(captionTextArea);
        captionScrollPane.setPreferredSize(new Dimension(width - 50, height / 6));
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

        // Adds panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    /**
     * Handles the action of uploading an image.
     * <p>
     * This method opens a file chooser dialog to select an image file, saves the selected image
     * to the specified directory, updates the image preview label, and changes the text of the upload button.
     *
     * @param event The action event triggered by the upload button.
     */
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
                String username = userController.getLoggedInUsername();
                int imageId = getNextImageId(username);
                String fileExtension = getFileExtension(selectedFile);
                String newFileName = username + "_" + imageId + "." + fileExtension;

                Path destPath = Paths.get("resources/img", "uploaded", newFileName);
                Files.copy(selectedFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

                // Saves the caption and image ID to a text file by calling saveImageInfo method
                saveImageInfo(username + "_" + imageId, username, captionTextArea.getText());

                // Loads the image from the saved path
                ImageIcon imageIcon = new ImageIcon(destPath.toString());

                // Checks if imagePreviewLabel has a valid size
                if (imagePreviewLabel.getWidth() > 0 && imagePreviewLabel.getHeight() > 0) {
                    Image image = imageIcon.getImage();

                    // Calculates the dimensions for the image preview
                    int previewWidth = imagePreviewLabel.getWidth();
                    int previewHeight = imagePreviewLabel.getHeight();
                    int imageWidth = image.getWidth(null);
                    int imageHeight = image.getHeight(null);
                    double widthRatio = (double) previewWidth / imageWidth;
                    double heightRatio = (double) previewHeight / imageHeight;
                    double scale = Math.min(widthRatio, heightRatio);
                    int scaledWidth = (int)(scale * imageWidth);
                    int scaledHeight = (int)(scale * imageHeight);

                    // Sets the image icon with the scaled image
                    imageIcon.setImage(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH));
                }

                imagePreviewLabel.setIcon(imageIcon);

                // Changes the text of the upload button once an image
                // has been uploaded
                uploadButton.setText("Upload Another Image");

                JOptionPane.showMessageDialog(this, "Image uploaded and preview updated!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Retrieves the next available image ID for the given user.
     * <p>
     * This method scans the directory for existing images uploaded by the user and returns
     * the next available ID.
     *
     * @param username The username of the user.
     *
     * @return The next available image ID.
     *
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * Saves the image information to a text file.
     * <p>
     * This method appends the image ID, username, caption, timestamp, and initial likes
     * and comments to the image details file.
     *
     * @param imageId The ID of the image.
     * @param username The username of the user.
     * @param caption The caption for the image.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void saveImageInfo(String imageId, String username, String caption) throws IOException {
        Path infoFilePath = Paths.get("resources/img", "image_details.txt");
        if (!Files.exists(infoFilePath)) {
            Files.createFile(infoFilePath);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try (BufferedWriter writer = Files.newBufferedWriter(infoFilePath, StandardOpenOption.APPEND)) {
            writer.write(String.format("ImageID: %s, Username: %s, Caption: %s, Timestamp: %s, Likes: 0, Comments: ", imageId, username, caption, timestamp));
            writer.newLine();
        }

    }

    /**
     * Retrieves the file extension of the given file.
     * <p>
     * This method returns the file extension of the provided file.
     *
     * @param file The file whose extension is to be retrieved.
     *
     * @return The file extension.
     */
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf + 1);
    }

    /**
     * Handles the action of saving the caption.
     * <p>
     * This method retrieves the caption text from the caption text area and displays a message dialog.
     *
     * @param event The action event triggered by the save button.
     */
    private void saveCaptionAction(ActionEvent event) {
        String captionText = captionTextArea.getText();
        JOptionPane.showMessageDialog(this, "Caption saved: " + captionText);
    }
}