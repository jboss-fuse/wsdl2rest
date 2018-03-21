package org.jboss.fuse.wsdl2rest.impl;

import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.cxf.tools.common.model.JavaModel;
import org.jboss.fuse.wsdl2rest.EndpointInfo;
import org.jboss.fuse.wsdl2rest.ResourceMapper;
import org.jboss.fuse.wsdl2rest.WSDLProcessor;
import org.jboss.fuse.wsdl2rest.impl.codegen.CamelContextGenerator;
import org.jboss.fuse.wsdl2rest.impl.codegen.JavaTypeGenerator;
import org.jboss.fuse.wsdl2rest.util.IllegalArgumentAssertion;

public class Wsdl2Rest {

    private final URL wsdlUrl;
    private final Path outpath;

    private URL jaxrsAddress;
    private URL jaxwsAddress;
    private Path camelContext;
    
    public Wsdl2Rest(URL wsdlUrl, Path outpath) {
        IllegalArgumentAssertion.assertNotNull(wsdlUrl, "wsdlUrl");
        IllegalArgumentAssertion.assertNotNull(outpath, "outpath");
        this.wsdlUrl = wsdlUrl;
        this.outpath = outpath;
    }

    public void setJaxrsAddress(URL jaxrsAddress) {
        this.jaxrsAddress = jaxrsAddress;
    }

    public void setJaxwsAddress(URL jaxwsAddress) {
        this.jaxwsAddress = jaxwsAddress;
    }

    public void setCamelContext(Path camelContext) {
        this.camelContext = camelContext;
    }

    public List<EndpointInfo> process() throws Exception {
        
        WSDLProcessor wsdlProcessor = new WSDLProcessorImpl();
        wsdlProcessor.process(wsdlUrl);
        
        List<EndpointInfo> clazzDefs = wsdlProcessor.getClassDefinitions();
        ResourceMapper resMapper = new ResourceMapperImpl();
        resMapper.assignResources(clazzDefs);

        JavaTypeGenerator typeGen = new JavaTypeGenerator(outpath, wsdlUrl);
        JavaModel javaModel = typeGen.execute();
        
        if (camelContext != null) {
            CamelContextGenerator camelGen = new CamelContextGenerator(outpath);
            camelGen.setCamelContext(camelContext);
            camelGen.setJaxrsAddress(jaxrsAddress);
            camelGen.setJaxwsAddress(jaxwsAddress);
            camelGen.process(clazzDefs, javaModel);
        }
        
        return Collections.unmodifiableList(clazzDefs);
    }
}
