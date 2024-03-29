package org.example.codeGenerator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author 刘文轩
 * @Date 2024/3/29 16:36
 */
@Configuration
@Data
public class PackageProperties {

    @Value("${code.package.parentName}")
    private String parentName;

    @Value("${code.package.controllerName}")
    private String controllerName;

    @Value("${code.package.serviceName}")
    private String serviceName;

    @Value("${code.package.serviceImplName}")
    private String serviceImplName;

    @Value("${code.package.entityName}")
    private String entityName;

    @Value("${code.package.mapperName}")
    private String mapperName;

    @Value("${code.package.xmlName}")
    private String xmlName;
}
