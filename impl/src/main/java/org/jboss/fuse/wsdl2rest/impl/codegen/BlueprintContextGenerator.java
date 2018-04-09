package org.jboss.fuse.wsdl2rest.impl.codegen;

import java.nio.file.Path;

public class BlueprintContextGenerator extends CamelContextGenerator {

    public BlueprintContextGenerator(Path contextPath) {
        super(contextPath);
    }

    protected String getTemplatePath() {
        return "templates/wsdl2rest-blueprint-context.vm";
    }
}
