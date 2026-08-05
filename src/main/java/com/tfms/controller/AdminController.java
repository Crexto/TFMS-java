package com.tfms.controller;

import com.tfms.model.dao.*;
import com.tfms.model.entity.*;
import com.tfms.view.AdminPanel;
import java.sql.Date;
import com.tfms.view.MainAppFrame;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

public class AdminController {

    private final AdminPanel view;

    private final SupplierDAO supplierDAO;
    private final RouteDAO routeDAO;
    private final LeafPriceDAO leafPriceDAO;
    private final VehicleDAO vehicleDAO;
    private final UserDAO userDAO;
    private MainAppFrame mainApp;

    public AdminController(AdminPanel view, MainAppFrame mainApp){

        this.view = view;
        this.mainApp = mainApp;
        supplierDAO = new SupplierDAO();
        routeDAO = new RouteDAO();
        vehicleDAO = new VehicleDAO();
        userDAO = new UserDAO();
        leafPriceDAO = new LeafPriceDAO();

        initialize();

    }

    private void initialize(){

        loadDashboard();

        loadSuppliers();
        loadRoutes();
        loadVehicles();
        loadUsers();
        loadPriceHistory();
        registerRouteEvents();
        registerVehicleEvents();
        registerUserEvents();

        registerSupplierEvents();

    }


    private void loadDashboard(){

        view.updateDashboard(

                supplierDAO.count(),

                routeDAO.count(),

                vehicleDAO.count(),

                userDAO.count()

        );

    }


    private void registerSupplierEvents(){

        view.getBtnSupplierAdd().addActionListener(e->addSupplier());

        view.getBtnSupplierUpdate().addActionListener(e->updateSupplier());

        view.getBtnSupplierDelete().addActionListener(e->deleteSupplier());

    }


    private void loadSuppliers(){

        DefaultTableModel model = view.getSupplierModel();

        model.setRowCount(0);

        List<Supplier> list = supplierDAO.getAll();

        for(Supplier s : list){

            model.addRow(new Object[]{

                    s.getSupplierId(),

                    s.getName(),

                    s.getPhone(),

                    s.getAddress(),

                    s.getRoute()

            });

        }

    }


    private void addSupplier(){

        if(view.getTxtSupplierName().getText().trim().isEmpty()){

            JOptionPane.showMessageDialog(view,

                    "Supplier name is required.");

            return;

        }

        Supplier supplier = new Supplier();

        supplier.setName(

                view.getTxtSupplierName().getText()

        );

        supplier.setPhone(

                view.getTxtSupplierPhone().getText()

        );

        supplier.setAddress(

                view.getTxtSupplierAddress().getText()

        );

        supplier.setRoute(

                view.getCmbRoute().getSelectedItem().toString()

        );

        if(supplierDAO.insert(supplier)){

            JOptionPane.showMessageDialog(view,

                    "Supplier added.");

            loadSuppliers();

            loadDashboard();

        }

    }


    private void updateSupplier(){

        int row = view.getSupplierTable().getSelectedRow();

        if(row==-1){

            JOptionPane.showMessageDialog(view,

                    "Select a supplier.");

            return;

        }

        Supplier supplier = new Supplier();

        supplier.setSupplierId(

                Integer.parseInt(

                        view.getSupplierModel()

                                .getValueAt(row,0).toString()

                )

        );

        supplier.setName(

                view.getTxtSupplierName().getText()

        );

        supplier.setPhone(

                view.getTxtSupplierPhone().getText()

        );

        supplier.setAddress(

                view.getTxtSupplierAddress().getText()

        );

        supplier.setRoute(

                view.getCmbRoute().getSelectedItem().toString()

        );

        supplierDAO.update(supplier);

        loadSuppliers();

    }

 
    private void deleteSupplier(){

        int row = view.getSupplierTable().getSelectedRow();

        if(row==-1){

            return;

        }

        int option = JOptionPane.showConfirmDialog(

                view,

                "Delete supplier?",

                "Confirm",

                JOptionPane.YES_NO_OPTION

        );

        if(option!=JOptionPane.YES_OPTION)

            return;

        int id = Integer.parseInt(

                view.getSupplierModel()

                        .getValueAt(row,0).toString()

        );

        supplierDAO.delete(id);

        loadSuppliers();

        loadDashboard();

    }
        //==================================================
    // Route Events
    //==================================================

