package com.tfms.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;

import com.tfms.model.entity.Inventory;
import com.tfms.model.dao.InventoryDAO;
import com.tfms.model.dao.LeafCollectionDAO;
import com.tfms.model.dao.StockHistoryDAO;
import com.tfms.model.dao.InvoiceDAO;
import com.tfms.model.dao.MachineDAO;
import com.tfms.model.dao.AttendanceDAO;
import com.tfms.model.dao.ProductionDAO;

public class ManagerPanel extends JPanel {
    
    private final InventoryDAO invDAO = new InventoryDAO();
    private final StockHistoryDAO historyDAO = new StockHistoryDAO();
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final ProductionDAO productionDAO = new ProductionDAO();
    private final LeafCollectionDAO leafDAO = new LeafCollectionDAO();
    private final MachineDAO machineDAO = new MachineDAO();
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();


    private final DefaultTableModel stockTableModel;
    private final DefaultTableModel historyTableModel;    
    private final DefaultTableModel stockHTableModel;    
    private final DefaultTableModel productionHTableModel;
    
    private JButton dispatchBtn;
    private JFormattedTextField DispatchQty;
    private JTextField buyerTxt;
    private JComboBox<String> gradeCombo;
    
    private JSpinner dateSpinnerFilter;
    private JButton saveProductionBtn;
    private JTextField remarksTxt;
    private JComboBox<String> gradeComboP;
    private JFormattedTextField ProductionQty;
    private JLabel totalStockLabel;

    public ManagerPanel(MainAppFrame app) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Factory Manager Dashboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> app.showScreen(MainAppFrame.LOGIN_PANEL));
        
        header.add(title, BorderLayout.WEST);
        header.add(logout, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 13));

        stockTableModel = new DefaultTableModel(new String[]{"Inventory ID", "Tea Grade", "Packaged Stock (kg)"}, 0);        
        stockHTableModel = new DefaultTableModel(new String[]{"Date", "Tea Grade", "Transaction", "Quantity (kg)", "Balance"}, 0);
        historyTableModel = new DefaultTableModel(new String[]{"Invoice ID", "Buyer Name", "Tea Grade", "Quantity (kg)", "Date/Time"}, 0);
        productionHTableModel = new DefaultTableModel(new String[]{"Batch No.", "Date", "Tea Grade", "Quantity", "Remarks"}, 0);
        
        tabbedPane.addTab("Dashboard", createDashboardTab());
        tabbedPane.addTab("View Inventory", createInventoryTab());        
        tabbedPane.addTab("View Stock History", createStockHistoryTab());      
        tabbedPane.addTab("Dispatch and Invoicing", createDispatchTab());
        tabbedPane.addTab("Dispatch History", createHistoryTab());
        tabbedPane.addTab("Add Production", createProductionTab());
        tabbedPane.addTab("Production History", createProductionHTab());
