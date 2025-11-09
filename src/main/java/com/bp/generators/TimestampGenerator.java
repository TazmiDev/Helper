package com.bp.generators;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

/**
 * 时间戳生成器
 */
public class TimestampGenerator implements Generator {
    private final MontoyaApi api;
    private final Logging logging;

    public TimestampGenerator(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
    }

    @Override
    public String getName() {
        return "时间戳生成器";
    }

    @Override
    public JPanel createOperationPanel(ResultCallback callback) {
        this.resultCallback = callback;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 顶部面板 - 时间戳类型选择
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(new JLabel("时间戳类型:"));
        
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JRadioButton currentRadio = new JRadioButton("当前时间");
        JRadioButton customRadio = new JRadioButton("自定义时间");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(currentRadio);
        typeGroup.add(customRadio);
        currentRadio.setSelected(true);
        typePanel.add(currentRadio);
        typePanel.add(customRadio);
        topPanel.add(typePanel);
        
        JCheckBox formatCheck = new JCheckBox("格式化为可读时间");
        topPanel.add(formatCheck);
        
        // 生成按钮
        JButton generateButton = new JButton("生成时间戳");
        generateButton.setPreferredSize(new Dimension(120, 30));
        topPanel.add(generateButton);
        
        // 自定义时间面板 - 默认隐藏
        JPanel customPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        customPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        customPanel.add(new JLabel("年:"));
        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(2025, 1970, 2100, 1));
        yearSpinner.setEditor(new JSpinner.NumberEditor(yearSpinner, "#"));
        customPanel.add(yearSpinner);
        
        customPanel.add(new JLabel("月:"));
        JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        monthSpinner.setEditor(new JSpinner.NumberEditor(monthSpinner, "#"));
        customPanel.add(monthSpinner);
        
        customPanel.add(new JLabel("日:"));
        JSpinner daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
        daySpinner.setEditor(new JSpinner.NumberEditor(daySpinner, "#"));
        customPanel.add(daySpinner);
        
        customPanel.add(new JLabel("时:"));
        JSpinner hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        hourSpinner.setEditor(new JSpinner.NumberEditor(hourSpinner, "#"));
        customPanel.add(hourSpinner);
        
        customPanel.add(new JLabel("分:"));
        JSpinner minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        minuteSpinner.setEditor(new JSpinner.NumberEditor(minuteSpinner, "#"));
        customPanel.add(minuteSpinner);
        
        customPanel.add(new JLabel("秒:"));
        JSpinner secondSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
        secondSpinner.setEditor(new JSpinner.NumberEditor(secondSpinner, "#"));
        customPanel.add(secondSpinner);
        
        customPanel.add(new JLabel("毫秒:"));
        JSpinner milliSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        milliSpinner.setEditor(new JSpinner.NumberEditor(milliSpinner, "#"));
        customPanel.add(milliSpinner);
        
        // 时间戳单位选择
        JPanel unitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        unitPanel.add(new JLabel("时间戳单位:"));
        JRadioButton secondsRadio = new JRadioButton("秒(s)");
        JRadioButton millisecondsRadio = new JRadioButton("毫秒(ms)");
        ButtonGroup unitGroup = new ButtonGroup();
        unitGroup.add(secondsRadio);
        unitGroup.add(millisecondsRadio);
        millisecondsRadio.setSelected(true);
        unitPanel.add(secondsRadio);
        unitPanel.add(millisecondsRadio);
        
        // 组装面板
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topPanel, BorderLayout.NORTH);
        centerPanel.add(customPanel, BorderLayout.CENTER);
        centerPanel.add(unitPanel, BorderLayout.SOUTH);
        
        panel.add(centerPanel, BorderLayout.CENTER);
        
        // 初始状态下隐藏自定义时间面板
        customPanel.setVisible(false);
        
        // 时间类型选择事件
        currentRadio.addActionListener(e -> {
            customPanel.setVisible(false);
            panel.revalidate();
            panel.repaint();
        });
        
        customRadio.addActionListener(e -> {
            customPanel.setVisible(true);
            panel.revalidate();
            panel.repaint();
        });
        
        // 生成按钮事件
        generateButton.addActionListener(e -> {
            StringBuilder result = new StringBuilder();
            long timestamp;
            
            if (currentRadio.isSelected()) {
                // 使用当前时间
                timestamp = System.currentTimeMillis();
                result.append("时间类型: 当前时间\n");
            } else {
                // 使用自定义时间
                int year = (Integer) yearSpinner.getValue();
                int month = (Integer) monthSpinner.getValue();
                int day = (Integer) daySpinner.getValue();
                int hour = (Integer) hourSpinner.getValue();
                int minute = (Integer) minuteSpinner.getValue();
                int second = (Integer) secondSpinner.getValue();
                int milli = (Integer) milliSpinner.getValue();
                
                // 验证日期有效性
                try {
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.set(year, month - 1, day, hour, minute, second);
                    cal.set(java.util.Calendar.MILLISECOND, milli);
                    timestamp = cal.getTimeInMillis();
                    
                    result.append("时间类型: 自定义时间\n");
                    result.append(String.format("自定义时间: %04d-%02d-%02d %02d:%02d:%02d.%03d\n", 
                            year, month, day, hour, minute, second, milli));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "无效的日期时间: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            // 根据单位选择输出时间戳
            if (secondsRadio.isSelected()) {
                long seconds = timestamp / 1000;
                result.append("时间戳(秒): ").append(seconds).append("\n");
                
                if (formatCheck.isSelected()) {
                    result.append("格式化时间: ").append(new java.util.Date(seconds * 1000).toString()).append("\n");
                }
            } else {
                result.append("时间戳(毫秒): ").append(timestamp).append("\n");
                
                if (formatCheck.isSelected()) {
                    result.append("格式化时间: ").append(new java.util.Date(timestamp).toString()).append("\n");
                }
            }
            
            // 回调到主面板设置结果
            if (resultCallback != null) {
                resultCallback.setResultText(result.toString());
            }
        });
        
        return panel;
    }

    private ResultCallback resultCallback;

    public void setResultCallback(ResultCallback callback) {
        this.resultCallback = callback;
    }
}