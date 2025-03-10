package controller;

import services.PostServices;

import java.util.ArrayList;

public class PostController {

    private final PostServices postServices;

    public PostController(){
        this.postServices = new PostServices();
    }

    public void addCommentToPost(String imageId, String comment) {
        postServices.addCommentToPost(imageId, comment);
    }

    public ArrayList<String[]> getCommentsForPost(String imageId) {
        return postServices.getCommentsForPost(imageId);
    }
}
