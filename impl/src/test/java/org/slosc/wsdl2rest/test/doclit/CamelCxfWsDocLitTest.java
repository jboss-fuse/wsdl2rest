package org.slosc.wsdl2rest.test.doclit;
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

import static org.slosc.wsdl2rest.test.doclit.Item.DATE_FORMAT;

import java.net.URL;
import java.util.Date;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.junit.Assert;
import org.junit.Test;
import org.slosc.wsdl2rest.util.SpringCamelContextFactory;

public class CamelCxfWsDocLitTest {

    @Test
    public void testEndpoint() throws Exception {

        URL resourceUrl = getClass().getResource("/doclit/doclit-camel-context.xml");
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            QName qname = new QName("http://doclit.test.wsdl2rest.slosc.org/", "AddressService");
            Service service = Service.create(new URL("http://localhost:8080/doclit/AddressService?wsdl"), qname);
            Address port = service.getPort(Address.class);
            Assert.assertNotNull("Address not null", port);

            Date dob = DATE_FORMAT.parse("11.11.1968");
            Item kermit = new ItemBuilder().name("Kermit").dateOfBirth(dob).build();
            Item frog = new ItemBuilder().name("Frog").dateOfBirth(dob).build();
            
            Assert.assertEquals("[]", port.listAddresses());
            Assert.assertEquals(1, (int) port.addAddress(kermit));
            Assert.assertEquals("Kermit", port.getAddress(1).getName());
            Assert.assertEquals(1, (int) port.updAddress(1, frog));
            Assert.assertEquals("Frog", port.delAddress(1).getName());
        } finally {
            camelctx.stop();
        }
    }
}