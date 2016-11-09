package org.slosc.wsdl2rest;

import java.util.List;

public interface ClassGenerator {
    void generateClasses(List<ClassDefinition> clazzDef);
}
