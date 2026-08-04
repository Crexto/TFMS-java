
package com.tfms.controller;

import com.tfms.model.entity.User;
import com.tfms.model.dao.UserDAO;
import com.tfms.view.LoginPanel;
import com.tfms.view.MainAppFrame;
import com.tfms.util.UserSession;

import javax.swing.*;

public class LoginController {
    private LoginPanel loginView;
    private UserDAO userDAO;
    private MainAppFrame mainApp;
    
    
    public LoginController(LoginPanel loginView, MainAppFrame mainApp){
        this.loginView = loginView;
        this.mainApp = mainApp;
        this.userDAO = new UserDAO();
        
        this.loginView.loginListener(e -> login());
       
    }
    
    private void login(){
        String username = loginView.getUsername();
        String password = loginView.getPassword();
       
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = userDAO.validate(username, password);
        
        if (user != null) {
            JOptionPane.showMessageDialog(loginView, "Welcome " + user.getUsername() + "!");
            UserSession.setLoggedInUser(user);
            loginView.clearFields();
            
            switch (user.getRole()) {
                case 0: 
                    mainApp.showScreen(MainAppFrame.ADMIN_PANEL); 
                    break;
                case 1: 
                    mainApp.showScreen(MainAppFrame.SUPERVISOR_PANEL);
                    
                    break;
                case 2: 
                    mainApp.showScreen(MainAppFrame.QC_PANEL); 
                    break;
                case 3: 
                    mainApp.showScreen(MainAppFrame.MANAGER_PANEL); 
                    break;
                default:
                    JOptionPane.showMessageDialog(loginView, "Unknown role level: " + user.getRole(), "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(loginView, "Invalid Username or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
}
