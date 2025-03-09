package services;

import model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class AuthServices {

    private static final String credentialsFilePath = "resources/data/credentials.txt";
    private static final String profilePhotoStoragePath = "resources/img/storage/profile/";

    // VERIFY CREDENTIALS
    public boolean verifyCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    String bio = credentials[2];
                    User newUser = new User(username, bio, password); // Assuming User constructor takes these parameters
                    saveUserInformation(newUser);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // TODO RENAME TO SAVE LOGGED IN USER
    private void saveUserInformation(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/data/users.txt", false))) {
            writer.write(user.toString());  // Implement a suitable toString method in User class
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean doesUsernameExist(String username) {
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

    public void saveCredentials(String username, String password, String bio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(credentialsFilePath, true))) {
            writer.write(username + ":" + password + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(profilePhotoStoragePath + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadProfilePicture(String username) {
        FileServices fileServices = new FileServices();
        File chosenFile = fileServices.openFileChooser("Upload Profile Picture", "jpg", "jpeg", "png");
        if(chosenFile != null) {
            saveProfilePicture(chosenFile, username);
        }
    }
}