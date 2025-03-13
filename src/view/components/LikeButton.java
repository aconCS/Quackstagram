package view.components;

import controller.PostController;

import javax.swing.*;
import java.awt.*;

public class LikeButton extends JPanel{

    private static final Color  UNLIKED_BUTTON_COLOR = new Color(255, 153, 153, 255);
    private static final Color  LIKED_BUTTON_COLOR =  new Color(255, 90, 95);
    private final PostController postController;

    public LikeButton(PostController postController){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        this.postController = postController;
        buildLikeButton();
    }

    public void refresh(){
        // Refresh the view
        removeAll();
        buildLikeButton();

        revalidate();
        repaint();
    }

    private void buildLikeButton() {
        JLabel likesLabel = new JLabel(postController.getLikesForPost() + " likes");
        likesLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        likesLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton likeButton = new JButton("❤");
        likeButton.setHorizontalAlignment(SwingConstants.CENTER);
        likeButton.setBorderPainted(false); // Remove border
        likeButton.setOpaque(true);

        if(postController.isPostLiked()){
            likeButton.setBackground(LIKED_BUTTON_COLOR);
        }else{
            likeButton.setBackground(UNLIKED_BUTTON_COLOR);
        }

        likeButton.addActionListener(e -> {
            postController.likeAction();
            refresh(); // Refresh the view
        });

        add(likeButton);
        add(likesLabel);
    }
}
