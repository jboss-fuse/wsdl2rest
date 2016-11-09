package org.slosc.wsdl2rest;

import java.util.List;

public interface ResourceMapper {
    
    void mapResources(String resourceName);

    List<String> getResources();

    String toString();

    void assignResources(List<ClassDefinition> svcClasses);
}
