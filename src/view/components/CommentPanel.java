package view.components;

import controller.PostController;
import services.FileServices;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CommentPanel extends JPanel{

    private final PostController postController;

    public CommentPanel(PostController postController) {
        this.postController = postController;

        setLayout(new BorderLayout());
        buildCommentsPanel();
    }

    public void refreshCommentsPanel(){
        removeAll();
        buildCommentsPanel();
        revalidate();
        repaint();
    }

    private void buildCommentsPanel(){
        JPanel commentsPanel = new JPanel();
        commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));

        // Read comments from the file
        ArrayList<String[]> comments = postController.getCommentsForPost();

        for(String[] comment: comments){
            // Individual comment Panel
            JPanel commentPanel = new JPanel();
            commentPanel.setLayout(new BorderLayout(5,5));
            commentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            commentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            ImageIcon profileIcon = new ImageIcon("resources/img/storage/profile/" + comment[0] + ".png");
            profileIcon.setImage(profileIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
            JLabel scaledIcon = new JLabel(profileIcon);

            String timestampMessage = FileServices.getElapsedTimestamp(comment[2]);
            if(!timestampMessage.equals("Just now")) {
                timestampMessage = FileServices.getElapsedTimestamp(comment[2]) + " ago";
            }

            JLabel commentLabel = new JLabel(comment[0] + ": " + comment[1]);
            JLabel timeStampLabel = new JLabel(timestampMessage);
            timeStampLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            timeStampLabel.setFont(new Font("Arial", Font.ITALIC, 12));
            timeStampLabel.setForeground(Color.GRAY);

            commentPanel.add(scaledIcon, BorderLayout.WEST);
            commentPanel.add(commentLabel, BorderLayout.CENTER);
            commentPanel.add(timeStampLabel, BorderLayout.EAST);
            commentsPanel.add(commentPanel);
        }

        JScrollPane scrollPane = new JScrollPane(commentsPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1000, 150)); // Set preferred size for the scroll pane

        add(scrollPane, BorderLayout.CENTER);
    }
}
