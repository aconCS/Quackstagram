package repository;

import controller.UserController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PostRepository {

    private final UserController userController;

    public PostRepository() {
        userController = new UserController();
    }

    public void writeCommentToPost(String imageId, String comment) {
        Path commentsPath = Paths.get("resources/data/", "comments/");
        String username = userController.getLoggedInUsername();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Load images from the uploaded folder
        File commentDir = new File("resources/data/comments");
        if (commentDir.exists() && commentDir.isDirectory()) {
            File[] commentFiles = commentDir.listFiles((dir, name) -> name.matches(imageId + "_.*\\.txt"));
            if (commentFiles != null && commentFiles.length > 0) {
                File commentFile = commentFiles[0]; // Assuming there's only one file per imageId
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(commentFile, true))) {
                    writer.write(username + ": " + comment + ": " + timestamp);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // If no comment file exists for the imageId, create a new one
                File newCommentFile = new File(commentsPath.toString(), imageId + "_comments.txt");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(newCommentFile))) {
                    writer.write(username + ": " + comment);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<String[]> loadCommentsForPost(String imageId) {
        ArrayList<String[]> comments = new ArrayList<>();

        File commentDir = new File("resources/data/comments");
        if (commentDir.exists() && commentDir.isDirectory()) {
            File[] commentFiles = commentDir.listFiles((dir, name) -> name.matches(imageId + "_.*\\.txt"));
            if (commentFiles != null && commentFiles.length > 0) {
                File commentFile = commentFiles[0]; // Assuming there's only one file per imageId
                try (BufferedReader reader = new BufferedReader(new FileReader(commentFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(": ", 3);
                        if (parts.length == 3) {
                            String[] comment = new String[]{parts[0], parts[1], parts[2]};
                            comments.add(comment);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(comments);
        return comments;
    }
}
