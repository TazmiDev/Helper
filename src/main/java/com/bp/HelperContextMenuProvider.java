package com.bp;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.http.message.HttpRequestResponse;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;
import burp.api.montoya.ui.contextmenu.MessageEditorHttpRequestResponse;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HelperContextMenuProvider implements ContextMenuItemsProvider {
    private final MontoyaApi api;
    private final DashboardPanel dashboardPanel;

    public HelperContextMenuProvider(MontoyaApi api, DashboardPanel dashboardPanel) {
        this.api = api;
        this.dashboardPanel = dashboardPanel;
    }

    @Override
    public List<Component> provideMenuItems(ContextMenuEvent event) {
        List<Component> menuItemList = new ArrayList<>();
        
        // 检查是否有选中的HTTP请求/响应
        if (event.messageEditorRequestResponse().isPresent()) {
            MessageEditorHttpRequestResponse selectedRequestResponse = event.messageEditorRequestResponse().get();
            
            // 创建"HelperExtension"子菜单
            JMenu helperMenu = new JMenu("HelperExtension");
            
            // 创建"SendToDashboard"菜单项
            JMenuItem sendToDashboardItem = new JMenuItem("SendToDashboard");
            sendToDashboardItem.addActionListener(e -> {
                // 获取选中的请求/响应
                HttpRequestResponse requestResponse = selectedRequestResponse.requestResponse();
                
                // 发送到Dashboard
                dashboardPanel.addTrafficItem(requestResponse);
              
            });
            
            helperMenu.add(sendToDashboardItem);
            menuItemList.add(helperMenu);
        }
        
        return menuItemList;
    }
}