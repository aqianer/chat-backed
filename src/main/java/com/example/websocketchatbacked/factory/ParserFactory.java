package com.example.websocketchatbacked.factory;

import com.example.websocketchatbacked.parser.result.FileParser;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ParserFactory {

    private final Map<String, FileParser> parserMap;

    // 构造方法注入Spring管理的所有FileParser实现类
    public ParserFactory(Map<String, FileParser> parserMap) {
        this.parserMap = parserMap;
    }
    
    public FileParser getParser(String fileExtension) {
        // 比如把markdownParser的key映射成md，你也可以自定义映射规则
        String beanName = switch (fileExtension.toLowerCase()) {
            case "md" -> "markdownParser";
            case "pdf" -> "pdfParser";
            case "docx", "doc" -> "wordParser";
            case "txt" -> "txtParser";
            default -> throw new IllegalArgumentException("Unsupported file extension: " + fileExtension);
        };
        return parserMap.get(beanName);
    }
}