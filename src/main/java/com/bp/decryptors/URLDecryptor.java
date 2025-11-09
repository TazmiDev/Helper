package com.bp.decryptors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * URL解密器，提供URL编码和解码功能
 */
public class URLDecryptor implements Decryptor {
    
    public URLDecryptor() {
    }
    
    @Override
    public String getName() {
        return "URL编码";
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
                JOptionPane.showMessageDialog(panel, "请输入要解码的URL编码文本", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String decoded = URLDecoder.decode(input, StandardCharsets.UTF_8.name());
                outputArea.setText(decoded);
                if (callback != null) {
                    callback.setResultText(decoded);
                }
            } catch (IllegalArgumentException | UnsupportedEncodingException ex) {
                JOptionPane.showMessageDialog(panel, "URL解码失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
                String encoded = URLEncoder.encode(input, StandardCharsets.UTF_8.name());
                outputArea.setText(encoded);
                if (callback != null) {
                    callback.setResultText(encoded);
                }
            } catch (UnsupportedEncodingException ex) {
                JOptionPane.showMessageDialog(panel, "URL编码失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        return panel;
    }
}