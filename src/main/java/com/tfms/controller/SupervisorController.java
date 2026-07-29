/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tfms.controller;

import com.tfms.model.entity.User;
//import com.tfms.model.dao.UserDAO;
import com.tfms.view.SupervisorPanel;
import com.tfms.view.MainAppFrame;


public class SupervisorController {
    private SupervisorPanel SuperView;
//    private UserDAO userDAO;
    private MainAppFrame mainApp;
    
    
    public SupervisorController(SupervisorPanel superView, MainAppFrame mainApp){
        this.SuperView = superView;
        this.mainApp = mainApp;
//        this.userDAO = new UserDAO();
        
        this.SuperView.LeafCollectionListener(e -> LeafCollection());
       
    }
    
    private void LeafCollection(){
        System.out.println("AWWWWW");
    }
}
