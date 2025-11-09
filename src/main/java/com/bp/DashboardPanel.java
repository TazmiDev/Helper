package com.bp;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.http.message.requests.HttpRequest;
import burp.api.montoya.ui.UserInterface;
import burp.api.montoya.ui.editor.EditorOptions;
import burp.api.montoya.ui.editor.HttpRequestEditor;
import burp.api.montoya.ui.editor.HttpResponseEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dashboard面板类，负责显示仪表盘相关内容
 */
public class DashboardPanel {
    private JPanel panel;
    private DefaultTableModel trafficTableModel;
    private DefaultTableModel payloadTableModel;
    private List<HttpRequestResponse> trafficList;
    private List<HttpRequestResponse> payloadList;
    private JSplitPane tableSplitPane;
    private JSplitPane trafficDetailSplitPane;
    private HttpRequestEditor requestEditor;
    private HttpResponseEditor responseEditor;
    private MontoyaApi api;
    
    /**
     * 构造函数，初始化Dashboard面板
     */
    public DashboardPanel(MontoyaApi api) {
        this.api = api;
        trafficList = new ArrayList<>();
        payloadList = new ArrayList<>();
        createPanel();
    }
    
    /**
     * 创建Dashboard面板
     */
    private void createPanel() {
        panel = new JPanel(new BorderLayout());
        
        // 创建流量包表格模型
        String[] trafficColumnNames = {
            "ID",
            "Time",
            "Method",
            "URL",
            "Status"
        };
        trafficTableModel = new DefaultTableModel(trafficColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // 创建流量包表格
        JTable trafficTable = new JTable(trafficTableModel);
        trafficTable.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        trafficTable.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 12));
        
        // 设置流量包表格列宽
        trafficTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        trafficTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        trafficTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        trafficTable.getColumnModel().getColumn(3).setPreferredWidth(400);
        trafficTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        // 创建payload表格模型
        String[] payloadColumnNames = {
            "ID",
            "Payload",
            "Method",
            "URL",
            "Time",
            "Length",
            "Status"
        };
        payloadTableModel = new DefaultTableModel(payloadColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // 创建payload表格
        JTable payloadTable = new JTable(payloadTableModel);
        payloadTable.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        payloadTable.getTableHeader().setFont(new Font("Microsoft YaHei", Font.BOLD, 12));
        
        // 设置payload表格列宽
        payloadTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        payloadTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        payloadTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        payloadTable.getColumnModel().getColumn(3).setPreferredWidth(300);
        payloadTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        payloadTable.getColumnModel().getColumn(5).setPreferredWidth(200);
        payloadTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        
        // 创建表格的滚动面板
        JScrollPane trafficScrollPane = new JScrollPane(trafficTable);
        JScrollPane payloadScrollPane = new JScrollPane(payloadTable);
        
        // 创建左右分栏面板（表格部分）
        tableSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, trafficScrollPane, payloadScrollPane);
        tableSplitPane.setResizeWeight(0.5); // 左右比例1:1
        tableSplitPane.setOneTouchExpandable(false);
        tableSplitPane.setContinuousLayout(true);
        tableSplitPane.setDividerLocation(0.5);
        
        // 创建底部信息面板
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel infoLabel = new JLabel(
            new String("提示：在Burp Suite中右键流量包，选择HelperExtension -> SendToDashboard发送流量包到此面板".getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8)
        );
        infoLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        infoPanel.add(infoLabel);
        
        // 创建请求和响应编辑器（使用Burp自带的编辑器）
        UserInterface userInterface = api.userInterface();
        requestEditor = userInterface.createHttpRequestEditor(EditorOptions.READ_ONLY);
        responseEditor = userInterface.createHttpResponseEditor(EditorOptions.READ_ONLY);
        
