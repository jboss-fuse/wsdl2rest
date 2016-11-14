package org.jboss.fuse.wsdl2rest.test.rpclit;
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
import java.nio.file.Path;
import java.util.List;

import org.jboss.fuse.wsdl2rest.EndpointInfo;
import org.jboss.fuse.wsdl2rest.MethodInfo;
import org.jboss.fuse.wsdl2rest.impl.Main;
import org.jboss.fuse.wsdl2rest.impl.Wsdl2Rest;
import org.junit.Assert;
import org.junit.Test;


public class GenerateRpcLitTest {

    @Test
    public void testGenerate() throws Exception {

        File wsdlFile = new File("src/test/resources/rpclit/Address.wsdl");
        Path outpath = new File("../tests/src/test/java").toPath();
        
        Wsdl2Rest tool = new Wsdl2Rest(wsdlFile.toURI().toURL(), outpath);
        
        List<EndpointInfo> clazzDefs = tool.process();
        Assert.assertEquals(1, clazzDefs.size());
        EndpointInfo clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.jboss.fuse.wsdl2rest.test.rpclit", clazzDef.getPackageName());
        Assert.assertEquals("Address", clazzDef.getClassName());

        List<MethodInfo> methods = clazzDef.getMethods();
        Assert.assertEquals(5, methods.size());
    }


    @Test
    public void testMain() throws Exception {

        
        String[] args = new String[] {"--wsdl=file:src/test/resources/rpclit/Address.wsdl", "--out=../tests/src/test/java"};
        List<EndpointInfo> clazzDefs = new Main().mainInternal(args);

        Assert.assertEquals(1, clazzDefs.size());
        EndpointInfo clazzDef = clazzDefs.get(0);
        Assert.assertEquals("org.jboss.fuse.wsdl2rest.test.rpclit", clazzDef.getPackageName());
        Assert.assertEquals("Address", clazzDef.getClassName());

        List<MethodInfo> methods = clazzDef.getMethods();
        Assert.assertEquals(5, methods.size());
    }
}