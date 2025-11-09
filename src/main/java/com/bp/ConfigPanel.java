package com.bp;

import com.bp.config.ConfigManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * 配置功能面板
 * 采用左侧边栏+右侧上下分区的布局形式
 */
public class ConfigPanel extends JFrame {
    private JPanel panel;
    private ConfigManager configManager;

    // 右侧面板组件
    private JPanel operationPanel; // 操作区
    private CardLayout operationCardLayout; // 操作区卡片布局
    private JPanel operationCards; // 操作区卡片容器
    
    // SQL注入扫描配置组件
    private JTextArea blacklistArea;
    private JTextArea excludeSuffixArea;
    private JCheckBox testCookieCheckBox;
    private JCheckBox acceptRepeaterCheckBox;
    private JCheckBox testErrorTypeCheckBox;
    private JCheckBox testCharTypeCheckBox;
    private JCheckBox testOrderTypeCheckBox;
    private JCheckBox testBoolTypeCheckBox;
    private JCheckBox testDiypayloadsCheckBox;
    private JCheckBox testNumberTypeCheckBox;
    private JTextField configFileField;
    
    // AI API配置组件
    private JPasswordField keyField;
    private JComboBox<String> modelCombo;
    private JTextField apiField;

    public ConfigPanel() {
        this.configManager = ConfigManager.getInstance();
        setTitle("配置设置");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        createPanel();
        setContentPane(panel);
    }

    /**
     * 创建配置面板
     */
    private void createPanel() {
        panel = new JPanel(new BorderLayout());

        // 创建左侧边栏
        JPanel sidebarPanel = createSidebar();

        // 创建右侧面板
        JPanel rightPanel = createRightPanel();

        // 创建左右分割面板
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, rightPanel);
        mainSplitPane.setDividerLocation(180);
        mainSplitPane.setResizeWeight(0.2);
        mainSplitPane.setOneTouchExpandable(false);
        mainSplitPane.setContinuousLayout(true);

        // 添加到主面板
        panel.add(mainSplitPane, BorderLayout.CENTER);
    }

    /**
     * 创建左侧边栏
     */
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 5));
        sidebar.setPreferredSize(new Dimension(180, 0));

        // 添加标题
        JLabel titleLabel = new JLabel("配置项", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        sidebar.add(titleLabel, BorderLayout.NORTH);

        // 创建配置项列表
        String[] configNames = {"SQL注入扫描配置", "AI API配置"};

        JList<String> configList = new JList<>(configNames);
        configList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configList.setSelectedIndex(0);
        configList.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 添加列表选择事件
        configList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = configList.getSelectedIndex();
                    showConfig(selectedIndex);
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(configList);
        sidebar.add(listScrollPane, BorderLayout.CENTER);

        return sidebar;
    }

    /**
     * 创建右侧面板
     */
    private JPanel createRightPanel() {
        // 创建操作区
        operationCardLayout = new CardLayout();
        operationCards = new JPanel(operationCardLayout);

        // 添加各种配置项的操作面板
        operationCards.add(createSQLInjectionConfigPanel(), "SQL注入扫描配置");
        operationCards.add(createAIAPIConfigPanel(), "AI API配置");

        // 默认显示第一个配置项
        showConfig(0);

        return operationCards;
    }

    /**
 * 创建SQL注入扫描配置面板
 */
