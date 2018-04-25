package org.cyjo.barcode.builder;

import org.cyjo.barcode.parser.CSVSimpleParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class BarcodeBuilderTest {
    @Test
    public void testBarcodeIntegration() throws IOException {
        File inputFile = new File("./src/test/resources/sample1.csv");
        if( ! inputFile.exists()){
            throw new IOException("can't find file at "+inputFile.getAbsolutePath());
        }
        String bcFormat = "Code128";
        int res = 300;
        String outPrefix = "test";
        String outFormat = "png";
        BarcodeBuilder bbuilder = new BarcodeBuilder();
        //add csv support
        bbuilder.registerParser(new CSVSimpleParser());

        //generate from given inputfile
        File outDir = bbuilder.createBarcodeImageFiles(Paths.get(inputFile.getAbsolutePath()), bcFormat, res, outPrefix, outFormat);
        assertTrue(outDir.list().length > 0);
        outDir.delete();
    }

}