package org.cyjo.barcode.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CSVSimpleParser implements InputParser {
    @Override
    public List<String> parse(Path inputFile) throws IOException {
        //assume one entry per line
        List<String> inputs = Files.readAllLines(inputFile);
        return inputs;
    }

    @Override
    public String getSupportedExtension() {
        return "csv";
    }
}
