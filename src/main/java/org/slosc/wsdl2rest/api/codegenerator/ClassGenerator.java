package org.slosc.wsdl2rest.api.codegenerator;

import java.util.List;

import org.slosc.wsdl2rest.api.service.ClassDefinition;

public interface ClassGenerator {
    void generateClasses(List<ClassDefinition> clazzDef);
}
