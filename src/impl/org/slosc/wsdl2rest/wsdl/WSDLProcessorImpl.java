package org.slosc.wsdl2rest.wsdl;

/*
 * Copyright (c) 2008 SL_OpenSource Consortium
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.apache.wsif.schema.Parser;
import org.apache.wsif.util.WSIFUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slosc.wsdl2rest.service.*;

import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
//import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.factory.*;
import javax.wsdl.xml.*;
import javax.xml.namespace.QName;
import java.util.*;
import java.io.File;

/**
 * Following class process WSDL document and generate a list of service interfaces & methods
 * It follows  JSR-101 specifications
 */
public class WSDLProcessorImpl implements WSDLProcessor {

    private static Log log = LogFactory.getLog(WSDLProcessorImpl.class);
    
    private static final String xsdURI = "http://www.w3.org/2001/XMLSchema";

    //Mapping XML types to Java types
    //the JAX-RPC mapping specification does not dictate a specific Java mapping for xsd:anyType.
    private static String Types[] = {
        "string", "normalizedString", "token", "byte", "unsignedByte",
        "base64Binary", "hexBinary", "integer", "positiveInteger",
        "negativeInteger", "nonNegativeInteger", "nonPositiveInteger", "int",
        "unsignedInt", "long", "unsignedLong", "short", "unsignedShort",
        "decimal", "float", "double", "boolean", "time", "dateTime", "duration",
        "date", "gMonth", "gYear", "gYearMonth", "gDay", "gMonthDay", "Name",
        "QName", "NCName", "anyURI", "language", "ID", "IDREF", "IDREFS",
        "ENTITY", "ENTITIES", "NOTATION", "NMTOKEN", "NMTOKENS",
        "anySimpleType"
    };

    private static Map typeRegistry = new HashMap();

    private Map<String, ClassDefinition> serviceDef = new HashMap<String, ClassDefinition>();
    private List<ClassDefinition> typeDefs = new ArrayList<ClassDefinition>();
    private Stack svc = new Stack();
    private Stack operation = new Stack();


    public WSDLProcessorImpl(){
        //TODO - fix this list
//        typeRegistry.put("string", String.class.getName());
//        typeRegistry.put("normalizedString", String.class.getName());
//        typeRegistry.put("byte", "byte");
//        typeRegistry.put("integer", "int");
//        typeRegistry.put("long", "long");
//        typeRegistry.put("float", "float");
//        typeRegistry.put("double", "double");
//        typeRegistry.put("short", "short");
//        typeRegistry.put("boolean", "boolean");
//        typeRegistry.put("unsignedShort", "short");
//        typeRegistry.put("decimal", "double");
//        typeRegistry.put("date", Date.class.getName());
//        typeRegistry.put("time", Date.class.getName());
    }

