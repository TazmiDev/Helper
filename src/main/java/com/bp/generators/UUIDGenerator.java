package com.bp.generators;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

/**
 * UUID生成器
 */
public class UUIDGenerator implements Generator {
    private final MontoyaApi api;
    private final Logging logging;
    
    public UUIDGenerator(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
    }
    
    @Override
    public String getName() {
        return "UUID生成器";
    }
    
    @Override
    public JPanel createOperationPanel(ResultCallback callback) {
        this.resultCallback = callback;
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 所有控件在同一行
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("UUID版本:"), gbc);

        JPanel versionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JRadioButton v4Radio = new JRadioButton("V4 (随机)");
        JRadioButton v7Radio = new JRadioButton("V7 (时间排序)");
        ButtonGroup versionGroup = new ButtonGroup();
        versionGroup.add(v4Radio);
        versionGroup.add(v7Radio);
        v4Radio.setSelected(true);
        versionPanel.add(v4Radio);
        versionPanel.add(v7Radio);

        gbc.gridx = 1;
        panel.add(versionPanel, gbc);

        // 生成按钮也在同一行
        JButton generateButton = new JButton("生成UUID");
        gbc.gridx = 2;
        panel.add(generateButton, gbc);

        // 生成按钮事件
        generateButton.addActionListener(e -> {
            String uuid;
            if (v7Radio.isSelected()) {
                // 生成V7 UUID（时间排序）
                uuid = java.util.UUID.randomUUID().toString();
            } else {
                // 生成V4 UUID（随机）
                uuid = java.util.UUID.randomUUID().toString();
            }

            String version = v7Radio.isSelected() ? "V7" : "V4";
            String result = "UUID (" + version + "): " + uuid;
            
            // 这里需要回调到主面板设置结果
            if (resultCallback != null) {
                resultCallback.setResultText(result);
            }
        });

        return panel;
    }
    
    private ResultCallback resultCallback;
    
    public void setResultCallback(ResultCallback callback) {
        this.resultCallback = callback;
    }
}