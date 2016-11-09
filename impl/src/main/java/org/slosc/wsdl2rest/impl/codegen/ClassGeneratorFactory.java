package org.slosc.wsdl2rest.impl.codegen;

import java.nio.file.Path;

import org.slosc.wsdl2rest.ClassGenerator;

public class ClassGeneratorFactory {
    
    public static ClassGenerator getClassGenerator(Path outputPath) {
        return new JSR311ClassGenerator(outputPath);
    }
}
