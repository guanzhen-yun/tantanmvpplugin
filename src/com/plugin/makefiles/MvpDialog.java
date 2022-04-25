package com.plugin.makefiles;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * mvpDialog
 */
public class MvpDialog extends JDialog {
    public final static int TYPE_CONSTRAINT = 0; //约束布局
    public final static int TYPE_LINEAR = 1; //线性布局
    public final static int TYPE_RELATIVE = 2; //相对布局
    public final static int TYPE_FRAME = 3; //帧布局
    public final static int TYPE_OTHER = -1; //其他 用户自定义

    private JTextField jTextField;
    private String selfRootName;//自定义的根布局名字 和 TYPE_OTHER 搭配使用
    private int rootViewType = TYPE_CONSTRAINT;//默认约束布局
    private boolean isCreateNewMvpPackage;//是否需要创建新的文件夹包裹mvp里面的各个类 默认不创建
    private boolean isCreateMvp = true;//是否需要创建Mvp框架 默认创建

    public MvpDialog(OnActionListener onActionListener) {
        setSize(350, 250);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel jPanelRoot = new JPanel(null);
        JCheckBox jCheckBox1 = new JCheckBox("是否创建新的文件夹");
        jCheckBox1.setBounds(10, 50, 200, 20);
        JCheckBox jCheckBox2 = new JCheckBox("是否需要创建Presenter");
        jCheckBox2.setBounds(10, 100, 200, 20);
        jCheckBox2.setSelected(true);
        jCheckBox1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // 获取事件源（即复选框本身）
                JCheckBox checkBox = (JCheckBox) e.getSource();
                isCreateNewMvpPackage = checkBox.isSelected();
            }
        });
        jCheckBox2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // 获取事件源（即复选框本身）
                JCheckBox checkBox = (JCheckBox) e.getSource();
                isCreateMvp = checkBox.isSelected();
            }
        });
        jPanelRoot.add(jCheckBox1);
        jPanelRoot.add(jCheckBox2);

        JButton jButton1 = new JButton("确定");
        JButton jButton2 = new JButton("取消");
        jButton1.setBounds(100, 180, 50, 30);
        jButton2.setBounds(180, 180, 50, 30);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(rootViewType == TYPE_OTHER && ("".equals(jTextField.getText()) || null == jTextField.getText())) {
                    JOptionPane.showMessageDialog(
                            jPanelRoot,
                            "自定义xmlRoot需要填写root！",
                            "未填写自定义root",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    dispose();
                    onActionListener.onResult(isCreateMvp, isCreateNewMvpPackage, rootViewType, jTextField.getText());
                }
            }
        });
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               dispose();
            }
        });
        jPanelRoot.add(jButton1);
        jPanelRoot.add(jButton2);

        JLabel jLabel = new JLabel("xml根布局类型");
        jLabel.setBounds(220, 10, 150, 20);
        jPanelRoot.add(jLabel);
        JRadioButton jRadioButton1 = new JRadioButton("线性布局");
        jRadioButton1.setBounds(220, 35, 100, 20);
        JRadioButton jRadioButton2 = new JRadioButton("相对布局");
        jRadioButton2.setBounds(220, 60, 100, 20);
        JRadioButton jRadioButton3 = new JRadioButton("帧布局");
        jRadioButton3.setBounds(220, 85, 100, 20);
        JRadioButton jRadioButton4 = new JRadioButton("约束布局");
        jRadioButton4.setBounds(220, 110, 100, 20);
        JRadioButton jRadioButton5 = new JRadioButton("自定义根布局");
        jRadioButton5.setBounds(220, 135, 150, 20);
        ButtonGroup btnGroup = new ButtonGroup();
        jRadioButton1.addChangeListener(changeListener);
        jRadioButton2.addChangeListener(changeListener);
        jRadioButton3.addChangeListener(changeListener);
        jRadioButton4.addChangeListener(changeListener);
        jRadioButton5.addChangeListener(changeListener);
        btnGroup.add(jRadioButton1);
        btnGroup.add(jRadioButton2);
        btnGroup.add(jRadioButton3);
        btnGroup.add(jRadioButton4);
        btnGroup.add(jRadioButton5);
        jRadioButton4.setSelected(true);
        jPanelRoot.add(jRadioButton1);
        jPanelRoot.add(jRadioButton2);
        jPanelRoot.add(jRadioButton3);
        jPanelRoot.add(jRadioButton4);
        jPanelRoot.add(jRadioButton5);

        jTextField = new JTextField(8);
        jTextField.setFont(new Font(null, Font.PLAIN, 20));
        jTextField.setBounds(220, 160, 100, 20);
        jPanelRoot.add(jTextField);
        jTextField.setVisible(false);
        setContentPane(jPanelRoot);
    }

    ChangeListener changeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            JRadioButton jRadioButton = ((JRadioButton)e.getSource());
            if(jRadioButton.isSelected() && jRadioButton.getText().equals("自定义根布局")) {
                if(jTextField != null) {
                    jTextField.setVisible(true);
                }
                rootViewType = TYPE_OTHER;
            } else if(jRadioButton.isSelected()) {
                if(jTextField != null) {
                    jTextField.setVisible(false);
                }
                if(jRadioButton.getText().equals("线性布局")) {
                    rootViewType = TYPE_LINEAR;
                } else if(jRadioButton.getText().equals("相对布局")) {
                    rootViewType = TYPE_RELATIVE;
                } else if(jRadioButton.getText().equals("帧布局")) {
                    rootViewType = TYPE_FRAME;
                } else if(jRadioButton.getText().equals("约束布局")) {
                    rootViewType = TYPE_CONSTRAINT;
                }
            }
        }
    };

    public static void main(String[] args) {
        MvpDialog dialog = new MvpDialog(new OnActionListener() {
            @Override
            public void onResult(boolean isMvp, boolean isCreatePack, int xmlRootType, String selRoot) {
                System.out.println(isMvp + "," + isCreatePack + "," + xmlRootType + "," + selRoot);
            }
        });
        dialog.setVisible(true);
    }

    public interface OnActionListener {
        void onResult(boolean isMvp, boolean isCreatePack, int xmlRootType, String selRoot);
    }

}
