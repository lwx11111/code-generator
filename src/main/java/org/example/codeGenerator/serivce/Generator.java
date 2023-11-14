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
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.codeGenerator.config.BaseConfig;
import org.example.codeGenerator.config.GeraltorConfig;
import org.example.codeGenerator.config.ThirdPartyToolsConfig;
import org.example.codeGenerator.domain.TempletItem;
import org.example.codeGenerator.util.ConsoleUtil;
import org.example.codeGenerator.util.StringHelper;
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
    BaseConfig baseConfig;

    @Autowired
    ThirdPartyToolsConfig toolsConfig;

    @Value("${code.templetGroup}")
    String templet;

    @Autowired
    GeraltorConfig geraltorConfig;

    @Autowired
    TempletService templetService;

    @Value("${code.module}")
    private boolean isNewModule;

    @Value("${code.excludedir}")
    private String excludedir;

    private String VIEW = "view";
    private String SERVICE = "service";
    private String CLOUD = "cloud";

    private String[] excludeDirArray;

    @PostConstruct
    private void init() {
        if (excludedir == null || "".equalsIgnoreCase(excludedir.trim())) {
            return;
        }
        excludeDirArray = excludedir.split(",");
    }

    private boolean isExclude(String relPath) {
        if (excludeDirArray == null || excludedir.length() < 1) {
            return false;
        }
        for (String dir : excludeDirArray) {
            if (relPath.startsWith(String.format("/%s", dir.trim()))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数据连接信息
     * @return DataSourceConfig
     */
    private DataSourceConfig dataSourceConfig() {
        //获取配置环境信息
        String datasource = geraltorConfig.getValue("datasource");
        datasource = datasource == null ? "dev" : datasource.trim();
        //数据库配置
        return new DataSourceConfig()
                .setDbType(DbType.getDbType(toolsConfig.getValue( "datasource_dbType")))
                .setUrl(toolsConfig.getValue(datasource + "_datasource_url"))
                .setUsername(toolsConfig.getValue(datasource + "_datasource_username"))
                .setPassword(toolsConfig.getValue(datasource + "_datasource_password"))
                .setDriverName(toolsConfig.getValue( "datasource_driver"));

    }

    // 配置
    private GlobalConfig globalConfig() {
        return new GlobalConfig()
                .setAuthor(baseConfig.getValue("AUTHOR"))
                .setOutputDir(String.format("%s/%s", geraltorConfig.getValue("outputDir"), geraltorConfig.getValue("moduleName")))
                .setFileOverride(true) // 是否覆盖已有文件
                .setOpen(false) // 是否打开输出目录
                .setDateType(DateType.TIME_PACK) // 时间采用java 8，（操作工具类：JavaLib => DateTimeUtils）
                .setActiveRecord(true)// 不需要ActiveRecord特性的请改为false
                .setEnableCache(false)// XML 二级缓存
                .setBaseResultMap(false)// XML ResultMap
                .setBaseColumnList(false)// XML columList
                .setKotlin(false) //是否生成 kotlin 代码
                // 自定义文件命名，注意 %s 会自动填充表实体属性！
                .setEntityName(baseConfig.getValue("FILE_NAME_MODEL"))
                .setMapperName(baseConfig.getValue("ILE_NAME_DAO"))
                .setXmlName(baseConfig.getValue("FILE_NAME_XML"))
                .setServiceName(baseConfig.getValue("FILE_NAME_SERVICE"))
                .setServiceImplName(baseConfig.getValue("FILE_NAME_SERVICE_IMPL"))
                .setControllerName(baseConfig.getValue("FILE_NAME_CONTROLLER"))
                .setIdType(IdType.ASSIGN_ID) // 主键类型
                .setSwagger2("true".equalsIgnoreCase(baseConfig.getValue("swagger2"))) // model swagger2
                ;
    }

    // 表名策略配置
    private StrategyConfig strategyConfig() {
        String[] tableNames = null;

        if (geraltorConfig.getTableNames().length == 1) {
            if (geraltorConfig.getTableNames()[0].trim() != null && !"".equalsIgnoreCase(geraltorConfig.getTableNames()[0].trim())) {
                log.warn("tableNames is not null");
                tableNames = geraltorConfig.getTableNames();
            }
        }

        return new StrategyConfig()
                .setCapitalMode(true) // 全局大写命名 ORACLE 注意
                .setSkipView(false) // 是否跳过视图
                //.setDbColumnUnderline(true)
                .setTablePrefix(geraltorConfig.getTablePrefixes())// 此处可以修改为您的表前缀(数组)
                .setFieldPrefix(geraltorConfig.getFieldPrefixes()) // 字段前缀
                .setNaming(NamingStrategy.underline_to_camel) // 表名生成策略
                .setInclude(tableNames)//修改替换成你需要的表名，多个表名传数组
                //.setExclude(new String[]{"test"}) // 排除生成的表
                .setEntityLombokModel("true".equalsIgnoreCase(baseConfig.getValue("entityLombokModel"))) // lombok实体
                .setEntityBuilderModel(true) // 【实体】是否为构建者模型（默认 false）
                .setEntityColumnConstant(false) // 【实体】是否生成字段常量（默认 false）// 可通过常量名获取数据库字段名 // 3.x支持lambda表达式
//                .setLogicDeleteFieldName(baseConfig.FIELD_LOGIC_DELETE_NAME) // 逻辑删除属性名称
                .setRestControllerStyle("true".equalsIgnoreCase(baseConfig.getValue("restControllerStyle")))
                .setEntityTableFieldAnnotationEnable(true)
                //.entityTableFieldAnnotationEnable(true)
                ;
    }

    // 包信息配置
    private PackageConfig packageConfig() {
        return new PackageConfig()
                .setParent(geraltorConfig.getValue("packageName"))
                .setController(baseConfig.getValue("PACKAGE_NAME_CONTROLLER"))
                .setEntity(baseConfig.getValue("PACKAGE_NAME_MODEL"))
                .setMapper(baseConfig.getValue("PACKAGE_NAME_DAO"))
                .setXml(baseConfig.getValue("PACKAGE_NAME_XML"))
                .setService(baseConfig.getValue("PACKAGE_NAME_SERVICE"))
                .setServiceImpl(baseConfig.getValue("PACKAGE_NAME_SERVICE_IMPL"))
//                .setModuleName(geraltorConfig.moduleName)
                ;
    }

    /**
     * 自定义配置 前端配置
     * @param packageConfig
     * @return
     */
    private InjectionConfig injectionConfig(final PackageConfig packageConfig) {
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
                Map<String, Object> map = new HashMap<>();
                map.putAll(geraltorConfig.getConfigMap());
                map.putAll(baseConfig.getConfigMap());
                map.putAll(toolsConfig.getConfigMap());
                map.put("appName", geraltorConfig.getValue("moduleName").toLowerCase());
                map.put("varDescriptor", "$");
                map.put("s", "$");
                map.put("a", "@");
                map.put("appModule", geraltorConfig.getValue("moduleName").toLowerCase());
                map.put("appVersion", geraltorConfig.getValue("moduleVersion"));
                String appClassName = StringHelper.toCamelCase(geraltorConfig.getValue("moduleName"), true) + "App";
                map.put("appClassName", appClassName);
                String packagePath = geraltorConfig.getValue("packageName").replace('.', '/');
                map.put("packagePath", packagePath);
                map.put("dbType", toolsConfig.getValue("datasource_dbType").trim().toLowerCase());
                this.setMap(map);
            }
        };

        List<TempletItem> templetItems = templetService.getTemplets();
        if (templetItems == null || templetItems.size() < 1) {
            return injectionConfig;
        }

        List<FileOutConfig> fileOutConfigList = new ArrayList<FileOutConfig>();
        injectionConfig.setFileOutConfigList(fileOutConfigList);
        for (TempletItem item : templetItems) {
            String templet = templetService.getTempletPath(item);
            if (!isNewModule) {
                if ("cloud".equalsIgnoreCase(item.getRelativePath())
                        || "boot".equalsIgnoreCase(item.getRelativePath())) {
                    continue;
                }
            }
            if (isExclude(item.getRelativePath())) {
                continue;
            }

            fileOutConfigList.add(new FileOutConfig(templet) {
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
//            log.info("read from console value: {}", ret);
            int fildNum = NumberUtil.parseInt(ret);
            if (fildNum >= 0 && fildNum < fields.size()) {
                return fildNum;
            }
        }
        log.error("read from console not expact");
        return -1;
    }

    private String getTemplet(String templetType, String templetName) {
        return String.format("/%s/%s/%s", templet, templetType, templetName);
    }

    private TemplateConfig templateConfigConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setController(getTemplet(SERVICE, "controller.java"));
        templateConfig.setEntity(getTemplet(SERVICE, "entity.java"));
        templateConfig.setEntityKt(getTemplet(SERVICE, "entity.kt"));
        templateConfig.setMapper(getTemplet(SERVICE, "mapper.java"));
        templateConfig.setXml(getTemplet(SERVICE, "mapper.xml"));
        templateConfig.setService(getTemplet(SERVICE, "service.java"));
        templateConfig.setServiceImpl(getTemplet(SERVICE, "serviceImpl.java"));
        return templateConfig;
    }

    /**
     * 执行器,生成代码
     */
    public void execute() {
        geraltorConfig.getGenrator().put("moduleName", geraltorConfig.getValue("moduleName").toLowerCase());
//        geraltorConfig.getGenrator().put("viewModuleName", geraltorConfig.getValue("viewModuleName").toLowerCase());

        GlobalConfig globalConfig = globalConfig();
        DataSourceConfig dataSourceConfig = dataSourceConfig();
        StrategyConfig strategyConfig = strategyConfig();
        PackageConfig packageConfig = packageConfig();
        InjectionConfig injectionConfig = injectionConfig(packageConfig);
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
