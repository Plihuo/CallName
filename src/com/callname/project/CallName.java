package com.callname.project;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class CallName {
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                CallNameFrame cf = new CallNameFrame();
                cf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                cf.setVisible(true);
            }
      });
    }
}