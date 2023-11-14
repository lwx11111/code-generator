package org.example.codeGenerator.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

/**
 * 数据库配置
 */
@ConfigurationProperties(prefix = "code") // 配置文件中的前缀
@PropertySource("classpath:third-party-tools.properties")
@Configuration
@Data
public class ThirdPartyToolsConfig extends AbsConfig {
    private Map<String, String> third_tools;

    @Override
    public Map<String, String> getConfigMap() {
        return getThird_tools();
    }
}
