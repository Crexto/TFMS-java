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
import java.util.EventObject;
import java.util.List;

import com.tfms.model.entity.Supplier;
import com.tfms.model.entity.Employee;
import com.tfms.model.entity.Machine;

public class SupervisorPanel extends JPanel {

    private final DefaultTableModel attendanceTableModel;   
    private final DefaultTableModel attendanceHTableModel;
    private final DefaultTableModel machineManageTableModel;
    private final DefaultTableModel receiptsTableModel;
     
    private JLabel todayLeavesLabel;
    private JLabel todayProductionLabel;
    private JLabel finalStockLabel;
    private JLabel pendingQCLabel;
    private JLabel rejectedBatchLabel;
    private JLabel runningMachineLabel;
    private JLabel maintenanceMachineLabel;
    private JLabel workersPresentLabel;
    
    private JButton receiptBtn;
    private JButton refreshBtn;
    private JButton attendanceBtn;
    private JButton machineSaveBtn;
    private JFormattedTextField receiptWeight;
    private JComboBox<Supplier> suppCombo;
    private JComboBox<Machine> machineCombo;
    private JComboBox<Employee> employeeComboBox;
    private JSpinner startTime;
    private JSpinner endTime;
    private JTextField reason;
    private JTextField remarks;
    private JButton saveDown;
    private JSpinner dateSpinnerFilter;

    public SupervisorPanel(MainAppFrame app) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Supervisor Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshBtn = new JButton("Refresh All"); 
        JButton logoutBtn = new JButton("Logout");
        
        logoutBtn.addActionListener(e -> app.showScreen(MainAppFrame.LOGIN_PANEL));

        rightHeader.add(refreshBtn);
        rightHeader.add(logoutBtn);

        header.add(title, BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);       

        add(header, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 13));

        attendanceTableModel = new DefaultTableModel(new String[]{"Worker ID", "Worker Name", "Role", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; 
            }
        };
        machineManageTableModel = new DefaultTableModel(new String[]{"Machine ID", "Machine Name", "Machine Type", "Assigned Worker", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 || column == 4; 
            }
        };
        attendanceHTableModel = new DefaultTableModel(new String[]{"Date", "Attendance ID", "Worker Name", "Role", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        receiptsTableModel = new DefaultTableModel(new String[]{"Date", "Receipt No.", "Supplier ID", "Supplier Name", "Weight (kg)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tabbedPane.addTab("Dashboard", createDashboardTab());    
        tabbedPane.addTab("Green Leaf Collection", createLeafCollectionTab());
        tabbedPane.addTab("Mark Attendance", createAttendanceTab());
        tabbedPane.addTab("Attendance Records", createAttendanceRecordsTab());
        tabbedPane.addTab("Machine Management", createMachineManagementTab());
        tabbedPane.addTab("Log Downtime", createLogDowntimeTab());        
        tabbedPane.addTab("Receipts", createReceiptRecordsTab());

        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createDashboardTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(8, 1, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Dashboard"),
            BorderFactory.createEmptyBorder(10, 15, 10, 10) 
        ));
        
        todayLeavesLabel = new JLabel();
        todayProductionLabel = new JLabel();
        finalStockLabel = new JLabel();
        pendingQCLabel = new JLabel();
        rejectedBatchLabel = new JLabel();
        runningMachineLabel = new JLabel();
        maintenanceMachineLabel = new JLabel();
        workersPresentLabel = new JLabel();

        formCard.add(todayLeavesLabel);
        formCard.add(todayProductionLabel);
        formCard.add(finalStockLabel);
        formCard.add(pendingQCLabel);
        formCard.add(rejectedBatchLabel);
        formCard.add(runningMachineLabel);
        formCard.add(maintenanceMachineLabel);
        formCard.add(workersPresentLabel); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(formCard, gbc);

        return panel;
    }

    private JPanel createLeafCollectionTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel formCard = new JPanel(new GridLayout(4, 2, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Green Leaf Collection"),
            BorderFactory.createEmptyBorder(10, 15, 10, 10) 
        ));
        
        receiptWeight = createNumberFormattedField();
        
        suppCombo = new JComboBox<>();
        receiptBtn = new JButton("Print Receipt");
        
        formCard.add(new JLabel("Supplier:"));
        formCard.add(suppCombo);

        formCard.add(new JLabel("Gross weight (kg):"));
        formCard.add(receiptWeight);

        formCard.add(new JLabel());
        formCard.add(receiptBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(formCard, gbc);

        return panel;
    }

    private JPanel createAttendanceTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Daily Worker Attendance"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JTextField searchField = new JTextField(10);
        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(filterBtn);

        JTable attendanceTable = new JTable(attendanceTableModel);
        attendanceTable.setRowHeight(28);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(attendanceTableModel);
        attendanceTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> applyFilter(sorter, searchField.getText(), null, false, null));

//        refreshBtn.addActionListener(e -> {
//            searchField.setText("");
//            sorter.setRowFilter(null);
//        });

        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Present", "Absent"});
        
        DefaultCellEditor singleClickEditor = new DefaultCellEditor(statusComboBox) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return true;
            }
        };
        
        attendanceTable.getColumnModel().getColumn(3).setCellEditor(singleClickEditor);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        attendanceBtn = new JButton("Save Attendance");
        
        bottomPanel.add(attendanceBtn);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createAttendanceRecordsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Attendance History"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JTextField searchField = new JTextField(10);
        JComboBox<String> attendanceComboFilter = new JComboBox<>(new String[]{"All", "Present", "Absent"});

        JCheckBox useDateFilter = new JCheckBox("Filter Date:");
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner attendanceDateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(attendanceDateSpinner, "yyyy-MM-dd");
        attendanceDateSpinner.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);
        attendanceDateSpinner.setEnabled(false);

        useDateFilter.addActionListener(e -> attendanceDateSpinner.setEnabled(useDateFilter.isSelected()));

        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(attendanceComboFilter);
        filterPanel.add(useDateFilter);
        filterPanel.add(attendanceDateSpinner);
        filterPanel.add(filterBtn);

        JTable historyTable = new JTable(attendanceHTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(attendanceHTableModel);
        historyTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> applyFilter(
            sorter, 
            searchField.getText(), 
            (String) attendanceComboFilter.getSelectedItem(), 
            useDateFilter.isSelected(), 
            attendanceDateSpinner.getValue()
        ));
