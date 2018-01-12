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


import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ServiceStatus;
import org.jboss.fuse.wsdl2rest.jaxws.Item;
import org.jboss.fuse.wsdl2rest.jaxws.ItemBuilder;
import org.jboss.fuse.wsdl2rest.jaxws.rpclit.Address;
import org.jboss.fuse.wsdl2rest.util.SpringCamelContextFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class CamelCxfWsRpcLitTest {

    static String CONTEXT_URL = "http://0.0.0.0:8081/jaxrs";
    
    @Test
    public void testJavaClient() throws Exception {

        URL resourceUrl = getClass().getResource("/rpclit/rpclit-camel-context.xml");
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            QName qname = new QName("http://rpclit.jaxws.wsdl2rest.fuse.jboss.org/", "AddressService");
            Service service = Service.create(new URL("http://localhost:8080/rpclit/AddressService?wsdl"), qname);
            Address port = service.getPort(Address.class);
            Assert.assertNotNull("Address not null", port);

            Date dob = Item.DATE_FORMAT.parse("11.11.1968");
            Item kermit = new ItemBuilder().id(100).name("Kermit").dateOfBirth(dob).build();
            Item frog = new ItemBuilder().id(100).name("Frog").dateOfBirth(dob).build();
            
            Assert.assertNull(port.listAddresses());
            Assert.assertEquals(100, (int) port.addAddress(kermit));
            Assert.assertEquals("Kermit", port.getAddress(100).getName());
            Assert.assertEquals(100, (int) port.updAddress(frog));
            Assert.assertEquals("Frog", port.delAddress(100).getName());
        } finally {
            camelctx.stop();
        }
    }
    
    @Test
    public void testCamelClient() throws Exception {

        URL resourceUrl = getClass().getResource("/rpclit/rpclit-camel-context.xml");
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            Date dob = Item.DATE_FORMAT.parse("11.11.1968");
            Item kermit = new ItemBuilder().id(100).name("Kermit").dateOfBirth(dob).build();
            Item frog = new ItemBuilder().id(100).name("Frog").dateOfBirth(dob).build();

            ProducerTemplate producer = camelctx.createProducerTemplate();
            Assert.assertNull(producer.requestBody("direct:listAddresses", null, List.class));
            Assert.assertEquals(100, (int) producer.requestBody("direct:addAddress", kermit, Integer.class));
            Assert.assertEquals("Kermit", producer.requestBody("direct:getAddress", 100, Item.class).getName());
            Assert.assertEquals(100, (int) producer.requestBody("direct:updAddress", frog, Integer.class));
            Assert.assertEquals("Frog", producer.requestBody("direct:getAddress", 100, Item.class).getName());
        } finally {
            camelctx.stop();
        }
    }

    @Test
    @Ignore("[#37] JAXRS message cannot be routed to JAXWS proxy") 
    public void testRestClient() throws Exception {

        URL resourceUrl = getClass().getResource("/rpclit/rpclit-camel-context.xml");
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
            
            Date dob = Item.DATE_FORMAT.parse("11.11.1968");
            Item kermit = new ItemBuilder().id(100).name("Kermit").dateOfBirth(dob).build();
            Item frog = new ItemBuilder().id(100).name("Frog").dateOfBirth(dob).build();
            
            // GET @Address#listAddresses()
            String res1 = client.target(CONTEXT_URL + "/addresses").request().get(String.class);
            Assert.assertEquals("", res1);

            // POST @Address#addAddress(Item)
            String payload = new ObjectMapper().writeValueAsString(kermit);
            Integer res2 = client.target(CONTEXT_URL + "/address").request().post(Entity.entity(payload, MediaType.APPLICATION_JSON), Integer.class);
            Assert.assertEquals(new Integer(100), res2);

            // GET @Address#listAddresses()
            List<?> res3 = client.target(CONTEXT_URL + "/addresses").request().get(List.class);
            Assert.assertEquals("[100]", res3.toString());

            // GET @Address#getAddress(int)
            Item res4 = client.target(CONTEXT_URL + "/address/100").request().get(Item.class);
            Assert.assertEquals("Kermit", res4.getName());

            // PUT @Address#updAddress(Item)
            payload = new ObjectMapper().writeValueAsString(frog);
            Integer res5 = client.target(CONTEXT_URL + "/address").request().put(Entity.entity(payload, MediaType.APPLICATION_JSON), Integer.class);
            Assert.assertEquals(new Integer(100), res5);

            // GET @Address#getAddress(int)
            Item res6 = client.target(CONTEXT_URL + "/address/100").request().get(Item.class);
            Assert.assertEquals("Frog", res6.getName());

            // DEL @Address#delAddress(int)
            client.target(CONTEXT_URL + "/address/100").request().delete();
            
            // GET @Address#listAddresses()
            String res7 = client.target(CONTEXT_URL + "/addresses").request().get(String.class);
            Assert.assertEquals("[]", res7);

        } finally {
            camelctx.stop();
        }
    }
}