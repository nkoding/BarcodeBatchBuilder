package org.cyjo.barcode.builder;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import net.sourceforge.barbecue.output.OutputException;
import org.cyjo.barcode.exceptions.UnknownBarcodeFormatException;
import org.cyjo.barcode.exceptions.UnknownInputFormatException;
import org.cyjo.barcode.exceptions.UnsupportedOuputFormatException;
import org.cyjo.barcode.parser.InputParser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public class BarcodeBuilder {
    private HashMap<String, InputParser> parsers;

    public BarcodeBuilder() {
        parsers = new HashMap<>();
    }

    public void registerParser(InputParser parser){
        parsers.put(parser.getSupportedExtension(), parser);
    }

    /**
     * creates a barcode image
     * @param inputFile textfile, csv file, excel file, see supported input formats for more
     * @param bcFormat format of the barcode. See barbecue doc for supported formats
     * @param resolution resulting image resolution
     * @param filePrefix a name prefix for generatedc files. all files will be created in ./barcodes_&lt;timestamp&gt;/PREFIX_[index].[png|jpg|gif]
     */
    public File createBarcodeImageFiles(Path inputFile, String bcFormat, int resolution, String filePrefix, String outputFormat){
        try {
            Barcode barcode;
            //parse input
            List<String> inputs=parseinputFile(inputFile);

            File bcOutDir = new File("./barcodes_"+System.currentTimeMillis());
            if( ! bcOutDir.exists()){
                bcOutDir.mkdir();
            }
            //build barcode for each input
            for(int i=0; i<inputs.size(); i++){
                String input = inputs.get(i);
                barcode = createBarcode(input, bcFormat);
                barcode.setResolution(resolution);

                //barcodes_2019034719872643/prefix_123.png
                File outFile = new File(bcOutDir.getAbsolutePath()+File.separator+filePrefix+"_"+i+"."+outputFormat);
                switch (outputFormat.toLowerCase()){
                    case "png":
                        BarcodeImageHandler.savePNG(barcode, outFile);
                        break;
                    case "jpg":
                    case "jpeg":
                        BarcodeImageHandler.saveJPEG(barcode, outFile);
                        break;
                    case "gif":
                        BarcodeImageHandler.saveGIF(barcode, outFile);
                        break;
                    default:
                        throw new UnsupportedOuputFormatException("Unsupported output format: "+outputFormat);
                }
                System.out.println("created barcode file: "+outFile.getAbsolutePath());
            }
            return bcOutDir;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnknownInputFormatException e) {
            e.printStackTrace();
        } catch (OutputException e) {
            e.printStackTrace();
        } catch (UnknownBarcodeFormatException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (UnsupportedOuputFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Barcode createBarcode(String input, String bcFormat) throws UnknownBarcodeFormatException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String factoryMethodName = "create"+bcFormat;
        Method method = BarcodeFactory.class.getDeclaredMethod(factoryMethodName, String.class);
        if( method == null){
            throw new UnknownBarcodeFormatException("Unknown barcode format: "+bcFormat);
        }
        //run factory method
        Barcode barcode = (Barcode) method.invoke(null, input);
        return barcode;
    }

    private List<String> parseinputFile(Path inputFile) throws IOException, UnknownInputFormatException {
        InputParser parser = findParser(inputFile);
        return parser.parse(inputFile);
    }

    private InputParser findParser(Path inputFile) throws UnknownInputFormatException {
        InputParser parser = parsers.get(getExt(inputFile.toFile()));
        if(parser == null){
            throw new UnknownInputFormatException("Dunno how to parse '*."+getExt(inputFile.toFile())+"' files.");
        }
        return parser;
    }

    private String getExt(File file) {
        String extension = "";
        String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }
}
