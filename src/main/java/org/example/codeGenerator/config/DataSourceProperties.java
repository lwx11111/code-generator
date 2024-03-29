package org.example.codeGenerator.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author 刘文轩
 * @Date 2024/3/29 13:44
 */
@Configuration
@Data
public class DataSourceProperties {

    @Value("${code.dataSource.url}")
    private String url;

    @Value("${code.dataSource.username}")
    private String username;

    @Value("${code.dataSource.password}")
    private String password;

    @Value("${code.dataSource.driver}")
    private String driver;

    @Value("${code.dataSource.dbType}")
    private String dbType;

}
