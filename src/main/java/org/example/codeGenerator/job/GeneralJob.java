package org.example.codeGenerator.job;


import jakarta.annotation.PostConstruct;
import org.example.codeGenerator.serivce.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class GeneralJob {
    @Autowired
    private GeneratorService generatorService;

    @Value("${code.isOpen}")
    private Boolean isOpen;

    /**
     * Spring容器的启动时自动的执行
     * 该注解的方法在整个Bean初始化中的执行顺序
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的初始化方法)
     */
    @PostConstruct
    public void init() {
        System.out.println("GeneralJob Initializing");
        if (isOpen) {
            generatorService.genralCode();
            System.exit(0);
        }

    }
}
