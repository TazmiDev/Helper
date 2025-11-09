package com.bp;

import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import burp.api.montoya.ui.UserInterface;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;

public class Main implements BurpExtension {
    @Override
    public void initialize(MontoyaApi api) {
        Logging logging = api.logging();
        UserInterface userInterface = api.userInterface();
        
        // 输出符号艺术字和中英文双语欢迎信息
        logging.logToOutput(" _  _  ____  __    ____  ____  ____ \r\n" + //
                            "/ )( \\(  __)(  )  (  _ \\(  __)(  _ \\\r\n" + //
                            ") __ ( ) _) / (_/\\ ) __/ ) _)  )   /\r\n" + //
                            "\\_)(_/(____)\\____/(__)  (____)(__\\_)");
        
        // 使用UTF-8编码确保中文正确显示
        try {
            logging.logToOutput(new String("\n欢迎使用Helper插件".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            logging.logToOutput(new String("Helper Extension loaded successfully".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            
            // 创建Helper标签页
            SwingUtilities.invokeLater(() -> {
                // 创建主面板
                JPanel mainPanel = new JPanel(new BorderLayout());
                
                // 创建标签页容器
                JTabbedPane tabbedPane = new JTabbedPane();
                
                // 创建模块化面板实例，传递api参数
                DashboardPanel dashboardPanel = new DashboardPanel(api);
                ConfigPanel configPanel = new ConfigPanel();
                GeneratorPanel generatorPanel = new GeneratorPanel(api);
                DecryptionPanel decryptionPanel = new DecryptionPanel();
                
                // 添加标签页到标签页容器
                tabbedPane.addTab("Dashboard", dashboardPanel.getPanel());
                tabbedPane.addTab("Generator", generatorPanel.getPanel());
                tabbedPane.addTab("Decryption", decryptionPanel.getPanel());
                tabbedPane.addTab("Config", configPanel.getPanel());
                
                // 添加组件到主面板
                mainPanel.add(tabbedPane, BorderLayout.CENTER);
                
                // Burp UI标签页
                userInterface.registerSuiteTab(new String("HelperExtension".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8), mainPanel);
                
                // 注册右键菜单提供者
                HelperContextMenuProvider contextMenuProvider = new HelperContextMenuProvider(api, dashboardPanel);
                api.userInterface().registerContextMenuItemsProvider(contextMenuProvider);
                
            });
        } catch (Exception e) {
            logging.logToError(e.getMessage());
        }
    }
}