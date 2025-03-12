package view.coreUI;

import controller.NavigationController;
import view.components.*;

import javax.swing.*;
import java.awt.*;

public class ExploreUI extends UIBase {

    private final int width = this.getWidth();
    private final int imageSize = width / 3; // Size for each image in the grid
    private ImageGrid imageGridPanel;
    private JPanel mainContentPanel;

    /**
     * Constructor for ExploreUI
     * <p>
     * This constructor initializes the ExploreUI components, including
     * the header panel, navigation panel, and main content panel which contains
     * the image grid of posts and a users search bar.
     * */
    public ExploreUI() {
        setTitle("Explore");

        buildUI();
    }

    /**
     * Initializes the user interface components for the Explore page.
     * <p>
     * This method sets up the header panel, navigation panel, main content panel,
     * and search panel.
     */
    private void buildUI() {
        // Clears existing components and sets the layout
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Creates header, navigation and main content panels
        JPanel headerPanel = new HeaderPanel("Explore");
        JPanel navigationPanel = new NavigationPanel(this);
        JPanel mainContentPanel = createMainContentPanel();

        // Adds panels to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * Creates the main content panel for the Explore page.
     * <p>
     * This method creates the main content panel which contains the image grid of posts
     * and a search panel for searching for users.
     *
     * @return The main content panel for the Explore page
     */
    private JPanel createMainContentPanel() {
        mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        imageGridPanel = new ImageGrid("", imageSize, false); // 3 columns, auto rows

        JPanel searchPanel = createSearchPanel();

        mainContentPanel.add(searchPanel, BorderLayout.NORTH);
        mainContentPanel.add(imageGridPanel, BorderLayout.WEST);
        return mainContentPanel;
    }

    /**
     * Creates the search panel for the Explore page.
     * <p>
     * This method creates the search panel which contains text fields for searching for users.
     *
     * @return The search panel for the Explore page.
     */
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));

        // Creates the search field for posts belonging to a given username
        JTextField searchPostField = new JTextField(" Search Posts");
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPostField.getPreferredSize().height));
        searchPostField.addActionListener(_ -> {
            imageGridPanel.setFilter(searchPostField.getText());
            imageGridPanel.refresh(); // Refresh grid to reflect new filter

            mainContentPanel.revalidate();
            mainContentPanel.repaint();

        });

        // Creates the search field for users whose name matches the given input
        JTextField searchUsersField = new JTextField(" Search Users");
        searchPanel.add(searchPostField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchPostField.getPreferredSize().height)); // Limit the height
        searchUsersField.addActionListener(_ -> {
            JFrame currFrame = (JFrame) SwingUtilities.getWindowAncestor(searchPanel);
            NavigationController.getInstance().navigate(currFrame, new SearchUserUI(searchUsersField.getText()));
        });

        // Adds the search fields to the search panel
        searchPanel.add(searchPostField);
        searchPanel.add(searchUsersField);

        return searchPanel;
    }
}
