package com.example.websocketchatbacked.parser.result;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordParseResult implements ParseResult {
    private String content;
}
