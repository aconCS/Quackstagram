package services;

import controller.UserController;
import repository.PostRepository;

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
}
