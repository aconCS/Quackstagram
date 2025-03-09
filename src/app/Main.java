package app;

import com.formdev.flatlaf.FlatLightLaf;
import view.authenticationUI.SignInUI;

import javax.swing.*;

class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        JFrame frame = new SignInUI();
        frame.setVisible(true);
    }
}