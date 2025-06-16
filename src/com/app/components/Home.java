package com.app.components;

import javax.swing.*;
import com.app.partials.interfaces.*;
import java.awt.*;

public class Home extends JPanel implements AppConstants{

    JPanel headingPanel ,mainPanel,subPanel;
    JLabel heading,image,version,footerPart,contact,email,address,website;

    public Home(){
        setLayout(flowLayoutCenter);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setBackground(Color.white);
        mainPanel = new JPanel(flowLayoutCenter);
        mainPanel.setPreferredSize(toolkit.getScreenSize());
        mainPanel.setBackground(Color.white);

        headingPanel = new JPanel(flowLayoutCenter);
        headingPanel.setBackground(Color.white);
        
        heading = new JLabel("Natraj Book Depot");
        heading.setFont(appHeadingFont);
        heading.setForeground(Color.red);

        headingPanel.add(heading);

        subPanel = new JPanel(flowLayoutCenter);
        subPanel.setBackground(Color.white);
        subPanel.setPreferredSize(new Dimension((int)toolkit.getScreenSize().getWidth(),400));

        ImageIcon imageIcon = new ImageIcon(Home.class.getResource("logo.jpg"));
        Image img  = imageIcon.getImage().getScaledInstance(130, 110, Image.SCALE_SMOOTH);
        image = new JLabel(new ImageIcon(img));
        image.setPreferredSize(new Dimension(getWidth(),120));
        subPanel.add(image);

        version = new JLabel("Version 1.0 - Inventory Management System");
        version.setFont(versionFont);
        version.setForeground(Color.darkGray);
        subPanel.add(version);

        contact = new JLabel("<html><strong>Contact :</strong>8956434705/9561734705</html>");
        contact.setFont(labelFont);
        subPanel.add(contact);

        email = new JLabel();
        website = new JLabel();
        address = new JLabel();
        footerPart = new JLabel();

        mainPanel.add(headingPanel);
        mainPanel.add(subPanel);

        add(mainPanel);
        setVisible(true);
    }
}