//
//        refreshBtn.addActionListener(e -> {
//            searchField.setText("");
//            attendanceComboFilter.setSelectedIndex(0);
//            useDateFilter.setSelected(false);
//            attendanceDateSpinner.setEnabled(false);
//            sorter.setRowFilter(null);
//        });

        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMachineManagementTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Machine Management"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JTextField searchField = new JTextField(10);
        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(filterBtn);

        JTable machineTable = new JTable(machineManageTableModel);
        machineTable.setRowHeight(28);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(machineManageTableModel);
        machineTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> applyFilter(sorter, searchField.getText(), null, false, null));


        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Running", "Under Maintenance", "Idle"});
        employeeComboBox = new JComboBox<>();
        
        DefaultCellEditor statusEditor = new DefaultCellEditor(statusComboBox) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return true;
            }
        };
        DefaultCellEditor employeeEditor = new DefaultCellEditor(employeeComboBox) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return true;
            }
        };
        
        machineTable.getColumnModel().getColumn(4).setCellEditor(statusEditor);
        machineTable.getColumnModel().getColumn(3).setCellEditor(employeeEditor);

        JScrollPane scrollPane = new JScrollPane(machineTable);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        machineSaveBtn = new JButton("Save");
        
        bottomPanel.add(machineSaveBtn);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createLogDowntimeTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(7, 2, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Log Downtime"),
            BorderFactory.createEmptyBorder(15, 20, 15, 20) 
        ));

        SpinnerDateModel startModel = new SpinnerDateModel();
        startTime = new JSpinner(startModel);
        JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(startTime, "HH:mm");
        startTime.setEditor(timeEditor1);

        SpinnerDateModel endModel = new SpinnerDateModel();
        endTime = new JSpinner(endModel);
        JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(endTime, "HH:mm");
        endTime.setEditor(timeEditor2);
        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinnerFilter = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerFilter, "yyyy-MM-dd");
        dateSpinnerFilter.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);

        machineCombo = new JComboBox<>();
        reason = new JTextField(10);
        remarks = new JTextField(10);
        saveDown = new JButton("Save Downtime");

        formCard.add(new JLabel("Machine:"));
        formCard.add(machineCombo);
        
        formCard.add(new JLabel("Date:"));
        formCard.add(dateSpinnerFilter);

        formCard.add(new JLabel("Start time:"));
        formCard.add(startTime);

        formCard.add(new JLabel("End time:"));
        formCard.add(endTime);

        formCard.add(new JLabel("Reason:"));
        formCard.add(reason);

        formCard.add(new JLabel("Remarks:"));
        formCard.add(remarks);

        formCard.add(new JLabel()); 
        formCard.add(saveDown);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(formCard, gbc);

        return panel;
    }

    private JPanel createReceiptRecordsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Receipt Records"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField searchField = new JTextField(10);

        JCheckBox useDateFilter = new JCheckBox("Filter Date:");
        JSpinner receiptDateSpinner = new JSpinner(new SpinnerDateModel());
        receiptDateSpinner.setEditor(new JSpinner.DateEditor(receiptDateSpinner, "yyyy-MM-dd"));
        receiptDateSpinner.setEnabled(false);

        useDateFilter.addActionListener(e -> receiptDateSpinner.setEnabled(useDateFilter.isSelected()));

        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(useDateFilter);
        filterPanel.add(receiptDateSpinner);
        filterPanel.add(filterBtn);

        JTable historyTable = new JTable(receiptsTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(receiptsTableModel);
        historyTable.setRowSorter(sorter);
        
        filterBtn.addActionListener(e -> applyFilter(sorter, searchField.getText(), null, useDateFilter.isSelected(), receiptDateSpinner.getValue()));

        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }
    
    private JFormattedTextField createNumberFormattedField() {
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);

        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0);
        formatter.setMaximum(1000000);

        JFormattedTextField field = new JFormattedTextField(formatter);
        field.setColumns(10);
        field.setValue(0);
        return field;
    }
    

    private void applyFilter(TableRowSorter<DefaultTableModel> sorter, String searchTxt, String categoryOption, boolean useDate, Object dateVal) {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (searchTxt != null && !searchTxt.trim().isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + searchTxt.trim()));
        }

        if (categoryOption != null && !categoryOption.equalsIgnoreCase("All") && !categoryOption.equalsIgnoreCase("All Grades")) {
            filters.add(RowFilter.regexFilter("(?i)^" + categoryOption + "$"));
        }

        if (useDate && dateVal != null) {
            SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFmt.format(dateVal);
            if (!formattedDate.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + formattedDate));
            }
        }

        if (filters.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.andFilter(filters));
        }
    }

    public void updateDashboard(int leavesToday, double productionToday, double totalStock, int pendingQC, int rejectedBatches, int runningMachines, int maintenanceMachines, int workersPresent) {
        todayLeavesLabel.setText("Today's Green Leaf Collection : " + String.format("%,d", leavesToday) + " kg");
        todayProductionLabel.setText("Today's Production : " + productionToday + " kg");
        finalStockLabel.setText("Final Tea Stock : " + totalStock + " kg");
        pendingQCLabel.setText("Pending QC Batches : " + pendingQC);
        rejectedBatchLabel.setText("Rejected Batches : " + rejectedBatches);
        runningMachineLabel.setText("Machines Running : " + runningMachines);
        maintenanceMachineLabel.setText("Machines Under Maintenance : " + maintenanceMachines);
        workersPresentLabel.setText("Workers Present : " + workersPresent);
    }

    public void setSuppliers(List<Supplier> suppliers) {
        suppCombo.removeAllItems();
        if (suppliers != null) {
            for (Supplier s : suppliers) {
                suppCombo.addItem(s);
            }
        }
    }

    public void setEmployees(List<Employee> employees) {
        employeeComboBox.removeAllItems();
        if (employees != null) {
            for (Employee e : employees) {
                employeeComboBox.addItem(e);
            }
        }
    }

    public void setMachines(List<Machine> machines) {
        machineCombo.removeAllItems();
        if (machines != null) {
            for (Machine m : machines) {
                machineCombo.addItem(m);
            }
        }
    }

    public void loadAttendanceData(List<Employee> employees) {
        attendanceTableModel.setRowCount(0);
        if (employees != null) {
            for (Employee e : employees) {
                attendanceTableModel.addRow(new Object[]{e.getId(), e.getName(), e.getPosition(), "-"});
            }
        }
    }

    public void loadAttendanceHistory(List<Object[]> attendanceList) {
        attendanceHTableModel.setRowCount(0);
        if (attendanceList != null) {
            for (Object[] row : attendanceList) {
                attendanceHTableModel.addRow(row);
            }
        }
    }

    public void loadMachineData(List<Object[]> machines) {
        machineManageTableModel.setRowCount(0);
        if (machines != null) {
            for (Object[] row : machines) {
                machineManageTableModel.addRow(row);
            }
        }
    }

    public void loadReceiptRecords(List<Object[]> receipts) {
        receiptsTableModel.setRowCount(0);
        if (receipts != null) {
            for (Object[] row : receipts) {
                receiptsTableModel.addRow(row);
            }
        }
    }

    public void clearDowntimeForm() {
        reason.setText("");
        remarks.setText("");
    }

    public void clearReceiptForm() {
        receiptWeight.setValue(0);
    }

    public void RefreshListener(ActionListener listener) {
        refreshBtn.addActionListener(listener);
        
    }
    
    public void LeafCollectionListener(ActionListener listener) {
        receiptBtn.addActionListener(listener);
    }

    public void AttendanceListener(ActionListener listener) {
        attendanceBtn.addActionListener(listener);
    }

    public void MachineListener(ActionListener listener) {
        machineSaveBtn.addActionListener(listener);
    }

    public void addSaveDowntimeListener(ActionListener listener) {
        saveDown.addActionListener(listener);
    }

    public Supplier getSupplier() {
        return (Supplier) suppCombo.getSelectedItem();
    }

    public int getWeight() {
        Object val = receiptWeight.getValue();
        return (val != null) ? ((Number) val).intValue() : 0;
    }

    public DefaultTableModel getAttendanceTable() {
        return attendanceTableModel;
    }

    public DefaultTableModel getMachineTable() {
        return machineManageTableModel;
    }

    public Machine getSelectedMachine() {
        return (Machine) machineCombo.getSelectedItem();
    }

    public java.util.Date getDowntimeDate() {
        return (java.util.Date) dateSpinnerFilter.getValue();
    }

    public java.util.Date getStartTime() {
        return (java.util.Date) startTime.getValue();
    }

    public java.util.Date getEndTime() {
        return (java.util.Date) endTime.getValue();
    }

    public String getReason() {
        return reason.getText().trim();
    }

    public String getRemarks() {
        return remarks.getText().trim();
    }
}