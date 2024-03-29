package org.example.codeGenerator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 刘文轩
 * @Date 2024/3/29 15:37
 */
@Configuration
@Data
public class GlobalProperties {

    @Value("${code.global.outputDir}")
    private String outputDir;

    @Value("${code.global.author}")
    private String author;

    @Value("${code.global.swagger2}")
    private String swagger2;

    @Value("${code.global.entityName}")
    private String entityName;

    @Value("${code.global.mapperName}")
    private String mapperName;

    @Value("${code.global.xmlName}")
    private String xmlName;

    @Value("${code.global.serviceName}")
    private String serviceName;

    @Value("${code.global.serviceImplName}")
    private String serviceImplName;

    @Value("${code.global.controllerName}")
    private String controllerName;
}
