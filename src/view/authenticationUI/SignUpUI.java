package view.authenticationUI;

import controller.AuthController;
import controller.NavigationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignUpUI extends JFrame {

    private final AuthController AUTH_CONTROLLER;
    private final JTextField USERNAME_FIELD;
    private final JTextField PASSWORD_FIELD;
    private final JTextField BIO_FIELD;

    public SignUpUI() {
        AUTH_CONTROLLER = new AuthController();
        USERNAME_FIELD = new JTextField();
        PASSWORD_FIELD = new JPasswordField();
        BIO_FIELD = new JTextField();

        new AuthUIBuilder(this, "Quackstagram - Register")
                .addTextFieldPanel("Username", USERNAME_FIELD)
                .addTextFieldPanel("Password", PASSWORD_FIELD)
                .addTextFieldPanel("Bio", BIO_FIELD)
                .addButton("Register", Color.BLUE, this::onRegisterClicked)
                .addButton("Login instead", Color.WHITE, this::switchButtonClicked)
                .buildUI();
    }

    private void onRegisterClicked(ActionEvent event) {
        String username = USERNAME_FIELD.getText();
        String password = PASSWORD_FIELD.getText();
        String bio = BIO_FIELD.getText();

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
