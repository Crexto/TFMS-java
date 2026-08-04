/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tfms.controller;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Time;
import java.sql.Date;

import com.tfms.model.entity.LeafCollection;
import com.tfms.model.entity.User;
import com.tfms.model.entity.Supplier;
import com.tfms.model.entity.Attendance;
import com.tfms.model.entity.Employee;
import com.tfms.model.entity.Machine;
import com.tfms.model.dao.ProductionDAO;
import com.tfms.model.dao.SupplierDAO;
import com.tfms.model.dao.InventoryDAO;
import com.tfms.model.dao.DowntimeDAO;
import com.tfms.model.dao.LeafCollectionDAO;
import com.tfms.model.dao.AttendanceDAO;
import com.tfms.model.dao.MachineDAO;
import com.tfms.model.dao.EmployeeDAO;
import com.tfms.util.UserSession;
import com.tfms.view.SupervisorPanel;
import com.tfms.view.MainAppFrame;


public class SupervisorController {
    private final SupervisorPanel superView;
    private final LeafCollectionDAO leafDAO;
    private final InventoryDAO invDAO;
    private final SupplierDAO supplierDAO;
    private final ProductionDAO productionDAO;
    private final DowntimeDAO downtimeDAO;
    private final AttendanceDAO attendanceDAO;
    private final MachineDAO machineDAO;    
    private final EmployeeDAO employeeDAO;
    private final MainAppFrame mainApp;
    
    
    public SupervisorController(SupervisorPanel superView, MainAppFrame mainApp){
        this.superView = superView;
        this.mainApp = mainApp;
        this.invDAO = new InventoryDAO();
        this.supplierDAO = new SupplierDAO();
        this.productionDAO = new ProductionDAO();        
        this.downtimeDAO = new DowntimeDAO();
        this.leafDAO = new LeafCollectionDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.machineDAO = new MachineDAO();
        this.employeeDAO = new EmployeeDAO();
        
        this.superView.AttendanceListener(e -> handleAttendance());
        this.superView.LeafCollectionListener(e -> handleLeafCollection());
        this.superView.MachineListener(e -> handleMachine());
        this.superView.addSaveDowntimeListener(e -> handleDowntime());
        this.superView.RefreshListener(e -> refreshAllTables());
        
        refreshAllTables();
       
    }
    