private JPanel createSQLInjectionConfigPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // 配置面板
    JPanel configPanel = new JPanel(new BorderLayout());
    configPanel.setBorder(new TitledBorder("SQL注入扫描配置"));

    // 黑名单域名配置
    JPanel blacklistPanel = new JPanel(new BorderLayout());
    blacklistPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    blacklistPanel.add(new JLabel("黑名单域名:"), BorderLayout.NORTH);
    blacklistArea = new JTextArea(3, 30);
    blacklistArea.setLineWrap(true);
    blacklistArea.setWrapStyleWord(true);
    blacklistArea.setText(configManager.getString("sql.blacklist.domains"));
    JScrollPane blacklistScrollPane = new JScrollPane(blacklistArea);
    blacklistPanel.add(blacklistScrollPane, BorderLayout.CENTER);

    // 排除后缀配置
    JPanel excludeSuffixPanel = new JPanel(new BorderLayout());
    excludeSuffixPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    excludeSuffixPanel.add(new JLabel("排除后缀:"), BorderLayout.NORTH);
    excludeSuffixArea = new JTextArea(3, 30);
    excludeSuffixArea.setLineWrap(true);
    excludeSuffixArea.setWrapStyleWord(true);
    excludeSuffixArea.setText(configManager.getString("sql.exclude.suffix"));
    JScrollPane excludeSuffixScrollPane = new JScrollPane(excludeSuffixArea);
    excludeSuffixPanel.add(excludeSuffixScrollPane, BorderLayout.CENTER);

    // 扫描类型配置
    JPanel scanTypePanel = new JPanel(new BorderLayout());
    scanTypePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    scanTypePanel.add(new JLabel("扫描类型:"), BorderLayout.NORTH);
    
    JPanel checkBoxPanel = new JPanel(new GridLayout(0, 2, 10, 5));
    testCookieCheckBox = new JCheckBox("测试cookie");
    testCookieCheckBox.setSelected(configManager.getBoolean("sql.test.cookie"));
    acceptRepeaterCheckBox = new JCheckBox("接受repeater");
    acceptRepeaterCheckBox.setSelected(configManager.getBoolean("sql.accept.repeater"));
    testErrorTypeCheckBox = new JCheckBox("测试报错类型");
    testErrorTypeCheckBox.setSelected(configManager.getBoolean("sql.test.error.type"));
    testCharTypeCheckBox = new JCheckBox("测试字符类型");
    testCharTypeCheckBox.setSelected(configManager.getBoolean("sql.test.char.type"));
    testOrderTypeCheckBox = new JCheckBox("测试order类型");
    testOrderTypeCheckBox.setSelected(configManager.getBoolean("sql.test.order.type"));
    testBoolTypeCheckBox = new JCheckBox("测试bool类型");
    testBoolTypeCheckBox.setSelected(configManager.getBoolean("sql.test.bool.type"));
    testDiypayloadsCheckBox = new JCheckBox("测试diypayloads");
    testDiypayloadsCheckBox.setSelected(configManager.getBoolean("sql.test.diypayloads"));
    testNumberTypeCheckBox = new JCheckBox("测试数字类型");
    testNumberTypeCheckBox.setSelected(configManager.getBoolean("sql.test.number.type"));
    
    checkBoxPanel.add(testCookieCheckBox);
    checkBoxPanel.add(acceptRepeaterCheckBox);
    checkBoxPanel.add(testErrorTypeCheckBox);
    checkBoxPanel.add(testCharTypeCheckBox);
    checkBoxPanel.add(testOrderTypeCheckBox);
    checkBoxPanel.add(testBoolTypeCheckBox);
    checkBoxPanel.add(testDiypayloadsCheckBox);
    checkBoxPanel.add(testNumberTypeCheckBox);
    
    scanTypePanel.add(checkBoxPanel, BorderLayout.CENTER);

    // 配置文件目录
    JPanel configFilePanel = new JPanel(new BorderLayout());
    configFilePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    configFilePanel.add(new JLabel("配置文件目录:"), BorderLayout.NORTH);
    configFileField = new JTextField(30);
    configFileField.setText(configManager.getConfigFilePath());
    configFileField.setEditable(false);
    configFilePanel.add(configFileField, BorderLayout.CENTER);

    // 保存按钮
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton saveButton = new JButton("保存配置");
    saveButton.setPreferredSize(new Dimension(100, 30));
    saveButton.addActionListener(e -> saveSQLInjectionConfig());
    buttonPanel.add(saveButton);

    // 添加所有配置区域到主面板
    JSplitPane topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, blacklistPanel, excludeSuffixPanel);
    topSplitPane.setDividerLocation(150);
    topSplitPane.setResizeWeight(0.5);
    topSplitPane.setEnabled(false);

    JSplitPane middleSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSplitPane, scanTypePanel);
    middleSplitPane.setDividerLocation(300);
    middleSplitPane.setResizeWeight(0.5);
    middleSplitPane.setEnabled(false);

    JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, middleSplitPane, configFilePanel);
    bottomSplitPane.setDividerLocation(450);
    bottomSplitPane.setResizeWeight(0.5);
    bottomSplitPane.setEnabled(false);

    panel.add(bottomSplitPane, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    return panel;
}

    /**
 * 创建AI API配置面板
 */
