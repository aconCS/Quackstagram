package view.components;

import controller.NavigationController;
import controller.PostController;
import view.coreUI.PostUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CommentButton extends JPanel {
    private static final Color COMMENT_BUTTON_COLOR = new Color(175, 248, 170, 255); // Color for the like button
    private final PostController postController;
    private final boolean navigateToPost;
    private final CommentPanel commentPanel;

    public CommentButton(PostController postController, CommentPanel commentPanel, boolean navigateToPost){
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        this.postController = postController;
        this.navigateToPost = navigateToPost;
        this.commentPanel = commentPanel;
        buildCommentButton();
    }

    private void refresh(){
        // Refresh the view
        removeAll();
        buildCommentButton();

        revalidate();
        repaint();
    }

    private void buildCommentButton() {
        JLabel commentLabel = new JLabel(postController.getCommentsForPost().size() + " comments");
        commentLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        commentLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        JButton commentButton = new JButton("💬");
        commentButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        commentButton.setBackground(COMMENT_BUTTON_COLOR); // Set the background color for the like button
        commentButton.setOpaque(true);
        commentButton.setBorderPainted(false);
        commentButton.addActionListener(e -> {
            if(navigateToPost){
                handlePostNavigation();
                handleCommentAction();
            }else{
                handleCommentAction();
            }
        });

        add(commentButton);
        add(commentLabel);
    }

    private void handlePostNavigation(){
        JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        NavigationController.getInstance().navigate(currFrame, new PostUI(postController));
    }

    private void handleCommentAction() {
        // Create a dialog for user to enter comment
        JDialog commentDialog = new JDialog((Frame) null, "Add Comment", true);
        commentDialog.setLayout(new BorderLayout());
        commentDialog.setSize(300, 150);

        // Text field for entering comment
        JTextField commentField = new JTextField();
        commentDialog.add(commentField, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = getSubmitButton(commentField, commentDialog);
        commentDialog.add(submitButton, BorderLayout.SOUTH);

        // Show the dialog
        commentDialog.setLocationRelativeTo(null);
        commentDialog.setVisible(true);
    }

    private JButton getSubmitButton(JTextField commentField, JDialog commentDialog) {
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String comment = commentField.getText();
                if (!comment.isEmpty()) {
                    postController.addCommentToPost(comment);
                    commentPanel.refreshCommentsPanel();
                    commentDialog.dispose();
                    refresh();

                }
            }
        });
        return submitButton;
    }
}