        // 创建左右分栏面板（请求响应详情部分）
        trafficDetailSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
            requestEditor.uiComponent(), responseEditor.uiComponent());
        trafficDetailSplitPane.setResizeWeight(0.5); // 左右比例1:1
        trafficDetailSplitPane.setOneTouchExpandable(false);
        trafficDetailSplitPane.setContinuousLayout(true);
        trafficDetailSplitPane.setDividerLocation(0.5);
        
        // 添加流量包表格选择监听器
        trafficTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && trafficTable.getSelectedRow() != -1) {
                int selectedRow = trafficTable.getSelectedRow();
                HttpRequestResponse requestResponse = trafficList.get(selectedRow);
                displayTrafficDetails(requestResponse, true); // 只显示请求
            }
        });
        
        // 添加payload表格选择监听器
        payloadTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && payloadTable.getSelectedRow() != -1) {
                int selectedRow = payloadTable.getSelectedRow();
                HttpRequestResponse requestResponse = payloadList.get(selectedRow);
                displayTrafficDetails(requestResponse, false); // 显示请求和响应
            }
        });
        
        // 创建垂直分割面板（上半部分是流量列表，下半部分是请求响应详情）
        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tableSplitPane, trafficDetailSplitPane);
        verticalSplitPane.setDividerLocation(300);
        verticalSplitPane.setResizeWeight(0.4);
        
        // 更新面板布局
        panel.removeAll();
        panel.add(verticalSplitPane, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
    }
    
    /**
     * 添加流量包到Dashboard
     * @param requestResponse HTTP请求响应对象
     */
    public void addTrafficItem(HttpRequestResponse requestResponse) {
        // 获取请求信息
        HttpRequest request = requestResponse.request();
        String method = request.method();
        String url = request.url();
        
        // 获取响应状态码
        String statusCode = "N/A";
        if (requestResponse.response() != null) {
            statusCode = String.valueOf(requestResponse.response().statusCode());
        }
        
        // 获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        
        // 添加到流量列表
        trafficList.add(requestResponse);
        
        // 添加到表格
        Object[] rowData = {
            trafficList.size(),
            time,
            method,
            url,
            statusCode
        };
        trafficTableModel.addRow(rowData);
    }
    
    /**
     * 添加payload到Dashboard
     * @param requestResponse HTTP请求响应对象
     */
    public void addPayloadItem(HttpRequestResponse requestResponse) {
        // 获取请求信息
        HttpRequest request = requestResponse.request();
        String method = request.method();
        String url = request.url();
        
        // 获取响应状态码
        String statusCode = "N/A";
        if (requestResponse.response() != null) {
            statusCode = String.valueOf(requestResponse.response().statusCode());
        }
        
        // 获取当前时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date());
        
        // 获取请求体作为payload
        String payload = request.bodyToString();
        if (payload.length() > 100) {
            payload = payload.substring(0, 100) + "...";
        }
        
        // 获取请求体长度
        int length = request.bodyToString().length();
        
        // 添加到payload列表
        payloadList.add(requestResponse);
        
        // 添加到表格
        Object[] rowData = {
            payloadList.size(),
            payload,
            method,
            url,
            time,
            length,
            statusCode
        };
        payloadTableModel.addRow(rowData);
    }
    
    /**
     * 显示流量包详细信息（使用Burp自带的编辑器）
     * @param requestResponse HTTP请求响应对象
     * @param showRequestOnly 是否只显示请求
     */
    private void displayTrafficDetails(HttpRequestResponse requestResponse, boolean showRequestOnly) {
        // 设置请求到编辑器
        requestEditor.setRequest(requestResponse.request());
        
        if (showRequestOnly) {
            // 只显示请求，隐藏响应编辑器
            trafficDetailSplitPane.setDividerLocation(1.0);
            trafficDetailSplitPane.setResizeWeight(1.0);
        } else {
            // 显示请求和响应
            if (requestResponse.response() != null) {
                responseEditor.setResponse(requestResponse.response());
            } else {
                // 如果没有响应，创建一个空的响应
                responseEditor.setResponse(null);
            }
            trafficDetailSplitPane.setDividerLocation(0.5);
            trafficDetailSplitPane.setResizeWeight(0.5); // 左右比例1:1
        }
    }
    
    /**
     * 获取Dashboard面板
     * @return Dashboard面板
     */
    public JPanel getPanel() {
        return panel;
    }
}