package view.coreUI;

import controller.PostController;
import services.FileServices;
import view.components.*;

import javax.swing.*;
import java.awt.*;

public class PostUI extends UIBase {

    private final int width = this.getWidth();
    private final int imageDimension = width/2;

    private final PostController postController;
    private final CommentPanel commentPanel;

    public PostUI(PostController postController) {
        this.postController = postController;
        commentPanel = new CommentPanel(postController);

        buildUI();
    }

    private void buildUI() {
        add(new HeaderPanel(postController.getImageOwner() + "'s post"), BorderLayout.NORTH);
        add(createBodyPanel(), BorderLayout.CENTER);
        add(new NavigationPanel(this), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel createBodyPanel() {
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

        bodyPanel.add(createImageLabel());
        bodyPanel.add(createInfoPanel());
        bodyPanel.add(commentPanel);

        return bodyPanel;
    }

    private JLabel createImageLabel(){
        // Crop the image to the fixed size
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon imageIcon = FileServices.createScaledIcon(postController.getImagePath(), imageDimension, imageDimension);
        if (imageIcon != null) {
            imageLabel.setIcon(imageIcon);
        } else {
            imageLabel.setText("Image not found");
        }

        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return imageLabel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel likeButton = new LikeButton(postController);
        JPanel commentButton = new CommentButton(postController, commentPanel, false);
        buttonPanel.add(likeButton);
        buttonPanel.add(commentButton);

        return buttonPanel;
    }

    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        String imageOwner = postController.getImageOwner();
        JPanel userNavPanel = new UserNavPanel(imageOwner);
        infoPanel.add(userNavPanel); // User navigation panel

        JPanel captionWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel caption = new JLabel(postController.getPostCaption());
        captionWrapper.add(caption);
        infoPanel.add(captionWrapper); // Caption

        JPanel buttonsPanel = createButtonsPanel();
        infoPanel.add(buttonsPanel); // Buttons panel

        return infoPanel;
    }
}
