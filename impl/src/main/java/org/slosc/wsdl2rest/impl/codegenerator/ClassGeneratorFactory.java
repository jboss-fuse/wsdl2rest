package org.slosc.wsdl2rest.impl.codegenerator;

import org.slosc.wsdl2rest.ClassGenerator;

public class ClassGeneratorFactory {
    
    public static ClassGenerator getClassGenerator() {
        return getClassGenerator(null);
    }

    public static ClassGenerator getClassGenerator(String outputPath) {
        return new JSR311ClassGenerator(outputPath);
    }
}
