package model;

import java.util.List;
import java.util.ArrayList;

// Represents a user on Quackstagram
public class User {
    //private UserServices userServices;
    private String username;
    private String bio;
    private String password;
    private int postsCount;
    private int followersCount;
    private int followingCount;
    private List<Post> posts;

    public User(String username, String bio, String password) {
        this.username = username;
        this.bio = bio;
        this.password = password;
        this.posts = new ArrayList<>();

        // Initialize counts to 0
        this.postsCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
    }

    public User(String username){
        this.username = username;
    }

    // Getter methods
    public String getUsername() { return username; }
    public String getBio() { return bio; }
    public int getPostsCount() { return postsCount; }
    public int getFollowersCount() { return followersCount; }
    public int getFollowingCount() { return followingCount; }
    public List<Post> getPosts() { return posts; }

    // Setter methods
    public void setBio(String bio) {this.bio = bio; }
    public void setFollowersCount(int followersCount) { this.followersCount = followersCount; }
    public void setFollowingCount(int followingCount) { this.followingCount = followingCount; }
    public void setPostCount(int postCount) { this.postsCount = postCount;}
    public void setPosts(List<Post> posts) { this.posts = posts; }

    // Implement the toString method for saving user information
    @Override
    public String toString() {
    return username + ":" + bio + ":" + password; // Format as needed
}
}