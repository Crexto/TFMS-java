package com.tfms.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    
    private Image backgroundImage;
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn;

    public LoginPanel(MainAppFrame app) {
        
        try {
            backgroundImage = ImageIO.read(new File("src/main/resources/background.jpg"));
           
        } catch (Exception e) {
            System.err.println("Could not load background image: " + e.getMessage());
        }
        
        setLayout(new GridBagLayout());
        setBackground(new Color(15, 15, 15)); 

        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setBackground(new Color(255, 255, 255, 230));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 224, 230), 1),
                new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;


        JLabel titleLabel = new JLabel("TEA FACTORY MANAGEMENT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(34, 139, 34));

        JLabel subtitleLabel = new JLabel("Sign in to your account", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(120, 130, 140));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        userField = new JTextField(18);
        userField.setPreferredSize(new Dimension(220, 32));

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        passField = new JPasswordField(18);
        passField.setPreferredSize(new Dimension(220, 32));

     

        loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        loginBtn.setBackground(new Color(34, 139, 34)); // Dark green
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setOpaque(true);
        loginBtn.setBorderPainted(false); 
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(220, 36));

        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(titleLabel, gbc);

        gbc.gridy = 1;
        card.add(subtitleLabel, gbc);

        gbc.gridy = 2; gbc.gridwidth = 2;
        card.add(userLabel, gbc);
        gbc.gridy = 3;
        card.add(userField, gbc);

        gbc.gridy = 4;
        card.add(passLabel, gbc);
        gbc.gridy = 5;
        card.add(passField, gbc);

        gbc.gridy = 8;
        gbc.insets = new Insets(18, 8, 8, 8); 
        card.add(loginBtn, gbc);

        add(card);
    }
    
    public String getUsername(){
        return userField.getText().trim();
    }
    
    public String getPassword(){
        return new String(passField.getPassword());
    }
    
    public void loginListener(ActionListener listener){
        loginBtn.addActionListener(listener);

    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            // Draw image stretched to cover the full panel size
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}