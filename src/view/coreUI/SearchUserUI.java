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

    public SearchUserUI(String filter) {
        this.userController = new UserController();
        this.filter = filter;

        setLayout(new BorderLayout(5, 5));
        buildUI();
    }

    public void buildUI(){
        add(new HeaderPanel("Search: " + filter), BorderLayout.NORTH);
        add(createBodyPanel(), BorderLayout.CENTER);
        add(new NavigationPanel(this), BorderLayout.SOUTH);
    }

    public JPanel createBodyPanel(){
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.PAGE_AXIS));

        ArrayList<String> users = userController.getAllUsers();

        for (String user : users) {
            if (user.toLowerCase().contains(filter.toLowerCase())) {
                PostController postController = new PostController(user);
                bodyPanel.add(new UserNavPanel(postController));
            }
        }

        return bodyPanel;
    }


}
