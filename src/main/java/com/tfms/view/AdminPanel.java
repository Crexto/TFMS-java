package com.tfms.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AdminPanel extends JPanel {
    public AdminPanel(MainAppFrame app) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Factory Administrator Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> app.showScreen(MainAppFrame.LOGIN_PANEL));
        header.add(title, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Supplier & Route Registration"));

        JTextField nameTxt = new JTextField();
        JTextField routeTxt = new JTextField();
        JTextField truckTxt = new JTextField();
        JTextField priceTxt = new JTextField();
        JButton saveBtn = new JButton("Save Supplier");

        formPanel.add(new JLabel("Supplier Name:")); formPanel.add(nameTxt);
        formPanel.add(new JLabel("Route Name:")); formPanel.add(routeTxt);
        formPanel.add(new JLabel("Assigned Truck:")); formPanel.add(truckTxt);
        formPanel.add(new JLabel("Base Price (LKR):")); formPanel.add(priceTxt);
        formPanel.add(new JLabel()); formPanel.add(saveBtn);

        String[] cols = {"ID", "Supplier Name", "Route", "Vehicle", "Base Price (LKR)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        model.addRow(new Object[]{"SUP-001", "High Country Tea Co.", "Route 04", "WP NC-4521", "250.00"});
        JTable table = new JTable(model);

        saveBtn.addActionListener(e -> {
            if (!nameTxt.getText().trim().isEmpty()) {
                model.addRow(new Object[]{"SUP-00" + (model.getRowCount() + 1), nameTxt.getText(), routeTxt.getText(), truckTxt.getText(), priceTxt.getText()});
                nameTxt.setText(""); routeTxt.setText(""); truckTxt.setText(""); priceTxt.setText("");
            }
        });

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(formPanel, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }
}