    public Map process(String wsdlURI, String username, String password){
        try{
            File f = new File(wsdlURI);
            if(f.exists())
                wsdlURI = f.toURL().toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return process(wsdlURI);
    }

    public Map process(String wsdlURI){

        try{
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", true);
            reader.setFeature("javax.wsdl.importDocuments", true);
            Definition def = reader.readWSDL(null, wsdlURI);

            prcessSchemaTypes(def);
            processServices(def);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<ClassDefinition> getTypeDefs() {
        for(String key: serviceDef.keySet()){
            typeDefs.add(serviceDef.get(key));    
        }
        return typeDefs;
    }

    public Map<String, ClassDefinition> getServiceDef() {
        return serviceDef;
    }

    private void processImports(Definition def) {
        Map imports = def.getImports();
        for (Object o : imports.values()) {
            Import imp = (Import) o;
            Definition importDef = imp.getDefinition();

        }
    }

    private void prcessSchemaTypes(Definition def) throws Exception {
        //Get ride of this once we implement our own type mappings.
        Parser.getTypeMappings(def, typeRegistry, true, null);
        Iterator itr = typeRegistry.keySet().iterator();
        log.info("Type mapping:");
        while(itr.hasNext()){
            QName key = (QName)itr.next();
            log.info("Key: "+key+"\tValue: "+typeRegistry.get(key));
        }
        
    }

    public void processMessages(Definition def){
        Map messages = def.getMessages();
        log.info("Messages: ");
        for (Object o : messages.values()) {
            Message msg = (Message) o;
            if (!msg.isUndefined()) {
                log.info("\t"+msg.getQName());
            }
        }
    }

    private void processPortTypes(Definition def) {

        Map portTypes = def.getPortTypes();
        log.info("PortTypes: ");
        for (Object o : portTypes.values()) {
            PortType ptype = (PortType) o;
            if (!ptype.isUndefined()) {
                log.info("\t"+ptype.getQName());
            }
        }
    }

    private void processBindings(Definition def) {

        Map bindings = def.getBindings();
        log.info("Bindings: ");
        for (Object o : bindings.values()) {
            Binding  binding = (Binding) o;
            if (! binding.isUndefined()) {
                log.info( "\t"+binding.getQName());
            }
        }
    }

    private void processBindings(Definition def, Binding binding){

        log.info("\tBinding: "+binding.getQName().getLocalPart());
        processPortTypes(def, binding.getPortType());
    }

    /* WSDL 1.1 spec: 2.4.5 Names of Elements within an Operation
     * If the name attribute is not specified on a one-way or notification message, it defaults to the name of the operation.
     * If the name attribute is not specified on the input or output messages of a request-response or solicit-response operation,
     * the name defaults to the name of the operation with "Request"/"Solicit" or "Response" appended, respectively.
     * Each fault element must be named to allow a binding to specify the concrete format of the fault message.
     * The name of the fault element is unique within the set of faults defined for the operation.
     */
    private void processPortTypes(Definition def, PortType portTypes){

        log.info("\tPortType: "+portTypes.getQName().getLocalPart());
        log.info("\tOperations: ");

        for (Object op : portTypes.getOperations()) {
            Operation oper = (Operation) op;
            String operation = oper.getName();
            log.info("\t\tOperation: " + operation);
            
            ClassDefinitionImpl svcDef = (ClassDefinitionImpl) serviceDef.get(this.svc.peek());
            svcDef.addMethod(operation);
            List paramOrder = oper.getParameterOrdering();
//            if(paramOrder.get(0))
            Input in = oper.getInput();
            Output out = oper.getOutput();
            Map f = oper.getFaults();


            log.info("\t\t\tInput: ");
            if(in != null){
                if(in.getName() == null) in.setName(operation);
                processMessages(def, in.getMessage(),operation, 0);
            }
            
            log.info("\t\t\tOutput: ");
            if(out != null) {
                if(out.getName() == null) out.setName(operation+"Response");
                processMessages(def, out.getMessage(), operation, 1);
            }

            log.info("\t\t\tFaults: ");
            if(f != null){
                for (Object o : f.values()) {
                    Fault fault = (Fault) o;
                    processMessages(def, fault.getMessage(),operation, 2);
                }
            }
        }
    }


    private static Part getWrappedDocLiteralPart(List parts, String operationName) {
		boolean wrapped = !(parts==null);
		Part elementPart = null;
		for (int i = 0; wrapped && i < parts.size(); i++) {
			Part p = (Part) parts.get(i);
			if (p.getElementName() != null) {
				if (elementPart == null) {
					elementPart = p;
   				    String pName = p.getElementName().getLocalPart();
                    if(pName.endsWith("Response")) pName = pName.substring(0, pName.lastIndexOf("Response"));
                    if (!operationName.equals(pName)) {
					   wrapped = false;
				    }
				} else {
					wrapped = false;
				}
			}
		}
		if (!wrapped) {
			elementPart = null;
		}
		return elementPart;
	}

    private List unwrapParts(Definition def, Message message, String operation){
        List parts = message.getOrderedParts(null);
        Part p = getWrappedDocLiteralPart(parts, operation);
        if (p != null) {
            List unWrappedParts = null;
            try {
                unWrappedParts = WSIFUtils.unWrapPart(p, def);
            } catch (Exception e) {
                log.error("Exception in unWrapping Parts: ", e);
            }
            parts.remove(p);
            if(unWrappedParts != null){
                parts.addAll(unWrappedParts);
            }
        }
        return parts;
    }

    public void processMessages(Definition def, Message message, String operation, int type) {
        log.info("\t\t\tMessage: "+message.getQName().getLocalPart());
        if(!message.isUndefined() && message.getParts() != null){

            List parts = unwrapParts(def, message, operation);
            if(parts == null || parts.size() == 0) return;
            List<Param> params = new ArrayList<Param>();
            int indx = -1;
            List<String> imports = new ArrayList<String>();
            for(Object p: parts){
               Part part = (Part)p;

               if(part != null){
                   QName elmName = part.getElementName();
                   if(elmName == null) continue;
                   log.info("\t\t\tPart: "+ elmName.getPrefix()+":"+elmName.getLocalPart());
                   String paramType = (String)typeRegistry.get(elmName);
                   if(paramType == null) continue; 
                   log.info("\t\t\t\tParams: "+paramType+" "+part.getName());
                   //prcessSchemaTypes(def, part.getElementName());
                   int packLoc = paramType.lastIndexOf(".");
                   if(packLoc > 0){
                       String imp = paramType.substring(0,packLoc);
                       if(!imp.equals("java.lang"))
                            imports.add(imp);
                       paramType = paramType.substring(packLoc+1);
                   }
                   params.add(new ParamImpl(paramType, part.getName()));
                   indx++;
               }
           }
           if(indx >= 0){
               ClassDefinitionImpl svcDef = (ClassDefinitionImpl) serviceDef.get(this.svc.peek());
               MethodInfoImpl mInf = svcDef.getMethodInfo(operation);
               switch(type){
                   case 0: mInf.setParams(params); break;
                   case 1: mInf.setReturnType(params.get(0).getParamType()); break;
                   case 2: mInf.setExceptionType(params.get(0).getParamType());
               }
               svcDef.setImports(imports);
           }
        }
    }

   public static final String [] NS_URI_SCHEMA_XSDS ={"http://www.w3.org/1999/XMLSchema",
           "http://www.w3.org/2000/10/XMLSchema", "http://www.w3.org/2001/XMLSchema"};
  


    //TODO - fix this; this is really complex part. make it as single iteration at begin and then reused the popuplated info.
    private void prcessSchemaTypes(Definition def, QName type) {
        
        Types types = def.getTypes();
        Map imports = def.getImports();
        //TODO - add support for imported schemas
        if(types == null) return;


        for (Object oee : types.getExtensibilityElements()) {
            QName qn = ((ExtensibilityElement)oee).getElementType();
            String ns = qn.getNamespaceURI();
            //ignore anything other than schema;
            // TODO imports are not supported at this time
            if(!(qn.getLocalPart().equals("schema") &&
                    (NS_URI_SCHEMA_XSDS[0].equals(ns)
                    ||NS_URI_SCHEMA_XSDS[1].equals(ns)
                    ||NS_URI_SCHEMA_XSDS[2].equals(ns)))) continue;
            Element e = null;//((Schema) oee).getElement();
            //ignore any other targetNamespaces for current lookup.
            String targetNamespace = e.getAttribute("targetNamespace");
            if(!targetNamespace.equals(type.getNamespaceURI())) continue;

            NodeList nL = e.getChildNodes();
            for(int i=0;i<nL.getLength();i++){
                Node node = nL.item(i);
                if(node.getNodeType() != Node.ELEMENT_NODE) continue;
                Element el = (Element)node;

                //check if this element is the one we are looking for?
                Node name = el.getAttributes().getNamedItem("name");
                if(!name.getNodeValue().equals(type.getLocalPart())) continue;

                //ok now we found the required element; now see what is the type of that element
                String elLocalName = el.getLocalName();
                if (elLocalName.equals("complexType")) {
                    //this need to map to a bean parameter
                    processSchemaComplexTypes(el, type);
				} else if (elLocalName.equals("simpleType")) {
                    processSchemaSimpleType(el, type);
				} else if (elLocalName.equals("element")) {
                    processSchemaElementType(el, type);
				} else{
                    //TODO ignore other element types for now
                }

                Node complexTypes = el.getElementsByTagNameNS(xsdURI,"complexType").item(0);
                if(complexTypes == null) continue;
                Node sequence     = ((Element)complexTypes).getElementsByTagNameNS(xsdURI,"sequence").item(0);

                NodeList elements = ((Element)sequence).getElementsByTagNameNS(xsdURI,"element");
                for(int k=0;k<elements.getLength();k++){
                    Node elNode = elements.item(k);
                    Node seqName = elNode.getAttributes().getNamedItem("name");
                    Node seqType = elNode.getAttributes().getNamedItem("type");

                    String typ = seqType.getNodeValue();
                    int loc    = typ.indexOf(":");

                    if(loc > 0) {
                        typ = (String)typeRegistry.get(typ.substring(loc+1));
                    }
                    log.info("\t\t\tparam: "+typ+" "+seqName.getNodeValue());
                }
            }
        }
    }

    private void processSchemaElementType(Element el, QName type) {

    }

    private void processSchemaSimpleType(Element el, QName type) {

    }

    private void processSchemaComplexTypes(Element el, QName type) {
        
    }

    private void processServices(Definition def) {

        String svcPackageName=def.getTargetNamespace();
        Map services = def.getServices();
        log.info("Services: ");
        for (Object o : services.values()) {
            Service svc = (Service) o;
            final String svcName = svc.getQName().getLocalPart();
            log.info("\t"+svcName);
            ClassDefinitionImpl svcDef = new ClassDefinitionImpl();
            svcDef.setClassName(svcName);
            svcDef.setPackageName(svcPackageName);
            serviceDef.put(svcName, svcDef);
            this.svc.push(svcName);

            Map ports = svc.getPorts();
            for(Object po: ports.values()){
                Port port = (Port) po;
                log.info("\tPort: "+port.getName());
                processBindings(def, port.getBinding());

            }
        }

    }


    //following is a plain vanila test for this class

    public static void main(String[] args) {

        if(args.length < 3){
            usage();
        }

        WSDLProcessor wsdlProcessor = new WSDLProcessorImpl();
        wsdlProcessor.process(args[0], args[1], args[2]);
        List<ClassDefinition> svcClasses = wsdlProcessor.getTypeDefs();

        for(ClassDefinition clazzDef : svcClasses){
            System.out.println("\npackage "+clazzDef.getPackageName()+";\n\n");
            if(clazzDef.getImports() != null){
                for(String impo : clazzDef.getImports()){
                  System.out.println("import "+impo+";");
                }
            }
            System.out.print("\n\npublic interface ");
            System.out.println(clazzDef.getClassName()+" {\n");
            for(MethodInfo mInf:clazzDef.getMethods()){
                System.out.print("\t"+mInf.getReturnType()+" ");
                System.out.print(mInf.getMethodName()+"(");
                List<Param> params = mInf.getParams();
                if(params != null){
                    int i=0; int size = params.size();
                    for(Param p : params){
                        String comma = (++i != size)?", ":"";
                        System.out.print(p.getParamType()+" "+p.getParamName()+comma);
                    }
                }
                String excep = mInf.getExceptionType() != null?(" throws "+ mInf.getExceptionType()):"";
                System.out.println(")"+excep+";");
            }
            System.out.println("}\n\n\n");

        }
    }

    private static void usage() {
        System.out.println("Usage: wsdl2rest <wsdl>");
    }
}
