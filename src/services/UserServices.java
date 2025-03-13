package services;

import model.Post;
import model.User;
import repository.UserRepository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserServices{

    private final UserRepository userRepository;
    private final String username;
    private final User user;


    public UserServices(User user) {
        this.userRepository = new UserRepository();
        this.username = user.getUsername();
        this.user = user;
    }

    public void initializeUserData(){
        setBio(loadBioData(username));
        user.setFollowersCount(loadFollowerCount(username));
        user.setFollowingCount(loadFollowingCount(username));
        user.setPostCount(loadPostCount(username));
        user.setPosts(loadUserPosts(username));

        System.out.println(user.getUsername() + "'s bio: " + user.getBio());
        System.out.println("Post count: " + user.getPostsCount());
    }

    public void setBio(String bio){
        user.setBio(bio);
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

    public List<Path> getPostPaths() {
        List<Path> postPaths = new ArrayList<>();

        for(Post post : user.getPosts()){
            postPaths.add(Paths.get(post.imagePath()));
        }

        return postPaths;
    }

    public ArrayList<String> getAllUsers() {
        try {
            return userRepository.loadAllUsers();

        } catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void changeBioData(String bio) throws IOException { userRepository.changeBioData(bio); }

    public void followUser(String follower, String followed) { userRepository.writeFollowData(follower, followed); }

    public String getLoggedInUsername() { return userRepository.readLoggedInUsername(); }

    public int loadPostCount(String username) { return userRepository.readPostCount(username); }

    public int loadFollowerCount(String username){ return userRepository.readFollowersData(username).size();}

    public int loadFollowingCount(String username){ return userRepository.readFollowingData(username).size(); }

    public String loadBioData(String username) { return userRepository.readBioData(username); }

    public List<Post> loadUserPosts(String username) { return userRepository.readUserPosts(username); }
}