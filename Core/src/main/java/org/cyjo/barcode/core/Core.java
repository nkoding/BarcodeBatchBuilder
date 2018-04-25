package org.cyjo.barcode.core;

import org.cyjo.barcode.builder.BarcodeBuilder;
import org.cyjo.barcode.parser.CSVSimpleParser;

import java.nio.file.Paths;

public class Core {
    public static void main(String[] args){
        if(args.length != 3){
            System.out.println("Error in args! usage:\nCore <pathToInputFile> <bcFormat> <resolution> <outPrefix> <outFormat>");
            System.exit(-1);
        }
        String inputFile = args[0];
        String bcFormat = args[1];
        int res = Integer.parseInt(args[2]);
        String outPrefix = args[3];
        String outFormat = args[4];

        BarcodeBuilder bbuilder = new BarcodeBuilder();
        //add csv support
        bbuilder.registerParser(new CSVSimpleParser());

        //generate from given inputfile
        bbuilder.createBarcodeImageFiles(Paths.get(inputFile), bcFormat, res, outPrefix, outFormat);
    }
}