//        tabbedPane.addTab("Reports", createReportsTab());

        add(tabbedPane, BorderLayout.CENTER);
        
        refreshAllTables();
    }
    
    private JPanel createDashboardTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(10, 1, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Dashboard Summary"),
            BorderFactory.createEmptyBorder(10, 15, 10, 10) 
        ));

        formCard.add(new JLabel("Today's Green Leaf Collection : "+String.format("%,d", leafDAO.getLeavesToday())+" kg"));
        formCard.add(new JLabel("Today's Production "+productionDAO.getProductionToday()+" kg"));
        formCard.add(new JLabel("Final Tea Stock : "+invDAO.getTotal() +" kg"));        
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

    private JPanel createInventoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Finished Tea Packaged Stock"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15) 
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton refreshBtn = new JButton("Refresh");

        filterPanel.add(refreshBtn);

        JTable table = new JTable(stockTableModel);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(stockTableModel);
        table.setRowSorter(sorter);


        refreshBtn.addActionListener(e -> {
            refreshAllTables();
        });

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
        JButton refreshBtn = new JButton("Refresh");
                
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Tea Grade:"));
        filterPanel.add(gradeFilter);
        filterPanel.add(useDateFilter);
        filterPanel.add(stockDateSpinner);
        filterPanel.add(filterBtn);
        filterPanel.add(refreshBtn);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(filterPanel, BorderLayout.WEST);

        JTable table = new JTable(stockHTableModel);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);
        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(stockHTableModel);
        table.setRowSorter(sorter);

        filterBtn.addActionListener(e -> {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();
            
            String searchTxt = searchField.getText().trim();
            if (!searchTxt.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + searchTxt));
            }

            String selectedGrade = (String) gradeFilter.getSelectedItem();
            if (selectedGrade != null && !selectedGrade.equals("All Grades")) {
                filters.add(RowFilter.regexFilter("(?i)^" + selectedGrade + "$", 1));
            }

            if (useDateFilter.isSelected()) {
                SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFmt.format(stockDateSpinner.getValue());
                if (!formattedDate.isEmpty()) {
                    filters.add(RowFilter.regexFilter("(?i)" + formattedDate));
                }
            }

            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        });

        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            gradeFilter.setSelectedIndex(0);
            useDateFilter.setSelected(false);
            stockDateSpinner.setEnabled(false);
            sorter.setRowFilter(null);
            refreshAllTables();
        });

        containerPanel.add(topContainer, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

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
        
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class); 
        formatter.setAllowsInvalid(false);      
        formatter.setMinimum(0);                
        formatter.setMaximum(1000000);
        
        DispatchQty = new JFormattedTextField(formatter);
        DispatchQty.setColumns(10);
        DispatchQty.setValue(0);
 
        dispatchBtn = new JButton("Generate Invoice and Dispatch");

        formCard.add(new JLabel("Buyer / Broker Name:"));
        formCard.add(buyerTxt);

        formCard.add(new JLabel("Select Tea Grade:"));
        formCard.add(gradeCombo);

        formCard.add(new JLabel("Quantity Sold (kg):"));
        formCard.add(DispatchQty);

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
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dispatchDateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dispatchDateSpinner, "yyyy-MM-dd");
        dispatchDateSpinner.setEditor(dateEditor);
        dispatchDateSpinner.setEnabled(false); 

        useDateFilter.addActionListener(e -> dispatchDateSpinner.setEnabled(useDateFilter.isSelected()));

        JButton filterBtn = new JButton("Filter");
        JButton refreshBtn = new JButton("Refresh");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Tea Grade:"));
        filterPanel.add(gradeComboFilter);
        filterPanel.add(useDateFilter);
        filterPanel.add(dispatchDateSpinner);
        filterPanel.add(filterBtn);
        filterPanel.add(refreshBtn);

        JTable historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(historyTableModel);
        historyTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            String searchTxt = searchField.getText().trim();
            if (!searchTxt.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + searchTxt));
            }

            String grade = (String) gradeComboFilter.getSelectedItem();
            if (grade != null && !grade.equals("All Grades")) {
                filters.add(RowFilter.regexFilter("^" + grade + "$"));
            }

            if (useDateFilter.isSelected()) {
                SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFmt.format(dispatchDateSpinner.getValue());
                if (!formattedDate.isEmpty()) {
                    filters.add(RowFilter.regexFilter("(?i)" + formattedDate));
                }
            }

            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        });

        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            gradeComboFilter.setSelectedIndex(0);
            useDateFilter.setSelected(false);
            dispatchDateSpinner.setEnabled(false);
            sorter.setRowFilter(null);
            refreshAllTables();
        });

        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }
    

    private JPanel createProductionTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(7, 2, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Add Production"),
            BorderFactory.createEmptyBorder(10, 15, 10, 10) 
        ));
    
        gradeComboP = new JComboBox<>(new String[]{"BOP", "BOPF", "Dust 1"});
        remarksTxt = new JTextField(8);        
        
        NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(false);
        
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class); 
        formatter.setAllowsInvalid(false);      
        formatter.setMinimum(0);                
        formatter.setMaximum(1000000);
        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinnerFilter = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerFilter, "yyyy-MM-dd");
        dateSpinnerFilter.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);
        
        ProductionQty = new JFormattedTextField(formatter);
        ProductionQty.setColumns(10);
        ProductionQty.setValue(0);
 
        saveProductionBtn = new JButton("Save Production");
        
        formCard.add(new JLabel("Select Product:"));
        formCard.add(gradeComboP);
        
        formCard.add(new JLabel("Date:"));
        formCard.add(dateSpinnerFilter);
        
        formCard.add(new JLabel("Quantity (kg):"));
        formCard.add(ProductionQty);

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
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner prodDateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(prodDateSpinner, "yyyy-MM-dd");
        prodDateSpinner.setEditor(dateEditor);
        prodDateSpinner.setEnabled(false);

        useDateFilter.addActionListener(e -> prodDateSpinner.setEnabled(useDateFilter.isSelected()));

        JButton filterBtn = new JButton("Filter");
        JButton refreshBtn = new JButton("Refresh");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Tea Grade:"));
        filterPanel.add(gradeComboFilter);
        filterPanel.add(useDateFilter);
        filterPanel.add(prodDateSpinner);
        filterPanel.add(filterBtn);
        filterPanel.add(refreshBtn);

        JTable historyTable = new JTable(productionHTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(productionHTableModel);
        historyTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            String searchTxt = searchField.getText().trim();
            if (!searchTxt.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + searchTxt));
            }

            String grade = (String) gradeComboFilter.getSelectedItem();
            if (grade != null && !grade.equals("All Grades")) {
                filters.add(RowFilter.regexFilter("^" + grade + "$"));
            }

            if (useDateFilter.isSelected()) {
                SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFmt.format(prodDateSpinner.getValue());
                if (!formattedDate.isEmpty()) {
                    filters.add(RowFilter.regexFilter("(?i)" + formattedDate));
                }
            }

            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        });

        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            gradeComboFilter.setSelectedIndex(0);
            useDateFilter.setSelected(false);
            prodDateSpinner.setEnabled(false);
            sorter.setRowFilter(null);
            refreshAllTables();
        });

        containerPanel.add(filterPanel, BorderLayout.NORTH);
        containerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(containerPanel, BorderLayout.CENTER);

        return panel;
    }

