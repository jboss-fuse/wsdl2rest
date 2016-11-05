package org.slosc.wsdl2rest.impl.codegenerator;

import org.slosc.wsdl2rest.api.codegenerator.ClassGenerator;
import org.slosc.wsdl2rest.impl.codegenerator.jsr311.JSR311ClassGenerator;

public class ClassGeneratorFactory {
    public static ClassGenerator getClassGenerator(String type){
        if(type.equals("jsr-311")) return new JSR311ClassGenerator();
        return new ClassGeneratorImpl();
    }

    public static ClassGenerator getClassGenerator(String type, String outputPath){
        if(type.equals("jsr-311")) return new JSR311ClassGenerator(outputPath);
        return new ClassGeneratorImpl(outputPath);
    }
}
