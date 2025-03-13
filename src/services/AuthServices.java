package services;

import model.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class AuthServices {

    private static final String CREDENTIALS_FILE_PATH = "resources/data/credentials.txt";
    private static final String PROFILE_PHOTO_STORAGE_PATH = "resources/img/storage/profile/";

    /*
    * Reads the credentials file and verifies the entered username and password.
    * Creates a new User object with the entered username and bio upon successful verification.
    * */
    public boolean verifyCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    String bio = credentials[2];
                    User newUser = new User(username, bio, password); // Assuming User constructor takes these parameters
                    saveLoggedInUser(newUser);
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    * Reads the credentials file and checks if the entered username already exists.
    * */
    public boolean doesUsernameExist(String username) {
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

    /*
    * Writes the entered username, password, and bio to the credentials file.
    * */
    public void saveCredentials(String username, String password, String bio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE_PATH, true))) {
            writer.write(username + ":" + password + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Saves the profile picture of the user with the entered username once a non-null file is chosen.
    * */
    public boolean uploadProfilePicture(String username) {
        FileServices fileServices = new FileServices();
        File chosenFile = fileServices.openFileChooser("Upload Profile Picture", "jpg", "jpeg", "png");
        if(chosenFile != null) {
            saveProfilePicture(chosenFile, username);
            return true;
        }
        return false;
    }

    /*
     * Writes image to a file with the username as the filename.
     * */
    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(PROFILE_PHOTO_STORAGE_PATH + username + ".png");
            System.out.println(outputFile.getAbsolutePath());
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLoggedInUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/data/users.txt", false))) {
            writer.write(user.toString());  // Implement a suitable toString method in User class
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}