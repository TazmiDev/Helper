package com.bp.config;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 配置文件管理器
 * 负责读取和保存配置文件
 */
public class ConfigManager {
    private static String CONFIG_FILE_PATH;
    private static ConfigManager instance;
    private Properties properties;

    /**
     * 私有构造函数，实现单例模式
     */
    private ConfigManager() {
        // 动态获取用户主目录
        String userHome = System.getProperty("user.home");
        CONFIG_FILE_PATH = userHome + File.separator + "HelperConfig.txt";
        
        properties = new Properties();
        loadConfig();
    }

    /**
     * 获取ConfigManager实例
     * @return ConfigManager实例
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * 加载配置文件
     */
    private void loadConfig() {
        // 确保CONFIG_FILE_PATH已初始化
        if (CONFIG_FILE_PATH == null) {
            String userHome = System.getProperty("user.home");
            CONFIG_FILE_PATH = userHome + File.separator + "HelperConfig.txt";
        }
        
        File configFile = new File(CONFIG_FILE_PATH);
        
        // 如果配置文件不存在，创建默认配置文件
        if (!configFile.exists()) {
            createDefaultConfigFile();
            return;
        }

        try (InputStream input = new FileInputStream(configFile)) {
            // 使用UTF-8编码读取配置文件
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        } catch (IOException ex) {
            ex.printStackTrace();
            // 如果读取失败，创建默认配置文件
            createDefaultConfigFile();
        }
    }

    /**
     * 创建默认配置文件
     */
    private void createDefaultConfigFile() {
        // 设置默认值
        setDefaultValues();
        
        // 保存默认配置到文件
        saveConfig();
    }

    /**
     * 设置默认配置值
     */
    private void setDefaultValues() {
        // SQL注入扫描配置默认值
        properties.setProperty("sql.blacklist.domains", "");
        properties.setProperty("sql.exclude.suffix", ".jpg,.jpeg,.png,.gif,.css,.js,.ico,.woff,.woff2,.ttf,.otf");
        properties.setProperty("sql.test.cookie", "true");
        properties.setProperty("sql.accept.repeater", "false");
        properties.setProperty("sql.test.error.type", "true");
        properties.setProperty("sql.test.char.type", "true");
        properties.setProperty("sql.test.order.type", "true");
        properties.setProperty("sql.test.bool.type", "true");
        properties.setProperty("sql.test.diypayloads", "false");
        properties.setProperty("sql.test.number.type", "true");
        
        // AI API配置默认值
        properties.setProperty("ai.api.key", "");
        properties.setProperty("ai.api.model", "gpt-3.5-turbo");
        properties.setProperty("ai.api.url", "https://api.openai.com/v1/chat/completions");
    }

    /**
     * 保存配置到文件
     */
    public void saveConfig() {
        try {
            // 确保CONFIG_FILE_PATH已初始化
            if (CONFIG_FILE_PATH == null) {
                String userHome = System.getProperty("user.home");
                CONFIG_FILE_PATH = userHome + File.separator + "HelperConfig.txt";
            }
            
            // 确保目录存在
            File configFile = new File(CONFIG_FILE_PATH);
            File parentDir = configFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // 使用UTF-8编码保存配置文件
            try (OutputStream output = new FileOutputStream(CONFIG_FILE_PATH)) {
                properties.store(new OutputStreamWriter(output, StandardCharsets.UTF_8), "Helper Configuration");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取字符串配置值
     * @param key 配置键
     * @return 配置值
     */
    public String getString(String key) {
        return properties.getProperty(key, "");
    }

    /**
     * 获取字符串配置值，带默认值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public String getString(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 设置字符串配置值
     * @param key 配置键
     * @param value 配置值
     */
    public void setString(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * 获取布尔配置值
     * @param key 配置键
     * @return 配置值
     */
    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key, "false"));
    }

    /**
     * 获取布尔配置值，带默认值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    /**
     * 设置布尔配置值
     * @param key 配置键
     * @param value 配置值
     */
    public void setBoolean(String key, boolean value) {
        properties.setProperty(key, String.valueOf(value));
    }

    /**
     * 获取配置文件路径
     * @return 配置文件路径
     */
    public String getConfigFilePath() {
        // 确保CONFIG_FILE_PATH已初始化
        if (CONFIG_FILE_PATH == null) {
            String userHome = System.getProperty("user.home");
            CONFIG_FILE_PATH = userHome + File.separator + "HelperConfig.txt";
        }
        return CONFIG_FILE_PATH;
    }
}