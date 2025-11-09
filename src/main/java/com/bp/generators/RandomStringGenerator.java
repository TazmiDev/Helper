package com.bp.generators;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.security.SecureRandom;

/**
 * 随机字符串生成器
 */
public class RandomStringGenerator implements Generator {
    private final MontoyaApi api;
    private final Logging logging;
    
    public RandomStringGenerator(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
    }
    
    @Override
    public String getName() {
        return "随机字符串生成器";
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
        panel.add(new JLabel("长度:"), gbc);

        JTextField lengthField = new JTextField("16", 5);
        gbc.gridx = 1;
        panel.add(lengthField, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("字符类型:"), gbc);

        JPanel charTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JCheckBox uppercaseCheck = new JCheckBox("大写字母");
        JCheckBox lowercaseCheck = new JCheckBox("小写字母");
        JCheckBox numbersCheck = new JCheckBox("数字");
        JCheckBox symbolsCheck = new JCheckBox("特殊字符");

        // 默认选中大写字母、小写字母和数字
        uppercaseCheck.setSelected(true);
        lowercaseCheck.setSelected(true);
        numbersCheck.setSelected(true);

        charTypePanel.add(uppercaseCheck);
        charTypePanel.add(lowercaseCheck);
        charTypePanel.add(numbersCheck);
        charTypePanel.add(symbolsCheck);

        gbc.gridx = 3;
        gbc.gridwidth = 2;
        panel.add(charTypePanel, gbc);

        // 生成按钮也在同一行
        JButton generateButton = new JButton("生成随机字符串");
        gbc.gridx = 5;
        gbc.gridwidth = 1;
        panel.add(generateButton, gbc);

        // 生成按钮事件
        generateButton.addActionListener(e -> {
            try {
                int length = Integer.parseInt(lengthField.getText());
                if (length <= 0) {
                    JOptionPane.showMessageDialog(panel, "长度必须大于0", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                StringBuilder charSet = new StringBuilder();
                if (uppercaseCheck.isSelected())
                    charSet.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                if (lowercaseCheck.isSelected())
                    charSet.append("abcdefghijklmnopqrstuvwxyz");
                if (numbersCheck.isSelected())
                    charSet.append("0123456789");
                if (symbolsCheck.isSelected())
                    charSet.append("!@#$%^&*()_+-=[]{}|;:,.<>?");

                if (charSet.length() == 0) {
                    JOptionPane.showMessageDialog(panel, "请至少选择一种字符类型", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                SecureRandom random = new SecureRandom();
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    result.append(charSet.charAt(random.nextInt(charSet.length())));
                }

                // 这里需要回调到主面板设置结果
                if (resultCallback != null) {
                    resultCallback.setResultText(result.toString());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "请输入有效的数字", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
    
    private ResultCallback resultCallback;
    
    public void setResultCallback(ResultCallback callback) {
        this.resultCallback = callback;
    }
}