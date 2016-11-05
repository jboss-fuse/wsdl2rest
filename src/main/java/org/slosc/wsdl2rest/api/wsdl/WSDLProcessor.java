package org.slosc.wsdl2rest.api.wsdl;

import java.util.Map;

import org.slosc.wsdl2rest.api.service.ClassDefinition;

import java.util.List;

public interface WSDLProcessor {
    Map process(String wsdlURI, String username, String password);

    Map process(String wsdlURI);

    List<ClassDefinition> getTypeDefs();

    Map<String, ClassDefinition> getServiceDef();
}
