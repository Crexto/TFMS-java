package com.tfms.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.text.SimpleDateFormat;

public class ManagerPanel extends JPanel {

    private final DefaultTableModel stockTableModel;
    private final DefaultTableModel historyTableModel;    
    private final DefaultTableModel stockHTableModel;    
    private final DefaultTableModel productionTableModel;
    private final DefaultTableModel productionHTableModel;
    private final DefaultTableModel wasteRecordsTableModel;
    

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


        stockTableModel = new DefaultTableModel(new String[]{"Tea Grade", "Packaged Stock (kg)", "Status"}, 0);        
        stockHTableModel = new DefaultTableModel(new String[]{"Date", "Tea Grade", "Transaction", "Quantity (kg)", "Balance"}, 0);
        historyTableModel = new DefaultTableModel(new String[]{"Invoice ID", "Buyer Name", "Tea Grade", "Quantity (kg)", "Date/Time"}, 0);
        productionHTableModel = new DefaultTableModel(new String[]{"Date", "Total amount of Tea produced (kg)"}, 0);
        productionTableModel = new DefaultTableModel(new String[]{"Batch No.", "Tea Grade", "Date", "Quantity Produced", "QC Status"}, 0);
        wasteRecordsTableModel = new DefaultTableModel(new String[]{"Date", "Reason", "Quantity (kg)"}, 0);
        
        tabbedPane.addTab("Dashboard", createDashboardTab());
        tabbedPane.addTab("View Inventory", createInventoryTab());        
        tabbedPane.addTab("View Stock History", createStockHistoryTab());      
        tabbedPane.addTab("Dispatch and Invoicing", createDispatchTab());
        tabbedPane.addTab("Dispatch History", createHistoryTab());
        tabbedPane.addTab("Production Batches", createProductionBatchTab());
        tabbedPane.addTab("Production History", createProductionHTab());
        tabbedPane.addTab("Waste Records", createWasteRecordsTab());
        tabbedPane.addTab("Reports", createReportsTab());

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

        formCard.add(new JLabel("Today's Leaf Collection : 4444 kg"));
        formCard.add(new JLabel("Today's Production      : 1,120 kg"));
        formCard.add(new JLabel("Final Tea Stock         : 1,280 kg"));        
        formCard.add(new JLabel("Pending QC Batches      : 3"));        
        formCard.add(new JLabel("Rejected Batches        : 1"));
        formCard.add(new JLabel(" "));
        formCard.add(new JLabel("Sales Today             : $4,200"));       
        formCard.add(new JLabel("Suppliers Today         : 45"));        
        formCard.add(new JLabel("Workers Present         : 62"));


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

       JTable table = new JTable(stockTableModel);
       table.setRowHeight(24);
       JScrollPane scrollPane = new JScrollPane(table);

       JLabel totalStockLabel = new JLabel("Total Stock : 4444 kg");
       totalStockLabel.setFont(totalStockLabel.getFont().deriveFont(Font.BOLD, 13f));

       JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
       bottomPanel.add(totalStockLabel);

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
        JSpinner dateSpinnerFilter = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerFilter, "yyyy-MM-dd");
        dateSpinnerFilter.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);

        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 5));

        JComboBox<String> gradeFilter = new JComboBox<>(new String[]{"BOP", "BOPF", "Dust 1"});
        gradeFilter.setSelectedItem("BOP");
        JTextField dateField = new JTextField(8); 
        JButton filterBtn = new JButton("Filter");
                
        filterPanel.add(new JLabel("Tea Grade:"));
        filterPanel.add(gradeFilter);
        filterPanel.add(new JLabel("Date:"));
        filterPanel.add(dateSpinnerFilter);
        filterPanel.add(filterBtn);


        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(filterPanel, BorderLayout.WEST);


        JTable table = new JTable(stockHTableModel);
        table.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(table);

        
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(stockHTableModel);
        table.setRowSorter(sorter);

        filterBtn.addActionListener(e -> {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            
            String selectedGrade = (String) gradeFilter.getSelectedItem();
            if (selectedGrade != null) {
                filters.add(RowFilter.regexFilter("(?i)^" + selectedGrade + "$", 1));
            }

//            String dateText = dateField.getText().trim();
//            if (!dateText.isEmpty()) {
//                filters.add(RowFilter.regexFilter("(?i)" + dateText, 0));
//            }

            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
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
    
        JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"BOP", "BOPF", "Dust 1"});
        
        JTextField buyerTxt = new JTextField(8);       
        JTextField qtyTxt = new JTextField(8);
 
        JButton dispatchBtn = new JButton("Generate Invoice and Dispatch");

        formCard.add(new JLabel("Buyer / Broker Name:"));
        formCard.add(buyerTxt);

        formCard.add(new JLabel("Select Tea Grade:"));
        formCard.add(gradeCombo);

        formCard.add(new JLabel("Quantity Sold (kg):"));
        formCard.add(qtyTxt);

        formCard.add(new JLabel()); // Spacer
        formCard.add(dispatchBtn);

        // Wrap inside GridBagLayout to keep form centered and neat
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(formCard, gbc);

        return panel;
    }

    // --- TAB 3: History/Reports Tab ---
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

        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinnerFilter = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerFilter, "yyyy-MM-dd");
        dateSpinnerFilter.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);

      
        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Tea Grade:"));
        filterPanel.add(gradeComboFilter);
        filterPanel.add(new JLabel("Date:"));
        filterPanel.add(dateSpinnerFilter);
        filterPanel.add(filterBtn);

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
    
    
    private JPanel createProductionBatchTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Production Batches"),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JTextField searchField = new JTextField(10);

        JComboBox<String> gradeComboFilter = new JComboBox<>(new String[]{"All Grades", "BOP", "BOPF", "Dust 1"});

        
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinnerFilter = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinnerFilter, "yyyy-MM-dd");
        dateSpinnerFilter.setEditor(dateEditor);
        dateEditor.getTextField().setColumns(8);

      
        JButton filterBtn = new JButton("Filter");

        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(new JLabel("Tea Grade:"));
        filterPanel.add(gradeComboFilter);
        filterPanel.add(new JLabel("Date:"));
        filterPanel.add(dateSpinnerFilter);
        filterPanel.add(filterBtn);

        JTable historyTable = new JTable(productionTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(productionTableModel);
        historyTable.setRowSorter(sorter);

        filterBtn.addActionListener(e -> {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            String searchTxt = searchField.getText().trim();
            if (!searchTxt.isEmpty()) {
                filters.add(RowFilter.regexFilter("(?i)" + searchTxt));
            }

            String grade = (String) gradeComboFilter.getSelectedItem();
            if (grade != null && !grade.equals("All Grades")) {
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
    
    
    private JPanel createWasteRecordsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel containerPanel = new JPanel(new BorderLayout(5, 5));
        containerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Waste Records"),
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

        JTable historyTable = new JTable(wasteRecordsTableModel);
        historyTable.setRowHeight(24);
        JScrollPane scrollPane = new JScrollPane(historyTable);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(wasteRecordsTableModel);
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
    
    
     private JPanel createReportsTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formCard = new JPanel(new GridLayout(10, 1, 10, 15));
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Generate Reports"),
            BorderFactory.createEmptyBorder(10, 15, 10, 10) 
        ));

        formCard.add(new JLabel("Today's Leaf Collection : 4444 kg"));
        formCard.add(new JLabel("Today's Production      : 1,120 kg"));
        formCard.add(new JLabel("Final Tea Stock         : 1,280 kg"));        
        formCard.add(new JLabel("Pending QC Batches      : 3"));        
        formCard.add(new JLabel("Rejected Batches        : 1"));
        formCard.add(new JLabel(" "));
        formCard.add(new JLabel("Sales Today             : $4,200"));       
        formCard.add(new JLabel("Suppliers Today         : 45"));        
        formCard.add(new JLabel("Workers Present         : 62"));


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(formCard, gbc);

        return panel;
    }



    
}