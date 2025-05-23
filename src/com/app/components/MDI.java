package com.app.components;
import com.app.components.layout.*;
import java.awt.event.*;

import javax.swing.JOptionPane;
public class MDI extends Navbar{
    public MDI(){
        setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        JOptionPane.showMessageDialog(rootPane, e, getTitle(), ABORT);
    }
}
