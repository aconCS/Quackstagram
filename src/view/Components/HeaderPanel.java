package view.Components;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    public HeaderPanel() {
        buildHeaderPanel();
    }

    private void buildHeaderPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel lblRegister = new JLabel("Quackstagram");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE); // Set the text color to white
        add(lblRegister);
        setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
    }

}
