package org.slosc.wsdl2rest.impl;

import org.slosc.wsdl2rest.api.codegenerator.ClassGenerator;
import org.slosc.wsdl2rest.api.mappings.ResourceMapper;
import org.slosc.wsdl2rest.api.service.ClassDefinition;
import org.slosc.wsdl2rest.api.wsdl.WSDLProcessor;
import org.slosc.wsdl2rest.impl.codegenerator.ClassGeneratorFactory;
import org.slosc.wsdl2rest.impl.mappings.ResourceMapperImp;
import org.slosc.wsdl2rest.impl.ui.Wsdl2RestForm;
import org.slosc.wsdl2rest.impl.util.MessageWriter;
import org.slosc.wsdl2rest.impl.util.MessageWriterFactory;
import org.slosc.wsdl2rest.impl.wsdl.WSDLProcessorImpl;

import java.util.List;
import java.io.*;


public class Wsdl2Rest {

    private MessageWriter msgWriter = MessageWriterFactory.getMessageWriter();
    private List<ClassDefinition> svcClasses; 

    public void process(String... args){
        WSDLProcessor wsdlProcessor = new WSDLProcessorImpl();
        wsdlProcessor.process(args[0], args[1], args[2]);
        svcClasses = wsdlProcessor.getTypeDefs();

        // Assign resources to Class, method and parameter definitions.
        ResourceMapper resMapper = new ResourceMapperImp();
        resMapper.assignResources(svcClasses);
    }

    public List<ClassDefinition> getSvcClasses() {
        return svcClasses;
    }

    public void generateClasses(String toLocation){
        if(toLocation == null || toLocation.length() == 0) return;
        
        File clazzFileLocation = new File(toLocation);
        if(!clazzFileLocation.exists()) msgWriter.write(MessageWriter.TYPE.WARN, "Existing files will be over writtern ...");
        String outputPath = toLocation + File.separator;
        clazzFileLocation.delete();
        clazzFileLocation.mkdirs();

        ClassGenerator gen = ClassGeneratorFactory.getClassGenerator("jsr-311", outputPath);
        gen.generateClasses(svcClasses);
    }

    private static void usage() {
        System.out.println("Usage: java -cp <classpath> org.slosc.wsdl2rest.Wsdl2Rest <wsdl-file> <username> <password> <outputpath>");
    }

    public static void main(String [] args){

        if(args.length < 4){
            usage();
           System.exit(-1);
        }

        Wsdl2Rest wsdl2rest = new Wsdl2Rest();

        wsdl2rest.process(args);
        wsdl2rest.generateClasses(args[3]);

    }
}
