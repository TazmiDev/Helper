package com.bp;

import com.bp.decryptors.Decryptor;
import com.bp.decryptors.DecryptorManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * 解密功能面板
 * 采用左侧边栏+右侧上下分区的布局形式
 */
public class DecryptionPanel {
    private JPanel panel;
    private DecryptorManager decryptorManager;

    // 右侧面板组件
    private JPanel operationPanel; // 操作区
    private CardLayout operationCardLayout; // 操作区卡片布局
    private JPanel operationCards; // 操作区卡片容器

    public DecryptionPanel() {
        this.decryptorManager = new DecryptorManager();
        createPanel();
    }

    /**
     * 创建解密面板
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
        JLabel titleLabel = new JLabel("解密工具", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        sidebar.add(titleLabel, BorderLayout.NORTH);

        // 创建解密器列表
        String[] decryptorNames = decryptorManager.getDecryptorNames();

        JList<String> decryptorList = new JList<>(decryptorNames);
        decryptorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        decryptorList.setSelectedIndex(0);
        decryptorList.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 添加列表选择事件
        decryptorList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = decryptorList.getSelectedIndex();
                    showDecryptor(selectedIndex);
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(decryptorList);
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

        // 添加各种解密器的操作面板，不需要回调函数
        for (Decryptor decryptor : decryptorManager.getDecryptors()) {
            operationCards.add(decryptor.createOperationPanel(null), decryptor.getName());
        }

        // 默认显示第一个解密器
        showDecryptor(0);

        return operationCards;
    }

    /**
     * 显示指定的解密器
     */
    private void showDecryptor(int index) {
        Decryptor decryptor = decryptorManager.getDecryptor(index);
        if (decryptor != null) {
            operationCardLayout.show(operationCards, decryptor.getName());
        }
    }

    /**
     * 获取Decryption面板
     * @return Decryption面板
     */
    public JPanel getPanel() {
        return panel;
    }
}