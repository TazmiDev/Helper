package com.bp.decryptors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 哈希分析器，提供常见哈希算法的识别和计算功能
 */
public class HashAnalyzer implements Decryptor {
    
    public HashAnalyzer() {
    }
    
    @Override
    public String getName() {
        return "加密方式识别";
    }
    
    @Override
    public JPanel createOperationPanel(ResultCallback callback) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 输入区域 - 上面
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new TitledBorder("输入文本"));
        JTextArea inputArea = new JTextArea(10, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        
        // 按钮区域 - 中间
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton identifyButton = new JButton("识别哈希");
        
        // 设置按钮大小一致
        identifyButton.setPreferredSize(new Dimension(100, 30));
        
        buttonPanel.add(identifyButton);
        
        // 输出区域 - 下面
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(new TitledBorder("输出结果"));
        JTextArea outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);
        // 使用支持中文的字体
        outputArea.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
        
        // 创建垂直分割面板，包含输入和按钮区域
        JSplitPane topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, buttonPanel);
        topSplitPane.setDividerLocation(300);
        topSplitPane.setResizeWeight(0.8);
        topSplitPane.setEnabled(false);
        
        // 创建垂直分割面板，包含按钮和输出区域
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSplitPane, outputPanel);
        mainSplitPane.setDividerLocation(350);
        mainSplitPane.setResizeWeight(0.5);
        mainSplitPane.setEnabled(false);
        
        panel.add(mainSplitPane, BorderLayout.CENTER);
        
        // 识别哈希按钮事件
        identifyButton.addActionListener(e -> {
            try {
                String inputText = inputArea.getText().trim();
                
                if (inputText.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入要识别的哈希", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                StringBuilder result = new StringBuilder();
                result.append("哈希识别结果:\n");
                
                if (isMD5(inputText)) {
                    result.append("- 可能是MD5哈希\n");
                }
                
                if (isSHA1(inputText)) {
                    result.append("- 可能是SHA-1哈希\n");
                }
                
                if (isSHA256(inputText)) {
                    result.append("- 可能是SHA-256哈希\n");
                }
                
                if (isSHA512(inputText)) {
                    result.append("- 可能是SHA-512哈希\n");
                }
                
                if (isBase64(inputText)) {
                    try {
                        byte[] decoded = Base64.getDecoder().decode(inputText);
                        result.append("- 可能是Base64编码\n");
                        
                        // 尝试多种编码方式解码，并提供所有可能的结果
                        String[] encodings = {"UTF-8", "GBK", "GB2312", "Big5", "ISO-8859-1"};
                        boolean hasValidDecoding = false;
                        
                        for (String encoding : encodings) {
                            try {
                                String decodedText = new String(decoded, encoding);
                                // 检查解码结果是否包含乱码字符
                                boolean hasGarbled = decodedText.contains("�") || 
                                                   (encoding.equals("ISO-8859-1") && 
                                                    decodedText.matches(".*[\\x80-\\xFF].*"));
                                
                                if (!hasGarbled) {
                                    result.append("- ").append(encoding).append("解码: ").append(decodedText).append("\n");
                                    hasValidDecoding = true;
                                } else if (encoding.equals("ISO-8859-1")) {
                                    // ISO-8859-1总是显示，作为最后的备选
                                    result.append("- ").append(encoding).append("解码: ").append(decodedText).append("\n");
                                }
                            } catch (Exception ex) {
                                // 忽略编码错误，继续尝试下一个编码
                            }
                        }
                        
                        if (!hasValidDecoding) {
                            result.append("- 所有常见编码方式均无法正确解码\n");
                        }
                    } catch (Exception ex) {
                        result.append("- 可能是Base64编码（但解码失败）\n");
                    }
                }
                
                if (result.length() == "哈希识别结果:\n".length()) {
                    result.append("- 无法识别的哈希类型\n");
                }
                
                outputArea.setText(result.toString());
                if (callback != null) {
                    callback.setResultText(result.toString());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "哈希识别失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                System.err.println("哈希识别错误: " + ex.getMessage());
            }
        });
        
        return panel;
    }
    
    /**
     * 判断是否为MD5哈希
     */
    private boolean isMD5(String hash) {
        return hash.matches("^[a-fA-F0-9]{32}$");
    }
    
    /**
     * 判断是否为SHA-1哈希
     */
    private boolean isSHA1(String hash) {
        return hash.matches("^[a-fA-F0-9]{40}$");
    }
    
    /**
     * 判断是否为SHA-256哈希
     */
    private boolean isSHA256(String hash) {
        return hash.matches("^[a-fA-F0-9]{64}$");
    }
    
    /**
     * 判断是否为SHA-512哈希
     */
    private boolean isSHA512(String hash) {
        return hash.matches("^[a-fA-F0-9]{128}$");
    }
    
    /**
     * 判断是否为Base64编码
     */
    private boolean isBase64(String text) {
        try {
            Base64.getDecoder().decode(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}