package com.tfms.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.EventObject;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.text.SimpleDateFormat;

import com.tfms.model.dao.SupplierDAO;
import com.tfms.model.dao.EmployeeDAO;
import com.tfms.model.dao.AttendanceDAO;
import com.tfms.model.dao.MachineDAO;
import com.tfms.model.dao.LeafCollectionDAO;
import com.tfms.model.entity.Supplier;
import com.tfms.model.entity.Employee;
import com.tfms.model.entity.Machine;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionListener;

public class SupervisorPanel extends JPanel {

    private final DefaultTableModel attendanceTableModel;   
    private final DefaultTableModel attendanceHTableModel;
    private final DefaultTableModel machineManageTableModel;
    private final DefaultTableModel recieptsTableModel;
     
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final MachineDAO machineDAO = new MachineDAO();
    private final LeafCollectionDAO leafDAO = new LeafCollectionDAO();
    
    private JButton RecipetBtn;
    private JButton AttendanceBtn;
    private JButton machineSaveBtn;
    private JFormattedTextField recipetWeight;
    private JComboBox<Supplier> suppCombo;
    private JComboBox<Machine> machineCombo;
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
        
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> app.showScreen(MainAppFrame.LOGIN_PANEL));
        
        header.add(title, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);
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
        recieptsTableModel = new DefaultTableModel(new String[]{"Date", "Receipt No.", "Supplier ID", "Supplier Name", "Weight (kg)"}, 0) {
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
        tabbedPane.addTab("Receipts", createRecieptRecordsTab());

        add(tabbedPane, BorderLayout.CENTER);
    }
    
    
    private JPanel createDashboardTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(10, 1, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Dashboard"),
            BorderFactory.createEmptyBorder(10, 15, 10, 10) 
        ));
     
        formCard.add(new JLabel("Today's Green Leaf Collection : "+String.format("%,d", leafDAO.getLeavesToday())+" kg"));
        formCard.add(new JLabel("Pending QC Batches : 3")); 
        formCard.add(new JLabel("Rejected Batches : 1"));
        formCard.add(new JLabel("Machines Running : "+machineDAO.getRunningMachines()));       
        formCard.add(new JLabel("Machines Under Maintenance : "+machineDAO.getDownMachines()));        
        formCard.add(new JLabel("Workers Present : "+attendanceDAO.getAllPresent()));

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
        
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class); 
        formatter.setAllowsInvalid(false);      
        formatter.setMinimum(0);                
        formatter.setMaximum(1000000);
        
        recipetWeight = new JFormattedTextField(formatter);
        recipetWeight.setColumns(10);
        recipetWeight.setValue(0);
        
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();

        suppCombo = new JComboBox<>();
        for (Supplier s : suppliers) {
            suppCombo.addItem(s);
        }
       
        RecipetBtn = new JButton("Print Receipt");
        
        formCard.add(new JLabel("Supplier:"));
        formCard.add(suppCombo);

        formCard.add(new JLabel("Gross weight (kg):"));
        formCard.add(recipetWeight);

        formCard.add(new JLabel());
        formCard.add(RecipetBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(formCard, gbc);

        return panel;
    }
    
    public Supplier getSupplier() {
        return (Supplier) suppCombo.getSelectedItem();
    }

    public int getWeight() {
        Object val = recipetWeight.getValue();
        return (val != null) ? ((Number) val).intValue() : 0;
    }

    public void LeafCollectionListener(ActionListener listener){
        RecipetBtn.addActionListener(listener);
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
        JButton refreshBtn = new JButton("Refresh");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(filterBtn);
        filterPanel.add(refreshBtn);

        JTable attendanceTable = new JTable(attendanceTableModel);
        attendanceTable.setRowHeight(28);
        
        Runnable loadData = () -> {
            attendanceTableModel.setRowCount(0);
            
            List<Employee> employees = employeeDAO.getAllEmployees();
            for (Employee e : employees) {
                attendanceTableModel.addRow(new Object[]{e.getId(), e.getName(), e.getPosition(), "-"});
            }
        };
        loadData.run();

        refreshBtn.addActionListener(e -> loadData.run());

        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Present", "Absent"});
        
        DefaultCellEditor singleClickEditor = new DefaultCellEditor(statusComboBox) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return true;
            }
        };
        
        attendanceTable.getColumnModel().getColumn(3).setCellEditor(singleClickEditor);

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(attendanceTableModel);
        attendanceTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> filterTable(sorter, searchField.getText().trim()));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        AttendanceBtn = new JButton("Save Attendance");
        
        bottomPanel.add(AttendanceBtn);
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public void AttendanceListener(ActionListener listener){
        AttendanceBtn.addActionListener(listener);
    }
    
    public DefaultTableModel getAttendanceTable() {
        return attendanceTableModel;
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

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinnerFilter = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerFilter, "yyyy-MM-dd");
        dateSpinnerFilter.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);

        JButton filterBtn = new JButton("Filter");
        JButton refreshBtn = new JButton("Refresh");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(attendanceComboFilter);
        filterPanel.add(new JLabel("Date:"));
        filterPanel.add(dateSpinnerFilter);
        filterPanel.add(filterBtn);
        filterPanel.add(refreshBtn);

        JTable historyTable = new JTable(attendanceHTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        Runnable loadData = () -> {
            attendanceHTableModel.setRowCount(0);
            
            List<Object[]> attendance = attendanceDAO.getAllAttendance();
            for (Object[] a : attendance) {
                attendanceHTableModel.addRow(a);
            }
        };
        loadData.run();

        refreshBtn.addActionListener(e -> loadData.run());

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(attendanceHTableModel);
        historyTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            String searchTxt = searchField.getText().trim();
            if (!searchTxt.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + searchTxt));
            }

            String grade = (String) attendanceComboFilter.getSelectedItem();
            if (grade != null && !grade.equals("All")) {
                filters.add(RowFilter.regexFilter("(?i)" + grade));
            }

            SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFmt.format(dateSpinnerFilter.getValue());
            if (!formattedDate.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + formattedDate));
            }

            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        });

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
        JButton refreshBtn = new JButton("Refresh");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(filterBtn);
        filterPanel.add(refreshBtn);

        JTable machineTable = new JTable(machineManageTableModel);
        machineTable.setRowHeight(28);
        
        Runnable loadData = () -> {
            machineManageTableModel.setRowCount(0);
            
            List<Object[]> machines = machineDAO.getAllMachines();
            for (Object[] m : machines) {
                machineManageTableModel.addRow(m);
            }
        };
        loadData.run();

        refreshBtn.addActionListener(e -> loadData.run());

        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Running", "Under Maintenance", "Idle"});
        
        JComboBox<Employee> employeeComboBox = new JComboBox<>();
        
        List<Employee> employees = employeeDAO.getAllEmployees();

        for (Employee e : employees) {
            employeeComboBox.addItem(e);
        }
        
        DefaultCellEditor singleClickEditor = new DefaultCellEditor(statusComboBox) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return true;
            }
        };
        DefaultCellEditor singleClickEditor2 = new DefaultCellEditor(employeeComboBox) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return true;
            }
        };
        
        machineTable.getColumnModel().getColumn(4).setCellEditor(singleClickEditor);
        machineTable.getColumnModel().getColumn(3).setCellEditor(singleClickEditor2);

        JScrollPane scrollPane = new JScrollPane(machineTable);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(machineManageTableModel);
        machineTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> filterTable(sorter, searchField.getText().trim()));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        machineSaveBtn = new JButton("Save");
        
        bottomPanel.add(machineSaveBtn);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
      
    public void MachineListener(ActionListener listener){
        machineSaveBtn.addActionListener(listener);
    }
    
    public DefaultTableModel getMachineTable() {
        return machineManageTableModel;
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
        
        List<Machine> machines = machineDAO.getAllClassMachines();

        for (Machine m : machines) {
            machineCombo.addItem(m);
        }

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

    public void addSaveDowntimeListener(ActionListener listener) {
        saveDown.addActionListener(listener);
    }

    private JPanel createRecieptRecordsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Reciepts Records"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JTextField searchField = new JTextField(10);

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinnerFilter = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerFilter, "yyyy-MM-dd");
        dateSpinnerFilter.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);

        JButton filterBtn = new JButton("Filter");
        JButton refreshBtn = new JButton("Refresh");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Date:"));
        filterPanel.add(dateSpinnerFilter);
        filterPanel.add(filterBtn);
        filterPanel.add(refreshBtn);

        JTable historyTable = new JTable(recieptsTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        Runnable loadData = () -> {
            recieptsTableModel.setRowCount(0);
            
            List<Object[]> reciepts = leafDAO.getAllReciepts();
            for (Object[] r : reciepts) {
                recieptsTableModel.addRow(r);
            }
        };
        loadData.run();

        refreshBtn.addActionListener(e -> loadData.run());

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(recieptsTableModel);
        historyTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            String searchTxt = searchField.getText().trim();
            if (!searchTxt.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + searchTxt));
            }

            SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFmt.format(dateSpinnerFilter.getValue());
            if (!formattedDate.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + formattedDate));
            }

            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        });

        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }

    private void filterTable(TableRowSorter<DefaultTableModel> sorter, String searchText) {
        if (searchText.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchText));
        }
    }
}