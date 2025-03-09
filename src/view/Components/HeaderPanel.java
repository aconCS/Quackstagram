package view.Components;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    public HeaderPanel() {
        buildHeaderPanel();
    }

    private void buildHeaderPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height

        JLabel lblRegister = new JLabel("Quackstagram");
        lblRegister.setFont(new Font("Arial", Font.BOLD, 16));
        lblRegister.setForeground(Color.WHITE);
        lblRegister.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblRegister, BorderLayout.CENTER);
    }

}
