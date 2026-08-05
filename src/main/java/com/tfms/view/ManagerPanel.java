package com.tfms.view;

import com.tfms.model.entity.Inventory;

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

public class ManagerPanel extends JPanel {

    private JLabel todayLeavesLabel;
    private JLabel todayProductionLabel;
    private JLabel finalStockLabel;
    private JLabel pendingQCLabel;
    private JLabel rejectedBatchLabel;
    private JLabel runningMachineLabel;
    private JLabel maintenanceMachineLabel;
    private JLabel workersPresentLabel;

    private final DefaultTableModel stockTableModel;
    private final DefaultTableModel historyTableModel;
    private final DefaultTableModel stockHTableModel;
    private final DefaultTableModel productionHTableModel;
    private final DefaultTableModel machineDowntimeTableModel;
    
    private JButton refreshBtn;

    private JButton dispatchBtn;
    private JFormattedTextField dispatchQty;
    private JTextField buyerTxt;
    private JComboBox<String> gradeCombo;

    private JSpinner dateSpinnerFilter;
    private JButton saveProductionBtn;
    private JTextField remarksTxt;
    private JComboBox<String> gradeComboP;
    private JFormattedTextField productionQty;
    private JLabel totalStockLabel;

    public ManagerPanel(MainAppFrame app) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Factory Manager Dashboard");
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

        stockTableModel = new DefaultTableModel(new String[]{"Inventory ID", "Tea Grade", "Packaged Stock (kg)"}, 0);
        stockHTableModel = new DefaultTableModel(new String[]{"Date", "Tea Grade", "Transaction", "Quantity (kg)", "Balance"}, 0);
        historyTableModel = new DefaultTableModel(new String[]{"Invoice ID", "Buyer Name", "Tea Grade", "Quantity (kg)", "Date/Time"}, 0);
        productionHTableModel = new DefaultTableModel(new String[]{"Batch No.", "Date", "Tea Grade", "Quantity", "Remarks"}, 0);
        machineDowntimeTableModel = new DefaultTableModel(new String[]{"Machine", "Date", "Start time", "End time", "Reason", "Remarks"}, 0);

        tabbedPane.addTab("Dashboard", createDashboardTab());
        tabbedPane.addTab("View Inventory", createInventoryTab());
        tabbedPane.addTab("View Stock History", createStockHistoryTab());
        tabbedPane.addTab("Dispatch and Invoicing", createDispatchTab());
        tabbedPane.addTab("Dispatch History", createHistoryTab());
        tabbedPane.addTab("Add Production", createProductionTab());
        tabbedPane.addTab("Production History", createProductionHTab());
        tabbedPane.addTab("Machine Downtimes", createMachineDowntimesTab());

