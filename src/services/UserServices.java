package services;

import model.User;
import repository.UserRepository;

import java.io.IOException;

public class UserServices{

    private final UserRepository userRepository;
    private final String username;
    private final User user;


    public UserServices(User user) {
        this.userRepository = new UserRepository();
        this.username = user.getUsername();
        this.user = user;

        initializeUserData();
    }

    public void initializeUserData(){
        user.setBio(setBioData(username));
        user.setFollowersCount(setFollowerCount(username));
        user.setFollowingCount(setFollowingCount(username));
        user.setPostCount(setPostCount(username));

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

    public int setPostCount(String username) { return userRepository.readPostCount(username); }

    public int setFollowerCount(String username){ return userRepository.readFollowersData(username).size();}

    public int setFollowingCount(String username){ return userRepository.readFollowingData(username).size(); }

    public String setBioData(String username) { return userRepository.readBioData(username); }
}