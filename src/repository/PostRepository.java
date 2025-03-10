package repository;

import controller.UserController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
        return comments;
    }

    public void writeLikeToPost(String imageId) {
        String username = userController.getLoggedInUsername();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Load images from the uploaded folder*
        File likeDir = new File("resources/data/likes");
        File[] likeFiles = likeDir.listFiles((dir, name) -> name.matches(imageId + "_.*\\.txt"));
        if (likeFiles != null && likeFiles.length > 0) {
            File likeFile = likeFiles[0]; // Assuming there's only one file per imageId
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(likeFile, true))) {
                writer.write(username + ": " + timestamp);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int loadLikesForPost(String imageId) {
        int likeCount = 0;

        File commentDir = new File("resources/data/likes");
        if (commentDir.exists() && commentDir.isDirectory()) {
            File[] commentFiles = commentDir.listFiles((dir, name) -> name.matches(imageId + "_.*\\.txt"));
            if (commentFiles != null && commentFiles.length > 0) {
                File likesFile = commentFiles[0]; // Assuming there's only one file per imageId
                try (BufferedReader reader = new BufferedReader(new FileReader(likesFile))) {
                    while ((reader.readLine()) != null) {
                        likeCount++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return likeCount;
    }

    public boolean isAlreadyLiked(String imageId, String username) {
        String likedImagePath = "resources/data/likes/" + imageId + "_likes.txt";
        String likesPath = "resources/data/likes/" + imageId + "_likes.txt";

        // If file doesnt exist create it
        if (new File(likesPath).exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(likedImagePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(username)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                new File(likesPath).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void deleteLikeFromPost(String imageId) throws IOException {
        String likesPath = "resources/data/likes/" + imageId + "_likes.txt";

        File file = new File(likesPath);
        File temp = new File("resources/data/temp.txt");
        PrintWriter out = new PrintWriter(new FileWriter(temp));
        Files.lines(file.toPath())
                .filter(line -> !line.contains(userController.getLoggedInUsername()))
                .forEach(out::println);
        out.flush();
        out.close();
        if(!file.delete()){
            System.out.println("Could not delete file");
            return;
        }
        if(!temp.renameTo(file)){
            System.out.println("Could not rename file");
        }
    }

    public void writeNotification(String imageId){
        // Record the like in notifications.txt
        String currentUser = userController.getLoggedInUsername();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String imageOwner = imageId.split("_")[0];

        String notification = String.format("%s; %s; %s; %s\n", imageOwner, currentUser, imageId, timestamp);
        try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("resources/data", "notifications.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            notificationWriter.write(notification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
