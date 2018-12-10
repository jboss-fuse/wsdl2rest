/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.fuse.wsdl2rest.test.calculator;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import org.jboss.fuse.wsdl2rest.EndpointInfo;
import org.jboss.fuse.wsdl2rest.impl.Wsdl2Rest;
import org.junit.Assert;
import org.junit.Test;


public class CalculatorDocLitTest {

    @Test
    public void testCalculator() throws Exception {
        
        File wsdlFile = new File("src/test/resources/calculator/calculator.wsdl");
        Assert.assertTrue(wsdlFile.exists());
        
        Path outpath = Paths.get("target/wsdl2rest/calculator");
        Wsdl2Rest wsdl2Rest = new Wsdl2Rest(wsdlFile.toURI().toURL(), outpath);

        List<EndpointInfo> clazzDefs = wsdl2Rest.process();
        Assert.assertEquals(1, clazzDefs.size());
        EndpointInfo clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.Example", clazzDef.getPackageName());
        Assert.assertEquals("ICalculator", clazzDef.getClassName());
        
        Path contextPath = Paths.get("camel", "wsdl2rest-camel-context.xml");
        Path outContextPath = outpath.resolve(contextPath);
        File cctxFile = outContextPath.toFile();
        Assert.assertTrue(cctxFile.exists());
        
        // validate that we are getting the JAX-WS service address from the WSDL file
        Assert.assertTrue(searchForValue("http://Example.org/ICalculator?", outContextPath));
    }

    @Test
    public void testCalculatorJAXWSOverride() throws Exception {
        
        File wsdlFile = new File("src/test/resources/calculator/calculator.wsdl");
        Assert.assertTrue(wsdlFile.exists());
        
        Path outpath = Paths.get("target/wsdl2rest/calculatorJAXWSOverride");
        Wsdl2Rest wsdl2Rest = new Wsdl2Rest(wsdlFile.toURI().toURL(), outpath);
        
        // override the JAX-WS service address
        wsdl2Rest.setJaxwsAddress(new URL("http://other.com/calcservice"));

        List<EndpointInfo> clazzDefs = wsdl2Rest.process();
        Assert.assertEquals(1, clazzDefs.size());
        EndpointInfo clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.Example", clazzDef.getPackageName());
        Assert.assertEquals("ICalculator", clazzDef.getClassName());
        
        Path contextPath = Paths.get("camel", "wsdl2rest-camel-context.xml");
        Path outContextPath = outpath.resolve(contextPath);
        File cctxFile = outContextPath.toFile();
        Assert.assertTrue(cctxFile.exists());
        
        // validate that we are overriding the JAX-WS service address from the WSDL file
        Assert.assertTrue(searchForValue("http://other.com/calcservice?", outContextPath));
    }

    @Test
    public void testCalculatorNoServiceInWSDL() throws Exception {
        
        File wsdlFile = new File("src/test/resources/calculator/calculatorNoAddress.wsdl");
        Assert.assertTrue(wsdlFile.exists());
        
        Path outpath = Paths.get("target/wsdl2rest/calculatorNoService");
        Wsdl2Rest wsdl2Rest = new Wsdl2Rest(wsdlFile.toURI().toURL(), outpath);
        
        List<EndpointInfo> clazzDefs = wsdl2Rest.process();
        Assert.assertEquals(1, clazzDefs.size());
        EndpointInfo clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.Example", clazzDef.getPackageName());
        Assert.assertEquals("ICalculator", clazzDef.getClassName());
        
        Path contextPath = Paths.get("camel", "wsdl2rest-camel-context.xml");
        Path outContextPath = outpath.resolve(contextPath);
        File cctxFile = outContextPath.toFile();
        Assert.assertTrue(cctxFile.exists());
        
        // validate that we are getting the default JAX-WS service address since there is no
        // service address in the wsdl 
        Assert.assertTrue(searchForValue("http://localhost:8080/somepath?", outContextPath));
    }

    @Test
    public void testCalculatorNoServiceInWSDLAndJAXWSOverride() throws Exception {
        
        File wsdlFile = new File("src/test/resources/calculator/calculatorNoAddress.wsdl");
        Assert.assertTrue(wsdlFile.exists());
        
        Path outpath = Paths.get("target/wsdl2rest/calculatorNoServiceWithOverride");
        Wsdl2Rest wsdl2Rest = new Wsdl2Rest(wsdlFile.toURI().toURL(), outpath);

        // override the JAX-WS service address
        wsdl2Rest.setJaxwsAddress(new URL("http://other.com/calc"));

        List<EndpointInfo> clazzDefs = wsdl2Rest.process();
        Assert.assertEquals(1, clazzDefs.size());
        EndpointInfo clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.Example", clazzDef.getPackageName());
        Assert.assertEquals("ICalculator", clazzDef.getClassName());
        
        Path contextPath = Paths.get("camel", "wsdl2rest-camel-context.xml");
        Path outContextPath = outpath.resolve(contextPath);
        File cctxFile = outContextPath.toFile();
        Assert.assertTrue(cctxFile.exists());
        
        // validate that we are overriding the default since no service address exists in WSDL
        Assert.assertTrue(searchForValue("http://other.com/calc?", outContextPath));
    }

    private boolean searchForValue(String value, Path filePath) throws Exception {
        Scanner kb = new Scanner(value);
    	try {
	        String name = kb.nextLine();
	
	        List<String> lines = Files.readAllLines(filePath);
	        for (String line : lines) {
	            if (line.contains(name)) {
	                return true;
	            }
	        }
    	} finally {
    		kb.close();
    	}
    	return false;
    }    
}
