package services;

import model.User;
import repository.UserRepository;

import java.io.IOException;

public class UserServices{

    private final UserRepository userRepository;
    private final User user;
    private final String username;

    public UserServices(String username) {
        this.userRepository = new UserRepository();
        this.username = username;
        user = new User(username);

        initializeUserData();
    }

    public void initializeUserData(){
        user.setBio(getBioData(username));
        user.setFollowersCount(getFollowerCount(username));
        user.setFollowingCount(getFollowingCount(username));
        user.setPostCount(getPostCount(username));

        System.out.println("Bio for " + user.getUsername() + ": " + user.getBio());
        System.out.println("Number of posts for this user: " + user.getPostsCount());
    }

    public boolean isFollowing(String currentUser, String loggedInUser)  {
        return userRepository.isAlreadyFollowing(currentUser, loggedInUser);
    }

    public void unFollowUser(String follower, String followed) {
        try{
            userRepository.removeFollowData(follower, followed);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void followUser(String follower, String followed) { userRepository.writeFollowData(follower, followed); }

    public String getLoggedInUsername() { return userRepository.readLoggedInUsername(); }

    public int getPostCount(String username) { return userRepository.readPostCount(username); }

    public int getFollowerCount(String username){ return userRepository.readFollowersData(username).size();}

    public int getFollowingCount(String username){ return userRepository.readFollowingData(username).size(); }

    public String getBioData(String username) {
        return userRepository.readBioData(username);
    }

}