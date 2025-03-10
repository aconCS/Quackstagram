package view.coreUI;

import view.Components.HeaderPanel;
import view.Components.ImageGrid;
import view.Components.NavigationPanel;
import view.Components.UIBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExploreUI extends UIBase {

    private final int WIDTH = this.getWidth();
    private final int IMAGE_SIZE = WIDTH / 3; // Size for each image in the grid
    private ImageGrid imageGridPanel;

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
        JPanel mainContentPanel = new JPanel();
        imageGridPanel = new ImageGrid("", IMAGE_SIZE, false); // 3 columns, auto rows

        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height)); // Limit the height
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // Update the filter in the ImageGrid
                imageGridPanel.setFilter(searchField.getText()); // Add this setter to update the filter string in the grid
                imageGridPanel.refresh(); // Refresh grid to reflect new filter

                mainContentPanel.revalidate();
                mainContentPanel.repaint();

            }
        });

        // Main content panel that holds both the search bar and the image grid

        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(imageGridPanel); // This will stretch to take up remaining space
        return mainContentPanel;
    }
}
