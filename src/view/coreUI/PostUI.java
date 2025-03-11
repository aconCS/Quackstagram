package view.coreUI;

import controller.NavigationController;
import controller.PostController;
import services.FileServices;
import view.Components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PostUI extends UIBase {

    private final int WIDTH = this.getWidth();
    private final int IMAGE_DIMENSION = WIDTH/2;

    private final PostController postController;
    private final CommentPanel commentPanel;

    public PostUI (PostController postController) {
        this.postController = postController;
        commentPanel = new CommentPanel(postController);

        buildPostUI();
    }

    public void refresh() {
        // Refresh the view
        removeAll();
        
        revalidate();
        repaint();
    }

    private void buildPostUI() {
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

    public JLabel createImageLabel(){
        // Crop the image to the fixed size
        JLabel imageLabel = new JLabel();
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon imageIcon = FileServices.createScaledIcon(postController.getImagePath(), IMAGE_DIMENSION, IMAGE_DIMENSION);
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

        JPanel userNavPanel = new UserNavPanel(postController);
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
