package org.slosc.wsdl2rest.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.wsdl.WSDLException;

import org.slosc.wsdl2rest.ClassDefinition;
import org.slosc.wsdl2rest.ClassGenerator;
import org.slosc.wsdl2rest.ResourceMapper;
import org.slosc.wsdl2rest.WSDLProcessor;
import org.slosc.wsdl2rest.impl.codegenerator.ClassGeneratorFactory;
import org.slosc.wsdl2rest.impl.mappings.ResourceMapperImpl;
import org.slosc.wsdl2rest.impl.util.MessageWriter;
import org.slosc.wsdl2rest.impl.util.MessageWriterFactory;
import org.slosc.wsdl2rest.impl.wsdl.WSDLProcessorImpl;

public class Wsdl2Rest {

    private MessageWriter msgWriter = MessageWriterFactory.getMessageWriter();
    private List<ClassDefinition> svcClasses;

    public void process(URI wsdlURI) throws WSDLException {
        WSDLProcessor wsdlProcessor = new WSDLProcessorImpl();
        wsdlProcessor.process(wsdlURI);
        svcClasses = wsdlProcessor.getTypeDefs();

        // Assign resources to Class, method and parameter definitions.
        ResourceMapper resMapper = new ResourceMapperImpl();
        resMapper.assignResources(svcClasses);
    }

    public List<ClassDefinition> getSvcClasses() {
        return svcClasses;
    }

    public void generateClasses(String toLocation) throws IOException {
        if (toLocation == null || toLocation.length() == 0)
            return;

        File clazzFileLocation = new File(toLocation);
        if (!clazzFileLocation.exists())
            msgWriter.write(MessageWriter.TYPE.WARN, "Existing files will be over writtern ...");
        String outputPath = toLocation + File.separator;
        clazzFileLocation.delete();
        clazzFileLocation.mkdirs();

        ClassGenerator gen = ClassGeneratorFactory.getClassGenerator(outputPath);
        gen.generateClasses(svcClasses);
    }

    private static void usage() {
        System.out.println("Usage: java -cp <classpath> org.slosc.wsdl2rest.Wsdl2Rest <wsdl-uri> <outputpath>");
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 2) {
            usage();
            System.exit(-1);
        }

        Wsdl2Rest wsdl2rest = new Wsdl2Rest();
        wsdl2rest.process(new URI(args[0]));
        wsdl2rest.generateClasses(args[1]);

    }
}
