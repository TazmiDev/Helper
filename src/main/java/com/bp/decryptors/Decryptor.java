package com.bp.decryptors;

import javax.swing.JPanel;

/**
 * 解密器接口，所有解密器都需要实现此接口
 */
public interface Decryptor {
    /**
     * 获取解密器名称
     * @return 解密器名称
     */
    String getName();
    
    /**
     * 创建解密器的操作面板
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