package org.slosc.wsdl2rest;

import java.util.List;

public interface ResourceMapper {
    
    List<String> getResources();

    void assignResources(List<ClassDefinition> svcClasses);
}
