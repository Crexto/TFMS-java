
package com.tfms;
import javax.swing.*;
import com.tfms.view.MainAppFrame;

public class Main {
    public static void main(String[] args) {
        // Set Look & Feel for native OS appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            MainAppFrame app = new MainAppFrame();
            
            app.setVisible(true);
        });
    }
}
