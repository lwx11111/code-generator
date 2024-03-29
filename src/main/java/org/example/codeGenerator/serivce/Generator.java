package org.example.codeGenerator.serivce;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.example.codeGenerator.config.*;
import org.example.codeGenerator.domain.TemplateItem;
import org.example.codeGenerator.util.ConsoleUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 核心类
 */
@Service
@Slf4j
public class Generator {

    @Autowired
    private TempletService templetService;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private GlobalProperties globalProperties;

    @Autowired
    private PackageProperties packageProperties;

    @Autowired
    private StrategyProperties strategyProperties;


    /**
     * 模版路径
     */
    @Value("${code.templatePath}")
    private String templatePath;

    /**
     * 模版包名
     */
    private String SERVICE = "service";

    /**
     * 获取模版实际路径
     * @param templateType 模版包名
     * @param templateName 模版名
     * @return
     */
    private String getTemplate(String templateType, String templateName) {
        return String.format("/%s/%s/%s", templatePath, templateType, templateName);
    }

    /**
     * 数据源配置
     * @return DataSourceConfig
     */
    private DataSourceConfig dataSourceConfig() {
        //获取配置环境信息
//        String datasource = dataSourceProperties.getActive(
//        datasource = datasource == null ? "dev" : datasource.trim();
        return new DataSourceConfig()
                .setDbType(DbType.getDbType(dataSourceProperties.getDbType()))
                .setUrl(dataSourceProperties.getUrl())
                .setUsername(dataSourceProperties.getUsername())
                .setPassword(dataSourceProperties.getPassword())
                .setDriverName(dataSourceProperties.getDriver());
    }

    /**
     * 全局策略配置
     * @return
     */
    private GlobalConfig globalConfig() {
        return new GlobalConfig()
                // 输出路径
                .setOutputDir(globalProperties.getOutputDir())
                // 是否覆盖已有文件
                .setFileOverride(true)
                // 是否打开输出目录
                .setOpen(true)
                // XML 二级缓存
                .setEnableCache(false)
                // 开发人员
                .setAuthor(globalProperties.getAuthor())
                //是否生成 kotlin 代码
                .setKotlin(false)
                // 是否生成swagger2
                .setSwagger2("true".equalsIgnoreCase(globalProperties.getSwagger2()))
                // 是否需要ActiveRecord
                .setActiveRecord(true)
                // XML ResultMap
                .setBaseResultMap(false)
                // XML columList
                .setBaseColumnList(false)
                // 时间类型
                .setDateType(DateType.TIME_PACK)
                // 自定义文件命名，注意 %s 会自动填充表实体属性！
                .setEntityName(globalProperties.getEntityName())
                .setMapperName(globalProperties.getMapperName())
                .setXmlName(globalProperties.getXmlName())
                .setServiceName(globalProperties.getServiceName())
                .setServiceImplName(globalProperties.getServiceImplName())
                .setControllerName(globalProperties.getControllerName())
                // 主键类型
                .setIdType(IdType.ASSIGN_ID);
    }

    /**
     * 自定义模板
     * @return
     */
    private TemplateConfig templateConfigConfig() {
        return new TemplateConfig()
                // Java实体
                .setEntity(this.getTemplate(SERVICE, "entity.java"))
                // Kotin实体
                .setEntityKt(this.getTemplate(SERVICE, "entity.kt"))
                .setService(this.getTemplate(SERVICE, "service.java"))
                .setServiceImpl(this.getTemplate(SERVICE, "serviceImpl.java"))
                .setMapper(this.getTemplate(SERVICE, "mapper.java"))
                .setXml(this.getTemplate(SERVICE, "mapper.xml"))
                .setController(this.getTemplate(SERVICE, "controller.java"));
    }

    /**
     * 包信息（目录名字）配置
     * @return
     */
    private PackageConfig packageConfig() {
        return new PackageConfig()
                // 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
                .setParent(packageProperties.getParentName())
                .setController(packageProperties.getControllerName())
                .setEntity(packageProperties.getEntityName())
                .setMapper(packageProperties.getMapperName())
                .setXml(packageProperties.getXmlName())
                .setService(packageProperties.getServiceName())
                .setServiceImpl(packageProperties.getServiceImplName());
    }

