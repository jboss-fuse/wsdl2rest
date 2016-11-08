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
import org.slosc.wsdl2rest.impl.ResourceMapperImpl;
import org.slosc.wsdl2rest.impl.WSDLProcessorImpl;
import org.slosc.wsdl2rest.impl.codegenerator.ClassGeneratorFactory;


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
        Assert.assertEquals(5, methods.size());
        
        MethodInfo method = clazzDef.getMethodInfo("listAddresses");
        Assert.assertEquals("int[]", method.getReturnType());
        List<Param> params = method.getParams();
        Assert.assertEquals(0, params.size());
        
        method = clazzDef.getMethodInfo("getAddress");
        Assert.assertEquals("String", method.getReturnType());
        params = method.getParams();
        Assert.assertEquals(1, params.size());
        Assert.assertEquals("arg0", params.get(0).getParamName());
        Assert.assertEquals("int", params.get(0).getParamType());
        
        method = clazzDef.getMethodInfo("addAddress");
        Assert.assertEquals("int", method.getReturnType());
        params = method.getParams();
        Assert.assertEquals(1, params.size());
        Assert.assertEquals("arg0", params.get(0).getParamName());
        Assert.assertEquals("String", params.get(0).getParamType());
        
        method = clazzDef.getMethodInfo("updAddress");
        Assert.assertEquals("String", method.getReturnType());
        params = method.getParams();
        Assert.assertEquals(2, params.size());
        Assert.assertEquals("arg0", params.get(0).getParamName());
        Assert.assertEquals("int", params.get(0).getParamType());
        Assert.assertEquals("arg1", params.get(1).getParamName());
        Assert.assertEquals("String", params.get(1).getParamType());
        
        method = clazzDef.getMethodInfo("delAddress");
        Assert.assertEquals("String", method.getReturnType());
        params = method.getParams();
        Assert.assertEquals(1, params.size());
        Assert.assertEquals("arg0", params.get(0).getParamName());
        Assert.assertEquals("int", params.get(0).getParamType());
        
        ResourceMapper resMapper = new ResourceMapperImpl();
        resMapper.assignResources(clazzDefs);

        ClassGenerator gen = ClassGeneratorFactory.getClassGenerator("../tests/src/test/java");
        gen.generateClasses(clazzDefs);
    }
}