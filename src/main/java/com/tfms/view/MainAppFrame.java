package com.tfms.view;

import com.tfms.controller.AdminController;
import com.tfms.controller.LoginController;
import com.tfms.controller.ManagerController;
import com.tfms.controller.QualityControlController;
import com.tfms.controller.SupervisorController;

import javax.swing.*;
import java.awt.*;

public class MainAppFrame extends JFrame {
    
    public static final String LOGIN_PANEL = "LOGIN";
    public static final String ADMIN_PANEL = "ADMIN_DASHBOARD";
    public static final String SUPERVISOR_PANEL = "SUPERVISOR_DASHBOARD";
    public static final String QC_PANEL = "QC_DASHBOARD";
    public static final String MANAGER_PANEL = "MANAGER_DASHBOARD";
    
    private final CardLayout cardLayout;
    private final JPanel mainContainer;
    
    public MainAppFrame() {
        setTitle("Tea Factory Management System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        LoginPanel loginPanel = new LoginPanel(this);
        SupervisorPanel superPanel = new SupervisorPanel(this);
        ManagerPanel manaPanel = new ManagerPanel(this);
        QualityControlPanel qualityPanel = new QualityControlPanel(this);
        AdminPanel adminPanel = new AdminPanel(this);
        
        new LoginController(loginPanel, this);
        new SupervisorController(superPanel, this);
        new ManagerController(manaPanel, this);
        new QualityControlController(qualityPanel, this);
        new AdminController(adminPanel, this);
        
        mainContainer.add(loginPanel, LOGIN_PANEL);        
        mainContainer.add(adminPanel, ADMIN_PANEL);        
        mainContainer.add(manaPanel, MANAGER_PANEL);
        mainContainer.add(qualityPanel, QC_PANEL);
        mainContainer.add(superPanel, SUPERVISOR_PANEL);

        add(mainContainer);
        
        showScreen(LOGIN_PANEL);
    }
    
    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
    }
}