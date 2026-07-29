
package com.tfms.view;
import com.tfms.controller.LoginController;
import com.tfms.controller.SupervisorController;
import javax.swing.*;
import java.awt.*;


public class MainAppFrame extends JFrame {
    
    public static final String LOGIN_PANEL = "LOGIN";
    public static final String ADMIN_PANEL = "ADMIN_DASHBOARD";
    public static final String SUPERVISOR_PANEL = "SUPERVISOR_DASHBOARD";
    public static final String QC_PANEL = "QC_DASHBOARD";
    public static final String MANAGER_PANEL = "MANAGER_DASHBOARD";
    
    
    private CardLayout cardLayout;
    private JPanel mainContainer;
    
    public MainAppFrame() {
        setTitle("Tea Factory Management System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        
        LoginPanel loginPanel = new LoginPanel(this);
        new LoginController(loginPanel, this);
        
        SupervisorPanel superPanel = new SupervisorPanel(this);
        new SupervisorController(superPanel, this);
        
        mainContainer.add(loginPanel, LOGIN_PANEL);        
        mainContainer.add(new AdminPanel(this), ADMIN_PANEL);        
        mainContainer.add(new ManagerPanel(this), MANAGER_PANEL);
        mainContainer.add(superPanel, SUPERVISOR_PANEL);


        add(mainContainer);
    }
    
    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
    }
    
    
}
