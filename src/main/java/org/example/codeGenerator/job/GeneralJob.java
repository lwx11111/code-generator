package org.example.codeGenerator.job;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.codeGenerator.config.GlobalProperties;
import org.example.codeGenerator.serivce.Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

/**
 * 入口类
 */
@Service
@Slf4j
public class GeneralJob {

    @Value("${code.isOpen}")
    private Boolean isOpen;

    @Autowired
    private GlobalProperties globalProperties;

    @Autowired
    private Generator generator;

    @Resource
    private WebApplicationContext applicationContext;

    /**
     * Spring容器的启动时自动的执行
     * 该注解的方法在整个Bean初始化中的执行顺序
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的初始化方法)
     */
    @PostConstruct
    public void init() {
        if (isOpen) {
            String outputDir = globalProperties.getOutputDir();
            log.info("开始生成代码，输出目录为: {}", outputDir);
            try {
                generator.execute();
            } catch (Exception e) {
                log.error("生成代码有问题", e);
            }
            log.info("结束生成代码，输出目录为:{}", outputDir);
            System.exit(SpringApplication.exit(applicationContext));
        }
    }
}
