package view.coreUI;

import controller.UserController;
import model.Post;
import view.Components.NavigationPanel;
import view.Components.ProfileHeader;
import view.Components.UIBase;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.*;
import java.util.List;
import java.nio.file.*;
import java.util.stream.Stream;

public class ProfileUI extends UIBase {

    private final int WIDTH = this.getWidth();


    private final int GRID_IMAGE_SIZE = WIDTH / 3; // Static size for grid images

    private JPanel contentPanel; // Panel to display the image grid or the clicked image
    private JPanel headerPanel; // Panel for the header
    private UserController userController;
    private String username;

    public ProfileUI(String username) {
        this.username = username;
        setTitle("DACS Profile");

        //TODO ACCESS VIA AUTHCONTROLLER
        userController = new UserController(username);

        contentPanel = new JPanel();
        headerPanel = new ProfileHeader(username, userController); // Initialize header panel

        initializeUI();
    }

    private void initializeUI() {
        getContentPane().removeAll(); // Clear existing components

        // Re-add the header and navigation panels
        add(headerPanel, BorderLayout.NORTH);
        add(new NavigationPanel(this), BorderLayout.SOUTH);

        // Initialize the image grid
        initializeImageGrid();

        revalidate();
        repaint();
    }

    private void initializeImageGrid() {
        contentPanel.removeAll(); // Clear existing content
        contentPanel.setLayout(new GridLayout(0, 3, 5, 5)); // Grid layout for image grid

        java.util.List<Path> postPaths = userController.getPostPaths();

        for(Path path : postPaths){
            System.out.println(path.toString());
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(path.toString())
                    .getImage()
                    .getScaledInstance(GRID_IMAGE_SIZE, GRID_IMAGE_SIZE, Image.SCALE_SMOOTH));
            JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    displayImage(imageIcon); // Call method to display the clicked image
                }
            });
            contentPanel.add(imageLabel);
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER); // Add the scroll pane to the center

        revalidate();
        repaint();
    }

    private void displayImage(ImageIcon imageIcon) {
        contentPanel.removeAll(); // Remove existing content
        contentPanel.setLayout(new BorderLayout()); // Change layout for image display

        JLabel fullSizeImageLabel = new JLabel(imageIcon);
        fullSizeImageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(fullSizeImageLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            getContentPane().removeAll(); // Remove all components from the frame
            initializeUI(); // Re-initialize the UI
        });
        contentPanel.add(backButton, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }



}