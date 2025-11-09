package com.bp.decryptors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * HTML解密器，提供HTML实体编码和解码功能
 */
public class HTMLDecryptor implements Decryptor {
    
    public HTMLDecryptor() {
    }
    
    @Override
    public String getName() {
        return "HTML实体";
    }
    
    @Override
    public JPanel createOperationPanel(ResultCallback callback) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 输入区域 - 中上部
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new TitledBorder("输入文本"));
        JTextArea inputArea = new JTextArea(10, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        
        // 按钮区域 - 中间
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton decodeButton = new JButton("解码");
        JButton encodeButton = new JButton("编码");
        
        // 设置按钮大小一致
        decodeButton.setPreferredSize(new Dimension(100, 30));
        encodeButton.setPreferredSize(new Dimension(100, 30));
        
        buttonPanel.add(decodeButton);
        buttonPanel.add(encodeButton);
        
        // 输出区域 - 中下部，与输入区域高度相同
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(new TitledBorder("输出结果"));
        JTextArea outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        
        // 创建垂直分割面板，包含输入区域和按钮
        JSplitPane inputButtonSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, buttonPanel);
        inputButtonSplitPane.setDividerLocation(250);
        inputButtonSplitPane.setResizeWeight(0.85);
        inputButtonSplitPane.setEnabled(false); // 禁用拖动分割线
        
        // 创建垂直分割面板，包含按钮和输出区域
        JSplitPane buttonOutputSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputButtonSplitPane, outputPanel);
        buttonOutputSplitPane.setDividerLocation(300);
        buttonOutputSplitPane.setResizeWeight(0.5);
        buttonOutputSplitPane.setEnabled(false); // 禁用拖动分割线
        
        // 添加组件到主面板
        panel.add(buttonOutputSplitPane, BorderLayout.CENTER);
        
        // 解码按钮事件
        decodeButton.addActionListener(e -> {
            String input = inputArea.getText().trim();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "请输入要解码的HTML实体编码", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                // 使用简单的HTML实体解码
                String decoded = input
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&amp;", "&")
                    .replace("&quot;", "\"")
                    .replace("&#39;", "'")
                    .replace("&#x27;", "'");
                
                // 处理数字实体引用
                java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("&#(\\d+);");
                java.util.regex.Matcher matcher = pattern.matcher(decoded);
                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    int codePoint = Integer.parseInt(matcher.group(1));
                    matcher.appendReplacement(sb, String.valueOf((char) codePoint));
                }
                matcher.appendTail(sb);
                
                // 处理十六进制实体引用
                pattern = java.util.regex.Pattern.compile("&#x([0-9a-fA-F]+);");
                matcher = pattern.matcher(sb.toString());
                sb = new StringBuffer();
                while (matcher.find()) {
                    int codePoint = Integer.parseInt(matcher.group(1), 16);
                    matcher.appendReplacement(sb, String.valueOf((char) codePoint));
                }
                matcher.appendTail(sb);
                
                decoded = sb.toString();
                outputArea.setText(decoded);
                if (callback != null) {
                    callback.setResultText(decoded);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "HTML实体解码失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 编码按钮事件
        encodeButton.addActionListener(e -> {
            String input = inputArea.getText();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "请输入要编码的文本", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                // 使用简单的HTML实体编码
                String encoded = input
                    .replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
                
                outputArea.setText(encoded);
                if (callback != null) {
                    callback.setResultText(encoded);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "HTML实体编码失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }
}