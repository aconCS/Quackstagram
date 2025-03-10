package controller;

import model.User;
import services.UserServices;

public class UserController {
    private final UserServices userServices;
    private final User user;

    public UserController(String username){
        user = new User(username);
        this.userServices = new UserServices(user);
    }

    public UserController(){
        this.userServices = null;
        this.user = null;
    }

    public String getBio(){ return user.getBio(); }

    public int getPostCount(){ return user.getPostsCount(); }

    public int getFollowerCount(){ return user.getFollowersCount(); }

    public int getFollowingCount(){ return user.getFollowingCount();}

    public boolean isFollowing(String currentUser, String loggedInUser){ return userServices.isFollowing(currentUser, loggedInUser); }

    public void unFollowUser(String follower, String followed){ userServices.unFollowUser(follower, followed); }

    public void followUser(String follower, String followed){ userServices.followUser(follower, followed); }

    public String getLoggedInUsername(){ return userServices.getLoggedInUsername(); }
}
