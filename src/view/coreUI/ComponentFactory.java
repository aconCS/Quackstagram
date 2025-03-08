package view.coreUI;

import javax.swing.*;
import java.awt.*;

public class ComponentFactory{
    static JPanel createHeaderPanel(){
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);

        return headerPanel;
    }

    static JPanel topOfHeaderPanel(){
        JPanel topOfHeaderPanel = new JPanel();
        topOfHeaderPanel.setLayout(new BoxLayout(topOfHeaderPanel, BoxLayout.X_AXIS));
        topOfHeaderPanel.setBackground(Color.GRAY);

        return topOfHeaderPanel;
    }

    static JLabel createProfilePicture(User currentUser){
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("resources/img/storage/profile/" + currentUser.getUsername() + ".png").getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return profileImage;
    }
}