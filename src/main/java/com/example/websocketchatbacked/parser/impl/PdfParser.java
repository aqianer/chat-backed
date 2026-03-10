package com.example.websocketchatbacked.parser.impl;

import com.example.websocketchatbacked.parser.result.ParseResult;
import com.example.websocketchatbacked.parser.result.PdfPage;
import com.example.websocketchatbacked.parser.result.PdfParseResult;
import com.example.websocketchatbacked.exception.BusinessException;
import com.example.websocketchatbacked.parser.result.FileParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PdfParser implements FileParser {

    @Override
    public ParseResult parse(String filePath) throws IOException {
        Path path = Path.of(filePath);
        // 读取文件
        byte[] pdfContent = Files.readAllBytes(path);

        List<PdfPage> pages = new ArrayList<>();
        try {
            PDDocument document = PDDocument.load(pdfContent);
            PDFTextStripper stripper = new PDFTextStripper();
            // 可以在这里加定制：按页码分块
            for (int page = 1; page <= document.getNumberOfPages(); page++) {
                stripper.setStartPage(page);
                stripper.setEndPage(page);
                String pageContent = stripper.getText(document);
                pages.add(new PdfPage(page, pageContent));
            }
        } catch (Exception e) {
            throw new BusinessException(400, "解析PDF文件失败");
        }


        return new PdfParseResult(pages);
    }

    // 这个方法你完全可以定制，比如用语义分块、按标题拆分
    private List<String> splitContentIntoBlocks(String content) {
        // 示例：按空行拆分段落，你可以改成按字数、按正则匹配标题
        return Arrays.stream(content.split("\\n\\s*\\n"))
                .filter(block -> block.trim().length() > 10) // 过滤太短的块
                .collect(Collectors.toList());
    }


}