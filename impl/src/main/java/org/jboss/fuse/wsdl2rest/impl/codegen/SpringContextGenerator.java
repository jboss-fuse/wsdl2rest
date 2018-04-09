package org.jboss.fuse.wsdl2rest.impl.codegen;

import java.nio.file.Path;

public class SpringContextGenerator extends CamelContextGenerator {

    public SpringContextGenerator(Path contextPath) {
        super(contextPath);
    }

    protected String getTemplatePath() {
        return "templates/wsdl2rest-spring-context.vm";
    }
}
