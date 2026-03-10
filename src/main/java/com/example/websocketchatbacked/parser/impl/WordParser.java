package com.example.websocketchatbacked.parser.impl;

import com.example.websocketchatbacked.parser.result.ParseResult;
import com.example.websocketchatbacked.exception.BusinessException;
import com.example.websocketchatbacked.parser.result.FileParser;
import com.example.websocketchatbacked.parser.result.WordParseResult;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Component
public class WordParser implements FileParser {

    @Override
    public ParseResult parse(String filePath) throws IOException {
        File file = new File(filePath);
        StringBuilder content = new StringBuilder();

        try {
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(fis);
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                content.append(paragraph.getText()).append("\n");
            }
        } catch (Exception e) {
            throw new BusinessException(422, "word文件解析失败");
        }
        return new WordParseResult(content.toString());
    }

}