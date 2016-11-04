package org.slosc.wsdl2rest;

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

import org.slosc.wsdl2rest.wsdl.WSDLProcessorImpl;
import org.slosc.wsdl2rest.service.ClassDefinition;
import org.slosc.wsdl2rest.wsdl.WSDLProcessor;
import org.slosc.wsdl2rest.mappings.ResourceMapperImp;
import org.slosc.wsdl2rest.mappings.ResourceMapper;
import org.slosc.wsdl2rest.codegenerator.ClassGenerator;
import org.slosc.wsdl2rest.codegenerator.ClassGeneratorFactory;
import org.slosc.wsdl2rest.util.MessageWriter;
import org.slosc.wsdl2rest.util.MessageWriterFactory;
import org.slosc.wsdl2rest.ui.Wsdl2RestForm;

import java.util.List;
import java.io.*;


public class Wsdl2Rest {

    private MessageWriter msgWriter = MessageWriterFactory.getMessageWriter();
    private List<ClassDefinition> svcClasses; 

    public void process(String... args){
        WSDLProcessor wsdlProcessor = new WSDLProcessorImpl();
        wsdlProcessor.process(args[0], args[1], args[2]);
        svcClasses = wsdlProcessor.getTypeDefs();

        // Assign resources to Class, method and parameter definitions.
        ResourceMapper resMapper = new ResourceMapperImp();
        resMapper.assignResources(svcClasses);
    }

    public List<ClassDefinition> getSvcClasses() {
        return svcClasses;
    }

    public void generateClasses(String toLocation){
        if(toLocation == null || toLocation.length() == 0) return;
        
        File clazzFileLocation = new File(toLocation);
        if(!clazzFileLocation.exists()) msgWriter.write(MessageWriter.TYPE.WARN, "Existing files will be over writtern ...");
        String outputPath = toLocation + File.separator;
        clazzFileLocation.delete();
        clazzFileLocation.mkdirs();

        ClassGenerator gen = ClassGeneratorFactory.getClassGenerator("jsr-311", outputPath);
        gen.generateClasses(svcClasses);
    }

    private static void usage() {
        System.out.println("Usage: java -cp <classpath> org.slosc.wsdl2rest.Wsdl2Rest <wsdl-file> <username> <password> <outputpath>");
    }

    public static void main(String [] args){

        if(args.length < 4){
            usage();
           System.exit(-1);
        }

        Wsdl2Rest wsdl2rest = new Wsdl2Rest();

        wsdl2rest.process(args);
        wsdl2rest.generateClasses(args[3]);

    }
}
