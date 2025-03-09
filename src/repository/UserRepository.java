package repository;

import model.Post;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final String followersFilePath = "resources/data/following.txt";
    private Path usersFilePath = Paths.get("resources/data", "users.txt");

    public UserRepository(){ }

    public List<Post> loadPostData(String username){
        List<Post> posts = new ArrayList<>();
        Path imageDetailsFilePath = Paths.get("resources/img", "image_details.txt");
        try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
            String line;
            while ((line = imageDetailsReader.readLine()) != null) {
                if (line.contains("Username: " + username)) {
                    //imageCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    // Method to read the username of the logged in user
    public String readLoggedInUsername(){
        String loggedInUsername = "";
        // Read the current user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                loggedInUsername = parts[0];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loggedInUsername;
    }

    // TODO : CHANGE LOGIC
    public List<Post> readPostData(String username){
        List<Post> posts = new ArrayList<>();
        Path imageDetailsFilePath = Paths.get("resources/img", "image_details.txt");
        try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
            String line;
            while ((line = imageDetailsReader.readLine()) != null) {
                if (line.contains("Username: " + username)) {
                    //imageCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return posts;
    }

    // Method to follow a user
    public void writeFollowData(String follower, String followed) {
        if (!isAlreadyFollowing(follower, followed)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(followersFilePath, true))) {
                writer.write(follower + ":" + followed);
                writer.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Method to check if a user is already following another user
    private boolean isAlreadyFollowing(String follower, String followed) {
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(follower + ":" + followed)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Method to get the list of followers for a user
    public List<String> readFollowersData(String username){
        List<String> followers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[1].equals(username)) {
                    followers.add(parts[0]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return followers;
    }

    // Method to get the list of users a user is following
    public List<String> readFollowingData(String username){
        List<String> following = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    following.add(parts[1]);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return following;
    }

    public String readBioData(String username){
        String bio = "";
        Path bioDetailsFilePath = Paths.get("resources/data", "credentials.txt");
        try (BufferedReader bioDetailsReader = Files.newBufferedReader(bioDetailsFilePath)) {
            String line;
            while ((line = bioDetailsReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username) && parts.length >= 3) {
                    bio = parts[2];
                    break; // Exit the loop once the matching bio is found
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bio;
    }
}