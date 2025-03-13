package view.components;

import controller.NavigationController;
import controller.PostController;
import services.FileServices;
import view.coreUI.PostUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ImageGrid extends JPanel{

    private final int imageSize;
    private String filter;
    private final boolean isExact;

    public ImageGrid(String filter, int imageSize, boolean isExact) {
        setLayout(new BorderLayout(5,5));
        this.imageSize = imageSize;
        this.isExact = isExact;
        this.filter = filter;

        initializeScrollGrid();
    }

    public void setFilter(String filter){
        this.filter = filter;
    }

    public void refresh(){
        removeAll();
        initializeScrollGrid();

        revalidate();
        repaint();
    }

    private void initializeScrollGrid() {
        JPanel imageGrid = createGrid();

        JScrollPane scrollPane = new JScrollPane(imageGrid);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createGrid() {
        JPanel imageGrid = new JPanel(new GridLayout(0, 3, 2, 2));
        ArrayList<String> imageIds = FileServices.getAllImageIds();

        for(String imageId : imageIds){
            String username = new PostController(imageId).getImageOwner();
            if(isExact && !username.equals(filter)) continue;
            if(!isExact && !username.toLowerCase().contains(filter.toLowerCase())) continue;

            PostController postController = new PostController(imageId);
            String imagePath = postController.getImagePath();
            ImageIcon imageIcon = FileServices.createScaledIcon(imagePath, imageSize, imageSize);

            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PostController postController = new PostController(imageId);
                    JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(imageLabel);
                    NavigationController.getInstance().navigate(currFrame, new PostUI(postController));
                }
            });
            imageGrid.add(imageLabel);
        }
        return imageGrid;
    }
}
