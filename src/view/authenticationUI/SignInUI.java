package view.authenticationUI;

import controller.AuthController;
import controller.NavigationController;
import model.User;
import view.Components.UIBase;
import view.coreUI.ProfileUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SignInUI extends UIBase {

    private final AuthController authController;
    private final JTextField usernameField;
    private final JTextField passwordField;

    public SignInUI() {
        setTitle("Quackstagram - Sign in");
        authController = new AuthController();

        // Create fields to pass to AuthUIBuilder
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        new AuthUIBuilder(this)
                .addTextFieldPanel("Username", usernameField)
                .addTextFieldPanel("Password", passwordField)
                .addButton("Sign in", this::onSignInClicked)
                .addButton("Create account", this::onRegisterNowClicked)
                .buildUI();
    }

    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        System.out.println(enteredUsername + " <-> " + enteredPassword);
        User currentUser = new User(enteredUsername, "bio", enteredPassword);
        if (authController.verifyCredentials(enteredUsername, enteredPassword)) {
            System.out.println("Valid Credentials");
            // TODO TAKE USER ACCESS AWAY FROM signInUI MODEL
            NavigationController.getInstance().navigate(this, new ProfileUI(currentUser));
        } else {
            System.out.println("Invalid Credentials");
        }
    }

    private void onRegisterNowClicked(ActionEvent event) {
        NavigationController.getInstance().navigate(this, new SignUpUI());
    }
}