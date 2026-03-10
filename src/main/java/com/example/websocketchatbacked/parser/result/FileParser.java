package com.example.websocketchatbacked.parser.result;

import java.io.IOException;

public interface FileParser {
    
    ParseResult parse(String file) throws IOException;
}