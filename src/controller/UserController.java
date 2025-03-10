package controller;

import model.Post;
import model.User;
import services.UserServices;

import java.nio.file.Path;
import java.util.List;

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

    public List<Path> getPostPaths(){ return userServices.getPostPaths(); }

    public List<Post> getPosts(){ return user.getPosts(); }

    public String getBio(){ return user.getBio(); }

    public int getPostCount(){ return user.getPostsCount(); }

    public int getFollowerCount(){ return user.getFollowersCount(); }

    public int getFollowingCount(){ return user.getFollowingCount();}

    public boolean isFollowing(String currentUser, String loggedInUser){ return userServices.isFollowing(currentUser, loggedInUser); }

    public void unFollowUser(String follower, String followed){
        userServices.unFollowUser(follower, followed);
        user.setFollowersCount(userServices.loadFollowerCount(followed));
    }

    public void followUser(String follower, String followed){
        userServices.followUser(follower, followed);
        user.setFollowersCount(userServices.loadFollowerCount(followed));
    }

    public String getLoggedInUsername(){ return userServices.getLoggedInUsername(); }
}
