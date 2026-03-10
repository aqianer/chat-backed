package com.example.websocketchatbacked.splitter;

import com.example.websocketchatbacked.parser.result.ParseResult;

import java.util.List;

public interface FileSplitter {
    
    List<String> split(ParseResult parseResult);
    
    String getStrategyName();
}