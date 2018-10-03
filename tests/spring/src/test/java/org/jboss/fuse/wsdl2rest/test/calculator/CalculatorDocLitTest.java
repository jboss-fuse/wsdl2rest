/*
 * #%L
 * Wildfly Camel :: Testsuite
 * %%
 * Copyright (C) 2013 - 2014 RedHat
 * %%
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
 * #L%
 */

package org.jboss.fuse.wsdl2rest.test.calculator;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.jboss.fuse.wsdl2rest.EndpointInfo;
import org.jboss.fuse.wsdl2rest.impl.Wsdl2Rest;
import org.junit.Assert;
import org.junit.Test;


public class CalculatorDocLitTest {

    @Test
    public void testJavaClient() throws Exception {
        
        File wsdlFile = new File("src/test/resources/calculator/Calculator.wsdl");
        Assert.assertTrue(wsdlFile.exists());
        
        Path outpath = Paths.get("target/wsdl2rest/calculator");
        Wsdl2Rest wsdl2Rest = new Wsdl2Rest(wsdlFile.toURI().toURL(), outpath);

        List<EndpointInfo> clazzDefs = wsdl2Rest.process();
        Assert.assertEquals(1, clazzDefs.size());
        EndpointInfo clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.Example", clazzDef.getPackageName());
        Assert.assertEquals("ICalculator", clazzDef.getClassName());
        
        File cctxFile = outpath.resolve(Paths.get("camel", "wsdl2rest-camel-context.xml")).toFile();
        Assert.assertTrue(cctxFile.exists());
    }
}
