package services;

import model.User;
import repository.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserServices{

    private UserRepository userRepository;
    private User user;
    private String username;

    public UserServices(String username) {
        this.userRepository = new UserRepository();
        this.username = username;
        user = new User(username);

        initializeUserData();
    }

    public void initializeUserData(){
        // Initialize counts
        int imageCount = 0;

        // TODO EXTRACT LOGIC TO loadPostData in UserServices
        // TODO EXTRACT DATA HANDLING TO readPostData in UserRepository
        // Step 1: Read image_details.txt to count the number of images posted by the user
        Path imageDetailsFilePath = Paths.get("resources/img", "image_details.txt");
        try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
            String line;
            while ((line = imageDetailsReader.readLine()) != null) {
                if (line.contains("Username: " + user.getUsername())) {
                    imageCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the values in the User object
        String currentUsername = user.getUsername();
        user.setBio(getBioData(currentUsername));
        user.setFollowersCount(getFollowerCount(currentUsername));
        user.setFollowingCount(getFollowingCount(currentUsername));
        user.setPostCount(imageCount);

        System.out.println("Bio for " + user.getUsername() + ": " + user.getBio());
        System.out.println("Number of posts for this user: " + user.getPostsCount());
    }

    public void followUser(String follower, String followed) { userRepository.writeFollowData(follower, followed); }

    public String getLoggedInUsername() { return userRepository.readLoggedInUsername(); }

    public int getPostCount(String username) { return userRepository.readPostData(username).size(); }

    public int getFollowerCount(String username){ return userRepository.readFollowersData(username).size();}

    public int getFollowingCount(String username){ return userRepository.readFollowingData(username).size(); }

    public String getBioData(String username) {
        return userRepository.readBioData(username);
    }

}