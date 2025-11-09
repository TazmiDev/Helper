package com.bp;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;
import com.bp.generators.Generator;
import com.bp.generators.GeneratorManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

/**
 * Generator面板类，负责提供各种生成器功能
 * 采用左侧边栏+右侧上下分区的布局形式
 */
public class GeneratorPanel {
    private JPanel panel;
    private MontoyaApi api;
    private Logging logging;
    private GeneratorManager generatorManager;

    // 右侧面板组件
    private JPanel operationPanel; // 操作区
    private JPanel resultPanel; // 结果区
    private CardLayout operationCardLayout; // 操作区卡片布局
    private JPanel operationCards; // 操作区卡片容器

    /**
     * 构造函数，初始化Generator面板
     */
    public GeneratorPanel(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
        this.generatorManager = new GeneratorManager(api);
        createPanel();
    }

    /**
     * 创建Generator面板
     */
    private void createPanel() {
        panel = new JPanel(new BorderLayout());

        // 创建左侧边栏
        JPanel sidebarPanel = createSidebar();

        // 创建右侧上下分区
        JSplitPane rightSplitPane = createRightPanel();

        // 创建左右分割面板
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebarPanel, rightSplitPane);
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
        JLabel titleLabel = new JLabel("生成器工具", JLabel.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        sidebar.add(titleLabel, BorderLayout.NORTH);

        // 创建生成器列表
        String[] generatorNames = generatorManager.getGeneratorNames();

        JList<String> generatorList = new JList<>(generatorNames);
        generatorList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        generatorList.setSelectedIndex(0);
        generatorList.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 添加列表选择事件
        generatorList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedIndex = generatorList.getSelectedIndex();
                    showGenerator(selectedIndex);
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(generatorList);
        sidebar.add(listScrollPane, BorderLayout.CENTER);

        return sidebar;
    }

    /**
     * 创建右侧上下分区
     */
    private JSplitPane createRightPanel() {
        // 创建操作区
        operationCardLayout = new CardLayout();
        operationCards = new JPanel(operationCardLayout);

        // 添加各种生成器的操作面板
        for (Generator generator : generatorManager.getGenerators()) {
            operationCards.add(generator.createOperationPanel(this::setResultText), generator.getName());
        }

        // 创建结果区
        resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(new EmptyBorder(5, 10, 10, 10));

        JLabel resultLabel = new JLabel("生成结果");
        resultLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        resultLabel.setBorder(new EmptyBorder(10, 0, 10, 0)); // 增加上下边距，增加标签高度
        resultPanel.add(resultLabel, BorderLayout.NORTH);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultPanel.add(resultScrollPane, BorderLayout.CENTER);

        // 创建上下分割面板
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, operationCards, resultPanel);
        rightSplitPane.setDividerLocation(0.25); // 调整分割位置，使结果区域占75%高度
        rightSplitPane.setResizeWeight(0.25); // 设置顶部操作区域占25%高度，底部结果区域占75%高度
        rightSplitPane.setOneTouchExpandable(false);
        rightSplitPane.setContinuousLayout(true);

        // 默认显示第一个生成器
        showGenerator(0);

        return rightSplitPane;
    }

    /**
     * 显示指定的生成器
     */
    private void showGenerator(int index) {
        Generator generator = generatorManager.getGenerator(index);
        if (generator != null) {
            operationCardLayout.show(operationCards, generator.getName());
        }
    }

    /**
     * 设置结果区域文本
     */
    private void setResultText(String text) {
        JScrollPane scrollPane = (JScrollPane) resultPanel.getComponent(1);
        JTextArea resultArea = (JTextArea) scrollPane.getViewport().getView();
        resultArea.setText(text);
    }

    /**
     * 获取主面板
     */
    public JPanel getPanel() {
        return panel;
    }
}