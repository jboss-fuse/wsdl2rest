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
package org.jboss.fuse.wsdl2rest.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import javax.xml.transform.Source;

import org.jboss.fuse.wsdl2rest.EndpointInfo;
import org.jboss.fuse.wsdl2rest.MethodInfo;
import org.jboss.fuse.wsdl2rest.impl.Wsdl2Rest;
import org.junit.Test;
import org.w3c.dom.Node;
import org.xmlunit.builder.Input;
import org.xmlunit.xpath.JAXPXPathEngine;

public class WithUnderscoreTest {
	
	 @Test
	    public void testJavaClient() throws Exception {
	        File wsdlFile = new File("src/test/resources/A_NAME_WITH_UNDERSCORE_SRV.1.wsdl");
	        assertThat(wsdlFile).exists();
	        
	        Path outpath = Paths.get("target/wsdl2rest/A_NAME_WITH_UNDERSCORE_SRV.1");
	        Wsdl2Rest wsdl2Rest = new Wsdl2Rest(wsdlFile.toURI().toURL(), outpath);

	        List<EndpointInfo> clazzDefs = wsdl2Rest.process();
	        assertThat(clazzDefs).hasSize(1);
	        EndpointInfo clazzDef = clazzDefs.get(0);
	        assertThat(clazzDef.getPackageName()).isEqualTo("1.com/Enterprise/HCM/services/A_NAME_WITH_UNDERSCORE_SRV.oracle.xmlns");
	        assertThat(clazzDef.getClassName()).isEqualTo("A_NAME_WITH_UNDERSCORE_SRV_PortType");
	        MethodInfo method = clazzDef.getMethod("A_SECOND_NAME_WITH_UNDERSCORE_EMAIL_OPR_SRV");
	        assertThat(method.getPath()).isEqualTo("a_second_name_with_underscore_email_opr_srv/{arg0}");
	        
	        File cctxFile = outpath.resolve(Paths.get("camel", "wsdl2rest-camel-context.xml")).toFile();
	        assertThat(cctxFile).exists();
	        
	        Source camelSourceFile = Input.fromFile(cctxFile).build();
	        JAXPXPathEngine jaxpxPathEngine = new JAXPXPathEngine();
	        jaxpxPathEngine.setNamespaceContext(Collections.singletonMap("camel", "http://camel.apache.org/schema/spring"));
			Iterable<Node> restPostNodes = jaxpxPathEngine.selectNodes("//camel:camelContext/camel:rest/camel:get", camelSourceFile);
	        assertThat(restPostNodes).hasSize(1);
	    }
}
