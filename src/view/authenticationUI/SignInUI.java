package view.authenticationUI;

import model.User;
import view.coreUI.UserProfileUI;
import controller.AuthController;
import controller.NavigationController;
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

    private final JTextField USERNAME_FIELD;
    private final JTextField PASSWORD_FIELD;
    private User newUser;
    
    private final AuthController authController;

    public SignInUI() {
        authController = new AuthController();
        // Create fields to pass to AuthUIBuilder
        USERNAME_FIELD = new JTextField();
        PASSWORD_FIELD = new JPasswordField();

         new AuthUIBuilder(this,"Quackstagram - Sign in")
                .addTextFieldPanel("Username", USERNAME_FIELD)
                .addTextFieldPanel("Password", PASSWORD_FIELD)
                .addButton("Sign in", Color.RED, this::onSignInClicked)
                .addButton("Create account", Color.BLUE, this::onRegisterNowClicked)
                .buildUI();
    }

    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = USERNAME_FIELD.getText();
        String enteredPassword = PASSWORD_FIELD.getText();

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