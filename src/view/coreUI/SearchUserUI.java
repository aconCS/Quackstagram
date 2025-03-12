package view.coreUI;

import controller.PostController;
import controller.UserController;
import view.components.HeaderPanel;
import view.components.NavigationPanel;
import view.components.UIBase;
import view.components.UserNavPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SearchUserUI extends UIBase {

    private final UserController userController;
    private final String filter;

    /**
     * Constructor for SearchUserUI.
     *
     * This constructor initializes the SearchUserUI components which include
     * the header and navigation panel, and the body panel which contains the
     * users that match the filter.
     *
     * @param filter The user input that is used to filter the users or posts.
     */
    public SearchUserUI(String filter) {
        this.userController = new UserController();
        this.filter = filter;

        setLayout(new BorderLayout(5, 5));
        buildUI();
    }

    /**
     * Initializes the user interface components for the SearchUserUI.
     * <p>
     * This method sets up the header panel, navigation panel, and
     * body panel containing users, posts, or nothing.
     */
    private void buildUI(){
        add(new HeaderPanel("Search: " + filter), BorderLayout.NORTH);
        add(createBodyPanel(), BorderLayout.CENTER);
        add(new NavigationPanel(this), BorderLayout.SOUTH);
    }

    /***
     * Creates the body panel for the SearchUserUI.
     * <p>
     * This method creates a JPanel that contains the users or posts that
     * match the filter.
     *
     * @return JPanel containing the users that match the filter.
     */
    private JPanel createBodyPanel(){
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.PAGE_AXIS));

        // Initializes a string of all usernames
        ArrayList<String> users = userController.getAllUsers();

        // Iterates over the list of users and filters them based on the provided filter string.
        // For each user that matches the filter, a UserNavPanel is then created and added to the
        // bodyPanel. The UserNavPanel allows the logged-in user to navigate to the selected user's
        // profile when clicked.
        for (String user : users) {
            if (user.toLowerCase().contains(filter.toLowerCase())) {
                bodyPanel.add(new UserNavPanel(user));
            }
        }

        return bodyPanel;
    }


}
