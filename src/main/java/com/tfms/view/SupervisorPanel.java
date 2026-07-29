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
import com.tfms.model.entity.Supplier;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionListener;

public class SupervisorPanel extends JPanel {

    private final DefaultTableModel attendanceTableModel;   
    private final DefaultTableModel attendanceHTableModel;
    private final DefaultTableModel machineManageTableModel;
    private final DefaultTableModel recieptsTableModel;
    
    private JButton RecipetBtn;

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
        machineManageTableModel = new DefaultTableModel(new String[]{"Machine ID", "Assigned Worker", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 2; 
            }
        };
        attendanceHTableModel = new DefaultTableModel(new String[]{"Date", "Worker ID", "Worker Name", "Role", "Status"}, 0);
        recieptsTableModel = new DefaultTableModel(new String[]{"Date", "Reciept No.", "Supplier ID", "Supplier Name", "Weight (kg)"}, 0);
        
        
        tabbedPane.addTab("Dashboard", createDashboardTab());    
        tabbedPane.addTab("Green Leaf Collection", createLeafCollectionTab());
        tabbedPane.addTab("Mark Attendance", createAttendanceTab());
        tabbedPane.addTab("Attendance Records", createAttendanceRecordsTab());
        tabbedPane.addTab("Machine Management", createMachineManagementTab());
        tabbedPane.addTab("Log Downtime", createLogDowntimeTab());        
        tabbedPane.addTab("Reciepts", createRecieptRecordsTab());


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

        formCard.add(new JLabel("Today's Green Leaf Collection : 4444 kg"));
        formCard.add(new JLabel("Total Green Leaves      : 1,120 kg"));
        formCard.add(new JLabel("Pending QC Batches      : 3"));        
        formCard.add(new JLabel("Machines Running            : $4,200"));       
        formCard.add(new JLabel("Machine Downtime        : 45"));        
        formCard.add(new JLabel("Workers Present         : 62"));


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
        
        JFormattedTextField numberField = new JFormattedTextField(formatter);
        numberField.setColumns(10);
        numberField.setValue(0);
        
        SupplierDAO supplierDAO = new SupplierDAO();
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();

        JComboBox<Supplier> suppCombo = new JComboBox<>();
        for (Supplier s : suppliers) {
            suppCombo.addItem(s);
}
       
        RecipetBtn = new JButton("Print Reciept");
        
        formCard.add(new JLabel("Supplier:"));
        formCard.add(suppCombo);

        formCard.add(new JLabel("Gross weight (kg):"));
        formCard.add(numberField);

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
    
    public void LeafCollectionListener(ActionListener listener){
        RecipetBtn.addActionListener(listener);
    }

    private JPanel createAttendanceTab() {

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Daily Worker Attendance"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JTable attendanceTable = new JTable(attendanceTableModel);
        attendanceTable.setRowHeight(28);

        attendanceTableModel.addRow(new Object[]{"W001", "John Doe", "Leaf Collector", "Present"});
        attendanceTableModel.addRow(new Object[]{"W002", "Jane Smith", "Sorter", "Absent"});
        attendanceTableModel.addRow(new Object[]{"W003", "Robert Paul", "Packer", "Present"});

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
        JButton saveBtn = new JButton("Save Attendance");
        
        bottomPanel.add(saveBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        
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

        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinnerFilter = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerFilter, "yyyy-MM-dd");
        dateSpinnerFilter.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);

      
        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(attendanceComboFilter);
        filterPanel.add(new JLabel("Date:"));
        filterPanel.add(dateSpinnerFilter);
        filterPanel.add(filterBtn);

        JTable historyTable = new JTable(attendanceHTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);

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

        JTable machineTable = new JTable(machineManageTableModel);
        machineTable.setRowHeight(28);

        machineManageTableModel.addRow(new Object[]{"Roller1", "John Doe", "Running"});
        machineManageTableModel.addRow(new Object[]{"Sorter1", "Jane Smith", "Maintenance"});

        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Running", "Under Maintanance", "Idle"});
        JComboBox<String> workersComboBox = new JComboBox<>(new String[]{"jhon", "fager", "awa"});
        
        DefaultCellEditor singleClickEditor = new DefaultCellEditor(statusComboBox) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return true;
            }
        };
        DefaultCellEditor singleClickEditor2 = new DefaultCellEditor(workersComboBox) {
            @Override
            public boolean isCellEditable(EventObject e) {
                return true;
            }
        };
        
        
        machineTable.getColumnModel().getColumn(2).setCellEditor(singleClickEditor);
        machineTable.getColumnModel().getColumn(1).setCellEditor(singleClickEditor2);

        JScrollPane scrollPane = new JScrollPane(machineTable);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Save");
        
        bottomPanel.add(saveBtn);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel, BorderLayout.CENTER);
        
        return panel;
    }
      
      
    private JPanel createLogDowntimeTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(6, 2, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Log Downtime"),
            BorderFactory.createEmptyBorder(15, 20, 15, 20) 
        ));

        SpinnerDateModel startModel = new SpinnerDateModel();
        JSpinner startTime = new JSpinner(startModel);
        JSpinner.DateEditor timeEditor1 = new JSpinner.DateEditor(startTime, "HH:mm");
        startTime.setEditor(timeEditor1);

        SpinnerDateModel endModel = new SpinnerDateModel();
        JSpinner endTime = new JSpinner(endModel);
        JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(endTime, "HH:mm");
        endTime.setEditor(timeEditor2);

        JComboBox<String> machineCombo = new JComboBox<>(new String[]{"roller1", "BOPF", "Dust 1"});

        JTextField reason = new JTextField(10);
        JTextField remar = new JTextField(10);
        JButton addBtn = new JButton("Save Downtime");

        formCard.add(new JLabel("Machine:"));
        formCard.add(machineCombo);

        formCard.add(new JLabel("Start time:"));
        formCard.add(startTime);

        formCard.add(new JLabel("End time:"));
        formCard.add(endTime);

        formCard.add(new JLabel("Reason:"));
        formCard.add(reason);

        formCard.add(new JLabel("Remarks:"));
        formCard.add(remar);

        formCard.add(new JLabel()); 
        formCard.add(addBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(formCard, gbc);

        return panel;
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

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Date:"));
        filterPanel.add(dateSpinnerFilter);
        filterPanel.add(filterBtn);

        JTable historyTable = new JTable(recieptsTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);

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
}