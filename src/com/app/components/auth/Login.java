package com.app.components.auth;

import javax.swing.*;
// import com.app.components.MDI;
import java.awt.*;

public class Login extends JFrame {
    protected final Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    public Login() {
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());
        setSize(500, 300);
        setBackground(Color.white);
        Dimension size = toolkit.getScreenSize();
        int x = (int) ((size.getWidth() - 500) / 2);
        int y = (int) ((size.getHeight() - 300) / 2);
        setLocation(new Point(x, y));
        dispose();
        setVisible(true);
    }
}