package org.slosc.wsdl2rest.impl.wsdl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Fault;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slosc.wsdl2rest.ClassDefinition;
import org.slosc.wsdl2rest.Param;
import org.slosc.wsdl2rest.WSDLProcessor;
import org.slosc.wsdl2rest.impl.service.ClassDefinitionImpl;
import org.slosc.wsdl2rest.impl.service.MethodInfoImpl;
import org.slosc.wsdl2rest.impl.service.ParamImpl;
import org.slosc.wsdl2rest.util.IllegalStateAssertion;

/**
 * Following class process WSDL document and generate a list of service interfaces & methods
 * It follows  JSR-101 specifications
 */
public class WSDLProcessorImpl implements WSDLProcessor {

    private static Logger log = LoggerFactory.getLogger(WSDLProcessorImpl.class);

    private static final String xsdURI = "http://www.w3.org/2001/XMLSchema";

    private Map<QName, String> typeRegistry = new HashMap<>(); 
    private Map<String, ClassDefinition> serviceMap = new HashMap<String, ClassDefinition>();
    private List<ClassDefinition> clazzDefs = new ArrayList<ClassDefinition>();
    private Stack<String> services = new Stack<>();

    public WSDLProcessorImpl() {
        typeRegistry.put(new QName(xsdURI, "string"), String.class.getName());
        typeRegistry.put(new QName(xsdURI, "normalizedString"), String.class.getName());
        typeRegistry.put(new QName(xsdURI, "byte"), "byte");
        typeRegistry.put(new QName(xsdURI, "integer"), "int");
        typeRegistry.put(new QName(xsdURI, "long"), "long");
        typeRegistry.put(new QName(xsdURI, "float"), "float");
        typeRegistry.put(new QName(xsdURI, "double"), "double");
        typeRegistry.put(new QName(xsdURI, "short"), "short");
        typeRegistry.put(new QName(xsdURI, "boolean"), "boolean");
        typeRegistry.put(new QName(xsdURI, "unsignedShort"), "short");
        typeRegistry.put(new QName(xsdURI, "decimal"), "double");
        typeRegistry.put(new QName(xsdURI, "date"), Date.class.getName());
        typeRegistry.put(new QName(xsdURI, "time"), Date.class.getName());
    }

    public void process(URI wsdlURI) throws WSDLException {

        WSDLFactory factory = WSDLFactory.newInstance();
        WSDLReader reader = factory.newWSDLReader();
        reader.setFeature("javax.wsdl.verbose", true);
        reader.setFeature("javax.wsdl.importDocuments", true);
        Definition def = reader.readWSDL(null, wsdlURI.toString());

        processSchemaTypes(def);
        processServices(def);
    }

    public List<ClassDefinition> getTypeDefs() {
        for (String key : serviceMap.keySet()) {
            clazzDefs.add(serviceMap.get(key));
        }
        return clazzDefs;
    }

    public Map<String, ClassDefinition> getServiceDef() {
        return serviceMap;
    }

    @SuppressWarnings("unchecked")
    private void processSchemaTypes(Definition def) {
        for (ExtensibilityElement exel : (List<ExtensibilityElement>) def.getExtensibilityElements()) {
            if (exel instanceof Schema) {
                throw new UnsupportedOperationException("Schema types not supported");
            }
        }
    }

    private void processBindings(Definition def, Binding binding) {
        log.info("\tBinding: {}", binding.getQName().getLocalPart());
        processPortTypes(def, binding.getPortType());
    }

