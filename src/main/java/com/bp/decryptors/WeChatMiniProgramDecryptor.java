package com.bp.decryptors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 微信小程序解密器，提供微信小程序加密数据解密功能
 */
public class WeChatMiniProgramDecryptor implements Decryptor {
    
    public WeChatMiniProgramDecryptor() {
    }
    
    @Override
    public String getName() {
        return "微信小程序";
    }
    
    @Override
    public JPanel createOperationPanel(ResultCallback callback) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 设置面板 - 顶部，sessionKey和IV配置在同一行
        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        settingsPanel.setBorder(new TitledBorder("微信小程序配置"));
        
        settingsPanel.add(new JLabel("SessionKey:"));
        JPasswordField sessionKeyField = new JPasswordField(40);
        settingsPanel.add(sessionKeyField);
        
        settingsPanel.add(new JLabel("IV:"));
        JPasswordField ivField = new JPasswordField(20);
        settingsPanel.add(ivField);
        
        // 输入区域 - 中上部
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new TitledBorder("加密数据(encryptedData)"));
        JTextArea inputArea = new JTextArea(10, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        
        // 按钮区域 - 中间
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton decryptButton = new JButton("解密");
        JButton encryptButton = new JButton("加密");
        
        // 设置按钮大小一致
        decryptButton.setPreferredSize(new Dimension(100, 30));
        encryptButton.setPreferredSize(new Dimension(100, 30));
        
        buttonPanel.add(decryptButton);
        buttonPanel.add(encryptButton);
        
        // 输出区域 - 中下部，与输入区域高度相同
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(new TitledBorder("解密结果"));
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
        
        // 创建主分割面板，包含设置区域和底部区域
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, settingsPanel, buttonOutputSplitPane);
        mainSplitPane.setDividerLocation(60);
        mainSplitPane.setResizeWeight(0.15);
        mainSplitPane.setEnabled(false); // 禁用拖动分割线
        
        panel.add(mainSplitPane, BorderLayout.CENTER);
        
        // 解密按钮事件
        decryptButton.addActionListener(e -> {
            try {
                String sessionKey = new String(sessionKeyField.getPassword());
                String iv = new String(ivField.getPassword());
                String encryptedData = inputArea.getText().trim();
                
                if (sessionKey.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入SessionKey", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (iv.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入IV", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (encryptedData.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入加密数据", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 微信小程序数据解密
                String decrypted = decryptWeChatData(encryptedData, sessionKey, iv);
                outputArea.setText(decrypted);
                if (callback != null) {
                    callback.setResultText(decrypted);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "微信小程序数据解密失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                System.err.println("微信小程序数据解密错误: " + ex.getMessage());
            }
        });
        
        // 加密按钮事件
        encryptButton.addActionListener(e -> {
            try {
                String sessionKey = new String(sessionKeyField.getPassword());
                String iv = new String(ivField.getPassword());
                String plainData = inputArea.getText().trim();
                
                if (sessionKey.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入SessionKey", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (iv.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入IV", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (plainData.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入要加密的数据", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // 微信小程序数据加密
                String encrypted = encryptWeChatData(plainData, sessionKey, iv);
                outputArea.setText(encrypted);
                if (callback != null) {
                    callback.setResultText(encrypted);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "微信小程序数据加密失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                System.err.println("微信小程序数据加密错误: " + ex.getMessage());
            }
        });
        
        return panel;
    }
    
    /**
     * 解密微信小程序数据
     * @param encryptedData 加密数据
     * @param sessionKey 会话密钥
     * @param iv 初始向量
     * @return 解密后的数据
     * @throws Exception 解密异常
     */
    private String decryptWeChatData(String encryptedData, String sessionKey, String iv) throws Exception {
        try {
            // Base64解码
            byte[] dataBytes = Base64.getDecoder().decode(encryptedData);
            byte[] keyBytes = Base64.getDecoder().decode(sessionKey);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            
            // AES-128-CBC解密
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(dataBytes);
            
            // 格式化JSON输出
            String jsonStr = new String(decryptedBytes, StandardCharsets.UTF_8);
            return formatJson(jsonStr);
        } catch (Exception e) {
            throw new Exception("解密失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 加密微信小程序数据
     * @param plainData 明文数据
     * @param sessionKey 会话密钥
     * @param iv 初始向量
     * @return 加密后的数据
     * @throws Exception 加密异常
     */
    private String encryptWeChatData(String plainData, String sessionKey, String iv) throws Exception {
        try {
            // Base64解码
            byte[] keyBytes = Base64.getDecoder().decode(sessionKey);
            byte[] ivBytes = Base64.getDecoder().decode(iv);
            
            // AES-128-CBC加密
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encryptedBytes = cipher.doFinal(plainData.getBytes(StandardCharsets.UTF_8));
            
            // Base64编码
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new Exception("加密失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 格式化JSON字符串
     * @param jsonStr JSON字符串
     * @return 格式化后的JSON字符串
     */
    private String formatJson(String jsonStr) {
        try {
            // 简单的JSON格式化，添加缩进
            StringBuilder formatted = new StringBuilder();
            int indent = 0;
            boolean inString = false;
            
            for (int i = 0; i < jsonStr.length(); i++) {
                char c = jsonStr.charAt(i);
                
                if (c == '"' && (i == 0 || jsonStr.charAt(i - 1) != '\\')) {
                    inString = !inString;
                    formatted.append(c);
                } else if (!inString) {
                    switch (c) {
                        case '{':
                        case '[':
                            formatted.append(c).append('\n');
                            indent++;
                            for (int j = 0; j < indent; j++) {
                                formatted.append("  ");
                            }
                            break;
                        case '}':
                        case ']':
                            formatted.append('\n');
                            indent--;
                            for (int j = 0; j < indent; j++) {
                                formatted.append("  ");
                            }
                            formatted.append(c);
                            break;
                        case ',':
                            formatted.append(c).append('\n');
                            for (int j = 0; j < indent; j++) {
                                formatted.append("  ");
                            }
                            break;
                        case ':':
                            formatted.append(c).append(' ');
                            break;
                        default:
                            if (c != ' ') {
                                formatted.append(c);
                            }
                            break;
                    }
                } else {
                    formatted.append(c);
                }
            }
            
            return formatted.toString();
        } catch (Exception e) {
            // 格式化失败，返回原始字符串
            return jsonStr;
        }
    }
}