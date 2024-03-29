package org.example.codeGenerator.domain;

import lombok.Data;

/**
 * @Author 刘文轩
 * @Date 2024/3/29 17:54
 */
@Data
public class TemplateItem {

    private String name;
    private String relativePath;
    private String outPath;
    private String outFileName;
}
