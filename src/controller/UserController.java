package controller;

import model.User;
import services.UserServices;

import java.io.IOException;
import java.util.ArrayList;

public class UserController {
    private final UserServices userServices;
    private final User user;

    public UserController(String username){
        user = new User(username);
        this.userServices = new UserServices(user);
        userServices.initializeUserData();
    }

    public UserController(){
        this.user = new User("");
        this.userServices = new UserServices(user);
    }

    public String getBio(){ return user.getBio(); }

    public int getPostCount(){ return user.getPostsCount(); }

    public int getFollowerCount(){ return user.getFollowersCount(); }

    public int getFollowingCount(){ return user.getFollowingCount();}

    public boolean isFollowing(String currentUser, String loggedInUser){ return userServices.isFollowing(currentUser, loggedInUser); }

    public void unfollowUser(String follower, String followed){
        userServices.unFollowUser(follower, followed);
        user.setFollowersCount(userServices.loadFollowerCount(followed));
    }

    public void followUser(String follower, String followed){
        userServices.followUser(follower, followed);
        user.setFollowersCount(userServices.loadFollowerCount(followed));
    }

    public void changeProfilePicture(){
        AuthController authController = new AuthController();
        if(authController.uploadProfilePicture(user.getUsername())){
            System.out.println("Failed to upload profile Picture");
        }
    }

    public void editBio(String bio){
        try{
            if (bio.isEmpty()){
                System.out.println("Empty bio");
            }else{
                userServices.changeBioData(bio);
                userServices.setBio(bio);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public ArrayList<String> getAllUsers(){
        return userServices.getAllUsers();
    }

    public String getLoggedInUsername(){ return userServices.getLoggedInUsername(); }
}
