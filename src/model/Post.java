package model;

import java.util.List;
import java.util.ArrayList;

// Represents a post on Quackstagram
public class Post {
    private final String imagePath;
    private final String caption;
    private int likesCount;
    private final List<String> comments;

    public Post(String imagePath, String caption) {
        this.imagePath = imagePath;
        this.caption = caption;
        this.likesCount = 0;
        this.comments = new ArrayList<>();
    }

    public Post(String imagePath, String caption, int likesCount, List<String> comments) {
        this.imagePath = imagePath;
        this.caption = caption;
        this.likesCount = likesCount;
        this.comments = comments;
    }

    // Add a comment to the picture
    public void addComment(String comment) {
        comments.add(comment);
    }

    // Increment likes count
    public void like() {
        likesCount++;
    }

    // Getter methods for picture details
    public String getImagePath() { return imagePath; }
    public String getCaption() { return caption; }
    public int getLikesCount() { return likesCount; }
    public List<String> getComments() { return comments; }
}
