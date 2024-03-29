package org.example.codeGenerator.serivce;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.example.codeGenerator.config.GlobalProperties;
import org.example.codeGenerator.domain.TemplateItem;
import org.example.codeGenerator.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class TempletService {

    @Autowired
    private GlobalProperties globalProperties;

    @Value("${code.templatePath}")
    private String templatePath;

    /**
     * 模板文件JSON
     */
    @Value("classpath:${code.templatePath}/vue3/templatesVue.json")
    private Resource TemplateItemsJson;

    /**
     * 获取模板文件
     * @return
     */
    public List<TemplateItem> listTemplateItems() {
        try {
            System.out.println("TemplateItemsJson: " + TemplateItemsJson.getURL());
            String templateItemData = IoUtil.read(TemplateItemsJson.getInputStream(), Charset.forName("UTF-8"));
            return JSON.parseArray(templateItemData, TemplateItem.class);
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }

    /**
     * 获取模版路径
     * @return
     */
    public String getTemplateItemPath(TemplateItem item) {
        String relPath = item.getRelativePath();
        if (relPath != null && !"".equalsIgnoreCase(relPath.trim())) {
            return String.format("/%s/%s/%s", templatePath, relPath, item.getName());
        }
        return String.format("/%s/%s", templatePath, item.getName());
    }

    /**
     * 获取输出文件
     * @param item
     * @param map
     * @return
     */
    public String getOutputFile(TemplateItem item, Map<String, Object> map) {
        String outPath = StringHelper.repalceParams(item.getOutPath(), map, null);
        String fileName = StringHelper.repalceParams(item.getOutFileName(), map, null);
        String outputDir = globalProperties.getOutputDir();
        return String.format("%s/%s/%s", outputDir, outPath, fileName);
    }

}
