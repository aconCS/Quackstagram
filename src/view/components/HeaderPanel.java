package view.components;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    private String title;

    public HeaderPanel(String title) {
        this.title = title;
        buildHeaderPanel();
    }

    /*
    * Builds the Quackstagram header panel that every UI frame will have.
    * */
    private void buildHeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

        JLabel lblRegister = new JLabel(title);
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE);
        lblRegister.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblRegister, BorderLayout.CENTER);
    }

}
