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

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.junit.Assert;
import org.junit.Test;
import org.slosc.wsdl2rest.util.SpringCamelContextFactory;

public class CamelWebServiceRPCLiteralTest {

    @Test
    public void testGenerate() throws Exception {

        URL resourceUrl = getClass().getResource("/rpclit/rpclit-camel-context.xml");
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            QName qname = new QName("http://rpclit.test.wsdl2rest.slosc.org/", "AddressService");
            Service service = Service.create(new URL("http://localhost:8080/AddressService/AddressPort?wsdl"), qname);
            Address port = service.getPort(Address.class);
            Assert.assertNotNull("Address not null", port);

            Assert.assertNull(port.listAddresses());
            Assert.assertEquals(1, (int) port.addAddress("Kermit"));
            Assert.assertEquals("Kermit", port.getAddress(1));
            Assert.assertEquals("Kermit", port.updAddress(1, "Frog"));
            Assert.assertEquals("Frog", port.delAddress(1));
        } finally {
            camelctx.stop();
        }
    }
}