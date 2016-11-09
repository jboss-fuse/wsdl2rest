package org.slosc.wsdl2rest;

import java.io.IOException;
import java.util.List;

public interface ClassGenerator {

    void generateClasses(List<EndpointInfo> clazzDef) throws IOException;
}