    /**
     * 数据库表配置
     * @return
     */
    private StrategyConfig strategyConfig() {
        String[] tableNames = null;

        // 处理特定表名
        if (strategyProperties.getTableNames().trim() != null && !"".equals(strategyProperties.getTableNames().trim())) {
            // 以,分割
            tableNames = strategyProperties.getTableNames().split(",");
            System.out.println(tableNames);
        }

        return new StrategyConfig()
                // 全局大写命名 ORACLE 注意
                .setCapitalMode(true)
                // 是否跳过视图
                .setSkipView(false)
                // 数据库表映射到实体的命名策略
                .setNaming(NamingStrategy.underline_to_camel)
                // 需要包含的表名，多个表名传数组
                .setInclude(tableNames)
                // 【实体】是否生成字段常量（默认 false）// 可通过常量名获取数据库字段名 // 3.x支持lambda表达式
                .setEntityColumnConstant(false)
                // lombok实体
                .setEntityLombokModel("true".equalsIgnoreCase(strategyProperties.getEntityLombokModel()))
                // 【实体】是否为构建者模型（默认 false）
                .setEntityBuilderModel(true)
                //生成 @RestController 控制器
                .setRestControllerStyle("true".equalsIgnoreCase(strategyProperties.getRestControllerStyle()))
                // 是否生成实体时，生成字段注解
                .setEntityTableFieldAnnotationEnable(true);
    }


    /**
     * 自定义属性注入 自定义配置 前端配置
     * @return
     */
    private InjectionConfig injectionConfig() {
        InjectionConfig injectionConfig = new InjectionConfig() {

            /**
             * 初始化map，用于模板中的变量替换
             */
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<>();
                this.setMap(map);
            }
        };

        // 自定义模板
        List<TemplateItem> templateItems = templetService.listTemplateItems();
        if (templateItems == null || templateItems.size() < 1) {
            return injectionConfig;
        }

        List<FileOutConfig> fileOutConfigList = new ArrayList<FileOutConfig>();
        injectionConfig.setFileOutConfigList(fileOutConfigList);

        for (TemplateItem item : templateItems) {
            String templatePath = templetService.getTemplateItemPath(item);

            fileOutConfigList.add(new FileOutConfig(templatePath) {
                @Override
                public String outputFile(TableInfo tableInfo) {
                    injectionConfig.getMap().put("entity", tableInfo.getEntityName());
                    injectionConfig.getMap().put("entityLower", tableInfo.getEntityName().toLowerCase());
                    checkPrimary(tableInfo);
                    return templetService.getOutputFile(item, injectionConfig.getMap());
                }
            });
        }
        return injectionConfig;
    }

    private void checkPrimary(TableInfo tableInfo) {
        if (tableInfo == null) {
            return;
        }
        List<TableField> fields = tableInfo.getFields();
        if (fields == null || fields.size() < 1) {
            return;
        }
        for (TableField field : fields) {
            if (field.isKeyFlag()) {
                return;
            }
        }

        int keyFieldNum = getKeyFieldNum(fields, tableInfo.getName());
        if (keyFieldNum >= 0 && keyFieldNum < fields.size()) {
            log.info(" set  field [ {} ] as primary", fields.get(keyFieldNum).getName());
            fields.get(keyFieldNum).setKeyFlag(true);
        } else {
            log.info("not right set primary key，random set field [ {} ] as primary", fields.get(0).getName());
            fields.get(0).setKeyFlag(true);
        }
    }

    private int getKeyFieldNum(List<TableField> fields, String tableName) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(String.format("please select one field as Primary for the table %s\n", tableName));
        int i = 0;
        for (TableField field : fields) {
            buffer.append(String.format("%d : %s\n", i++, field.getName()));
        }
        buffer.append(String.format("please select the field num between 0 and %s : ", fields.size() - 1));
        for (int loopNum = 0; loopNum < 3; loopNum++) {
            String ret = ConsoleUtil.readConsole(buffer.toString());
            int fildNum = NumberUtil.parseInt(ret);
            if (fildNum >= 0 && fildNum < fields.size()) {
                return fildNum;
            }
        }
        log.error("read from console not expact");
        return -1;
    }


    /**
     * 执行器,生成代码
     */
    public void execute() {
        GlobalConfig globalConfig = globalConfig();
        DataSourceConfig dataSourceConfig = dataSourceConfig();
        StrategyConfig strategyConfig = strategyConfig();
        PackageConfig packageConfig = packageConfig();
        InjectionConfig injectionConfig = injectionConfig();
        TemplateConfig templateConfig = templateConfigConfig();

        AutoGenerator autoGenerator = new AutoGenerator()
                .setGlobalConfig(globalConfig)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(packageConfig)
                .setTemplate(templateConfig)
                .setCfg(injectionConfig);
        autoGenerator.execute();
    }

}
