package com.epam.training.gen.ai.splitter;

import com.microsoft.semantickernel.text.TextChunker;

import java.util.List;

public class TextSplitter {


    public static List<String> splitSource(String input) {
        return TextChunker.splitPlainTextParagraphs(TextChunker.splitPlainTextLines(input, 300),
                150);
    }
}
