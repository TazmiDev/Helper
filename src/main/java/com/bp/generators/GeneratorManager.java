package com.bp.generators;

import burp.api.montoya.MontoyaApi;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成器管理器，负责管理所有生成器实例
 */
public class GeneratorManager {
    private final List<Generator> generators;
    private final MontoyaApi api;
    
    public GeneratorManager(MontoyaApi api) {
        this.api = api;
        this.generators = new ArrayList<>();
        initializeGenerators();
    }
    
    /**
     * 初始化所有生成器
     */
    private void initializeGenerators() {
        // 添加各种生成器
        generators.add(new RandomStringGenerator(api));
        generators.add(new TimestampGenerator(api));
        generators.add(new UUIDGenerator(api));
        generators.add(new XSSPayloadGenerator(api));
        generators.add(new IdentityGenerator(api));
        generators.add(new EnterpriseInfoGenerator(api));
    }
    
    /**
     * 获取所有生成器
     * @return 生成器列表
     */
    public List<Generator> getGenerators() {
        return generators;
    }
    
    /**
     * 获取所有生成器名称
     * @return 生成器名称数组
     */
    public String[] getGeneratorNames() {
        String[] names = new String[generators.size()];
        for (int i = 0; i < generators.size(); i++) {
            names[i] = generators.get(i).getName();
        }
        return names;
    }
    
    /**
     * 根据索引获取生成器
     * @param index 索引
     * @return 生成器实例
     */
    public Generator getGenerator(int index) {
        if (index >= 0 && index < generators.size()) {
            return generators.get(index);
        }
        return null;
    }
}