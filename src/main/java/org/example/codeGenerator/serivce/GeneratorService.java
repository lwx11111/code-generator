package org.example.codeGenerator.serivce;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.codeGenerator.config.BaseConfig;
import org.example.codeGenerator.config.GeraltorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

@Service
@Slf4j
public class GeneratorService {

    @Autowired
    GeraltorConfig geraltorConfig;

    @Autowired
    BaseConfig baseConfig;

    @Autowired
    Generator generator;

    @Resource
    private WebApplicationContext applicationContext;


    public void genralCode() {
        String outdir = geraltorConfig.getValue("outputDir");
        log.info("genralCode start ，output dir: {}", outdir);
        try {
            generator.execute();
        } catch (Exception e) {
            log.error("genralCode error：", e);
        }
        log.info("genralCode finish ，output dir:{}", outdir);
        System.exit(SpringApplication.exit(applicationContext));
    }
}
