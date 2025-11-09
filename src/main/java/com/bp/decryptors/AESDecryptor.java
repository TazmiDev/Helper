package com.bp.decryptors;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES解密器，提供AES加密和解密功能
 */
public class AESDecryptor implements Decryptor {
    
    public AESDecryptor() {
    }
    
    @Override
    public String getName() {
        return "AES";
    }
    
    @Override
    public JPanel createOperationPanel(ResultCallback callback) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 设置面板 - 顶部，密钥和模式配置在同一行
        JPanel settingsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        settingsPanel.setBorder(new TitledBorder("AES配置"));
        
        settingsPanel.add(new JLabel("密钥:"));
        JPasswordField keyField = new JPasswordField(20);
        settingsPanel.add(keyField);
        
        settingsPanel.add(new JLabel("IV:"));
        JPasswordField ivField = new JPasswordField(20);
        settingsPanel.add(ivField);
        
        settingsPanel.add(new JLabel("模式:"));
        String[] modes = {"AES/ECB/PKCS5Padding", "AES/CBC/PKCS5Padding", "AES/CFB/PKCS5Padding", "AES/OFB/PKCS5Padding"};
        JComboBox<String> modeCombo = new JComboBox<>(modes);
        settingsPanel.add(modeCombo);
        
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
        JButton decryptButton = new JButton("解密");
        JButton encryptButton = new JButton("加密");
        
        // 设置按钮大小一致
        decryptButton.setPreferredSize(new Dimension(100, 30));
        encryptButton.setPreferredSize(new Dimension(100, 30));
        
        buttonPanel.add(decryptButton);
        buttonPanel.add(encryptButton);
        
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
        panel.add(settingsPanel, BorderLayout.NORTH);
        panel.add(buttonOutputSplitPane, BorderLayout.CENTER);
        
        // 解密按钮事件
        decryptButton.addActionListener(e -> {
            try {
                String key = new String(keyField.getPassword());
                String iv = new String(ivField.getPassword());
                String ciphertext = inputArea.getText().trim();
                
                if (key.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入密钥", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (ciphertext.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入要解密的密文", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String mode = (String) modeCombo.getSelectedItem();
                byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                
                // 确保密钥长度为16、24或32字节
                byte[] keyBytesAdjusted;
                if (keyBytes.length < 16) {
                    keyBytesAdjusted = new byte[16];
                    System.arraycopy(keyBytes, 0, keyBytesAdjusted, 0, keyBytes.length);
                } else if (keyBytes.length > 32) {
                    keyBytesAdjusted = new byte[32];
                    System.arraycopy(keyBytes, 0, keyBytesAdjusted, 0, 32);
                } else if (keyBytes.length % 8 != 0) {
                    int newLength = ((keyBytes.length / 8) + 1) * 8;
                    keyBytesAdjusted = new byte[newLength];
                    System.arraycopy(keyBytes, 0, keyBytesAdjusted, 0, keyBytes.length);
                } else {
                    keyBytesAdjusted = keyBytes;
                }
                
                SecretKeySpec secretKey = new SecretKeySpec(keyBytesAdjusted, "AES");
                Cipher cipher = Cipher.getInstance(mode);
                
                if (mode.contains("CBC") || mode.contains("CFB") || mode.contains("OFB")) {
                    byte[] ivBytes;
                    if (iv.isEmpty()) {
                        // 如果没有提供IV，使用全零IV
                        ivBytes = new byte[16];
                    } else {
                        ivBytes = iv.getBytes(StandardCharsets.UTF_8);
                        if (ivBytes.length < 16) {
                            byte[] ivBytesAdjusted = new byte[16];
                            System.arraycopy(ivBytes, 0, ivBytesAdjusted, 0, ivBytes.length);
                            ivBytes = ivBytesAdjusted;
                        } else if (ivBytes.length > 16) {
                            ivBytes = new byte[16];
                            System.arraycopy(iv.getBytes(StandardCharsets.UTF_8), 0, ivBytes, 0, 16);
                        }
                    }
                    
                    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
                } else {
                    cipher.init(Cipher.DECRYPT_MODE, secretKey);
                }
                
                byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
                String decrypted = new String(decryptedBytes, StandardCharsets.UTF_8);
                outputArea.setText(decrypted);
                if (callback != null) {
                    callback.setResultText(decrypted);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "AES解密失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                System.err.println("AES解密错误: " + ex.getMessage());
            }
        });
        
        // 加密按钮事件
        encryptButton.addActionListener(e -> {
            try {
                String key = new String(keyField.getPassword());
                String iv = new String(ivField.getPassword());
                String plaintext = inputArea.getText();
                
                if (key.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入密钥", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (plaintext.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请输入要加密的明文", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String mode = (String) modeCombo.getSelectedItem();
                byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                
                // 确保密钥长度为16、24或32字节
                byte[] keyBytesAdjusted;
                if (keyBytes.length < 16) {
                    keyBytesAdjusted = new byte[16];
                    System.arraycopy(keyBytes, 0, keyBytesAdjusted, 0, keyBytes.length);
                } else if (keyBytes.length > 32) {
                    keyBytesAdjusted = new byte[32];
                    System.arraycopy(keyBytes, 0, keyBytesAdjusted, 0, 32);
                } else if (keyBytes.length % 8 != 0) {
                    int newLength = ((keyBytes.length / 8) + 1) * 8;
                    keyBytesAdjusted = new byte[newLength];
                    System.arraycopy(keyBytes, 0, keyBytesAdjusted, 0, keyBytes.length);
                } else {
                    keyBytesAdjusted = keyBytes;
                }
                
                SecretKeySpec secretKey = new SecretKeySpec(keyBytesAdjusted, "AES");
                Cipher cipher = Cipher.getInstance(mode);
                
                if (mode.contains("CBC") || mode.contains("CFB") || mode.contains("OFB")) {
                    byte[] ivBytes;
                    if (iv.isEmpty()) {
                        // 如果没有提供IV，生成随机IV
                        ivBytes = new byte[16];
                        new SecureRandom().nextBytes(ivBytes);
                    } else {
                        ivBytes = iv.getBytes(StandardCharsets.UTF_8);
                        if (ivBytes.length < 16) {
                            byte[] ivBytesAdjusted = new byte[16];
                            System.arraycopy(ivBytes, 0, ivBytesAdjusted, 0, ivBytes.length);
                            ivBytes = ivBytesAdjusted;
                        } else if (ivBytes.length > 16) {
                            ivBytes = new byte[16];
                            System.arraycopy(iv.getBytes(StandardCharsets.UTF_8), 0, ivBytes, 0, 16);
                        }
                    }
                    
                    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
                } else {
                    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                }
                
                byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
                String encrypted = Base64.getEncoder().encodeToString(encryptedBytes);
                outputArea.setText(encrypted);
                if (callback != null) {
                    callback.setResultText(encrypted);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "AES加密失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                System.err.println("AES加密错误: " + ex.getMessage());
            }
        });
        
        return panel;
    }
}