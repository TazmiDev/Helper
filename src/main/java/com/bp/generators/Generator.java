package com.bp.generators;

import javax.swing.JPanel;

/**
 * 生成器接口，所有生成器都需要实现此接口
 */
public interface Generator {
    /**
     * 获取生成器名称
     * @return 生成器名称
     */
    String getName();
    
    /**
     * 创建生成器的操作面板
     * @param callback 结果回调接口
     * @return 操作面板
     */
    JPanel createOperationPanel(ResultCallback callback);
    
    /**
     * 结果回调接口
     */
    interface ResultCallback {
        void setResultText(String text);
    }
}