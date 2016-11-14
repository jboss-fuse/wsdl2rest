package org.jboss.fuse.wsdl2rest;

import java.net.URI;
import java.util.List;

import javax.wsdl.WSDLException;

public interface WSDLProcessor {

    void process(URI wsdlURI) throws WSDLException;

    List<EndpointInfo> getClassDefinitions();
}
