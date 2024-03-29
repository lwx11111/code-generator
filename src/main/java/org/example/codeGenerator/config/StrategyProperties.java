package org.example.codeGenerator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 刘文轩
 * @Date 2024/3/29 16:54
 */
@Configuration
@Data
public class StrategyProperties {

    @Value("${code.strategy.tableNames}")
    private String tableNames;

    @Value("${code.strategy.entityLombokModel}")
    private String entityLombokModel;

    @Value("${code.strategy.restControllerStyle}")
    private String restControllerStyle;
}
