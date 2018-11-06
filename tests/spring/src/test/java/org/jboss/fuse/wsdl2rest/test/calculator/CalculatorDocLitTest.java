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

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Source;

import org.jboss.fuse.wsdl2rest.EndpointInfo;
import org.jboss.fuse.wsdl2rest.impl.Wsdl2Rest;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xmlunit.builder.Input;
import org.xmlunit.xpath.JAXPXPathEngine;


public class CalculatorDocLitTest {

    @Test
    public void testJavaClient() throws Exception {
        
        File wsdlFile = new File("src/test/resources/calculator/calculator.wsdl");
        assertThat(wsdlFile).exists();
        
        Path outpath = Paths.get("target/wsdl2rest/calculator");
        Wsdl2Rest wsdl2Rest = new Wsdl2Rest(wsdlFile.toURI().toURL(), outpath);

        List<EndpointInfo> clazzDefs = wsdl2Rest.process();
        Assert.assertEquals(1, clazzDefs.size());
        EndpointInfo clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.Example", clazzDef.getPackageName());
        Assert.assertEquals("ICalculator", clazzDef.getClassName());
        
        File cctxFile = outpath.resolve(Paths.get("camel", "wsdl2rest-camel-context.xml")).toFile();
        assertThat(cctxFile).exists();
        Source camelSourceFile = Input.fromFile(cctxFile).build();
        JAXPXPathEngine jaxpxPathEngine = new JAXPXPathEngine();
        jaxpxPathEngine.setNamespaceContext(Collections.singletonMap("camel", "http://camel.apache.org/schema/spring"));
		Iterable<Node> restPostNodes = jaxpxPathEngine.selectNodes("//camel:camelContext/camel:rest/camel:post", camelSourceFile);
        assertThat(restPostNodes).hasSize(2);
    }
}