//    private JPanel createReportsTab() {
//        JPanel panel = new JPanel(new GridBagLayout());
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        JPanel formCard = new JPanel(new GridLayout(10, 1, 10, 15));
//        formCard.setBorder(BorderFactory.createCompoundBorder(
//            BorderFactory.createTitledBorder("Generate Reports"),
//            BorderFactory.createEmptyBorder(10, 15, 10, 10) 
//        ));
//
//        formCard.add(new JLabel("Today's Leaf Collection : 4,444 kg"));
//        formCard.add(new JLabel("Today's Production      : 1,120 kg"));
//        formCard.add(new JLabel("Final Tea Stock         : 1,280 kg"));        
//        formCard.add(new JLabel("Pending QC Batches      : 3"));        
//        formCard.add(new JLabel("Rejected Batches        : 1"));
//        formCard.add(new JLabel(" "));
//        formCard.add(new JLabel("Sales Today             : $4,200"));        
//        formCard.add(new JLabel("Suppliers Today         : 45"));        
//        formCard.add(new JLabel("Workers Present         : 62"));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0; gbc.gridy = 0;
//        gbc.weightx = 1.0; gbc.weighty = 1.0;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.anchor = GridBagConstraints.NORTH;
//        panel.add(formCard, gbc);
//
//        return panel;
//    }


    public void DispatchListener(ActionListener listener) {
        dispatchBtn.addActionListener(listener);
    }

    public void ProductionListener(ActionListener listener) {
        saveProductionBtn.addActionListener(listener);
    }

    

    public String getBuyer() {
        return buyerTxt.getText().trim();
    }

    public String getGrade() {
        return (String) gradeCombo.getSelectedItem();
    }

    public int getDispatchQuantity() {
        Object val = DispatchQty.getValue();
        return (val != null) ? ((Number) val).intValue() : 0;
    }

    public Date getDate() {
        return (Date) dateSpinnerFilter.getValue();
    }

    public String getProductionGrade() {
        return (String) gradeComboP.getSelectedItem();
    }

    public int getProductionQuantity() {
        Object val = ProductionQty.getValue();
        return (val != null) ? ((Number) val).intValue() : 0;
    }

    public String getRemarks() {
        return remarksTxt.getText().trim();
    }

    
    
    
    public void clearDispatchForm() {
        buyerTxt.setText("");
        DispatchQty.setValue(0);
        if (gradeCombo.getItemCount() > 0) gradeCombo.setSelectedIndex(0);
    }

    public void clearProductionForm() {
        remarksTxt.setText("");
        ProductionQty.setValue(0);
        if (gradeComboP.getItemCount() > 0) gradeComboP.setSelectedIndex(0);
    }

    public void refreshAllTables() {
        stockTableModel.setRowCount(0);
        List<Inventory> invList = invDAO.getInventory();
        for (Inventory i : invList) {
            stockTableModel.addRow(new Object[]{i.getId(), i.getName(), i.getQuantity()});
        }
        if (totalStockLabel != null) {
            totalStockLabel.setText("Total Stock : " + invDAO.getTotal() + " kg");
        }

        stockHTableModel.setRowCount(0);
        for (Object[] row : historyDAO.getStockHistory()) {
            stockHTableModel.addRow(row);
        }

        historyTableModel.setRowCount(0);
        for (Object[] row : invoiceDAO.getAllInvoices()) {
            historyTableModel.addRow(row);
        }

        productionHTableModel.setRowCount(0);
        for (Object[] row : productionDAO.getAllProduction()) {
            productionHTableModel.addRow(row);
        }
    }
}