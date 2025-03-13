package repository;

import model.Post;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class UserRepository {

    private final String followersFilePath = "resources/data/following.txt";
    private final Path imageDetailsFilePath = Paths.get("resources/img", "image_details.txt");
    private final Path usersFilePath = Paths.get("resources/data", "users.txt");

    public UserRepository(){ }

    public int readPostCount(String username){
        int imageCount = 0;

        try (BufferedReader imageDetailsReader = Files.newBufferedReader(imageDetailsFilePath)) {
            String line;
            while ((line = imageDetailsReader.readLine()) != null) {
                if (line.contains("Username: " + username)) {
                    imageCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageCount;
    }

    // Method to read the username of the logged-in user
    public String readLoggedInUsername(){
        String loggedInUsername = "";
        // Read the current user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                loggedInUsername = parts[0];
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loggedInUsername;
    }

    // Method to follow a user
    public void writeFollowData(String follower, String followed) {
        System.out.println("Writing follow data");
        if (!isAlreadyFollowing(follower, followed)) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(followersFilePath, true))) {
                writer.write(follower + ":" + followed);
                writer.newLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void removeFollowData(String follower, String followed) throws IOException {
        System.out.println("Removing follow data");
        if (!isAlreadyFollowing(follower, followed)) {
            System.out.println("User is not following this user");
            return;
        }
        String followerLine = follower + ":" + followed;

        File file = new File(followersFilePath);
        File temp = new File("resources/data/temp.txt");
        PrintWriter out = new PrintWriter(new FileWriter(temp));
        Files.lines(file.toPath())
                .filter(line -> !line.contains(followerLine))
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

    // Method to check if a user is already following another user
    public boolean isAlreadyFollowing(String follower, String followed) {
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(follower + ":" + followed)) {
                    return true;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    // Method to get the list of followers for a user
    public List<String> readFollowersData(String username){
        List<String> followers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] parts = line.split(":");
                if (parts[1].equals(username)) {
                    followers.add(parts[0]);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return followers;
    }

    // Method to get the list of users a user is following
    public List<String> readFollowingData(String username){
        List<String> following = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(followersFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    following.add(parts[1]);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return following;
    }

    public String readBioData(String username){
        String bio = "";
        Path bioDetailsFilePath = Paths.get("resources/data", "credentials.txt");
        try (BufferedReader bioDetailsReader = Files.newBufferedReader(bioDetailsFilePath)) {
            String line;
            while ((line = bioDetailsReader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username) && parts.length >= 3) {
                    bio = parts[2];
                    break; // Exit the loop once the matching bio is found
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return bio;
    }

    public void changeBioData(String newBio) throws IOException {
        String detailsPath = "resources/data/credentials.txt";

        File file = new File(detailsPath);
        File temp = new File("resources/data/temp.txt");
        String loggedInUsername = readLoggedInUsername();
        PrintWriter out = new PrintWriter(new FileWriter(temp));

        if (!file.exists()) {
            return;
        }

        Files.lines(file.toPath())
                .forEach(line -> {
                    String[] parts = line.split(":");
                    if (parts[0].equals(loggedInUsername)) {
                        parts[2] = newBio;
                        out.println(String.join(":", parts));
                    } else {
                        out.println(line);
                    }
                });
        out.flush();
        out.close();

        if (!file.delete()) {
            System.out.println("Could not delete file");
            return;
        }
        if (!temp.renameTo(file)) {
            System.out.println("Could not rename file");
        }
    }

    public List<Post> readUserPosts(String currUsername){
        List<Post> posts = new ArrayList<>();

        Path detailsPath = Paths.get("resources/img", "image_details.txt");

        try (Stream<String> lines = Files.lines(detailsPath)) {
            // Iterate through the lines of the file one by one
            lines.forEach(line -> {
                if (line.contains("ImageID: ")) {
                    // Parse the details line into components
                    String[] parts = line.split(", ");
                    String username = parts[1].split(": ")[1];

                    // If the username matches the current user, parse the post details
                    if (username.equals(currUsername)) {
                        String imageId = parts[0].split(": ")[1];
                        String caption = parts[2].split(": ")[1];

                        int likes = 0;
                        try {
                            likes = Integer.parseInt(parts[4].split(": ")[1]);
                        } catch (NumberFormatException e) {
                            System.err.println("Failed to parse likes for image ID: " + imageId);
                        }

                        String imgPath = "resources/img/uploaded/" + imageId + ".png";

                        posts.add(new Post(imgPath));
                    }
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace(); // Handle the exception properly
        }


        return posts;
    }

    public ArrayList<String> loadAllUsers() throws IOException {
        ArrayList<String> users = new ArrayList<>();
        String userPath = "resources/data/credentials.txt";

        File file = new File(userPath);
        if (!file.exists()) {
            System.out.println("Credentials file does not exist");
            return users;
        }

        Files.lines(file.toPath())
                .forEach(line -> {
                    String[] parts = line.split(":");
                    users.add(parts[0]);
                });
        return users;
    }
}