    /* WSDL 1.1 spec: 2.4.5 Names of Elements within an Operation
     * If the name attribute is not specified on a one-way or notification message, it defaults to the name of the operation.
     * If the name attribute is not specified on the input or output messages of a request-response or solicit-response operation,
     * the name defaults to the name of the operation with "Request"/"Solicit" or "Response" appended, respectively.
     * Each fault element must be named to allow a binding to specify the concrete format of the fault message.
     * The name of the fault element is unique within the set of faults defined for the operation.
     */
    @SuppressWarnings("unchecked")
    private void processPortTypes(Definition def, PortType portTypes) {

        log.info("\tPortType: {}", portTypes.getQName().getLocalPart());
        log.info("\tOperations: ");

        for (Object op : portTypes.getOperations()) {
            Operation oper = (Operation) op;
            String operation = oper.getName();
            log.info("\t\tOperation: {}", operation);

            ClassDefinitionImpl svcDef = (ClassDefinitionImpl) serviceMap.get(this.services.peek());
            svcDef.addMethod(operation);
            Input in = oper.getInput();
            Output out = oper.getOutput();
            Map<QName, Fault> f = oper.getFaults();

            log.info("\t\t\tInput: ");
            if (in != null) {
                if (in.getName() == null) {
                    in.setName(operation);
                }
                processMessages(def, in.getMessage(), operation, 0);
            }

            log.info("\t\t\tOutput: ");
            if (out != null) {
                if (out.getName() == null) {
                    out.setName(operation + "Response");
                }
                processMessages(def, out.getMessage(), operation, 1);
            }

            log.info("\t\t\tFaults: ");
            if (f != null) {
                for (Object o : f.values()) {
                    Fault fault = (Fault) o;
                    processMessages(def, fault.getMessage(), operation, 2);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void processMessages(Definition def, Message message, String operation, int type) {
        log.info("\t\t\tMessage: {}", message.getQName().getLocalPart());
        if (!message.isUndefined() && message.getParts() != null) {

            List<Part> parts = message.getOrderedParts(null);
            List<String> imports = new ArrayList<String>();
            List<Param> params = new ArrayList<>();
            for (Part part : parts) {
                QName paramQName = part.getElementName();
                if (paramQName == null) {
                    paramQName = new QName(part.getName());
                }
                log.info("\t\t\tPart: {}:{}", paramQName.getPrefix(), paramQName.getLocalPart());
                QName typeQName = part.getTypeName();
                String typeName = typeRegistry.get(typeQName);
                IllegalStateAssertion.assertNotNull(typeName, "Unsupported parameter type: " + typeQName);
                if (typeName.startsWith("java.lang.")) {
                    typeName = typeName.substring(10);
                }
                log.info("\t\t\t\tParams: {} {}", typeName, paramQName);
                params.add(new ParamImpl(typeName, paramQName.getLocalPart()));
            }
            if (parts.size() > 0) {
                ClassDefinitionImpl svcDef = (ClassDefinitionImpl) serviceMap.get(this.services.peek());
                MethodInfoImpl mInf = (MethodInfoImpl) svcDef.getMethodInfo(operation);
                switch (type) {
                    case 0:
                        mInf.setParams(params);
                        break;
                    case 1:
                        mInf.setReturnType(params.get(0).getParamType());
                        break;
                    case 2:
                        mInf.setExceptionType(params.get(0).getParamType());
                }
                svcDef.setImports(imports);
            }
        }
    }

    public static final String[] NS_URI_SCHEMA_XSDS = { "http://www.w3.org/1999/XMLSchema", "http://www.w3.org/2000/10/XMLSchema", xsdURI };

    @SuppressWarnings("unchecked")
    private void processServices(Definition def) {

        String svcPackageName = def.getTargetNamespace();
        Map<QName, Service> services = def.getServices();
        log.info("Services: ");
        for (Service svc : services.values()) {
            String svcName = svc.getQName().getLocalPart();
            log.info("\t{}", svcName);
            ClassDefinitionImpl svcDef = new ClassDefinitionImpl();
            svcDef.setClassName(svcName);
            svcDef.setPackageName(svcPackageName);
            serviceMap.put(svcName, svcDef);
            this.services.push(svcName);

            Map<QName, Port> ports = svc.getPorts();
            for (Port port : ports.values()) {
                log.info("\tPort: {}", port.getName());
                processBindings(def, port.getBinding());

            }
        }

    }
}
