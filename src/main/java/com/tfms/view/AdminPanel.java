package com.tfms.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdminPanel extends JPanel {

    private MainAppFrame app;

    private JTabbedPane tabbedPane;

    // Dashboard Labels
    private JLabel lblSuppliers;
    private JLabel lblRoutes;
    private JLabel lblVehicles;
    private JLabel lblUsers;
    
    private JButton btnSupplierAdd;
    private JButton btnSupplierUpdate;
    private JButton btnSupplierDelete;

    private JButton btnRouteAdd;
    private JButton btnRouteUpdate;
    private JButton btnRouteDelete;

    private JButton btnVehicleAdd;
    private JButton btnVehicleUpdate;
    private JButton btnVehicleDelete;

    private JButton btnAddUser;
    private JButton btnUpdateUser;
    private JButton btnDeleteUser;

    private JButton btnUpdatePrice;

    public AdminPanel(MainAppFrame app) {

        this.app = app;

        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        //=========================
        // Header
        //=========================

        JPanel header = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Factory Administrator Dashboard");
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
        tabbedPane.addTab("Suppliers", createSupplierTab());
        tabbedPane.addTab("Routes", createRouteTab());
        tabbedPane.addTab("Vehicles", createVehicleTab());
        tabbedPane.addTab("Leaf Prices", createLeafPriceTab());
        tabbedPane.addTab("Users", createUserTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    //===================================================
    // Dashboard
    //===================================================

    private JPanel createDashboardTab(){

        JPanel panel = new JPanel(new GridLayout(2,2,20,20));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        lblSuppliers = new JLabel("Registered Suppliers : 0");
        lblRoutes = new JLabel("Registered Routes : 0");
        lblVehicles = new JLabel("Registered Vehicles : 0");
        lblUsers = new JLabel("System Users : 0");

        panel.add(createCard(lblSuppliers));
        panel.add(createCard(lblRoutes));
        panel.add(createCard(lblVehicles));
        panel.add(createCard(lblUsers));

        return panel;
    }

    private JPanel createCard(JLabel label){

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(25,25,25,25)
        ));

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("SansSerif",Font.BOLD,18));

        panel.add(label, BorderLayout.CENTER);

        return panel;
    }
        //===================================================
    // Supplier Tab
    //===================================================

    private JTextField txtSupplierName;
    private JTextField txtSupplierPhone;
    private JTextField txtSupplierAddress;
    private JComboBox<String> cmbRoute;

    private JTable supplierTable;
    private DefaultTableModel supplierModel;

    private JPanel createSupplierTab() {

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Supplier Management"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtSupplierName = new JTextField(20);
        txtSupplierPhone = new JTextField(20);
        txtSupplierAddress = new JTextField(20);

        cmbRoute = new JComboBox<>();

        gbc.gridx=0; gbc.gridy=0;
        form.add(new JLabel("Supplier Name"),gbc);

        gbc.gridx=1;
        form.add(txtSupplierName,gbc);

        gbc.gridx=0; gbc.gridy=1;
        form.add(new JLabel("Phone"),gbc);

        gbc.gridx=1;
        form.add(txtSupplierPhone,gbc);

        gbc.gridx=0; gbc.gridy=2;
        form.add(new JLabel("Address"),gbc);

        gbc.gridx=1;
        form.add(txtSupplierAddress,gbc);

        gbc.gridx=0; gbc.gridy=3;
        form.add(new JLabel("Route"),gbc);

        gbc.gridx=1;
        form.add(cmbRoute,gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnSupplierAdd = new JButton("Add");
        btnSupplierUpdate = new JButton("Update");
        btnSupplierDelete = new JButton("Delete");
        JButton btnSupplierClear = new JButton("Clear");

        buttons.add(btnSupplierAdd);
        buttons.add(btnSupplierUpdate);
        buttons.add(btnSupplierDelete);
        buttons.add(btnSupplierClear);

        gbc.gridx=0;
        gbc.gridy=4;
        gbc.gridwidth=2;
        form.add(buttons,gbc);

        supplierModel = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Supplier",
                        "Phone",
                        "Address",
                        "Route"
                },0);

        supplierTable = new JTable(supplierModel);

        panel.add(form,BorderLayout.NORTH);
        panel.add(new JScrollPane(supplierTable),BorderLayout.CENTER);

        return panel;

    }

    //===================================================
    // Route Tab
    //===================================================

    private JTextField txtRouteName;

    private JTable routeTable;
    private DefaultTableModel routeModel;

    private JPanel createRouteTab(){

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtRouteName = new JTextField(25);

        form.add(new JLabel("Route Name"));
        form.add(txtRouteName);

        btnRouteAdd = new JButton("Add");
        btnRouteUpdate = new JButton("Update");
        btnRouteDelete = new JButton("Delete");

        form.add(btnRouteAdd);
        form.add(btnRouteUpdate);
        form.add(btnRouteDelete);

        routeModel = new DefaultTableModel(
                new String[]{
                        "Route ID",
                        "Route Name"
                },0);

        routeTable = new JTable(routeModel);

        panel.add(form,BorderLayout.NORTH);
        panel.add(new JScrollPane(routeTable),BorderLayout.CENTER);

        return panel;

    }


    private JTextField txtVehicleNo;
    private JTextField txtDriver;
    private JTextField txtCapacity;

    private JTable vehicleTable;
    private DefaultTableModel vehicleModel;

    private JPanel createVehicleTab(){

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());

        form.setBorder(BorderFactory.createTitledBorder("Vehicle Management"));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets=new Insets(5,5,5,5);
        gbc.fill=GridBagConstraints.HORIZONTAL;

        txtVehicleNo=new JTextField(20);
        txtDriver=new JTextField(20);
        txtCapacity=new JTextField(20);

        gbc.gridx=0;
        gbc.gridy=0;
        form.add(new JLabel("Vehicle No"),gbc);

        gbc.gridx=1;
        form.add(txtVehicleNo,gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        form.add(new JLabel("Driver"),gbc);

        gbc.gridx=1;
        form.add(txtDriver,gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        form.add(new JLabel("Capacity"),gbc);

        gbc.gridx=1;
        form.add(txtCapacity,gbc);

        JPanel buttons=new JPanel();
        
        btnVehicleAdd = new JButton("Add");
        btnVehicleUpdate = new JButton("Update");
        btnVehicleDelete = new JButton("Delete");

        buttons.add(btnVehicleAdd);
        buttons.add(btnVehicleUpdate);
        buttons.add(btnVehicleDelete);

        gbc.gridx=0;
        gbc.gridy=3;
        gbc.gridwidth=2;

        form.add(buttons,gbc);

        vehicleModel=new DefaultTableModel(
                new String[]{
                        "Vehicle ID",
                        "Registration",
                        "Driver",
                        "Capacity"
                },0);

        vehicleTable=new JTable(vehicleModel);

        panel.add(form,BorderLayout.NORTH);
        panel.add(new JScrollPane(vehicleTable),BorderLayout.CENTER);

        return panel;

    }
    
    //===================================================
    // Leaf Price Tab
    //===================================================

    private JTextField txtLeafPrice;
    private JSpinner spnStartDate;

    private JTable priceHistoryTable;
    private DefaultTableModel priceHistoryModel;

    private JPanel createLeafPriceTab() {

        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createTitledBorder("Leaf Price Management"));

        // =========================
        // Form
        // =========================
        JPanel form = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtLeafPrice = new JTextField(15);
        spnStartDate = new JSpinner(new SpinnerDateModel());

        JSpinner.DateEditor editor = new JSpinner.DateEditor(spnStartDate, "yyyy-MM-dd");
        spnStartDate.setEditor(editor);

        btnUpdatePrice = new JButton("Update Price");

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Price Per Kg"), gbc);

        gbc.gridx = 1;
        form.add(txtLeafPrice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Effective Date"), gbc);

        gbc.gridx = 1;
        form.add(spnStartDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        form.add(btnUpdatePrice, gbc);

        panel.add(form, BorderLayout.NORTH);

        // =========================
        // Price History Table
        // =========================
        priceHistoryModel = new DefaultTableModel(
                new String[]{
                        "ID",
                        "Price (LKR)",
                        "Start Date",
                        "End Date"
                }, 0);

        priceHistoryTable = new JTable(priceHistoryModel);
        priceHistoryTable.setRowHeight(25);

        panel.add(new JScrollPane(priceHistoryTable), BorderLayout.CENTER);

        return panel;
    }

    //===================================================
    // User Tab
    //===================================================

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtFullName;

    private JComboBox<String> cmbRole;

    private JTable userTable;
    private DefaultTableModel userModel;

    private JPanel createUserTab(){

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("User Management"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtFullName = new JTextField(20);

        cmbRole = new JComboBox<>(new String[]{
                "ADMIN",
                "MANAGER",
                "SUPERVISOR",
                "QUALITY_CONTROL"
        });

        gbc.gridx=0;
        gbc.gridy=0;
        form.add(new JLabel("Username"), gbc);

        gbc.gridx=1;
        form.add(txtUsername, gbc);

        gbc.gridx=0;
        gbc.gridy=1;
        form.add(new JLabel("Password"), gbc);

        gbc.gridx=1;
        form.add(txtPassword, gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        form.add(new JLabel("Full Name"), gbc);

        gbc.gridx=1;
        form.add(txtFullName, gbc);

        gbc.gridx=0;
        gbc.gridy=3;
        form.add(new JLabel("Role"), gbc);

        gbc.gridx=1;
        form.add(cmbRole, gbc);

        JPanel buttons = new JPanel();

        btnAddUser = new JButton("Add");
        btnUpdateUser = new JButton("Update");
        btnDeleteUser = new JButton("Delete");

        buttons.add(btnAddUser);
        buttons.add(btnUpdateUser);
        buttons.add(btnDeleteUser);

        gbc.gridx=0;
        gbc.gridy=4;
        gbc.gridwidth=2;
        form.add(buttons, gbc);

        userModel = new DefaultTableModel(
                new String[]{
                        "User ID",
                        "Username",
                        "Full Name",
                        "Role",
                        "Status"
                },0);

        userTable = new JTable(userModel);
        userTable.setRowHeight(25);

        panel.add(form, BorderLayout.NORTH);
        panel.add(new JScrollPane(userTable), BorderLayout.CENTER);

        return panel;
    }

    //===================================================
    // Dashboard Update
    //===================================================

    public void updateDashboard(int suppliers,
                                int routes,
                                int vehicles,
                                int users){

        lblSuppliers.setText("Registered Suppliers : " + suppliers);
        lblRoutes.setText("Registered Routes : " + routes);
        lblVehicles.setText("Registered Vehicles : " + vehicles);
        lblUsers.setText("System Users : " + users);
    }

    //===================================================
    // Getters
    //===================================================
    public JTextField getTxtLeafPrice() {
        return txtLeafPrice;
    }

    public JSpinner getEffectiveDate() {
        return spnStartDate;
    }

    public JButton getBtnUpdatePrice() {
        return btnUpdatePrice;
    }

    public DefaultTableModel getPriceHistoryModel() {
        return priceHistoryModel;
    }

    public JButton getBtnSupplierAdd() {
        return btnSupplierAdd;
    }

    public JButton getBtnSupplierUpdate() {
        return btnSupplierUpdate;
    }

    public JButton getBtnSupplierDelete() {
        return btnSupplierDelete;
    }

    public JButton getBtnRouteAdd() {
        return btnRouteAdd;
    }

    public JButton getBtnRouteUpdate() {
        return btnRouteUpdate;
    }

    public JButton getBtnRouteDelete() {
        return btnRouteDelete;
    }

    public JButton getBtnVehicleAdd() {
        return btnVehicleAdd;
    }

    public JButton getBtnVehicleUpdate() {
        return btnVehicleUpdate;
    }

    public JButton getBtnVehicleDelete() {
        return btnVehicleDelete;
    }

    public JButton getBtnAddUser() {
        return btnAddUser;
    }

    public JButton getBtnUpdateUser() {
        return btnUpdateUser;
    }

    public JButton getBtnDeleteUser() {
        return btnDeleteUser;
    }


    public JTable getSupplierTable(){ return supplierTable; }
    public JTable getRouteTable(){ return routeTable; }
    public JTable getVehicleTable(){ return vehicleTable; }
    public JTable getUserTable(){ return userTable; }

    public DefaultTableModel getSupplierModel(){ return supplierModel; }
    public DefaultTableModel getRouteModel(){ return routeModel; }
    public DefaultTableModel getVehicleModel(){ return vehicleModel; }
    public DefaultTableModel getUserModel(){ return userModel; }

    public JTextField getTxtSupplierName(){ return txtSupplierName; }
    public JTextField getTxtSupplierPhone(){ return txtSupplierPhone; }
    public JTextField getTxtSupplierAddress(){ return txtSupplierAddress; }

    public JComboBox<String> getCmbRoute(){ return cmbRoute; }

    public JTextField getTxtRouteName(){ return txtRouteName; }

    public JTextField getTxtVehicleNo(){ return txtVehicleNo; }
    public JTextField getTxtDriver(){ return txtDriver; }
    public JTextField getTxtCapacity(){ return txtCapacity; }

    public JTextField getTxtUsername(){ return txtUsername; }
    public JPasswordField getTxtPassword(){ return txtPassword; }
    public JTextField getTxtFullName(){ return txtFullName; }
    public JComboBox<String> getCmbRole(){ return cmbRole; }

}