private JPanel createAIAPIConfigPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(new EmptyBorder(10, 10, 10, 10));

    // 配置面板
    JPanel configPanel = new JPanel(new BorderLayout());
    configPanel.setBorder(new TitledBorder("AI API配置"));

    // API Key配置
    JPanel keyPanel = new JPanel(new BorderLayout());
    keyPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    keyPanel.add(new JLabel("API Key:"), BorderLayout.NORTH);
    keyField = new JPasswordField(30);
    keyField.setText(configManager.getString("ai.api.key"));
    keyPanel.add(keyField, BorderLayout.CENTER);

    // 模型配置
    JPanel modelPanel = new JPanel(new BorderLayout());
    modelPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    modelPanel.add(new JLabel("模型:"), BorderLayout.NORTH);
    String[] models = {
        // 国外模型
        "gpt-3.5-turbo", "gpt-4", "gpt-4-turbo", "gpt-4o", 
        "claude-3-sonnet", "claude-3-opus", "claude-3-haiku",
        // 国内模型
        "deepseek-chat", "deepseek-coder", "kimi", "moonshot-v1-8k", "moonshot-v1-32k", "moonshot-v1-128k",
        "qwen-turbo", "qwen-plus", "qwen-max", "qwen2-72b-instruct",
        "glm-3-turbo", "glm-4", "glm-4v", "chatglm3-6b",
        "yi-34b-chat", "yi-34b-chat-200k", "yi-large",
        "baichuan2-turbo", "baichuan2-turbo-192k",
        "hunyuan-lite", "hunyuan-standard", "hunyuan-pro",
        "spark-lite", "spark-pro", "spark-max",
        "ernie-bot", "ernie-bot-turbo", "ernie-bot-4"
    };
    modelCombo = new JComboBox<>(models);
    modelCombo.setEditable(true);
    modelCombo.setSelectedItem(configManager.getString("ai.api.model", "gpt-3.5-turbo"));
    modelPanel.add(modelCombo, BorderLayout.CENTER);

    // API接口配置
    JPanel apiPanel = new JPanel(new BorderLayout());
    apiPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    apiPanel.add(new JLabel("API接口:"), BorderLayout.NORTH);
    apiField = new JTextField(30);
    apiField.setText(configManager.getString("ai.api.url", "https://api.openai.com/v1/chat/completions"));
    apiPanel.add(apiField, BorderLayout.CENTER);

    // 保存按钮
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    JButton saveButton = new JButton("保存配置");
    saveButton.setPreferredSize(new Dimension(100, 30));
    saveButton.addActionListener(e -> saveAIAPIConfig());
    buttonPanel.add(saveButton);

    // 添加所有配置区域到主面板
    JSplitPane topSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, keyPanel, modelPanel);
    topSplitPane.setDividerLocation(100);
    topSplitPane.setResizeWeight(0.5);
    topSplitPane.setEnabled(false);

    JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSplitPane, apiPanel);
    bottomSplitPane.setDividerLocation(200);
    bottomSplitPane.setResizeWeight(0.5);
    bottomSplitPane.setEnabled(false);

    panel.add(bottomSplitPane, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    return panel;
}

    /**
     * 显示指定的配置项
     */
    private void showConfig(int index) {
        String[] configNames = {"SQL注入扫描配置", "AI API配置"};
        if (index >= 0 && index < configNames.length) {
            operationCardLayout.show(operationCards, configNames[index]);
        }
    }

    /**
 * 保存SQL注入扫描配置
 */
private void saveSQLInjectionConfig() {
    try {
        // 保存黑名单域名
        configManager.setString("sql.blacklist.domains", blacklistArea.getText());
        
        // 保存排除后缀
        configManager.setString("sql.exclude.suffix", excludeSuffixArea.getText());
        
        // 保存扫描类型配置
        configManager.setBoolean("sql.test.cookie", testCookieCheckBox.isSelected());
        configManager.setBoolean("sql.accept.repeater", acceptRepeaterCheckBox.isSelected());
        configManager.setBoolean("sql.test.error.type", testErrorTypeCheckBox.isSelected());
        configManager.setBoolean("sql.test.char.type", testCharTypeCheckBox.isSelected());
        configManager.setBoolean("sql.test.order.type", testOrderTypeCheckBox.isSelected());
        configManager.setBoolean("sql.test.bool.type", testBoolTypeCheckBox.isSelected());
        configManager.setBoolean("sql.test.diypayloads", testDiypayloadsCheckBox.isSelected());
        configManager.setBoolean("sql.test.number.type", testNumberTypeCheckBox.isSelected());
        
        // 保存配置到文件
        configManager.saveConfig();
        
        JOptionPane.showMessageDialog(this, "SQL注入扫描配置保存成功！", "保存成功", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "保存配置失败：" + ex.getMessage(), "保存失败", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

/**
 * 保存AI API配置
 */
private void saveAIAPIConfig() {
    try {
        // 保存API Key
        String apiKey = new String(keyField.getPassword());
        configManager.setString("ai.api.key", apiKey);
        
        // 保存模型
        String model = (String) modelCombo.getSelectedItem();
        configManager.setString("ai.api.model", model);
        
        // 保存API接口
        String apiUrl = apiField.getText();
        configManager.setString("ai.api.url", apiUrl);
        
        // 保存配置到文件
        configManager.saveConfig();
        
        JOptionPane.showMessageDialog(this, "AI API配置保存成功！", "保存成功", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "保存配置失败：" + ex.getMessage(), "保存失败", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    /**
     * 获取Config面板
     * @return Config面板
     */
    public JPanel getPanel() {
        return panel;
    }
}