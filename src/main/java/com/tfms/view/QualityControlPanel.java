package com.tfms.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class QualityControlPanel extends JPanel {

    private MainAppFrame app;

    private JTabbedPane tabbedPane;

    // Dashboard Labels
    private JLabel lblToday;
    private JLabel lblApproved;
    private JLabel lblRejected;
    private JLabel lblAverage;

    public QualityControlPanel(MainAppFrame app) {

        this.app = app;

        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //=========================
        // Header
        //=========================

        JPanel header = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Quality Control Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e ->
                app.showScreen(MainAppFrame.LOGIN_PANEL));

        header.add(title, BorderLayout.WEST);
        header.add(btnLogout, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        //=========================
        // Tabs
        //=========================

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Dashboard", createDashboardTab());
        tabbedPane.addTab("Leaf Inspection", createInspectionTab());
        tabbedPane.addTab("Batch Approval", createApprovalTab());
        tabbedPane.addTab("Reports", createReportTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    //===================================================
    // Dashboard
    //===================================================

    private JPanel createDashboardTab() {

        JPanel panel = new JPanel(new GridLayout(2,2,20,20));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        lblToday = new JLabel("Today's Inspections : 0");
        lblApproved = new JLabel("Approved : 0");
        lblRejected = new JLabel("Rejected : 0");
        lblAverage = new JLabel("Average Grade : -");

        panel.add(createCard(lblToday));
        panel.add(createCard(lblApproved));
        panel.add(createCard(lblRejected));
        panel.add(createCard(lblAverage));

        return panel;
    }

    private JPanel createCard(JLabel label) {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(25,25,25,25)));

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
        //===================================================
    // Leaf Inspection Tab
    //===================================================

    private JComboBox<String> cmbCollection;
    private JTextField txtSupplier;
    private JTextField txtWeight;
    private JComboBox<String> cmbGrade;
    private JTextField txtMoisture;
    private JTextField txtCoarseLeaf;
    private JTextArea txtRemarks;

    private JButton btnApprove;
    private JButton btnClear;

    private JTable inspectionTable;
    private DefaultTableModel inspectionModel;

    private JPanel createInspectionTab() {

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Leaf Inspection"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbCollection = new JComboBox<>();

        txtSupplier = new JTextField(20);
        txtSupplier.setEditable(false);

        txtWeight = new JTextField(20);
        txtWeight.setEditable(false);

        cmbGrade = new JComboBox<>(new String[]{
                "A",
                "B",
                "C"
        });

        txtMoisture = new JTextField(20);
        txtCoarseLeaf = new JTextField(20);

        txtRemarks = new JTextArea(3,20);

        gbc.gridx=0;
        gbc.gridy=0;
        form.add(new JLabel("Collection ID"),gbc);

        gbc.gridx=1;
        form.add(cmbCollection,gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        form.add(new JLabel("Supplier"),gbc);

        gbc.gridx=1;
        form.add(txtSupplier,gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        form.add(new JLabel("Weight (kg)"),gbc);

        gbc.gridx=1;
        form.add(txtWeight,gbc);

        gbc.gridx=0;
        gbc.gridy=3;
        form.add(new JLabel("Grade"),gbc);

        gbc.gridx=1;
        form.add(cmbGrade,gbc);

        gbc.gridx=0;
        gbc.gridy=4;
        form.add(new JLabel("Moisture %"),gbc);

        gbc.gridx=1;
        form.add(txtMoisture,gbc);

        gbc.gridx=0;
        gbc.gridy=5;
        form.add(new JLabel("Coarse Leaf %"),gbc);

        gbc.gridx=1;
        form.add(txtCoarseLeaf,gbc);

        gbc.gridx=0;
        gbc.gridy=6;
        form.add(new JLabel("Remarks"),gbc);

        gbc.gridx=1;
        form.add(new JScrollPane(txtRemarks),gbc);

        JPanel buttons = new JPanel();

        btnApprove = new JButton("Approve");
        btnClear = new JButton("Clear");

        buttons.add(btnApprove);
        buttons.add(btnClear);

        gbc.gridx=0;
        gbc.gridy=7;
        gbc.gridwidth=2;

        form.add(buttons,gbc);

        inspectionModel = new DefaultTableModel(
                new String[]{
                        "Inspection ID",
                        "Collection ID",
                        "Supplier",
                        "Grade",
                        "Moisture %",
                        "Coarse %",
                        "Status"
                },0);

        inspectionTable = new JTable(inspectionModel);
        inspectionTable.setRowHeight(25);

        panel.add(form,BorderLayout.NORTH);
        panel.add(new JScrollPane(inspectionTable),BorderLayout.CENTER);

        return panel;

    }

    //===================================================
    // Batch Approval Tab
    //===================================================

    private JTable approvalTable;
    private DefaultTableModel approvalModel;

    private JButton btnReject;

    private JPanel createApprovalTab(){

        JPanel panel = new JPanel(new BorderLayout(10,10));

        approvalModel = new DefaultTableModel(
                new String[]{
                        "Collection ID",
                        "Supplier",
                        "Weight",
                        "Grade",
                        "Status"
                },0);

        approvalTable = new JTable(approvalModel);

        JPanel bottom = new JPanel();

        btnReject = new JButton("Reject");

        bottom.add(btnReject);

        panel.add(new JScrollPane(approvalTable),BorderLayout.CENTER);
        panel.add(bottom,BorderLayout.SOUTH);

        return panel;

    }

    //===================================================
    // Report Tab
    //===================================================

    private JTable reportTable;
    private DefaultTableModel reportModel;

    private JPanel createReportTab(){

        JPanel panel = new JPanel(new BorderLayout());

        reportModel = new DefaultTableModel(
                new String[]{
                        "Date",
                        "Collection ID",
                        "Supplier",
                        "Grade",
                        "Inspector"
                },0);

        reportTable = new JTable(reportModel);

        panel.add(new JScrollPane(reportTable),BorderLayout.CENTER);

        return panel;

    }
        //===================================================
    // Dashboard Update
    //===================================================

    public void updateDashboard(int today,
                                int approved,
                                int rejected,
                                String averageGrade) {

        lblToday.setText("Today's Inspections : " + today);
        lblApproved.setText("Approved : " + approved);
        lblRejected.setText("Rejected : " + rejected);
        lblAverage.setText("Average Grade : " + averageGrade);

    }

    //===================================================
    // Buttons
    //===================================================

    public JButton getBtnApprove() {
        return btnApprove;
    }

    public JButton getBtnReject() {
        return btnReject;
    }

    public JButton getBtnClear() {
        return btnClear;
    }

    //===================================================
    // Form Getters
    //===================================================

    public JComboBox<String> getCmbCollection() {
        return cmbCollection;
    }

    public JTextField getTxtSupplier() {
        return txtSupplier;
    }

    public JTextField getTxtWeight() {
        return txtWeight;
    }

    public JComboBox<String> getCmbGrade() {
        return cmbGrade;
    }

    public JTextField getTxtMoisture() {
        return txtMoisture;
    }

    public JTextField getTxtCoarseLeaf() {
        return txtCoarseLeaf;
    }

    public JTextArea getTxtRemarks() {
        return txtRemarks;
    }

    //===================================================
    // Tables
    //===================================================

    public JTable getInspectionTable() {
        return inspectionTable;
    }

    public JTable getApprovalTable() {
        return approvalTable;
    }

    public JTable getReportTable() {
        return reportTable;
    }

    //===================================================
    // Table Models
    //===================================================

    public DefaultTableModel getInspectionModel() {
        return inspectionModel;
    }

    public DefaultTableModel getApprovalModel() {
        return approvalModel;
    }

    public DefaultTableModel getReportModel() {
        return reportModel;
    }

}