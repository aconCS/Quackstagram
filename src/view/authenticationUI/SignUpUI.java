package view.authenticationUI;

import controller.AuthController;
import controller.NavigationController;
import view.components.UIBase;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SignUpUI extends UIBase {

    private final AuthController authController;
    private final JTextField usernameField;
    private final JTextField passwordField;
    private final JTextField bioField;

    public SignUpUI() {
        setTitle( "Quackstagram - Register");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        authController = new AuthController();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        bioField = new JTextField();

        new AuthUIBuilder(this)
                .addHeaderPanel("Register")
                .addTextFieldPanel("Username", usernameField)
                .addTextFieldPanel("Password", passwordField)
                .addTextFieldPanel("Bio", bioField)
                .addButton("Register", this::onRegisterClicked)
                .addButton("Login instead", this::switchButtonClicked)
                .buildUI();
    }

    /*
    * Event handler that triggers when the register button is clicked.
    * Checks if the entered username already exists and saves credentials if it doesn't.
    * Navigates to the SignInUI.
    * */
    private void onRegisterClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String bio = bioField.getText();

        if (authController.doesUsernameExist(username)) {
            JOptionPane.showMessageDialog(this,
                    "Username already exists. Please choose a different username.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if (username.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "Username can't be empty",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        } else if(password.length() < 6){
            JOptionPane.showMessageDialog(this,
                    "Password is too short",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        authController.saveCredentials(username, password, bio);
        authController.uploadProfilePicture(username);
        NavigationController.getInstance().navigate(this, new SignInUI());
    }

    /*
    * Event handler that triggers when the login-instead button is clicked.
    * Navigates to the SignInUI.
    * */
    private void switchButtonClicked(ActionEvent event) {
        NavigationController.getInstance().navigate(this, new SignInUI());
    }

}
