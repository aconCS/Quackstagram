package view.authenticationUI;

import controller.AuthController;
import controller.NavigationController;
import view.components.UIBase;
import view.coreUI.HomeUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SignInUI extends UIBase {

    private final AuthController authController;
    private final JTextField usernameField;
    private final JTextField passwordField;

    public SignInUI() {
        setTitle("Quackstagram - Sign in");
        authController = new AuthController();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create fields to pass to AuthUIBuilder
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        new AuthUIBuilder(this)
                .addHeaderPanel("Sign in")
                .addTextFieldPanel("Username", usernameField)
                .addTextFieldPanel("Password", passwordField)
                .addButton("Sign in", this::onSignInClicked)
                .addButton("Create account", this::onRegisterNowClicked)
                .buildUI();
    }

    /*
    * Event handler that triggers when the sign-in button is clicked.
    * Creates a new User object with the entered username and password upon successful verification.
    * Navigates to the ProfileUI with the new User object.
    * */
    private void onSignInClicked(ActionEvent event) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        if(enteredUsername == null || enteredPassword == null){
            JOptionPane.showMessageDialog(this, "Please enter a valid username/password");
            return;
        }

        System.out.println(enteredUsername + " <-> " + enteredPassword);
        if (authController.verifyCredentials(enteredUsername, enteredPassword)) {
            System.out.println("Valid Credentials");
            NavigationController.getInstance().navigate(this, new HomeUI());
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials");
            System.out.println("Invalid Credentials");
        }
    }

    /*
    * Event handler that triggers when the create-account button is clicked.
    * Navigates to the SignUpUI.
    * */
    private void onRegisterNowClicked(ActionEvent event) {
        NavigationController.getInstance().navigate(this, new SignUpUI());
    }
}