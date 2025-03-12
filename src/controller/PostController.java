package controller;

import services.PostServices;

import java.util.ArrayList;

public class PostController {

    private final PostServices postServices;
    private final String imageId;

    public PostController(String imageId){
        this.postServices = new PostServices();
        this.imageId = imageId;
    }

    public void addCommentToPost(String comment) {
        postServices.addCommentToPost(imageId, comment);
        postServices.setNotification(imageId, "comment");
    }

    public ArrayList<String[]> getCommentsForPost() { return postServices.getCommentsForPost(imageId); }

    public int getLikesForPost() { return postServices.getLikesForPost(imageId); }

    public void likeAction() {
        if(postServices.isPostLiked(imageId)){
            postServices.removeLikeFromPost(imageId);
            postServices.setNotification(imageId, "like");
        } else {
            postServices.addLikeToPost(imageId);
        }
    }

    public String getImageOwner() { return postServices.getImageOwner(imageId); }

    public boolean isPostLiked() { return postServices.isPostLiked(imageId); }

    public String getImagePath() { return postServices.getImagePath(imageId); }

    public String getPostCaption() { return postServices.getPostCaption(imageId); }
}
