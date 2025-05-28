package com.app.components.setting;

import javax.swing.JInternalFrame;
import java.awt.*;

public class ChangePassword extends JInternalFrame{
    public ChangePassword(){
        super("Change Password", false, true, true);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
