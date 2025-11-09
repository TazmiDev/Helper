package com.bp.generators;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.security.SecureRandom;
import java.util.Calendar;

/**
 * 企业信息生成器，生成包括企业名称、统一社会信用代码在内的虚拟企业信息
 */
public class EnterpriseInfoGenerator implements Generator {
    private final MontoyaApi api;
    private final Logging logging;
    private final SecureRandom random = new SecureRandom();
    private ResultCallback resultCallback;

    public EnterpriseInfoGenerator(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
    }

    @Override
    public String getName() {
        return "企业信息生成器";
    }

    @Override
    public JPanel createOperationPanel(ResultCallback callback) {
        this.resultCallback = callback;
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // 设置面板 - 顶部
        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder("生成设置"),
            new EmptyBorder(15, 10, 15, 10) // 增加上下边距，增加面板高度
        ));
        
        // 第一行：企业类型、所属行业、企业规模
        JPanel row1Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        row1Panel.setBorder(new EmptyBorder(5, 0, 5, 0)); // 增加上下边距
        row1Panel.add(new JLabel("企业类型:"));
        String[] enterpriseTypes = {"有限公司", "股份有限公司", "合伙企业", "个人独资企业", "集体所有制", "国有独资公司"};
        JComboBox<String> enterpriseTypeCombo = new JComboBox<>(enterpriseTypes);
        row1Panel.add(enterpriseTypeCombo);
        
        row1Panel.add(new JLabel("所属行业:"));
        String[] industries = {"信息技术", "金融服务", "制造业", "零售业", "建筑业", "教育培训", "医疗健康", "物流运输", "餐饮服务", "房地产"};
        JComboBox<String> industryCombo = new JComboBox<>(industries);
        row1Panel.add(industryCombo);
        
        row1Panel.add(new JLabel("企业规模:"));
        String[] scales = {"小型企业", "中型企业", "大型企业", "微型企业"};
        JComboBox<String> scaleCombo = new JComboBox<>(scales);
        row1Panel.add(scaleCombo);
        
        // 第二行：注册地区
        JPanel row2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        row2Panel.setBorder(new EmptyBorder(5, 0, 5, 0)); // 增加上下边距
        row2Panel.add(new JLabel("注册地区:"));
        String[] regions = {"北京", "上海", "广州", "深圳", "杭州", "成都", "武汉", "西安", "南京", "重庆"};
        JComboBox<String> regionCombo = new JComboBox<>(regions);
        row2Panel.add(regionCombo);
        
        // 第三行：生成内容选项
        JPanel row3Panel = new JPanel(new BorderLayout());
        row3Panel.setBorder(new EmptyBorder(10, 0, 10, 0)); // 增加上下边距
        row3Panel.add(new JLabel("生成内容:"), BorderLayout.NORTH);
        JPanel optionsPanel = new JPanel(new GridLayout(2, 5, 5, 5));
        
        JCheckBox nameCheck = new JCheckBox("企业名称", true);
        JCheckBox codeCheck = new JCheckBox("统一社会信用代码", true);
        JCheckBox addressCheck = new JCheckBox("注册地址", true);
        JCheckBox legalPersonCheck = new JCheckBox("法定代表人", true);
        JCheckBox capitalCheck = new JCheckBox("注册资本", true);
        JCheckBox phoneCheck = new JCheckBox("联系电话", true);
        JCheckBox emailCheck = new JCheckBox("电子邮箱", true);
        JCheckBox establishDateCheck = new JCheckBox("成立日期", true);
        JCheckBox businessScopeCheck = new JCheckBox("经营范围", true);
        
        optionsPanel.add(nameCheck);
        optionsPanel.add(codeCheck);
        optionsPanel.add(addressCheck);
        optionsPanel.add(legalPersonCheck);
        optionsPanel.add(capitalCheck);
        optionsPanel.add(phoneCheck);
        optionsPanel.add(emailCheck);
        optionsPanel.add(establishDateCheck);
        optionsPanel.add(businessScopeCheck);
        
        row3Panel.add(optionsPanel, BorderLayout.CENTER);
        
        // 第四行：生成按钮
        JPanel row4Panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        row4Panel.setBorder(new EmptyBorder(10, 0, 10, 0)); // 增加上下边距
        JButton generateButton = new JButton("生成企业信息");
        generateButton.setPreferredSize(new Dimension(150, 30));
        row4Panel.add(generateButton);
        
        // 将所有行添加到设置面板
        settingsPanel.add(row1Panel, BorderLayout.NORTH);
        settingsPanel.add(row2Panel, BorderLayout.CENTER);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(row3Panel, BorderLayout.NORTH);
        centerPanel.add(row4Panel, BorderLayout.CENTER);
        settingsPanel.add(centerPanel, BorderLayout.SOUTH);
        
        // 使用垂直布局添加各组件
        panel.add(settingsPanel, BorderLayout.NORTH);
        
        // 生成按钮事件
        generateButton.addActionListener(e -> {
            StringBuilder result = new StringBuilder();
            
            if (nameCheck.isSelected()) {
                String enterpriseType = (String) enterpriseTypeCombo.getSelectedItem();
                String industry = (String) industryCombo.getSelectedItem();
                result.append("企业名称: ").append(generateEnterpriseName(enterpriseType, industry)).append("\n");
            }
            
            if (codeCheck.isSelected()) {
                result.append("统一社会信用代码: ").append(generateUnifiedSocialCreditCode()).append("\n");
            }
            
            if (addressCheck.isSelected()) {
                String region = (String) regionCombo.getSelectedItem();
                result.append("注册地址: ").append(generateAddress(region)).append("\n");
            }
            
            if (legalPersonCheck.isSelected()) {
                result.append("法定代表人: ").append(generateLegalPerson()).append("\n");
            }
            
            if (capitalCheck.isSelected()) {
                String scale = (String) scaleCombo.getSelectedItem();
                result.append("注册资本: ").append(generateRegisteredCapital(scale)).append("\n");
            }
            
            if (phoneCheck.isSelected()) {
                result.append("联系电话: ").append(generatePhoneNumber()).append("\n");
            }
            
            if (emailCheck.isSelected()) {
                result.append("电子邮箱: ").append(generateEmail()).append("\n");
            }
            
            if (establishDateCheck.isSelected()) {
                result.append("成立日期: ").append(generateEstablishDate()).append("\n");
            }
            
            if (businessScopeCheck.isSelected()) {
                String industry = (String) industryCombo.getSelectedItem();
                result.append("经营范围: ").append(generateBusinessScope(industry)).append("\n");
            }
            
            if (resultCallback != null) {
                resultCallback.setResultText(result.toString());
            }
        });
        
        return panel;
    }
    
    /**
     * 生成企业名称
     */
    private String generateEnterpriseName(String type, String industry) {
        // 常见企业名称前缀
        String[] prefixes = {"中", "华", "国", "民", "金", "银", "汇", "通", "达", "信", "创", "新", "科", "技", "盛", "世", "天", "地", "东", "西"};
        
        // 常见企业名称中缀
        String[] infixes = {"科", "技", "信", "息", "金", "融", "投", "资", "贸", "易", "商", "务", "文", "化", "教", "育", "医", "疗", "建", "筑"};
        
        // 常见企业名称后缀
        String[] suffixes = {"有", "限", "公", "司", "股", "份", "集", "团", "控", "股", "实", "业", "发", "展", "投", "资", "管", "理", "咨", "询"};
        
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String infix = infixes[random.nextInt(infixes.length)];
        
        // 根据行业选择合适的后缀
        String suffix;
        if ("信息技术".equals(industry)) {
            suffix = "科技";
        } else if ("金融服务".equals(industry)) {
            suffix = "金融";
        } else if ("制造业".equals(industry)) {
            suffix = "实业";
        } else if ("零售业".equals(industry)) {
            suffix = "商贸";
        } else {
            suffix = suffixes[random.nextInt(suffixes.length)];
        }
        
        return "HelperBP" + prefix + infix + suffix + type;
    }
    
    /**
     * 生成统一社会信用代码
     */
    private String generateUnifiedSocialCreditCode() {
        // 统一社会信用代码由18位数字或大写英文字母组成
        // 第1位：登记管理部门代码（1-9）
        // 第2位：机构类别代码（1-9, A-Y）
        // 第3-8位：登记管理机关行政区划码（6位数字）
        // 第9-17位：主体标识码（9位数字或大写英文字母）
        // 第18位：校验码
        
        StringBuilder code = new StringBuilder();
        
        // 第1位：登记管理部门代码
        code.append(1 + random.nextInt(9));
        
        // 第2位：机构类别代码
        char categoryCode = (char) ('1' + random.nextInt(9));
        if (random.nextBoolean()) {
            categoryCode = (char) ('A' + random.nextInt(25));
        }
        code.append(categoryCode);
        
        // 第3-8位：登记管理机关行政区划码
        String[] regions = {"110000", "120000", "130000", "140000", "150000", "210000", "220000", "230000", "310000", "320000"};
        code.append(regions[random.nextInt(regions.length)]);
        
        // 第9-17位：主体标识码
        for (int i = 0; i < 9; i++) {
            if (random.nextBoolean()) {
                code.append(random.nextInt(10));
            } else {
                code.append((char) ('A' + random.nextInt(26)));
            }
        }
        
        // 第18位：校验码
        code.append(calculateCheckCode(code.toString()));
        
        return code.toString();
    }
    
    /**
     * 计算统一社会信用代码的校验码
     */
    private char calculateCheckCode(String code) {
        // 统一社会信用代码的校验码计算方法
        // 1. 将前17位字符转换为对应的值
        // 2. 对每一位值乘以对应的权重因子
        // 3. 求和后对31取模
        // 4. 用31减去模值，得到校验码的值
        // 5. 将校验码值转换为对应的字符
        
        int[] weights = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};
        int sum = 0;
        
        for (int i = 0; i < 17; i++) {
            char c = code.charAt(i);
            int value;
            
            if (c >= '0' && c <= '9') {
                value = c - '0';
            } else {
                value = 10 + (c - 'A');
            }
            
            sum += value * weights[i];
        }
        
        int mod = sum % 31;
        int checkValue = (31 - mod) % 31;
        
        if (checkValue < 10) {
            return (char) ('0' + checkValue);
        } else {
            return (char) ('A' + checkValue - 10);
        }
    }
    
    /**
     * 生成注册地址
     */
    private String generateAddress(String region) {
        // 各地区的区县
        String[][] districts = {
            {"朝阳区", "海淀区", "丰台区", "西城区", "东城区", "石景山区", "通州区", "顺义区"},
            {"浦东新区", "黄浦区", "徐汇区", "长宁区", "静安区", "普陀区", "虹口区", "杨浦区"},
            {"天河区", "越秀区", "海珠区", "荔湾区", "白云区", "黄埔区", "番禺区", "花都区"},
            {"南山区", "福田区", "罗湖区", "宝安区", "龙岗区", "龙华区", "坪山区", "光明区"},
            {"西湖区", "上城区", "下城区", "江干区", "拱墅区", "滨江区", "萧山区", "余杭区"},
            {"武侯区", "锦江区", "青羊区", "金牛区", "成华区", "龙泉驿区", "新都区", "温江区"},
            {"武昌区", "洪山区", "江岸区", "江汉区", "硚口区", "汉阳区", "青山区", "东西湖区"},
            {"雁塔区", "碑林区", "新城区", "莲湖区", "灞桥区", "未央区", "阎良区", "临潼区"},
            {"鼓楼区", "玄武区", "秦淮区", "建邺区", "雨花台区", "浦口区", "栖霞区", "江宁区"},
            {"渝中区", "江北区", "南岸区", "沙坪坝区", "九龙坡区", "大渡口区", "渝北区", "巴南区"}
        };
        
        int regionIndex = 0;
        switch (region) {
            case "北京": regionIndex = 0; break;
            case "上海": regionIndex = 1; break;
            case "广州": regionIndex = 2; break;
            case "深圳": regionIndex = 3; break;
            case "杭州": regionIndex = 4; break;
            case "成都": regionIndex = 5; break;
            case "武汉": regionIndex = 6; break;
            case "西安": regionIndex = 7; break;
            case "南京": regionIndex = 8; break;
            case "重庆": regionIndex = 9; break;
        }
        
        String district = districts[regionIndex][random.nextInt(districts[regionIndex].length)];
        
        // 随机路名
        String[] roadTypes = {"路", "街", "大道", "巷", "广场", "中心", "大厦", "广场"};
        String[] roadNames = {"解放", "人民", "中山", "建设", "和平", "友谊", "青年", "文化", "科学", "体育", "金融", "商务", "科技", "创新", "发展"};
        
        String roadName = roadNames[random.nextInt(roadNames.length)] + roadTypes[random.nextInt(roadTypes.length)];
        
        // 随机门牌号
        int number = 1 + random.nextInt(999);
        String building = "";
        if (random.nextBoolean()) {
            // 有楼号
            int buildingNumber = 1 + random.nextInt(50);
            int unitNumber = 1 + random.nextInt(6);
            int roomNumber = 1 + random.nextInt(30);
            building = String.format("%d号楼%d单元%d室", buildingNumber, unitNumber, roomNumber);
        } else {
            // 只有门牌号
            building = number + "号";
        }
        
        return region + "市" + district + roadName + building;
    }
    
    /**
     * 生成法定代表人
     */
    private String generateLegalPerson() {
        // 常见姓氏
        String[] surnames = {"王", "李", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴", "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗"};
        
        // 常见名字
        String[] names1 = {"伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军", "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀兰", "霞"};
        String[] names2 = {"伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军", "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀兰", "霞"};
        
        String surname = surnames[random.nextInt(surnames.length)];
        
        // 随机决定是单字名还是双字名
        if (random.nextBoolean()) {
            // 单字名
            String name = names1[random.nextInt(names1.length)];
            return surname + name;
        } else {
            // 双字名
            String name1 = names1[random.nextInt(names1.length)];
            String name2 = names2[random.nextInt(names2.length)];
            return surname + name1 + name2;
        }
    }
    
    /**
     * 生成注册资本
     */
    private String generateRegisteredCapital(String scale) {
        int capital;
        
        switch (scale) {
            case "微型企业":
                capital = 10 + random.nextInt(90); // 10-100万
                break;
            case "小型企业":
                capital = 100 + random.nextInt(900); // 100-1000万
                break;
            case "中型企业":
                capital = 1000 + random.nextInt(9000); // 1000-10000万
                break;
            case "大型企业":
                capital = 10000 + random.nextInt(90000); // 10000-100000万
                break;
            default:
                capital = 100 + random.nextInt(900);
        }
        
        return capital + "万元人民币";
    }
    
    /**
     * 生成联系电话
     */
    private String generatePhoneNumber() {
        // 生成企业固定电话或手机号
        if (random.nextBoolean()) {
            // 生成固定电话
            String[] prefixes = {"010", "021", "022", "023", "024", "025", "027", "028", "029", "0311"};
            String prefix = prefixes[random.nextInt(prefixes.length)];
            
            StringBuilder number = new StringBuilder(prefix);
            number.append("-");
            
            // 生成8位号码
            for (int i = 0; i < 8; i++) {
                number.append(random.nextInt(10));
            }
            
            return number.toString();
        } else {
            // 生成手机号
            String[] prefixes = {"130", "131", "132", "133", "134", "135", "136", "137", "138", "139", 
                                "150", "151", "152", "153", "155", "156", "157", "158", "159",
                                "180", "181", "182", "183", "184", "185", "186", "187", "188", "189"};
            String prefix = prefixes[random.nextInt(prefixes.length)];
            
            StringBuilder number = new StringBuilder(prefix);
            
            // 生成8位号码
            for (int i = 0; i < 8; i++) {
                number.append(random.nextInt(10));
            }
            
            return number.toString();
        }
    }
    
    /**
     * 生成电子邮箱
     */
    private String generateEmail() {
        // 常见企业邮箱前缀
        String[] prefixes = {"info", "service", "business", "contact", "admin", "support", "sales", "hr", "market", "finance"};
        
        // 常见邮箱后缀
        String[] suffixes = {"company.com", "enterprise.com", "business.com", "corp.com", "group.com", "tech.com", "trade.com", "industry.com"};
        
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        
        return prefix + "@" + suffix;
    }
    
    /**
     * 生成成立日期
     */
    private String generateEstablishDate() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        
        // 生成1-20年前的成立日期
        int establishYear = currentYear - 1 - random.nextInt(20);
        int establishMonth = 1 + random.nextInt(12);
        int establishDay = 1 + random.nextInt(28); // 简化处理，不考虑月份天数差异
        
        return String.format("%04d年%02d月%02d日", establishYear, establishMonth, establishDay);
    }
    
    /**
     * 生成经营范围
     */
    private String generateBusinessScope(String industry) {
        String[] scopes;
        
        switch (industry) {
            case "信息技术":
                scopes = new String[]{"软件开发；技术咨询；技术服务；技术转让；计算机系统集成；数据处理；网络技术开发；电子产品销售；", 
                                     "互联网技术开发；信息技术服务；软件销售；计算机软硬件开发；技术咨询；技术转让；技术服务；", 
                                     "计算机技术开发；技术服务；技术咨询；技术转让；数据处理；网络技术；软件开发；信息系统集成；"};
                break;
            case "金融服务":
                scopes = new String[]{"投资管理；资产管理；企业管理咨询；经济信息咨询；财务咨询；市场调查；企业营销策划；", 
                                     "金融信息咨询；投资咨询；企业管理咨询；商务信息咨询；经济贸易咨询；财务咨询；", 
                                     "接受金融机构委托从事金融信息技术外包；接受金融机构委托从事金融业务流程外包；投资管理；资产管理；"};
                break;
            case "制造业":
                scopes = new String[]{"生产加工；机械制造；电子产品制造；技术开发；技术咨询；技术转让；技术服务；", 
                                     "机械制造；五金制品加工；电子产品组装；技术开发；技术咨询；技术服务；技术转让；", 
                                     "生产制造；机械加工；电子产品生产；技术开发；技术咨询；技术转让；技术服务；"};
                break;
            case "零售业":
                scopes = new String[]{"销售日用品；服装鞋帽；家用电器；电子产品；工艺美术品；文化用品；体育用品；", 
                                     "零售；日用百货；服装；鞋帽；箱包；化妆品；电子产品；家用电器；文化用品；", 
                                     "销售；服装；鞋帽；箱包；化妆品；日用百货；电子产品；家用电器；文化用品；"};
                break;
            case "建筑业":
                scopes = new String[]{"建筑工程施工；市政工程；园林绿化；装饰装修工程；钢结构工程；管道工程；", 
                                     "房屋建筑工程；市政公用工程；地基与基础工程；土石方工程；建筑装饰装修工程；", 
                                     "建筑施工；市政工程；园林绿化；装饰装修；钢结构工程；管道工程；"};
                break;
            case "教育培训":
                scopes = new String[]{"教育咨询；企业管理咨询；文化艺术交流活动策划；会议服务；展览展示服务；", 
                                     "教育咨询；技术开发；技术咨询；技术转让；技术推广；技术服务；企业管理咨询；", 
                                     "教育培训；教育咨询；企业管理咨询；文化艺术交流活动策划；会议服务；展览展示服务；"};
                break;
            case "医疗健康":
                scopes = new String[]{"医疗技术咨询；健康管理咨询；医疗器械销售；保健用品销售；化妆品销售；", 
                                     "医疗技术咨询；健康管理；医疗器械销售；保健用品销售；化妆品销售；", 
                                     "医疗技术咨询；健康管理咨询；医疗器械销售；保健用品销售；化妆品销售；"};
                break;
            case "物流运输":
                scopes = new String[]{"道路货物运输；仓储服务；装卸服务；物流信息咨询；国内货运代理；", 
                                     "道路货物运输；仓储服务；装卸服务；物流信息咨询；国际货运代理；", 
                                     "道路货物运输；仓储服务；装卸服务；物流信息咨询；货运代理；"};
                break;
            case "餐饮服务":
                scopes = new String[]{"餐饮服务；餐饮管理；餐饮策划；餐饮咨询；餐饮技术开发；", 
                                     "餐饮服务；餐饮管理；餐饮策划；餐饮咨询；餐饮技术开发；", 
                                     "餐饮服务；餐饮管理；餐饮策划；餐饮咨询；餐饮技术开发；"};
                break;
            case "房地产":
                scopes = new String[]{"房地产开发；房地产销售；房地产咨询；物业管理；房地产经纪；", 
                                     "房地产开发；房地产销售；房地产咨询；物业管理；房地产经纪；", 
                                     "房地产开发；房地产销售；房地产咨询；物业管理；房地产经纪；"};
                break;
            default:
                scopes = new String[]{"技术开发；技术咨询；技术转让；技术服务；企业管理咨询；经济信息咨询；"};
        }
        
        return scopes[random.nextInt(scopes.length)];
    }
}