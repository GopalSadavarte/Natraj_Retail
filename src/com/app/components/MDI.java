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
import java.util.function.*;

import javax.swing.*;
import javax.swing.event.*;

public class MDI extends Navbar {

    JInternalFrame frame;
    final HashMap<String, Supplier<JInternalFrame>> supplier = new HashMap<>();
    final JDesktopPane desktop = new JDesktopPane();
    final HashMap<String, JInternalFrame> frameTracker = new HashMap<>();
    final HashMap<JInternalFrame, String> frameKeyMap = new HashMap<>();
    final InternalFrameAdapter internalFrameListener = new InternalFrameAdapter() {
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
    int OPTION_PANE_COUNT = 0;

    public MDI() {
        setBackground(Color.white);
        setContentPane(desktop);
        setIconImage(icon.getImage());
        desktop.add(new Home(), JLayeredPane.DEFAULT_LAYER);
        setSupplier();
        setShortcuts();
        setVisible(true);
    }

    private void setShortcuts() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEvent -> {
            String key = KeyEvent.getKeyText(keyEvent.getKeyCode());
            String modifier = InputEvent.getModifiersExText(keyEvent.getModifiersEx());
            if (keyEvent.getID() == KeyEvent.KEY_PRESSED) {
                frame = desktop.getSelectedFrame();
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

                if ((key.equals("F5") || key.equals("F7")) && frame instanceof SaleBill && OPTION_PANE_COUNT == 0) {
                    SaleBill bill = (SaleBill) frame;
                    OPTION_PANE_COUNT = 1;
                    if (key.equals("F5"))
                        bill.getPrintOption().setSelectedItem("Yes");
                    else
                        bill.getPrintOption().setSelectedItem("No");
                    bill.getSaveBtn().doClick();
                    OPTION_PANE_COUNT = 0;
                    return true;
                }

                if (key.toLowerCase().equals("escape") && desktop.getAllFrames().length > 0) {
                    if (dialogBox.isVisible()) {
                        dialogBox.setVisible(false);
                        return true;
                    }
                    if (frame instanceof SaleBill && OPTION_PANE_COUNT == 0) {
                        OPTION_PANE_COUNT = 1;
                        boolean res = JOptionPane.showConfirmDialog(null, "Are you sure to exit?", "Confirmation",
                                JOptionPane.OK_CANCEL_OPTION) == 0;
                        if (res) {
                            frameTracker.remove(frameKeyMap.get(frame));
                            frame.dispose();
                            OPTION_PANE_COUNT = 0;
                        }
                        if (!res)
                            OPTION_PANE_COUNT = 0;
                    } else if (OPTION_PANE_COUNT == 0) {
                        frameTracker.remove(frameKeyMap.get(frame));
                        frame.dispose();
                    }
                    return true;
                }
            }
            return false;
        });
    }

    JMenuItem source = null;

    private void setSupplier() {
        source = sources.get(
                "Purchase Entry Ctrl+P");
        supplier.put("Purchase Entry Ctrl+P", () -> new PurchaseEntry(source, frameTracker, frameKeyMap, dialogBox));

        source = sources.get(
                "Purchase Return Ctrl+Alt+P");
        supplier.put(
                "Purchase Return Ctrl+Alt+P", () -> new PurchaseReturn(source, frameTracker, frameKeyMap, dialogBox));
        source = sources.get(
                "Product Alt+I");

        supplier.put("Product Alt+I", () -> new Product(source, frameTracker, frameKeyMap, dialogBox));

        source = sources.get("Group");
        supplier.put("Group", () -> new Group(source, frameTracker, frameKeyMap, dialogBox));

        source = sources.get("Sub Group");
        supplier.put("Sub Group", () -> new SubGroup(source, frameTracker, frameKeyMap, dialogBox));
        source = sources.get(
                "Sale Ctrl+S");
        supplier.put("Sale Ctrl+S", () -> new SaleBill(source, frameTracker, frameKeyMap, dialogBox));

        source = sources.get(
                "Sales Return Ctrl+Alt+S");
        supplier.put(
                "Sales Return Ctrl+Alt+S", () -> new SalesReturnEntry(source, frameTracker, frameKeyMap, dialogBox));

        source = sources.get(
                "Expiry Entry Ctrl+E");
        supplier.put("Expiry Entry Ctrl+E", () -> new ExpiryEntry(source, frameTracker, frameKeyMap, dialogBox));

        supplier.put("Near Expiry", () -> new NearExpiry());
        supplier.put("Purchase Daily", () -> new DailyPurchaseReport());
        supplier.put("Purchase Weekly", () -> new WeeklyPurchaseReport());
        supplier.put("Purchase Monthly", () -> new MonthlyPurchaseReport());
        supplier.put("Purchase Yearly", () -> new YearlyPurchaseReport());
        supplier.put("Sale Yearly", () -> new YearlySalesReport());
        supplier.put("Sale Daily", () -> new DailySalesReport());
        supplier.put("Sale Monthly", () -> new MonthlySalesReport());
        supplier.put("Sale Weekly", () -> new WeeklySalesReport());
        supplier.put("Stock available", () -> new AvailableStockReport());
        supplier.put("Stock required", () -> new RequiredStockReport());
        supplier.put("Stock less sold", () -> new LessSoldStockReport());
        supplier.put("Stock Yearly", () -> new YearlyStockReport());
        supplier.put("Expiry Daily", () -> new DailyExpiryReport());
        supplier.put("Expiry Yearly", () -> new YearlyExpiryReport());
        supplier.put("Expiry Monthly", () -> new MonthlyExpiryReport());
        supplier.put("Expiry Weekly", () -> new WeeklyExpiryReport());
        supplier.put("Sales Return Daily", () -> new DailySalesReturnReport());
        supplier.put("Sales Return Yearly", () -> new YearlySalesReturnReport());
        supplier.put("Sales Return Monthly", () -> new MonthlySalesReturnReport());
        supplier.put("Sales Return Weekly", () -> new WeeklySalesReturnReport());
        supplier.put("Purchase Return Monthly", () -> new MonthlyPurchaseReturnReport());
        supplier.put("Purchase Return Yearly", () -> new YearlyPurchaseReturnReport());
        supplier.put("Purchase Return Daily", () -> new DailyPurchaseReturnReport());
        supplier.put("Purchase Return Weekly", () -> new WeeklyPurchaseReturnReport());
        supplier.put("Quotation", () -> new Quotation(dialogBox));
        supplier.put("Debit Note", () -> new DebitNote());
        supplier.put("Credit Note", () -> new CreditNoteList());
        supplier.put("Change Password", () -> new ChangePassword());
        supplier.put("Stock Analysis Daily", () -> new DailyStockChart());
        supplier.put("Stock Analysis Monthly", () -> new MonthlyStockChart());
        supplier.put("Stock Analysis Yearly", () -> new YearlyStockChart());
        supplier.put("Stock Analysis Weekly", () -> new WeeklyStockChart());
        supplier.put("Sale Analysis Yearly", () -> new YearlySalesChart());
        supplier.put("Sale Analysis Daily", () -> new DailySalesChart());
        supplier.put("Sale Analysis Monthly", () -> new MonthlySalesChart());
        supplier.put("Sale Analysis Weekly", () -> new WeeklySalesChart());
        supplier.put("Expiry Analysis Weekly", () -> new WeeklyExpiryChart());
        supplier.put("Expiry Analysis Daily", () -> new DailyExpiryChart());
        supplier.put("Expiry Analysis Monthly", () -> new MonthlyExpiryChart());
        supplier.put("Expiry Analysis Yearly", () -> new YearlyExpiryChart());
        supplier.put("Purchase Analysis Monthly", () -> new MonthlyPurchaseChart());
        supplier.put("Purchase Analysis Daily", () -> new DailyPurchaseChart());
        supplier.put("Purchase Analysis Yearly", () -> new YearlyPurchaseChart());
        supplier.put("Purchase Analysis Weekly", () -> new WeeklyPurchaseChart());

    }

    public void actionPerformed(ActionEvent e) {
        String key = e.getActionCommand();
        frame = null;
        try {
            if (key.equals("BackUp And Exit")) {
                boolean res = JOptionPane.showConfirmDialog(this, "Are you sure to exit?",
                        "Confirmation",
                        JOptionPane.OK_CANCEL_OPTION) == 0;
                if (res) {
                    dialogBox.dispose();
                    dispose();
                }
                return;
            }

            frame = supplier.get(key).get();
            if (!frameTracker.containsKey(key) || frame instanceof SaleBill) {
                desktop.add(frame);
                frameTracker.put(key, frame);
                frameKeyMap.put(frame, key);
                frame.toFront();
                frame.addInternalFrameListener(internalFrameListener);
            } else {
                frame = frameTracker.get(key);
                frame.toFront();
            }
            frame.setSelected(true);
        } catch (Exception exc) {
            System.out.println(exc.getMessage() + " at MDI.java 422");
        }
    }
}
