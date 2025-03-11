package view.coreUI;

import controller.NavigationController;
import view.Components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExploreUI extends UIBase {

    private final int WIDTH = this.getWidth();
    private final int IMAGE_SIZE = WIDTH / 3; // Size for each image in the grid
    private ImageGrid imageGridPanel;
    private SearchUserUI searchUsersPanel;
    private JPanel mainContentPanel;

    public ExploreUI() {
        setTitle("Explore");

        initializeUI();
    }

    private void initializeUI() {

        getContentPane().removeAll(); // Clear existing components
        setLayout(new BorderLayout()); // Reset the layout manager

        JPanel headerPanel = new HeaderPanel("Explore"); // Method from your InstagramProfileUI class
        JPanel navigationPanel = new NavigationPanel(this); // Method from your InstagramProfileUI class
        JPanel mainContentPanel = createMainContentPanel();

        // Add panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();

    }

    private JPanel createMainContentPanel() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        imageGridPanel = new ImageGrid("", IMAGE_SIZE, false); // 3 columns, auto rows

        JPanel searchPanel = createNavigationPanel();

        mainContentPanel.add(searchPanel, BorderLayout.NORTH);
        mainContentPanel.add(imageGridPanel, BorderLayout.WEST); // This will stretch to take up remaining space
        return mainContentPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));

        JTextField searchPostField = new JTextField(" Search Posts");
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPostField.getPreferredSize().height)); // Limit the height
        searchPostField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                imageGridPanel.setFilter(searchPostField.getText()); // Add this setter to update the filter string in the grid
                imageGridPanel.refresh(); // Refresh grid to reflect new filter

                mainContentPanel.revalidate();
                mainContentPanel.repaint();

            }
        });

        JTextField searchUsersField = new JTextField(" Search Users");
        searchPanel.add(searchPostField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPostField.getPreferredSize().height)); // Limit the height
        searchUsersField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(searchPanel);
                NavigationController.getInstance().navigate(currFrame, new SearchUserUI(searchUsersField.getText()));
            }
        });

        searchPanel.add(searchPostField);
        searchPanel.add(searchUsersField);

        return searchPanel;
    }
}
