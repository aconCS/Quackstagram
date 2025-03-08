package view.authenticationUI;

import controller.AuthController;
import controller.NavigationController;
import view.InstagramProfileUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignInUI extends JFrame {

    private final AuthController authController;
    private final JTextField usernameField;
    private final JTextField passwordField;

    public SignInUI() {
        authController = new AuthController();
        // Create fields to pass to AuthUIBuilder
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        new AuthUIBuilder(this, "Quackstagram - Sign in")
                .addTextFieldPanel("Username", usernameField)
                .addTextFieldPanel("Password", passwordField)
                .addButton("Sign in", Color.RED, this::onSignInClicked)
                .addButton("Create account", Color.BLUE, this::onRegisterNowClicked)
                .buildUI();
    }

    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        System.out.println(enteredUsername + " <-> " + enteredPassword);
        if (authController.verifyCredentials(enteredUsername, enteredPassword)) {
            System.out.println("Valid Credentials");
            // TODO TAKE USER ACCESS AWAY FROM signInUI MODEL
            //NavigationController.getInstance().navigate(this, new InstagramProfileUI());
        } else {
            System.out.println("Invalid Credentials");
        }
    }

    private void onRegisterNowClicked(ActionEvent event) {
        NavigationController.getInstance().navigate(this, new SignUpUI());
    }
}