package com.app.components.utilities;

import javax.swing.JInternalFrame;
import java.awt.*;

public class CreditNoteList extends JInternalFrame{
    public CreditNoteList(){
        super("Credit Note List", false, true, true);
        setBackground(Color.white);

        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }
}
