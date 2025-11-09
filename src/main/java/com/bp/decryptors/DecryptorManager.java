package com.bp.decryptors;

import java.util.ArrayList;
import java.util.List;

/**
 * 解密器管理器，负责管理所有解密器实例
 */
public class DecryptorManager {
    private final List<Decryptor> decryptors;
    
    public DecryptorManager() {
        this.decryptors = new ArrayList<>();
        initializeDecryptors();
    }
    
    /**
     * 初始化所有解密器
     */
    private void initializeDecryptors() {
        // 添加各种解密器
        decryptors.add(new Base64Decryptor());
        decryptors.add(new URLDecryptor());
        decryptors.add(new HTMLDecryptor());
        decryptors.add(new AESDecryptor());
        decryptors.add(new DESDecryptor());
        decryptors.add(new HashAnalyzer());
        decryptors.add(new ROT13Decryptor());
        decryptors.add(new WeChatMiniProgramDecryptor());
    }
    
    /**
     * 获取所有解密器
     * @return 解密器列表
     */
    public List<Decryptor> getDecryptors() {
        return decryptors;
    }
    
    /**
     * 获取所有解密器名称
     * @return 解密器名称数组
     */
    public String[] getDecryptorNames() {
        String[] names = new String[decryptors.size()];
        for (int i = 0; i < decryptors.size(); i++) {
            names[i] = decryptors.get(i).getName();
        }
        return names;
    }
    
    /**
     * 根据索引获取解密器
     * @param index 索引
     * @return 解密器实例
     */
    public Decryptor getDecryptor(int index) {
        if (index >= 0 && index < decryptors.size()) {
            return decryptors.get(index);
        }
        return null;
    }
}