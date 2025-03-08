package view.authenticationUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SignUpUI extends JFrame {

    private final JTextField USERNAME_FIELD;
    private final JTextField PASSWORD_FIELD;
    private final JTextField BIO_FIELD;
    private final String CREDENTIALS_FILE_PATH = "resources/data/credentials.txt";
    private final String PROFILE_PHOTO_STORAGE_PATH = "resources/img/storage/profile/";


    public SignUpUI() {
        USERNAME_FIELD = new JTextField();
        PASSWORD_FIELD = new JPasswordField();
        BIO_FIELD = new JTextField();

        new AuthUIBuilder(this, "Quackstagram - Register")
                .addTextFieldPanel("Username", USERNAME_FIELD)
                .addTextFieldPanel("Password", PASSWORD_FIELD)
                .addTextFieldPanel("Bio", BIO_FIELD)
                .addButton("Upload Profile Picture", Color.RED, this::onPictureUploadClicked)
                .addButton("Register", Color.BLUE, this::onRegisterClicked)
                .addButton("Login instead", Color.WHITE, this::switchButtonClicked)
                .buildUI();
    }

    private void onRegisterClicked(ActionEvent event) {
        String username = USERNAME_FIELD.getText();
        String password = PASSWORD_FIELD.getText();
        String bio = BIO_FIELD.getText();

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
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE_PATH))) {
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
            saveProfilePicture(selectedFile, USERNAME_FIELD.getText());
        }
    }

    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(PROFILE_PHOTO_STORAGE_PATH + username + ".png");
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
