package org.slosc.wsdl2rest;

import java.util.Map;

import javax.wsdl.WSDLException;

import java.net.URI;
import java.util.List;

public interface WSDLProcessor {

    void process(URI wsdlURI) throws WSDLException;

    List<ClassDefinition> getTypeDefs();

    Map<String, ClassDefinition> getServiceDef();
}
