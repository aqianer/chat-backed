package com.example.websocketchatbacked.parser.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PdfParseResult implements ParseResult{
    private List<PdfPage> pages;
}
