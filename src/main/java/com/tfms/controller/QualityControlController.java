package com.tfms.controller;

import com.tfms.model.dao.LeafCollectionDAO;
import com.tfms.model.dao.QualityDAO;
import com.tfms.model.entity.LeafCollection;
import com.tfms.model.entity.QualityInspection;
import com.tfms.model.entity.User;
import com.tfms.view.QualityControlPanel;
import com.tfms.util.UserSession;
import com.tfms.view.MainAppFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.util.List;

public class QualityControlController {

    private QualityControlPanel view;
    private final MainAppFrame mainApp;
    private LeafCollectionDAO leafDAO;
    private QualityDAO qualityDAO;

    public QualityControlController(QualityControlPanel view, MainAppFrame mainApp) {

        this.view = view;
        this.mainApp = mainApp;
       

        leafDAO = new LeafCollectionDAO();
        qualityDAO = new QualityDAO();

        initialize();
    }

    private void initialize() {

        loadCollections();
        loadInspectionTable();
        loadApprovalTable();
        loadReportTable();
        updateDashboard();

        view.getBtnApprove().addActionListener(e -> approveInspection());

        view.getBtnReject().addActionListener(e -> rejectInspection());

        view.getBtnClear().addActionListener(e -> clearForm());

        view.getCmbCollection().addActionListener(e -> loadCollectionDetails());

    }

    //======================================================
    // Load Collection IDs
    //======================================================

    private void loadCollections() {

        view.getCmbCollection().removeAllItems();

        List<LeafCollection> list = leafDAO.getPendingCollections();

        for (LeafCollection c : list) {
            view.getCmbCollection().addItem(String.valueOf(c.getCollectionId()));
        }

    }

    //======================================================
    // Load Supplier & Weight
    //======================================================

    private void loadCollectionDetails() {

        if (view.getCmbCollection().getSelectedItem() == null)
            return;

        int id = Integer.parseInt(
                view.getCmbCollection().getSelectedItem().toString());

        LeafCollection c = leafDAO.getById(id);

        if (c != null) {

            view.getTxtSupplier().setText(c.getSupplierName());

            view.getTxtWeight().setText(
                    String.valueOf(c.getGrossWeight()));

        }

    }

    //======================================================
    // Approve
    //======================================================

    private void approveInspection() {

        try {
            User currentUser = UserSession.getLoggedInUser();

            QualityInspection q = new QualityInspection();

            q.setCollectionId(Integer.parseInt(
                    view.getCmbCollection().getSelectedItem().toString()));

            q.setInspectorId(currentUser.getId());

            q.setGrade(view.getCmbGrade().getSelectedItem().toString());

            q.setMoisture(Double.parseDouble(
                    view.getTxtMoisture().getText()));

            q.setCoarseLeaf(Double.parseDouble(
                    view.getTxtCoarseLeaf().getText()));

            q.setRemarks(view.getTxtRemarks().getText());

            q.setStatus("APPROVED");

            q.setInspectionDate(
                    new Date(System.currentTimeMillis()));

            if (qualityDAO.insert(q)) {

                JOptionPane.showMessageDialog(view,
                        "Inspection Saved.");

                clearForm();

                loadCollections();
                loadInspectionTable();
                loadApprovalTable();
                loadReportTable();
                updateDashboard();

            }

        } catch (Exception ex) {
            System.out.println(ex);

            JOptionPane.showMessageDialog(view,
                    "Invalid Input");

        }

    }

    //======================================================
    // Reject
    //======================================================

    private void rejectInspection() {

        int row = view.getApprovalTable().getSelectedRow();

        if (row == -1)
            return;

        int id = Integer.parseInt(
                view.getApprovalModel().getValueAt(row,0).toString());

        qualityDAO.reject(id);

        loadApprovalTable();
        updateDashboard();

    }

    //======================================================
    // Clear
    //======================================================

    private void clearForm() {

        view.getTxtSupplier().setText("");
        view.getTxtWeight().setText("");
        view.getTxtMoisture().setText("");
        view.getTxtCoarseLeaf().setText("");
        view.getTxtRemarks().setText("");

    }

    //======================================================
    // Load Tables
    //======================================================

    private void loadInspectionTable() {

        DefaultTableModel model = view.getInspectionModel();

        model.setRowCount(0);

        for (QualityInspection q : qualityDAO.getAll()) {

            model.addRow(new Object[]{
                    q.getInspectionId(),
                    q.getCollectionId(),
                    q.getSupplierName(),
                    q.getGrade(),
                    q.getMoisture(),
                    q.getCoarseLeaf(),
                    q.getStatus()
            });

        }

    }

    private void loadApprovalTable() {

        DefaultTableModel model = view.getApprovalModel();

        model.setRowCount(0);

        for (QualityInspection q : qualityDAO.getPending()) {

            model.addRow(new Object[]{
                    q.getCollectionId(),
                    q.getSupplierName(),
                    q.getWeight(),
                    q.getGrade(),
                    q.getStatus()
            });

        }

    }

    private void loadReportTable() {

        DefaultTableModel model = view.getReportModel();

        model.setRowCount(0);

        for (QualityInspection q : qualityDAO.getAll()) {

            model.addRow(new Object[]{
                    q.getInspectionDate(),
                    q.getCollectionId(),
                    q.getSupplierName(),
                    q.getGrade(),
                    q.getInspectorName()
            });

        }

    }

    //======================================================
    // Dashboard
    //======================================================

    private void updateDashboard() {

        view.updateDashboard(

                qualityDAO.getTodayCount(),

                qualityDAO.getApprovedCount(),

                qualityDAO.getRejectedCount(),

                qualityDAO.getAverageGrade()

        );

    }

}