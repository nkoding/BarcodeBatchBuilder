package org.cyjo.barcode.parser;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface InputParser {

    /**
     * parse given inputfile
     * @param inputFile
     * @return list of barcode payloads
     */
    List<String> parse(Path inputFile) throws IOException;

    /**
     *   return a string like e.g. "csv"
     */
    String getSupportedExtension();
}
