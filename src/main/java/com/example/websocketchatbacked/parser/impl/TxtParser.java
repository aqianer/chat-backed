package com.example.websocketchatbacked.parser.impl;

import com.example.websocketchatbacked.parser.result.FileParser;
import com.example.websocketchatbacked.parser.result.ParseResult;
import com.example.websocketchatbacked.parser.result.TxtParseResult;
import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class TxtParser implements FileParser {
    @Override
    public ParseResult parse(String filePath) throws IOException {
        byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
        String charset = detectCharset(fileBytes);
        String content = new String(fileBytes, charset);

        TxtParseResult result = new TxtParseResult();
        result.setText(content);

        return result;
    }

    private String detectCharset(byte[] content) {
        UniversalDetector detector = new UniversalDetector(null);
        detector.handleData(content, 0, content.length);
        detector.dataEnd();

        String detectedCharset = detector.getDetectedCharset();
        detector.reset();
        return detectedCharset == null ? StandardCharsets.UTF_8.name() : detectedCharset;
    }
}