    private void handleLeafCollection() { 
        User user = UserSession.getLoggedInUser();
        Supplier selectedSupplier = superView.getSupplier();
        int weight = superView.getWeight();

        if (weight <= 0) {
            JOptionPane.showMessageDialog(superView, "Please enter a valid weight (greater than 0 kg).", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String confirmMessage = String.format(
            "Are you sure you want to submit this record?\n\nSupplier: %s\nGross Weight: %d kg",
            selectedSupplier.getName(), 
            weight
        );

        int confirmResult = JOptionPane.showConfirmDialog(
            superView,
            confirmMessage,
            "Confirm Leaf Collection",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmResult != JOptionPane.YES_OPTION) {
            return;
        }

        LeafCollection leaf = new LeafCollection(selectedSupplier.getSupplierId(), weight, user.getId());

        boolean success = leafDAO.leafCollect(leaf);

        if (success) {
            JOptionPane.showMessageDialog(superView, "Leaf collection record saved successfully!");
            refreshAllTables();
        } else {
            JOptionPane.showMessageDialog(superView, "Failed to save record to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    private void handleAttendance() { 
        User user = UserSession.getLoggedInUser();
        LocalDate date = LocalDate.now();
        
        boolean todayAtt = attendanceDAO.validateAttendance(date);
        
        if (todayAtt) {
            JOptionPane.showMessageDialog(superView, "Attendance for today has already been recorded.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String confirmMessage = String.format("Are you sure you want to submit this record?");

        int confirmResult = JOptionPane.showConfirmDialog(
            superView,
            confirmMessage,
            "Confirm Attendance Record",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmResult != JOptionPane.YES_OPTION) {
            return;
        }
        
        DefaultTableModel att = superView.getAttendanceTable();
        for (int viewRow = 0; viewRow < att.getRowCount(); viewRow++) {
  
            Object idObj = att.getValueAt(viewRow, 0);
            Object statusObj = att.getValueAt(viewRow, 3); 

            int empId = (int) idObj;
            
            String status = (String) statusObj;

            if ("-".equals(status)) {
                status = "Absent";
            }
            
            Attendance attendance = new Attendance(date, empId, status, user.getId());
            attendanceDAO.attendanceInsert(attendance);

        }
        refreshAllTables();
        JOptionPane.showMessageDialog(superView, "Attendance saved successfully!");
    }
    
    
    private void handleMachine() { 
        
        List<Object[]> machinesOld = machineDAO.getAllMachines();
        DefaultTableModel machinesNew = superView.getMachineTable();
        
        String confirmMessage = String.format("Are you sure you want to update this record?");

        int confirmResult = JOptionPane.showConfirmDialog(
            superView,
            confirmMessage,
            "Confirm Machine Management Record",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmResult != JOptionPane.YES_OPTION) {
            return;
        }

        int updatedCount = 0;

        for (int row = 0; row < machinesNew.getRowCount(); row++) {
            Employee newEmployee = (Employee) machinesNew.getValueAt(row, 3);
            Employee oldEmployee = (Employee) machinesOld.get(row)[3];
            
            if (newEmployee.getId() != oldEmployee.getId() || !machinesNew.getValueAt(row, 4).equals((String) machinesOld.get(row)[4])){
                boolean updated = machineDAO.updateMachine((int)machinesNew.getValueAt(row, 0), newEmployee.getId(),(String) machinesNew.getValueAt(row, 4));
                if (updated) {
                    updatedCount++;
                }
            }  
     
        }
        refreshAllTables();
        JOptionPane.showMessageDialog(superView, "Updated " + updatedCount + " machines successfully!");
     
    }
    
    private void handleDowntime() {
        
        Machine machine = superView.getSelectedMachine();
        Date date = new Date(superView.getDowntimeDate().getTime());
        Time startTime = new Time(superView.getStartTime().getTime());
        Time endTime = new Time(superView.getEndTime().getTime());
        String reason = superView.getReason();
        String remarks = superView.getRemarks();

        if (reason.isEmpty()) {
            JOptionPane.showMessageDialog(superView, "Please enter a reason for the downtime.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (endTime.before(startTime)) {
            JOptionPane.showMessageDialog(superView, "End time cannot be earlier than start time.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        boolean success = downtimeDAO.downtimeLog(machine.getId(), date, startTime, endTime, reason, remarks);
        machineDAO.setMachineDown(machine.getId());

        if (success) {
            JOptionPane.showMessageDialog(superView, "Downtime logged successfully!");
            refreshAllTables();

        } else {
            JOptionPane.showMessageDialog(superView, "Failed to log downtime.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return;
    }
    
    private void refreshAllTables() {

        superView.loadAttendanceData(employeeDAO.getAllEmployees());

        superView.loadAttendanceHistory(attendanceDAO.getAllAttendance());

        superView.loadMachineData(machineDAO.getAllMachines());

        superView.loadReceiptRecords(leafDAO.getAllReciepts());

        superView.setSuppliers(supplierDAO.getAll());
        
        superView.setEmployees(employeeDAO.getAllEmployees());
        
        superView.setMachines(machineDAO.getAllClassMachines());

        refreshDashboard();
    }
    
    private void refreshDashboard() {

        int leavesToday = leafDAO.getLeavesToday();
        double productionToday = productionDAO.getProductionToday();
        double totalStock = invDAO.getTotal();

        int pendingQC = 2;
        int rejectedBatches = 0;

        int runningMachines = machineDAO.getRunningMachines();
        int maintenanceMachines = machineDAO.getDownMachines();

        int workersPresent = attendanceDAO.getAllPresent();

        superView.updateDashboard(leavesToday, productionToday, totalStock, pendingQC, rejectedBatches, runningMachines, maintenanceMachines, workersPresent);
    }
}
