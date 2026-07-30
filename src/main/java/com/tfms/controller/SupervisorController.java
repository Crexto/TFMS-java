/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tfms.controller;
import javax.swing.*;
import com.tfms.model.entity.LeafCollection;
import com.tfms.model.entity.User;
import com.tfms.model.entity.Supplier;
import com.tfms.util.UserSession;
import com.tfms.view.SupervisorPanel;
import com.tfms.view.MainAppFrame;
import com.tfms.model.dao.LeafCollectionDAO;


public class SupervisorController {
    private SupervisorPanel superView;
    private LeafCollectionDAO leafDAO;
    private MainAppFrame mainApp;
    
    
    public SupervisorController(SupervisorPanel superView, MainAppFrame mainApp){
        this.superView = superView;
        this.mainApp = mainApp;
        this.leafDAO = new LeafCollectionDAO();
        
        
        this.superView.LeafCollectionListener(e -> handleLeafCollection());
       
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
            selectedSupplier.getUsername(), 
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

        LeafCollection leaf = new LeafCollection(selectedSupplier.getId(), weight, user.getId());

        boolean success = leafDAO.leafCollect(leaf);

        if (success) {
            JOptionPane.showMessageDialog(superView, "Leaf collection record saved successfully!");
        } else {
            JOptionPane.showMessageDialog(superView, "Failed to save record to database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
