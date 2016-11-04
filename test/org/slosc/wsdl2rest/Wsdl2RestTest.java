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


import junit.framework.*;
import org.slosc.wsdl2rest.Wsdl2Rest;
import org.slosc.wsdl2rest.util.WSDLFileFilter;

import java.io.File;

public class Wsdl2RestTest extends TestCase {
    Wsdl2Rest wsdl2Rest;

    public void testMain() throws Exception {
        String loc = System.getProperty("org.slosc.wsdl2rest.wsdl.wsdlLocations");
        String targetLoc = System.getProperty("org.slosc.wsdl2rest.wsdl.targetLocation");
        if(loc == null || targetLoc == null ) {
            System.out.println("No location defined for wsdls/output");
            return;
        }
        
        File wsdlLoc = new File(loc);
        if(!wsdlLoc.isDirectory()) return;

        File [] files = wsdlLoc.listFiles(new WSDLFileFilter());
        Wsdl2Rest wsdl2rest = new Wsdl2Rest();
        for(File f:files){
            wsdl2rest.process(f.getAbsolutePath(), "testUName", "testPassword");
            wsdl2rest.generateClasses(targetLoc);
        }
    }
}