package view.authenticationUI;

import model.User;
import view.InstagramProfileUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class SignInUI extends JFrame {

    private final JTextField usernameField;
    private final JTextField passwordField;
    private User newUser;
    

    public SignInUI() {
        // Create fields to pass to AuthUIBuilder
        usernameField = new JTextField();
        passwordField = new JPasswordField();

         new AuthUIBuilder(this,"Quackstagram - Sign in")
                .addTextFieldPanel("Username", usernameField)
                .addTextFieldPanel("Password", passwordField)
                .addButton("Sign in", Color.RED, this::onSignInClicked)
                .addButton("Create account", Color.BLUE, this::onRegisterNowClicked)
                .buildUI();
    }

    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();
        System.out.println(enteredUsername+" <-> "+enteredPassword);
        if (verifyCredentials(enteredUsername, enteredPassword)) {
            System.out.println("It worked");
             // Close the SignUpUI frame
            dispose();

            // Open the SignInUI frame
            SwingUtilities.invokeLater(() -> {
                InstagramProfileUI profileUI = new InstagramProfileUI(newUser);
                profileUI.setVisible(true);
            });
        } else {
            System.out.println("It Didn't");
        }
    }

    private void onRegisterNowClicked(ActionEvent event) {
        // Close the SignInUI frame
        dispose();

        // Open the SignUpUI frame
        SwingUtilities.invokeLater(() -> {
            SignUpUI signUpFrame = new SignUpUI();
            signUpFrame.setVisible(true);
        });
    }

    private boolean verifyCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/data/credentials.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    String bio = credentials[2];
                    // Create User object and save information
                    newUser = new User(username, bio, password); // Assuming User constructor takes these parameters
                    saveUserInformation(newUser);

                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveUserInformation(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources/data/users.txt", false))) {
            writer.write(user.toString());  // Implement a suitable toString method in User class
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
