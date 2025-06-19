package com.app.components;

import com.app.components.analytics.expiry.*;
import com.app.components.analytics.purchase.*;
import com.app.components.analytics.sales.*;
import com.app.components.analytics.stock.*;
import com.app.components.expiries.*;
import com.app.components.layout.*;
import com.app.components.purchase.*;
import com.app.components.reports.expiry.*;
import com.app.components.reports.purchase.*;
import com.app.components.reports.purchase_return.*;
import com.app.components.reports.sales.*;
import com.app.components.reports.sales_return.*;
import com.app.components.reports.stock.*;
import com.app.components.sales.*;
import com.app.components.setting.*;
import com.app.components.utilities.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class MDI extends Navbar {

    JInternalFrame frame;
    JDesktopPane desktop = new JDesktopPane();
    HashMap<String, JInternalFrame> frameTracker = new HashMap<>();
    HashMap<JInternalFrame, String> frameKeyMap = new HashMap<>();
    InternalFrameAdapter internalFrameListener = new InternalFrameAdapter() {
        public void internalFrameClosing(InternalFrameEvent e) {
            JInternalFrame frame = e.getInternalFrame();
            if (frame instanceof SaleBill) {
                boolean res = JOptionPane.showConfirmDialog(MDI.this, "Are you sure to exit?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION) == 0;
                if (res) {
                    String key = frameKeyMap.get(frame);
                    frameTracker.remove(key);
                    frame.dispose();
                }
            } else {
                String key = frameKeyMap.get(frame);
                frameTracker.remove(key);
            }
        }
    };
    ImageIcon icon = new ImageIcon(MDI.class.getResource("logo.jpg"));
    int optionPaneCount = 0;

    public MDI() {
        setBackground(Color.white);
        setContentPane(desktop);
        setIconImage(icon.getImage());
        desktop.add(new Home(), JLayeredPane.DEFAULT_LAYER);
        setShortcuts();
        setVisible(true);
    }

    private void setShortcuts() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEvent -> {
            String key = KeyEvent.getKeyText(keyEvent.getKeyCode());
            String modifier = InputEvent.getModifiersExText(keyEvent.getModifiersEx());
            if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                if (key.toLowerCase().equals("p") && modifier.toLowerCase().equals("ctrl")) {
                    ActionEvent event = new ActionEvent(sources.get(
                            "Purchase Entry Ctrl+P"), WIDTH, "Purchase Entry Ctrl+P");
                    actionPerformed(event);
                    return true;
                }
                if (key.toLowerCase().equals("p") && modifier.toLowerCase().equals("ctrl+alt")) {
                    ActionEvent event = new ActionEvent(sources.get(
                            "Purchase Return Ctrl+Alt+P"), WIDTH, "Purchase Return Ctrl+Alt+P");
                    actionPerformed(event);
                    return true;
                }
                if (key.toLowerCase().equals("i") && modifier.toLowerCase().equals("alt")) {
                    ActionEvent event = new ActionEvent(sources.get(
                            "Product Alt+I"), 2, "Product Alt+I");
                    actionPerformed(event);
                    return true;
                }
                if (key.toLowerCase().equals("s") && modifier.toLowerCase().equals("ctrl")) {
                    ActionEvent event = new ActionEvent(sources.get(
                            "Sale Ctrl+S"), 2, "Sale Ctrl+S");
                    actionPerformed(event);
                    return true;
                }
                if (key.toLowerCase().equals("s") && modifier.toLowerCase().equals("ctrl+alt")) {
                    ActionEvent event = new ActionEvent(sources.get(
                            "Sales Return Ctrl+Alt+S"), 2, "Sales Return Ctrl+Alt+S");
                    actionPerformed(event);
                    return true;
                }
                if (key.toLowerCase().equals("e") && modifier.toLowerCase().equals("ctrl")) {
                    ActionEvent event = new ActionEvent(sources.get(
                            "Expiry Entry Ctrl+E"), 2, "Expiry Entry Ctrl+E");
                    actionPerformed(event);
                    return true;
                }

                if (key.toLowerCase().equals("escape") && desktop.getAllFrames().length > 0) {
                    frame = desktop.getSelectedFrame();
                    if (dialogBox.isVisible()) {
                        dialogBox.setVisible(false);
                        return true;
                    }
                    if (frame instanceof SaleBill && optionPaneCount == 0) {
                        optionPaneCount = 1;
                        boolean res = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confirmation",
                                JOptionPane.OK_CANCEL_OPTION) == 0;
                        if (res) {
                            frameTracker.remove(frameKeyMap.get(frame));
                            frame.dispose();
                        }
                        optionPaneCount = 0;
                    } else {
                        frameTracker.remove(frameKeyMap.get(frame));
                        frame.dispose();
                    }
                }
            }
            return false;
        });
    }

    public void actionPerformed(ActionEvent e) {

        String action = e.getActionCommand();
        JMenuItem source = (JMenuItem) e.getSource();
        frame = null;
        String key = null;

        switch (action) {
            case "BackUp And Exit":
                boolean res = JOptionPane.showConfirmDialog(this, "Are you sure to exit?", "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION) == 0;
                if (res) {
                    dialogBox.dispose();
                    dispose();
                }
                return;
            case "Purchase Entry Ctrl+P":
                if (!frameTracker.containsKey("Purchase Entry Ctrl+P"))
                    frame = new PurchaseEntry(dialogBox);
                key = "Purchase Entry Ctrl+P";
                break;
            case "Purchase Return Ctrl+Alt+P":
                if (!frameTracker.containsKey("Purchase Return Ctrl+Alt+P"))
                    frame = new PurchaseReturn(dialogBox);
                key = "Purchase Return Ctrl+Alt+P";
                break;
            case "Product Alt+I":
                if (!frameTracker.containsKey("Product Alt+I"))
                    frame = new Product(source, frameTracker, frameKeyMap, dialogBox);
                key = "Product Alt+I";
                break;
            case "Group":
                if (!frameTracker.containsKey("Group"))
                    frame = new Group(source, frameTracker, frameKeyMap, dialogBox);
                key = "Group";
                break;
            case "Sub Group":
                if (!frameTracker.containsKey("Sub Group")) {
                    frame = new SubGroup(source, frameTracker, frameKeyMap, dialogBox);
                }
                key = "Sub Group";
                break;
            case "Sale Ctrl+S":
                frame = new SaleBill(source, frameTracker, frameKeyMap, dialogBox);
                key = "sale";
                break;
            case "Sales Return Ctrl+Alt+S":
                if (!frameTracker.containsKey("Sales Return Ctrl+Alt+S"))
                    frame = new SalesReturnEntry(dialogBox);
                key = "Sales Return Ctrl+Alt+S";
                break;
            case "Expiry Entry Ctrl+E":
                if (!frameTracker.containsKey("Expiry Entry Ctrl+E"))
                    frame = new ExpiryEntry(dialogBox);
                key = "Expiry Entry Ctrl+E";
                break;
            case "Near Expiry":
                if (!frameTracker.containsKey("Near Expiry"))
                    frame = new NearExpiry();
                key = "Near Expiry";
                break;
            case "Purchase Daily":
                if (!frameTracker.containsKey("Purchase Daily"))
                    frame = new DailyPurchaseReport();
                key = "Purchase Daily";
                break;
            case "Purchase Weekly":
                if (!frameTracker.containsKey("Purchase Weekly"))
                    frame = new WeeklyPurchaseReport();
                key = "Purchase Weekly";
                break;
            case "Purchase Monthly":
                if (!frameTracker.containsKey("Purchase Monthly"))
                    frame = new MonthlyPurchaseReport();
                key = "Purchase Monthly";
                break;
            case "Purchase Yearly":
                if (!frameTracker.containsKey("Purchase Yearly"))
                    frame = new YearlyPurchaseReport();
                key = "Purchase Yearly";
                break;
            case "Sale Yearly":
                if (!frameTracker.containsKey("Sale Yearly"))
                    frame = new YearlySalesReport();
                key = "Sale Yearly";
                break;
            case "Sale Daily":
                if (!frameTracker.containsKey("Sale Daily"))
                    frame = new DailySalesReport();
                key = "Sale Daily";
                break;
            case "Sale Monthly":
                if (!frameTracker.containsKey("Sale Monthly"))
                    frame = new MonthlySalesReport();
                key = "Sale Monthly";
                break;
            case "Sale Weekly":
                if (!frameTracker.containsKey("Sale Weekly"))
                    frame = new WeeklySalesReport();
                key = "Sale Weekly";
                break;
            case "Stock available":
                if (!frameTracker.containsKey("Stock available"))
                    frame = new AvailableStockReport();
                key = "Stock available";
                break;
            case "Stock required":
                if (!frameTracker.containsKey("Stock required"))
                    frame = new RequiredStockReport();
                key = "Stock required";
                break;
            case "Stock less sold":
                if (!frameTracker.containsKey("Stock less sold"))
                    frame = new LessSoldStockReport();
                key = "Stock less sold";
                break;
            case "Stock Yearly":
                if (!frameTracker.containsKey("Stock Yearly"))
                    frame = new YearlyStockReport();
                key = "Stock Yearly";
                break;
            case "Expiry Daily":
                if (!frameTracker.containsKey("Expiry Daily"))
                    frame = new DailyExpiryReport();
                key = "Expiry Daily";
                break;
            case "Expiry Yearly":
                if (!frameTracker.containsKey("Expiry Yearly"))
                    frame = new YearlyExpiryReport();
                key = "Expiry Yearly";
                break;
            case "Expiry Monthly":
                if (!frameTracker.containsKey("Expiry Monthly"))
                    frame = new MonthlyExpiryReport();
                key = "Expiry Monthly";
                break;
            case "Expiry Weekly":
                if (!frameTracker.containsKey("Expiry Weekly"))
                    frame = new WeeklyExpiryReport();
                key = "Expiry Weekly";
                break;
            case "Sales Return Daily":
                if (!frameTracker.containsKey("Sales Return Daily"))
                    frame = new DailySalesReturnReport();
                key = "Sales Return Daily";
                break;
            case "Sales Return Yearly":
                if (!frameTracker.containsKey("Sales Return Yearly"))
                    frame = new YearlySalesReturnReport();
                key = "Sales Return Yearly";
                break;
            case "Sales Return Monthly":
                if (!frameTracker.containsKey("Sales Return Monthly"))
                    frame = new MonthlySalesReturnReport();
                key = "Sales Return Monthly";
                break;
            case "Sales Return Weekly":
                if (!frameTracker.containsKey("Sale Weekly"))
                    frame = new WeeklySalesReturnReport();
                key = "Sales Return Weekly";
                break;
            case "Purchase Return Monthly":
                if (!frameTracker.containsKey("Purchase Return Monthly"))
                    frame = new MonthlyPurchaseReturnReport();
                key = "Purchase Return Monthly";
                break;
            case "Purchase Return Yearly":
                if (!frameTracker.containsKey("Purchase Return Yearly"))
                    frame = new YearlyPurchaseReturnReport();
                key = "Purchase Return Yearly";
                break;
            case "Purchase Return Daily":
                if (!frameTracker.containsKey("Purchase Return Daily"))
                    frame = new DailyPurchaseReturnReport();
                key = "Purchase Return Daily";
                break;
            case "Purchase Return Weekly":
                if (!frameTracker.containsKey("Purchase Return Weekly"))
                    frame = new WeeklyPurchaseReturnReport();
                key = "Purchase Return Weekly";
                break;
            case "Quotation":
                if (!frameTracker.containsKey("Quotation"))
                    frame = new Quotation(dialogBox);
                key = "Quotation";
                break;
            case "Debit Note":
                if (!frameTracker.containsKey("Debit Note"))
                    frame = new DebitNote();
                key = "Debit Note";
                break;
            case "Credit Note":
                if (!frameTracker.containsKey("Purchase Return Monthly"))
                    frame = new CreditNoteList();
                key = "Credit Note";
                break;
            case "Change Password":
                if (!frameTracker.containsKey("Change Password"))
                    frame = new ChangePassword();
                key = "Change Password";
                break;
            case "Stock Analysis Daily":
                if (!frameTracker.containsKey("Stock Analysis Daily"))
                    frame = new DailyStockChart();
                key = "Stock Analysis Daily";
                break;
            case "Stock Analysis Monthly":
                if (!frameTracker.containsKey("Stock Analysis Monthly"))
                    frame = new MonthlyStockChart();
                key = "Stock Analysis Monthly";
                break;
            case "Stock Analysis Yearly":
                if (!frameTracker.containsKey("Stock Analysis Yearly"))
                    frame = new YearlyStockChart();
                key = "Stock Analysis Yearly";
                break;
            case "Stock Analysis Weekly":
                if (!frameTracker.containsKey("Stock Analysis Weekly"))
                    frame = new WeeklyStockChart();
                key = "Stock Analysis Weekly";
                break;
            case "Sale Analysis Yearly":
                if (!frameTracker.containsKey("Sale Analysis Yearly"))
                    frame = new YearlyStockChart();
                key = "Sale Analysis Yearly";
                break;
            case "Sale Analysis Daily":
                if (!frameTracker.containsKey("Sale Analysis Daily"))
                    frame = new DailySalesChart();
                key = "Sale Analysis Daily";
                break;
            case "Sale Analysis Weekly":
                if (!frameTracker.containsKey("Sale Analysis Weekly"))
                    frame = new WeeklySalesChart();
                key = "Sale Analysis Weekly";
                break;
            case "Sale Analysis Monthly":
                if (!frameTracker.containsKey("Sale Analysis Monthly"))
                    frame = new MonthlySalesChart();
                key = "Sale Analysis Monthly";
                break;
            case "Expiry Analysis Weekly":
                if (!frameTracker.containsKey("Expiry Analysis Weekly"))
                    frame = new WeeklyExpiryChart();
                key = "Expiry Analysis Weekly";
                break;
            case "Expiry Analysis Daily":
                if (!frameTracker.containsKey("Expiry Analysis Daily"))
                    frame = new DailyExpiryChart();
                key = "Expiry Analysis Daily";
                break;
            case "Expiry Analysis Yearly":
                if (!frameTracker.containsKey("Expiry Analysis Yearly"))
                    frame = new YearlyExpiryChart();
                key = "Expiry Analysis Yearly";
                break;
            case "Expiry Analysis Monthly":
                if (!frameTracker.containsKey("Expiry Analysis Monthly"))
                    frame = new MonthlyExpiryChart();
                key = "Expiry Analysis Monthly";
                break;
            case "Purchase Analysis Monthly":
                if (!frameTracker.containsKey("Purchase Analysis Monthly"))
                    frame = new MonthlyPurchaseChart();
                key = "Purchase Analysis Monthly";
                break;
            case "Purchase Analysis Yearly":
                if (!frameTracker.containsKey("Purchase Analysis Yearly"))
                    frame = new YearlyPurchaseChart();
                key = "Purchase Analysis Yearly";
                break;
            case "Purchase Analysis Daily":
                if (!frameTracker.containsKey("Purchase Analysis Daily"))
                    frame = new DailyPurchaseChart();
                key = "Purchase Analysis Daily";
                break;
            case "Purchase Analysis Weekly":
                if (!frameTracker.containsKey("Purchase Analysis Weekly"))
                    frame = new WeeklyPurchaseChart();
                key = "Purchase Analysis Weekly";
                break;
        }

        if (frame != null) {
            desktop.add(frame);
            frameTracker.put(key, frame);
            frameKeyMap.put(frame, key);
            frame.toFront();
            frame.addInternalFrameListener(internalFrameListener);
        } else {
            frame = frameTracker.get(key);
            frame.toFront();
        }
        try {
            frame.setSelected(true);
        } catch (Exception exc) {
            System.out.println(exc.getMessage() + " at MDI.java 422");
        }
    }
}
