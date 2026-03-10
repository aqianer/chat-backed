package com.example.websocketchatbacked.parser.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfPage {

    private int pageNumber;
    private String content;

}
