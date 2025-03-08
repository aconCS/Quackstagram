package view.authenticationUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SignUpUI extends JFrame {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 500;

    private final JTextField usernameField;
    private final JTextField passwordField;
    private final JTextField bioField;
    private final String credentialsFilePath = "resources/data/credentials.txt";
    private final String profilePhotoStoragePath = "resources/img/storage/profile/";


    public SignUpUI() {
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        bioField = new JTextField();

        new AuthUIBuilder(this, "Quackstagram - Register")
                .addTextFieldPanel("Username", usernameField)
                .addTextFieldPanel("Password", passwordField)
                .addTextFieldPanel("Bio", bioField)
                .addButton("Upload Profile Picture", Color.RED, this::onPictureUploadClicked)
                .addButton("Register", Color.BLUE, this::onRegisterClicked)
                .addButton("Login instead", Color.WHITE, this::switchButtonClicked)
                .buildUI();
    }

    private void onRegisterClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String bio = bioField.getText();

        if (doesUsernameExist(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            saveCredentials(username, password, bio);
            handleProfilePictureUpload();
            dispose();
    
        // Open the SignInUI frame
        SwingUtilities.invokeLater(() -> {
            SignInUI signInFrame = new SignInUI();
            signInFrame.setVisible(true);
        });
        }
    }
    
    private boolean doesUsernameExist(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void onPictureUploadClicked(ActionEvent event) {
        handleProfilePictureUpload();
    }

     // Method to handle profile picture upload
     private void handleProfilePictureUpload() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveProfilePicture(selectedFile, usernameField.getText());
        }
    }

    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveCredentials(String username, String password, String bio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/data/credentials.txt", true))) {
            writer.write(username + ":" + password + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        
    private void switchButtonClicked(ActionEvent event) {
        // Close the SignUpUI frame
        dispose();

        // Open the SignInUI frame
        SwingUtilities.invokeLater(() -> {
            SignInUI signInFrame = new SignInUI();
            signInFrame.setVisible(true);
        });
    }

}
