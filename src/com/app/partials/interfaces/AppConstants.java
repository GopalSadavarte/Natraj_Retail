package com.app.partials.interfaces;

import java.awt.*;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public interface AppConstants {

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    FlowLayout flowLayoutCenter = new FlowLayout(FlowLayout.CENTER);
    FlowLayout flowLayoutLeft = new FlowLayout(FlowLayout.LEFT, 12, 5);

    Font menuFont = new Font("Arial", 15, 20);
    Font appHeadingFont = new Font("Cambria", 130, 130);
    Font labelFont = new Font("Cambria", 20, 20);
    Font versionFont = new Font("Cambria", 30, 30);
    Font headingFont = new Font("Cambria", 40, 40);
    Font headingFont2 = new Font("Cambria", 32, 32);
    Font smallFont = new Font("Cambria", 15, 15);

    Color borderColor = new Color(235, 236, 236);
    Color lightYellow = new Color(255, 242, 129);
    Color lemonYellow = new Color(255, 252, 221);
    Color lightBlue = new Color(3, 132, 192);
    Color skyBlue = new Color(27, 136, 245);
    Color red = new Color(253, 32, 25);
    Color orange = new Color(250, 83, 16);
    Color darkRed = new Color(216, 3, 3);
    Color parrotGreen = new Color(42, 194, 50);

    Dimension labelSize = new Dimension(170, 30);
    Dimension buttonSize = new Dimension(100, 40);
    Border border = BorderFactory.createLineBorder(borderColor);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
}