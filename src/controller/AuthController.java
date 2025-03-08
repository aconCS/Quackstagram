package controller;

import model.User;
import services.UserServices;

public class AuthController {

    private final UserServices userServices;

    public AuthController() {
        this.userServices = new UserServices();
    }

    public boolean verifyCredentials(String username, String password) {
        return userServices.verifyCredentials(username, password);
    }

    public boolean doesUsernameExist(String username) {
        return userServices.doesUsernameExist(username);
    }

    public void saveCredentials(String username, String password, String bio) {
        userServices.saveCredentials(username, password, bio);
    }

    public void uploadProfilePicture(String username) {
        userServices.uploadProfilePicture(username);
    }
}