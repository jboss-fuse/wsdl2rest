package org.jboss.fuse.wsdl2rest.impl.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.tools.common.model.JavaInterface;
import org.apache.cxf.tools.common.model.JavaMethod;
import org.apache.cxf.tools.common.model.JavaModel;
import org.apache.cxf.tools.common.model.JavaParameter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.jboss.fuse.wsdl2rest.EndpointInfo;
import org.jboss.fuse.wsdl2rest.MethodInfo;
import org.jboss.fuse.wsdl2rest.ParamInfo;
import org.jboss.fuse.wsdl2rest.impl.service.MethodInfoImpl;
import org.jboss.fuse.wsdl2rest.impl.service.ParamImpl;
import org.jboss.fuse.wsdl2rest.util.IllegalArgumentAssertion;
import org.jboss.fuse.wsdl2rest.util.IllegalStateAssertion;

public abstract class CamelContextGenerator {

    private Path contextPath;
    private URL jaxrsAddress;
    private URL jaxwsAddress;

    CamelContextGenerator(Path contextPath) {
        this.contextPath = contextPath;
    }

    public void setJaxrsAddress(URL jaxrsAddress) {
        this.jaxrsAddress = jaxrsAddress;
    }

    public void setJaxwsAddress(URL jaxwsAddress) {
        this.jaxwsAddress = jaxwsAddress;
    }

    public void process(List<EndpointInfo> clazzDefs, JavaModel javaModel) throws IOException {
        IllegalArgumentAssertion.assertNotNull(clazzDefs, "clazzDefs");
        IllegalArgumentAssertion.assertNotNull(javaModel, "javaModel");
        IllegalStateAssertion.assertNotNull(contextPath, "Camel context file name not set");
        IllegalArgumentAssertion.assertTrue(clazzDefs.size() == 1, "Multiple endpoints not supported");
        
        EndpointInfo epinfo = clazzDefs.get(0);
        
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        String tmplPath = getTemplatePath();
        try (InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(tmplPath))) {

            jaxrsAddress = jaxrsAddress != null ? jaxrsAddress : new URL("http://localhost:8081/jaxrs");
            String jaxrsHost = jaxrsAddress.getHost();
            String jaxrsPort = Integer.toString(jaxrsAddress.getPort());
            String jaxrsPath = jaxrsAddress.getPath();
            
            VelocityContext context = new VelocityContext();
            context.put("jaxwsAddress", jaxwsAddress != null ? jaxwsAddress : "http://localhost:8080/somepath");
            context.put("jaxrsHost", jaxrsHost);
            context.put("jaxrsPort", jaxrsPort);
            context.put("jaxrsPath", jaxrsPath);
            context.put("serviceClass", epinfo.getFQN());
            context.put("allMethods", epinfo.getMethods());
            
            addTypeMapping(epinfo, javaModel);

            File outfile = contextPath.toFile();
            outfile.getParentFile().mkdirs();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outfile))) {
                ve.evaluate(context, writer, tmplPath, reader);
            }
        }
    }

    protected abstract String getTemplatePath();
    
    private void addTypeMapping(EndpointInfo epinfo, JavaModel javaModel) {
        IllegalArgumentAssertion.assertTrue(javaModel.getInterfaces().size() == 1, "Multiple interfaces not supported");
        JavaInterface javaIntrf = javaModel.getInterfaces().get(epinfo.getClassName());
        for (MethodInfo method : epinfo.getMethods()) {
            if ("document".equals(method.getStyle())) {
                JavaMethod javaMethod = getJavaMethod(javaIntrf, method.getMethodName());
                List<ParamInfo> wrappedParams = new ArrayList<>();
                for (JavaParameter javaParam : javaMethod.getParameters()) {
                    String paramName = javaParam.getName();
                    wrappedParams.add(new ParamImpl(paramName, normalize(javaParam.getClassName())));
                }
                ((MethodInfoImpl) method).setWrappedParams(wrappedParams);
                ((MethodInfoImpl) method).setWrappedReturnType(normalize(javaMethod.getReturnValue()));
            }
        }
    }

    private String normalize(String typeName) {
        if (typeName.contains("List<") && typeName.endsWith(">")) {
            typeName = typeName.substring(typeName.indexOf('<') + 1);
            typeName = typeName.substring(0, typeName.indexOf('>'));
            typeName = typeName + "[]";
        }
        return typeName;
    }
    
    private JavaMethod getJavaMethod(JavaInterface intrf, String methodName) {
        JavaMethod result = null;
        for (JavaMethod method : intrf.getMethods()) {
            if (method.getName().equals(methodName))
                result = method;
        }
        IllegalStateAssertion.assertNotNull(result, "Cannot obtain java method for: " + methodName);
        return result;
    }
}
