package org.slosc.wsdl2rest.test.rpclit;
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

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slosc.wsdl2rest.ClassDefinition;
import org.slosc.wsdl2rest.ClassGenerator;
import org.slosc.wsdl2rest.MethodInfo;
import org.slosc.wsdl2rest.Param;
import org.slosc.wsdl2rest.ResourceMapper;
import org.slosc.wsdl2rest.WSDLProcessor;
import org.slosc.wsdl2rest.impl.codegenerator.ClassGeneratorFactory;
import org.slosc.wsdl2rest.impl.mappings.ResourceMapperImpl;
import org.slosc.wsdl2rest.impl.wsdl.WSDLProcessorImpl;


public class GenerateRPCLiteralTest {

    @Test
    public void testGenerate() throws Exception {

        WSDLProcessor wsdlProcessor = new WSDLProcessorImpl();
        wsdlProcessor.process(new File("src/test/resources/rpclit/Address.wsdl").toURI());

        List<ClassDefinition> clazzDefs = wsdlProcessor.getTypeDefs();
        Assert.assertEquals(1, clazzDefs.size());
        ClassDefinition clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.slosc.wsdl2rest.test.rpclit", clazzDef.getPackageName());
        Assert.assertEquals("AddressService", clazzDef.getClassName());

        List<MethodInfo> methods = clazzDef.getMethodInfos();
        Assert.assertEquals(2, methods.size());
        Assert.assertEquals("addAddress", methods.get(0).getMethodName());
        Assert.assertEquals("getAddress", methods.get(1).getMethodName());
        List<Param> params = methods.get(0).getParams();
        Assert.assertEquals(1, params.size());
        Assert.assertEquals("arg0", params.get(0).getParamName());
        Assert.assertEquals("String", params.get(0).getParamType());
        
        ResourceMapper resMapper = new ResourceMapperImpl();
        resMapper.assignResources(clazzDefs);

        ClassGenerator gen = ClassGeneratorFactory.getClassGenerator("../tests/src/test/java");
        gen.generateClasses(clazzDefs);
    }
}