package services;

import controller.UserController;
import repository.PostRepository;

import java.io.IOException;
import java.util.ArrayList;

public class PostServices {

    private final PostRepository postRepository;

    public PostServices() {
        this.postRepository = new PostRepository();
    }

    public void addCommentToPost(String imageId, String comment) {
        postRepository.writeCommentToPost(imageId, comment);
    }

    public ArrayList<String[]> getCommentsForPost(String imageId) {
        return postRepository.loadCommentsForPost(imageId);
    }

    public void  addLikeToPost(String imageId) {
        postRepository.writeLikeToPost(imageId);
    }

    public int getLikesForPost(String imageId) {
        return postRepository.loadLikesForPost(imageId);
    }

    public void removeLikeFromPost(String imageId) {
        try{
            postRepository.deleteLikeFromPost(imageId);
        }catch(IOException ex){
            ex.printStackTrace();
        }

    }

    public boolean isPostLiked(String imageId) {
        UserController userController = new UserController();
        return postRepository.isAlreadyLiked(imageId, userController.getLoggedInUsername());
    }

    public void setNotification(String imageId){
        postRepository.writeNotification(imageId);
    }

}
