package view.authenticationUI;

import controller.AuthController;
import controller.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignUpUI extends JFrame {

    private final AuthController authController;
    private final JTextField usernameField;
    private final JTextField passwordField;
    private final JTextField bioField;


    public SignUpUI() {
        authController = new AuthController();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        bioField = new JTextField();

        new AuthUIBuilder(this, "Quackstagram - Register")
                .addTextFieldPanel("Username", usernameField)
                .addTextFieldPanel("Password", passwordField)
                .addTextFieldPanel("Bio", bioField)
                .addButton("Register", Color.BLUE, this::onRegisterClicked)
                .addButton("Login instead", Color.WHITE, this::switchButtonClicked)
                .buildUI();
    }

    private void onRegisterClicked(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String bio = bioField.getText();

        if (authController.doesUsernameExist(username)) {
            JOptionPane.showMessageDialog(this,
                    "Username already exists. Please choose a different username.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            authController.saveCredentials(username, password, bio);
            authController.uploadProfilePicture(username);
            NavigationController.getInstance().navigate(this, new SignInUI());
        }
    }
        
    private void switchButtonClicked(ActionEvent event) {
        NavigationController.getInstance().navigate(this, new SignInUI());
    }

}