    private void registerRouteEvents() {

        view.getBtnRouteAdd().addActionListener(e -> addRoute());
        view.getBtnRouteUpdate().addActionListener(e -> updateRoute());
        view.getBtnRouteDelete().addActionListener(e -> deleteRoute());

    }

    //==================================================
    // Load Routes
    //==================================================

    private void loadRoutes() {

        DefaultTableModel model = view.getRouteModel();
        view.getCmbRoute().removeAllItems();

        model.setRowCount(0);

        List<Route> routes = routeDAO.getAll();

        for (Route r : routes) {

            model.addRow(new Object[]{
                    r.getRouteId(),
                    r.getRouteName()
            });

        }
        
        for (Route route : routes) {
            view.getCmbRoute().addItem(route.getRouteName());
        }

    }

    //==================================================
    // Add Route
    //==================================================

    private void addRoute() {

        if (view.getTxtRouteName().getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(view,
                    "Enter a route name.");
            return;
        }

        Route route = new Route();
        route.setRouteName(view.getTxtRouteName().getText());

        if (routeDAO.insert(route)) {

            JOptionPane.showMessageDialog(view,
                    "Route added successfully.");

            loadRoutes();
            loadDashboard();
        }

    }

    //==================================================
    // Update Route
    //==================================================

    private void updateRoute() {

        int row = view.getRouteTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view,
                    "Select a route.");
            return;
        }

        Route route = new Route();

        route.setRouteId(
                Integer.parseInt(
                        view.getRouteModel().getValueAt(row,0).toString()));

        route.setRouteName(
                view.getTxtRouteName().getText());

        routeDAO.update(route);

        loadRoutes();

    }

    //==================================================
    // Delete Route
    //==================================================

    private void deleteRoute() {

        int row = view.getRouteTable().getSelectedRow();

        if (row == -1)
            return;

        int id = Integer.parseInt(
                view.getRouteModel().getValueAt(row,0).toString());

        routeDAO.delete(id);

        loadRoutes();
        loadDashboard();

    }

    //==================================================
    // Vehicle Events
    //==================================================

    private void registerVehicleEvents() {

        view.getBtnVehicleAdd().addActionListener(e -> addVehicle());
        view.getBtnVehicleUpdate().addActionListener(e -> updateVehicle());
        view.getBtnVehicleDelete().addActionListener(e -> deleteVehicle());

    }

    //==================================================
    // Load Vehicles
    //==================================================

    private void loadVehicles() {

        DefaultTableModel model = view.getVehicleModel();
        model.setRowCount(0);

        List<Vehicle> vehicles = vehicleDAO.getAll();

        for (Vehicle v : vehicles) {

            model.addRow(new Object[]{
                    v.getVehicleId(),
                    v.getRegistrationNo(),
                    v.getDriverName(),
                    v.getCapacity()
            });

        }

    }

    //==================================================
    // Add Vehicle
    //==================================================

    private void addVehicle() {

        Vehicle vehicle = new Vehicle();

        vehicle.setRegistrationNo(
                view.getTxtVehicleNo().getText());

        vehicle.setDriverName(
                view.getTxtDriver().getText());

        vehicle.setCapacity(
                view.getTxtCapacity().getText());

        if (vehicleDAO.insert(vehicle)) {

            JOptionPane.showMessageDialog(view,
                    "Vehicle added.");

            loadVehicles();
            loadDashboard();

        }

    }

    //==================================================
    // Update Vehicle
    //==================================================

    private void updateVehicle() {

        int row = view.getVehicleTable().getSelectedRow();

        if (row == -1)
            return;

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleId(
                Integer.parseInt(
                        view.getVehicleModel().getValueAt(row,0).toString()));

        vehicle.setRegistrationNo(
                view.getTxtVehicleNo().getText());

        vehicle.setDriverName(
                view.getTxtDriver().getText());

        vehicle.setCapacity(
                view.getTxtCapacity().getText());

        vehicleDAO.update(vehicle);

        loadVehicles();

    }

    //==================================================
    // Delete Vehicle
    //==================================================

    private void deleteVehicle() {

        int row = view.getVehicleTable().getSelectedRow();

        if (row == -1)
            return;

        int id = Integer.parseInt(
                view.getVehicleModel().getValueAt(row,0).toString());

        vehicleDAO.delete(id);

        loadVehicles();
        loadDashboard();

    }

    //==================================================
    // Leaf Price
    //==================================================
    private void loadPriceHistory() {

        DefaultTableModel model = view.getPriceHistoryModel();
        model.setRowCount(0);

        List<LeafPrice> prices = leafPriceDAO.getAll();

        for (LeafPrice p : prices) {

            model.addRow(new Object[]{
                    p.getPriceId(),
                    p.getPrice(),
                    p.getStartDate(),
                    p.getEndDate()
            });
        }
    }

    private void updateLeafPrice() {

        try {

            double price = Double.parseDouble(view.getTxtLeafPrice().getText());

            java.util.Date utilDate = (java.util.Date) view.getEffectiveDate().getValue();

            java.sql.Date startDate = new java.sql.Date(utilDate.getTime());

            Date endDate = new Date(startDate.getTime() - (24L * 60 * 60 * 1000));

            leafPriceDAO.updateEndDate(endDate);

            LeafPrice leafPrice = new LeafPrice();
            leafPrice.setPrice(price);
            leafPrice.setStartDate(startDate);
            leafPrice.setEndDate(null);

            if (leafPriceDAO.insert(leafPrice)) {

                JOptionPane.showMessageDialog(view,
                        "Leaf price updated successfully.");

                view.getTxtLeafPrice().setText("");
                view.getEffectiveDate().setValue(new java.util.Date());

                loadPriceHistory();

            } else {

                JOptionPane.showMessageDialog(view,
                        "Failed to update leaf price.");

            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(view,
                    "Enter a valid price and date.\nDate format: yyyy-mm-dd");

        }
    }
    
    //==================================================
    // User Events
    //==================================================

    private void registerUserEvents() {

        view.getBtnAddUser().addActionListener(e -> addUser());
        view.getBtnUpdateUser().addActionListener(e -> updateUser());
        view.getBtnDeleteUser().addActionListener(e -> deleteUser());
        view.getBtnUpdatePrice().addActionListener(e -> updateLeafPrice());

    }

    //==================================================
    // Load Users
    //==================================================

    private void loadUsers() {

        DefaultTableModel model = view.getUserModel();
        model.setRowCount(0);

        List<User> users = userDAO.getAll();

        for (User u : users) {

            model.addRow(new Object[]{
                    u.getId(),
                    u.getUsername(),
                    u.getFullName(),
                    u.getRoleS(),
                    u.getStatus()
            });

        }

    }

    //==================================================
    // Add User
    //==================================================

    private void addUser() {

        if (view.getTxtUsername().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Username is required.");
            return;
        }

        User user = new User();

        user.setUsername(view.getTxtUsername().getText());
        user.setPassword(new String(view.getTxtPassword().getPassword()));
        user.setFullName(view.getTxtFullName().getText());
        user.setRole(view.getCmbRole().getSelectedItem().toString());

        if (userDAO.insert(user)) {

            JOptionPane.showMessageDialog(view, "User created successfully.");

            loadUsers();
            loadDashboard();
            clearUserFields();

        }

    }

    //==================================================
    // Update User
    //==================================================

    private void updateUser() {

        int row = view.getUserTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Select a user.");
            return;
        }

        User user = new User();

        user.setUserId(
                Integer.parseInt(
                        view.getUserModel().getValueAt(row, 0).toString()));

        user.setUsername(view.getTxtUsername().getText());
        user.setPassword(new String(view.getTxtPassword().getPassword()));
        user.setFullName(view.getTxtFullName().getText());
        user.setRole(view.getCmbRole().getSelectedItem().toString());

        userDAO.update(user);

        JOptionPane.showMessageDialog(view, "User updated.");

        loadUsers();

    }

    //==================================================
    // Delete User
    //==================================================

    private void deleteUser() {

        int row = view.getUserTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Select a user.");
            return;
        }

        int option = JOptionPane.showConfirmDialog(
                view,
                "Delete this user?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);

        if (option != JOptionPane.YES_OPTION)
            return;

        int id = Integer.parseInt(
                view.getUserModel().getValueAt(row, 0).toString());

        userDAO.delete(id);

        JOptionPane.showMessageDialog(view, "User deleted.");

        loadUsers();
        loadDashboard();

    }

    //==================================================
    // Helpers
    //==================================================

    private void clearUserFields() {

        view.getTxtUsername().setText("");
        view.getTxtPassword().setText("");
        view.getTxtFullName().setText("");
        view.getCmbRole().setSelectedIndex(0);

    }

}