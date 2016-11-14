package org.jboss.fuse.wsdl2rest.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.wsdl.WSDLException;

import org.jboss.fuse.wsdl2rest.ClassGenerator;
import org.jboss.fuse.wsdl2rest.EndpointInfo;
import org.jboss.fuse.wsdl2rest.ResourceMapper;
import org.jboss.fuse.wsdl2rest.WSDLProcessor;
import org.jboss.fuse.wsdl2rest.impl.codegen.ClassGeneratorFactory;
import org.jboss.fuse.wsdl2rest.impl.writer.MessageWriter;
import org.jboss.fuse.wsdl2rest.impl.writer.MessageWriterFactory;

public class Wsdl2Rest {

    private MessageWriter msgWriter = MessageWriterFactory.getMessageWriter();
    private List<EndpointInfo> endpointInfos;

    public void process(URI wsdlURI) throws WSDLException {
        
        WSDLProcessor wsdlProcessor = new WSDLProcessorImpl();
        wsdlProcessor.process(wsdlURI);
        
        endpointInfos = wsdlProcessor.getClassDefinitions();

        ResourceMapper resMapper = new ResourceMapperImpl();
        resMapper.assignResources(endpointInfos);
    }

    public List<EndpointInfo> getSvcClasses() {
        return endpointInfos;
    }

    public void generateClasses(String toLocation) throws IOException {
        
        if (toLocation == null || toLocation.length() == 0)
            return;

        File outdir = new File(toLocation);
        if (!outdir.exists())
            msgWriter.write(MessageWriter.TYPE.WARN, "Existing files will be over writtern ...");
        
        outdir.delete();
        outdir.mkdirs();

        ClassGenerator gen = ClassGeneratorFactory.getClassGenerator(outdir.toPath());
        gen.generateClasses(endpointInfos);
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
