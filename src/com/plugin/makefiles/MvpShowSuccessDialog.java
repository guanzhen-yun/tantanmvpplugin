package com.plugin.makefiles;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MvpShowSuccessDialog extends JDialog {
    JLabel jLabel;
    public MvpShowSuccessDialog() {
        setSize(220, 200);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel jPanelRoot = new JPanel(null);
        JLabel jLabel1 = new JLabel("创建完成");
        jLabel = new JLabel("");
        jLabel1.setBounds(70, 20, 100, 30);
        jLabel.setBounds(50, 60, 180, 30);
        jPanelRoot.add(jLabel1);
        jPanelRoot.add(jLabel);
        JButton jButton1 = new JButton("去刷新");
        jButton1.setBounds(75, 110, 60, 40);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        jPanelRoot.add(jButton1);
        setContentPane(jPanelRoot);
    }

    public static void main(String[] args) {
        MvpShowSuccessDialog dialog = new MvpShowSuccessDialog();
        dialog.setLabel("生成时间为10s");
        dialog.setVisible(true);
    }

    public void setLabel(String str) {
        jLabel.setText(jLabel.getText() + "\n" + str);
    }

}
