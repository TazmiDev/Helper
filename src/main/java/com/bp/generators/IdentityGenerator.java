package com.bp.generators;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.security.SecureRandom;
import java.util.Calendar;
/**
 * 身份信息生成器，生成包括身份证、银行卡、手机号在内的随机虚假个人信息
 */
public class IdentityGenerator implements Generator {
    private final MontoyaApi api;
    private final Logging logging;
    private final SecureRandom random = new SecureRandom();
    
    public IdentityGenerator(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
    }
    
    @Override
    public String getName() {
        return "身份信息生成器";
    }
    
    @Override
    public JPanel createOperationPanel(ResultCallback callback) {
        this.resultCallback = callback;
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 第一行：选择要生成的信息类型
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("生成类型:"), gbc);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JCheckBox idCardCheck = new JCheckBox("身份证");
        JCheckBox bankCardCheck = new JCheckBox("银行卡");
        JCheckBox phoneCheck = new JCheckBox("手机号");
        JCheckBox nameCheck = new JCheckBox("姓名");
        JCheckBox addressCheck = new JCheckBox("地址");
        JCheckBox emailCheck = new JCheckBox("邮箱");
        JCheckBox qqCheck = new JCheckBox("QQ号");
        JCheckBox wechatCheck = new JCheckBox("微信号");
        JCheckBox jobCheck = new JCheckBox("职业");
        JCheckBox companyCheck = new JCheckBox("公司");
        JCheckBox educationCheck = new JCheckBox("教育背景");
        JCheckBox ageCheck = new JCheckBox("年龄");
        JCheckBox genderCheck = new JCheckBox("性别");
        JCheckBox birthdayCheck = new JCheckBox("生日");
        
        // 默认选中身份证、银行卡和手机号
        idCardCheck.setSelected(true);
        bankCardCheck.setSelected(true);
        phoneCheck.setSelected(true);
        
        typePanel.add(idCardCheck);
        typePanel.add(bankCardCheck);
        typePanel.add(phoneCheck);
        typePanel.add(nameCheck);
        typePanel.add(addressCheck);
        typePanel.add(emailCheck);
        typePanel.add(qqCheck);
        typePanel.add(wechatCheck);
        typePanel.add(jobCheck);
        typePanel.add(companyCheck);
        typePanel.add(educationCheck);
        typePanel.add(ageCheck);
        typePanel.add(genderCheck);
        typePanel.add(birthdayCheck);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(typePanel, gbc);

