package com.tfms.controller;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.util.Date;

import com.tfms.model.entity.Production;
import com.tfms.model.entity.Invoice;
import com.tfms.model.entity.Inventory;
import com.tfms.model.dao.ProductionDAO;
import com.tfms.model.dao.StockHistoryDAO;
import com.tfms.model.dao.InvoiceDAO;
import com.tfms.model.dao.InventoryDAO;
import com.tfms.view.ManagerPanel;
import com.tfms.view.MainAppFrame;

public class ManagerController {
    private final ManagerPanel manaView;
    private final InventoryDAO invDAO;
    private final InvoiceDAO invoiceDAO;
    private final StockHistoryDAO ShistoryDAO;
    private final ProductionDAO productionDAO;
    private final MainAppFrame mainApp;

    public ManagerController(ManagerPanel manaView, MainAppFrame mainApp) {
        this.manaView = manaView;
        this.mainApp = mainApp;
        this.invDAO = new InventoryDAO();
        this.invoiceDAO = new InvoiceDAO();
        this.ShistoryDAO = new StockHistoryDAO();
        this.productionDAO = new ProductionDAO();

        this.manaView.DispatchListener(e -> handleDispatch());
        this.manaView.ProductionListener(e -> handleProduction());
    }

    private void handleDispatch() {
        String buyer = manaView.getBuyer();
        int quantity = manaView.getDispatchQuantity();
        String grade = manaView.getGrade();

        if (buyer == null || buyer.trim().isEmpty()) {
            JOptionPane.showMessageDialog(manaView, "Please enter a buyer name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(manaView, "Dispatch quantity must be greater than zero.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Inventory tea = invDAO.getTea(grade);
 

        if (tea.getQuantity() < quantity) {
            JOptionPane.showMessageDialog(
                manaView,
                "Insufficient stock!\nAvailable: " + tea.getQuantity() + " kg\nRequested: " + quantity + " kg",
                "Stock Error",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String confirmMsg = String.format("Confirm Dispatch Invoice?\n\nBuyer: %s\nGrade: %s\nQuantity: %d kg", buyer, grade, quantity);
        int confirm = JOptionPane.showConfirmDialog(manaView, confirmMsg, "Confirm Dispatch", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Invoice invoice = new Invoice(buyer, tea, quantity);
        boolean invoiceSaved = invoiceDAO.invoiceInsert(invoice);

        if (invoiceSaved) {
            int newQuantity = tea.getQuantity() - quantity;
            invDAO.updateQuantity(tea.getId(), newQuantity);
            ShistoryDAO.insertRecord(tea.getId(), "Invoice", -quantity, newQuantity, LocalDateTime.now());

            JOptionPane.showMessageDialog(manaView, "Dispatch recorded & invoice generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(manaView, "Failed to process dispatch invoice. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleProduction() {
        Date date = manaView.getDate();
        int quantity = manaView.getProductionQuantity();
        String grade = manaView.getProductionGrade();
        String remarks = manaView.getRemarks();

        if (quantity <= 0) {
            JOptionPane.showMessageDialog(manaView, "Production quantity must be greater than zero.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Inventory tea = invDAO.getTea(grade);
       

        String confirmMsg = String.format("Confirm New Production Batch?\n\nGrade: %s\nQuantity: %d kg\nRemarks: %s", grade, quantity, remarks.isEmpty() ? "None" : remarks);
        int confirm = JOptionPane.showConfirmDialog(manaView, confirmMsg, "Confirm Production", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        Production production = new Production(sqlDate, tea, quantity, remarks);
        boolean prodSaved = productionDAO.productionInsert(production);

        if (prodSaved) {
            int newQuantity = tea.getQuantity() + quantity;
            invDAO.updateQuantity(tea.getId(), newQuantity);
            ShistoryDAO.insertRecord(tea.getId(), "Production", quantity, newQuantity, LocalDateTime.now());

            JOptionPane.showMessageDialog(manaView, "Production batch has been added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(manaView, "Failed to add production batch. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}