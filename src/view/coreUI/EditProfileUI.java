package view.coreUI;

import controller.NavigationController;
import controller.UserController;
import view.Components.HeaderPanel;
import view.Components.NavigationPanel;
import view.Components.UIBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditProfileUI extends UIBase {

    private static final int PROFILE_IMAGE_SIZE = 200;
    private final UserController userController;

    public EditProfileUI(UserController userController) {
        this.userController = userController;
        setLayout( new BorderLayout());

        buildUI();
    }

    public void buildUI(){
        add(new HeaderPanel("Edit Profile"), BorderLayout.NORTH);
        add(createBodyPanel(), BorderLayout.CENTER);
        add(new NavigationPanel(this), BorderLayout.SOUTH);
    }

    public JPanel createBodyPanel(){
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

        bodyPanel.add(createEditPicturePanel());
        bodyPanel.add(createBioPanel());

        return bodyPanel;
    }

    public JPanel createEditPicturePanel(){
        JPanel profilePicturePanel = new JPanel();
        profilePicturePanel.setLayout(new BoxLayout(profilePicturePanel, BoxLayout.Y_AXIS));

        JLabel editPhotoLabel = new JLabel("Change Profile Picture");
        editPhotoLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Profile image
        String username = userController.getLoggedInUsername();
        String picturePath = "resources/img/storage/profile" + username + "png";
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("resources/img/storage/profile/" + username + ".png").getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Make the image clickable
        profileImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userController.changeProfilePicture();
                NavigationController.getInstance().navigate(EditProfileUI.this, new ProfileUI(username));
            }
        });

        editPhotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        profilePicturePanel.add(editPhotoLabel);
        editPhotoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        profilePicturePanel.add(profileImage);

        return profilePicturePanel;
    }

    public JPanel createBioPanel(){
        JPanel bioFieldPanel = new JPanel(new BorderLayout());
        bioFieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getHeight()/2));

        JLabel bioLabel = new JLabel("Change Bio");
        bioLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JTextField bioField = new JTextField();
        bioField.setMaximumSize(bioFieldPanel.getMaximumSize());
        bioField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String username = userController.getLoggedInUsername();
                userController.editBio(bioField.getText());
                NavigationController.getInstance().navigate(EditProfileUI.this, new ProfileUI(username));
            }
        });

        bioFieldPanel.add(bioLabel, BorderLayout.NORTH);
        bioFieldPanel.add(bioField, BorderLayout.CENTER);

        return bioFieldPanel;
    }


}