        // 第二行：地区选择
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("地区:"), gbc);

        String[] regions = { "北京", "上海", "广州", "深圳", "杭州", "成都", "武汉", "西安", "南京", "重庆" };
        JComboBox<String> regionCombo = new JComboBox<>(regions);
        gbc.gridx = 1;
        panel.add(regionCombo, gbc);

        // 第三行：生成按钮
        JButton generateButton = new JButton("生成身份信息");
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        panel.add(generateButton, gbc);

        // 生成按钮事件
        generateButton.addActionListener(e -> {
            StringBuilder result = new StringBuilder();
            
            if (idCardCheck.isSelected()) {
                result.append("身份证: ").append(generateIdCard()).append("\n");
            }
            
            if (bankCardCheck.isSelected()) {
                result.append("银行卡: ").append(generateBankCard()).append("\n");
            }
            
            if (phoneCheck.isSelected()) {
                result.append("手机号: ").append(generatePhoneNumber()).append("\n");
            }
            
            if (nameCheck.isSelected()) {
                result.append("姓名: ").append(generateName()).append("\n");
            }
            
            if (addressCheck.isSelected()) {
                String region = (String) regionCombo.getSelectedItem();
                result.append("地址: ").append(generateAddress(region)).append("\n");
            }
            
            if (emailCheck.isSelected()) {
                result.append("邮箱: ").append(generateEmail()).append("\n");
            }
            
            if (qqCheck.isSelected()) {
                result.append("QQ号: ").append(generateQQ()).append("\n");
            }
            
            if (wechatCheck.isSelected()) {
                result.append("微信号: ").append(generateWechat()).append("\n");
            }
            
            if (jobCheck.isSelected()) {
                result.append("职业: ").append(generateJob()).append("\n");
            }
            
            if (companyCheck.isSelected()) {
                result.append("公司: ").append(generateCompany()).append("\n");
            }
            
            if (educationCheck.isSelected()) {
                result.append("教育背景: ").append(generateEducation()).append("\n");
            }
            
            if (ageCheck.isSelected()) {
                result.append("年龄: ").append(generateAge()).append("\n");
            }
            
            if (genderCheck.isSelected()) {
                result.append("性别: ").append(generateGender()).append("\n");
            }
            
            if (birthdayCheck.isSelected()) {
                result.append("生日: ").append(generateBirthday()).append("\n");
            }
            
            // 这里需要回调到主面板设置结果
            if (resultCallback != null) {
                resultCallback.setResultText(result.toString());
            }
        });

        return panel;
    }
    
    /**
     * 生成身份证号码
     */
    private String generateIdCard() {
        // 地区代码（简化版）
        String[] areaCodes = { "110101", "310101", "440103", "440304", "330102", "510104", "420102", "610102", "320102", "500101" };
        String areaCode = areaCodes[random.nextInt(areaCodes.length)];
        
        // 出生日期（18-60岁之间）
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int birthYear = currentYear - 18 - random.nextInt(42); // 18-60岁
        int birthMonth = 1 + random.nextInt(12);
        int birthDay = 1 + random.nextInt(28); // 简化处理，不考虑月份天数差异
        
        String birthDate = String.format("%04d%02d%02d", birthYear, birthMonth, birthDay);
        
        // 顺序码（15-17位）
        String sequence = String.format("%03d", random.nextInt(1000));
        
        // 前17位
        String id17 = areaCode + birthDate + sequence;
        
        // 校验码（第18位）
        char[] verifyCode = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
        int[] weight = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += (id17.charAt(i) - '0') * weight[i];
        }
        
        char lastChar = verifyCode[sum % 11];
        
        return id17 + lastChar;
    }
    
    /**
     * 生成银行卡号
     */
    private String generateBankCard() {
        // 银行卡号通常是16-19位，这里生成16位
        StringBuilder cardNumber = new StringBuilder();
        
        // 银行标识（BIN），前6位
        String[] bins = { "622202", "622848", "621700", "621661", "622700", "622261", "621691", "622155" };
        cardNumber.append(bins[random.nextInt(bins.length)]);
        
        // 剩余位数（10位）
        for (int i = 0; i < 10; i++) {
            cardNumber.append(random.nextInt(10));
        }
        
        // 计算Luhn校验码
        String cardWithoutCheck = cardNumber.toString();
        int sum = 0;
        for (int i = 0; i < cardWithoutCheck.length(); i++) {
            int digit = cardWithoutCheck.charAt(i) - '0';
            if (i % 2 == cardWithoutCheck.length() % 2) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }
        
        int checkDigit = (10 - (sum % 10)) % 10;
        cardNumber.append(checkDigit);
        
        return cardNumber.toString();
    }
    
    /**
     * 生成手机号码
     */
    private String generatePhoneNumber() {
        // 手机号前缀
        String[] prefixes = { "130", "131", "132", "133", "134", "135", "136", "137", "138", "139",
                             "150", "151", "152", "153", "155", "156", "157", "158", "159",
                             "180", "181", "182", "183", "184", "185", "186", "187", "188", "189" };
        
        String prefix = prefixes[random.nextInt(prefixes.length)];
        
        // 后8位
        StringBuilder suffix = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            suffix.append(random.nextInt(10));
        }
        
        return prefix + suffix.toString();
    }
    
    /**
     * 生成姓名
     */
    private String generateName() {
        // 常见姓氏
        String[] surnames = { "王", "李", "张", "刘", "陈", "杨", "赵", "黄", "周", "吴", "徐", "孙", "胡", "朱", "高", "林", "何", "郭", "马", "罗" };
        
        // 常见名字
        String[] names1 = { "伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军", "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀兰", "霞" };
        String[] names2 = { "伟", "芳", "娜", "秀英", "敏", "静", "丽", "强", "磊", "军", "洋", "勇", "艳", "杰", "娟", "涛", "明", "超", "秀兰", "霞" };
        
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
     * 生成地址
     */
    private String generateAddress(String region) {
        // 根据地区生成对应的地址
        String[][] streets = {
            // 北京
            { "朝阳区", "海淀区", "东城区", "西城区", "丰台区" },
            // 上海
            { "浦东新区", "黄浦区", "徐汇区", "长宁区", "静安区" },
            // 广州
            { "天河区", "越秀区", "海珠区", "荔湾区", "白云区" },
            // 深圳
            { "南山区", "福田区", "罗湖区", "宝安区", "龙岗区" },
            // 杭州
            { "西湖区", "上城区", "下城区", "江干区", "拱墅区" },
            // 成都
            { "锦江区", "青羊区", "金牛区", "武侯区", "成华区" },
            // 武汉
            { "武昌区", "汉口区", "汉阳区", "江岸区", "江汉区" },
            // 西安
            { "雁塔区", "碑林区", "新城区", "莲湖区", "灞桥区" },
            // 南京
            { "鼓楼区", "玄武区", "秦淮区", "建邺区", "雨花台区" },
            // 重庆
            { "渝中区", "江北区", "南岸区", "沙坪坝区", "九龙坡区" }
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
        
        String district = streets[regionIndex][random.nextInt(streets[regionIndex].length)];
        
        // 随机路名
        String[] roadTypes = { "路", "街", "大道", "巷" };
        String[] roadNames = { "解放", "人民", "中山", "建设", "和平", "友谊", "青年", "文化", "科学", "体育" };
        
        String roadName = roadNames[random.nextInt(roadNames.length)] + roadTypes[random.nextInt(roadTypes.length)];
        
        // 随机门牌号
        int number = 1 + random.nextInt(999);
        String building = "";
        if (random.nextBoolean()) {
            // 有楼号
            int buildingNumber = 1 + random.nextInt(20);
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
     * 生成邮箱地址
     */
    private String generateEmail() {
        String[] prefixes = { "zhang", "wang", "li", "chen", "liu", "yang", "zhao", "huang", "zhou", "wu" };
        String[] suffixes = { "123", "abc", "xyz", "good", "happy", "lucky", "nice", "cool", "smart", "best" };
        String[] domains = { "qq.com", "163.com", "126.com", "gmail.com", "outlook.com", "sina.com", "sohu.com" };
        
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        String domain = domains[random.nextInt(domains.length)];
        
        // 随机决定是否包含数字
        if (random.nextBoolean()) {
            int number = 1 + random.nextInt(999);
            return prefix + suffix + number + "@" + domain;
        } else {
            return prefix + suffix + "@" + domain;
        }
    }
    
    /**
     * 生成QQ号
     */
    private String generateQQ() {
        // QQ号通常是5-12位数字，这里生成8-10位
        int length = 8 + random.nextInt(3);
        StringBuilder qq = new StringBuilder();
        
        // 第一位不能是0
        qq.append(1 + random.nextInt(9));
        
        // 剩余位
        for (int i = 1; i < length; i++) {
            qq.append(random.nextInt(10));
        }
        
        return qq.toString();
    }
    
    /**
     * 生成微信号
     */
    private String generateWechat() {
        // 微信号可以是字母、数字、下划线、减号，6-20位，必须以字母开头
        String[] prefixes = { "wx_", "weixin_", "my_", "user_", "cool_", "happy_", "lucky_" };
        String[] suffixes = { "123", "abc", "xyz", "good", "nice", "cool", "smart", "best" };
        
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        
        // 随机决定是否添加数字
        if (random.nextBoolean()) {
            int number = 1 + random.nextInt(999);
            return prefix + suffix + number;
        } else {
            return prefix + suffix;
        }
    }
    
    /**
     * 生成职业
     */
    private String generateJob() {
        String[] jobs = {
            "软件工程师", "产品经理", "设计师", "市场营销", "销售经理", "人力资源", "财务分析师",
            "教师", "医生", "律师", "建筑师", "记者", "编辑", "摄影师", "厨师", "司机",
            "会计", "行政助理", "项目经理", "数据分析师", "网络工程师", "客服代表", "采购专员"
        };
        
        return jobs[random.nextInt(jobs.length)];
    }
    
    /**
     * 生成公司名称
     */
    private String generateCompany() {
        String[] prefixes = { "北京", "上海", "深圳", "广州", "杭州", "成都", "武汉", "南京", "重庆", "西安" };
        String[] middles = { "科技", "信息", "网络", "数据", "智能", "创新", "互联", "数字", "云端", "智慧" };
        String[] suffixes = { "有限公司", "科技有限公司", "信息技术有限公司", "网络科技有限公司", "数据技术有限公司" };
        
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String middle = middles[random.nextInt(middles.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        
        return prefix + middle + suffix;
    }
    
    /**
     * 生成教育背景
     */
    private String generateEducation() {
        String[] educations = {
            "高中", "大专", "本科", "硕士", "博士"
        };
        
        String[] majors = {
            "计算机科学与技术", "软件工程", "电子信息工程", "通信工程", "自动化", "机械工程",
            "市场营销", "工商管理", "会计学", "金融学", "国际经济与贸易", "人力资源管理",
            "新闻学", "广告学", "汉语言文学", "英语", "法学", "心理学", "社会学", "历史学"
        };
        
        String education = educations[random.nextInt(educations.length)];
        String major = majors[random.nextInt(majors.length)];
        
        return education + " - " + major;
    }
    
    /**
     * 生成年龄
     */
    private String generateAge() {
        // 生成18-65岁之间的年龄
        int age = 18 + random.nextInt(48);
        return age + "岁";
    }
    
    /**
     * 生成性别
     */
    private String generateGender() {
        return random.nextBoolean() ? "男" : "女";
    }
    
    /**
     * 生成生日
     */
    private String generateBirthday() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        
        // 生成18-60岁之间的生日
        int birthYear = currentYear - 18 - random.nextInt(42);
        int birthMonth = 1 + random.nextInt(12);
        int birthDay = 1 + random.nextInt(28); // 简化处理，不考虑月份天数差异
        
        return String.format("%04d年%02d月%02d日", birthYear, birthMonth, birthDay);
    }
    
    private ResultCallback resultCallback;
    
    public void setResultCallback(ResultCallback callback) {
        this.resultCallback = callback;
    }
}