        add(tabbedPane, BorderLayout.CENTER);

    }

    private JPanel createDashboardTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(0, 1, 10, 12));
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Dashboard Summary"),
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

    private JPanel createInventoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Finished Tea Packaged Stock"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
       
        JTable table = new JTable(stockTableModel);
        table.setRowHeight(24);
        table.setRowSorter(new TableRowSorter<>(stockTableModel));
        JScrollPane scrollPane = new JScrollPane(table);

        totalStockLabel = new JLabel("Total Stock : 0 kg");
        totalStockLabel.setFont(totalStockLabel.getFont().deriveFont(Font.BOLD, 13f));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        bottomPanel.add(totalStockLabel);

        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);

        panel.add(containerPanel, BorderLayout.CENTER);
        return panel;
    }
    

    private JPanel createStockHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Finished Tea Packaged Stock History"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner stockDateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(stockDateSpinner, "yyyy-MM-dd");
        stockDateSpinner.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);
        stockDateSpinner.setEnabled(false);

        JCheckBox useDateFilter = new JCheckBox("Filter Date:");
        useDateFilter.addActionListener(e -> stockDateSpinner.setEnabled(useDateFilter.isSelected()));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));
        JTextField searchField = new JTextField(10);
        JComboBox<String> gradeFilter = new JComboBox<>(new String[]{"All Grades", "BOP", "BOPF", "Dust 1"});
        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Tea Grade:"));
        filterPanel.add(gradeFilter);
        filterPanel.add(useDateFilter);
        filterPanel.add(stockDateSpinner);
        filterPanel.add(filterBtn);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(filterPanel, BorderLayout.WEST);

        JTable table = new JTable(stockHTableModel);
        table.setRowHeight(24);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(stockHTableModel);
        table.setRowSorter(sorter);

        filterBtn.addActionListener(e -> applyFilter(sorter, searchField.getText(), (String) gradeFilter.getSelectedItem(), useDateFilter.isSelected(), stockDateSpinner.getValue()));
  

        containerPanel.add(topContainer, BorderLayout.NORTH);
        containerPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }
    

    private JPanel createDispatchTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(4, 2, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Bulk Buyer Dispatch"),
                BorderFactory.createEmptyBorder(10, 15, 10, 10)
        ));

        gradeCombo = new JComboBox<>(new String[]{"BOP", "BOPF", "Dust 1"});
        buyerTxt = new JTextField(8);

        dispatchQty = createNumberFormattedField();
        dispatchBtn = new JButton("Generate Invoice and Dispatch");

        formCard.add(new JLabel("Buyer / Broker Name:"));
        formCard.add(buyerTxt);
        formCard.add(new JLabel("Select Tea Grade:"));
        formCard.add(gradeCombo);
        formCard.add(new JLabel("Quantity Sold (kg):"));
        formCard.add(dispatchQty);
        formCard.add(new JLabel());
        formCard.add(dispatchBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(formCard, gbc);

        return panel;
    }
    

    private JPanel createHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Recent Dispatches and Issued Invoices"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField searchField = new JTextField(10);
        JComboBox<String> gradeComboFilter = new JComboBox<>(new String[]{"All Grades", "BOP", "BOPF", "Dust 1"});

        JCheckBox useDateFilter = new JCheckBox("Filter Date:");
        JSpinner dispatchDateSpinner = new JSpinner(new SpinnerDateModel());
        dispatchDateSpinner.setEditor(new JSpinner.DateEditor(dispatchDateSpinner, "yyyy-MM-dd"));
        dispatchDateSpinner.setEnabled(false);

        useDateFilter.addActionListener(e -> dispatchDateSpinner.setEnabled(useDateFilter.isSelected()));

        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Tea Grade:"));
        filterPanel.add(gradeComboFilter);
        filterPanel.add(useDateFilter);
        filterPanel.add(dispatchDateSpinner);
        filterPanel.add(filterBtn);

        JTable historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(24);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(historyTableModel);
        historyTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> applyFilter(sorter, searchField.getText(), (String) gradeComboFilter.getSelectedItem(), useDateFilter.isSelected(), dispatchDateSpinner.getValue()));
       

        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }
    

    private JPanel createProductionTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(5, 2, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Add Production"),
                BorderFactory.createEmptyBorder(10, 15, 10, 10)
        ));

        gradeComboP = new JComboBox<>(new String[]{"BOP", "BOPF", "Dust 1"});
        remarksTxt = new JTextField(8);

        dateSpinnerFilter = new JSpinner(new SpinnerDateModel());
        dateSpinnerFilter.setEditor(new JSpinner.DateEditor(dateSpinnerFilter, "yyyy-MM-dd"));

        productionQty = createNumberFormattedField();
        saveProductionBtn = new JButton("Save Production");

        formCard.add(new JLabel("Select Product:"));
        formCard.add(gradeComboP);
        formCard.add(new JLabel("Date:"));
        formCard.add(dateSpinnerFilter);
        formCard.add(new JLabel("Quantity (kg):"));
        formCard.add(productionQty);
        formCard.add(new JLabel("Remarks:"));
        formCard.add(remarksTxt);
        formCard.add(new JLabel());
        formCard.add(saveProductionBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(formCard, gbc);

        return panel;
    }
    

    private JPanel createProductionHTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Production History"),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField searchField = new JTextField(10);
        JComboBox<String> gradeComboFilter = new JComboBox<>(new String[]{"All Grades", "BOP", "BOPF", "Dust 1"});

        JCheckBox useDateFilter = new JCheckBox("Filter Date:");
        JSpinner prodDateSpinner = new JSpinner(new SpinnerDateModel());
        prodDateSpinner.setEditor(new JSpinner.DateEditor(prodDateSpinner, "yyyy-MM-dd"));
        prodDateSpinner.setEnabled(false);

        useDateFilter.addActionListener(e -> prodDateSpinner.setEnabled(useDateFilter.isSelected()));

        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Tea Grade:"));
        filterPanel.add(gradeComboFilter);
        filterPanel.add(useDateFilter);
        filterPanel.add(prodDateSpinner);
        filterPanel.add(filterBtn);

        JTable historyTable = new JTable(productionHTableModel);
        historyTable.setRowHeight(24);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(productionHTableModel);
        historyTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> applyFilter(sorter, searchField.getText(), (String) gradeComboFilter.getSelectedItem(), useDateFilter.isSelected(), prodDateSpinner.getValue()));
       
        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }
    

    private JPanel createMachineDowntimesTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Machine Downtimes"), // Fixed incorrect title
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JTextField searchField = new JTextField(10);

        JCheckBox useDateFilter = new JCheckBox("Filter Date:");
        JSpinner prodDateSpinner = new JSpinner(new SpinnerDateModel());
        prodDateSpinner.setEditor(new JSpinner.DateEditor(prodDateSpinner, "yyyy-MM-dd"));
        prodDateSpinner.setEnabled(false);

        useDateFilter.addActionListener(e -> prodDateSpinner.setEnabled(useDateFilter.isSelected()));

        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(useDateFilter);
        filterPanel.add(prodDateSpinner);
        filterPanel.add(filterBtn);

        JTable historyTable = new JTable(machineDowntimeTableModel);
        historyTable.setRowHeight(24);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(machineDowntimeTableModel);
        historyTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> applyFilter(sorter, searchField.getText(), null, useDateFilter.isSelected(), prodDateSpinner.getValue()));

        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(new JScrollPane(historyTable), BorderLayout.CENTER);
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

    private void applyFilter(TableRowSorter<DefaultTableModel> sorter, String searchTxt, String grade, boolean useDate, Object dateVal) {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (searchTxt != null && !searchTxt.trim().isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + searchTxt.trim()));
        }

        if (grade != null && !grade.equals("All Grades")) {
            filters.add(RowFilter.regexFilter("(?i)^" + grade + "$"));
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

    public void RefreshListener(ActionListener listener) {
        refreshBtn.addActionListener(listener);
    }

    public void DispatchListener(ActionListener listener) {
        dispatchBtn.addActionListener(listener);
    }

    public void ProductionListener(ActionListener listener) {
        saveProductionBtn.addActionListener(listener);
    }

    public void updateDashboard(int leavesToday, double productionToday, double totalStock, int pendingQC, int rejectedBatches, int runningMachines, int maintenanceMachines, int workersPresent) {
        todayLeavesLabel.setText("Today's Green Leaf Collection : " + String.format("%,d", leavesToday) + " kg");
        todayProductionLabel.setText("Today's Production : " + productionToday + " kg");
        finalStockLabel.setText("Final Tea Stock : " + totalStock + " kg");
        pendingQCLabel.setText("Approved Batches : " + pendingQC);
        rejectedBatchLabel.setText("Rejected Batches : " + rejectedBatches);
        runningMachineLabel.setText("Machines Running : " + runningMachines);
        maintenanceMachineLabel.setText("Machines Under Maintenance : " + maintenanceMachines);
        workersPresentLabel.setText("Workers Present : " + workersPresent);
    }

    public String getBuyer() {
        return buyerTxt.getText().trim();
    }

    public String getGrade() {
        return (String) gradeCombo.getSelectedItem();
    }

    public int getDispatchQuantity() {
        Object val = dispatchQty.getValue();
        return (val != null) ? ((Number) val).intValue() : 0;
    }

    public Date getDate() {
        return (Date) dateSpinnerFilter.getValue();
    }

    public String getProductionGrade() {
        return (String) gradeComboP.getSelectedItem();
    }

    public int getProductionQuantity() {
        Object val = productionQty.getValue();
        return (val != null) ? ((Number) val).intValue() : 0;
    }

    public String getRemarks() {
        return remarksTxt.getText().trim();
    }

    public void clearDispatchForm() {
        buyerTxt.setText("");
        dispatchQty.setValue(0);
        if (gradeCombo.getItemCount() > 0) gradeCombo.setSelectedIndex(0);
    }

    public void clearProductionForm() {
        remarksTxt.setText("");
        productionQty.setValue(0);
        if (gradeComboP.getItemCount() > 0) gradeComboP.setSelectedIndex(0);
    }

    public void loadInventory(List<Inventory> invList, double total) {
        stockTableModel.setRowCount(0);
        for (Inventory i : invList) {
            stockTableModel.addRow(new Object[]{
                    i.getId(),
                    i.getName(),
                    i.getQuantity()
            });
        }
        totalStockLabel.setText("Total Stock : " + total + " kg");
    }

    public void loadStockHistory(List<Object[]> history) {
        stockHTableModel.setRowCount(0);
        for (Object[] row : history) {
            stockHTableModel.addRow(row);
        }
    }

    public void loadInvoices(List<Object[]> invoices) {
        historyTableModel.setRowCount(0);
        for (Object[] row : invoices) {
            historyTableModel.addRow(row);
        }
    }

    public void loadProductionHistory(List<Object[]> production) {
        productionHTableModel.setRowCount(0);
        for (Object[] row : production) {
            productionHTableModel.addRow(row);
        }
    }

    public void loadMachineDowntime(List<Object[]> downtime) {
        machineDowntimeTableModel.setRowCount(0);
        for (Object[] row : downtime) {
            machineDowntimeTableModel.addRow(row);
        }
    }
}