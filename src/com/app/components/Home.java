package com.app.components;

import javax.swing.*;
import java.awt.*;

public class Home extends JPanel{
    public Home(){
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        add(new JLabel("This is Home Page and default layer"));
        setVisible(true);
    }
}
