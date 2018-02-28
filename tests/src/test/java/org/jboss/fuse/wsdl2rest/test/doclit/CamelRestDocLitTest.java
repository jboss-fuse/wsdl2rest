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

package org.jboss.fuse.wsdl2rest.test.doclit;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.ServiceStatus;
import org.jboss.fuse.wsdl2rest.jaxws.doclit.Address;
import org.jboss.fuse.wsdl2rest.jaxws.doclit.Item;
import org.jboss.fuse.wsdl2rest.util.SpringCamelContextFactory;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class CamelRestDocLitTest {

    static DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    
    static String JAXWS_URI = "http://localhost:8080/doclit/AddressService";
    static QName SERVICE_QNAME = new QName("http://doclit.jaxws.wsdl2rest.fuse.jboss.org/", "AddressService");

    static String JAXRS_URL = "http://0.0.0.0:8081/jaxrs";
    
    @BeforeClass
    public static void beforeClass() {
        Endpoint endpoint = Endpoint.create(new AddressBean());
        endpoint.getProperties().put(Endpoint.WSDL_SERVICE, SERVICE_QNAME);
        endpoint.publish(JAXWS_URI);
        Assert.assertTrue(endpoint.isPublished());
    }
    
    @Test
    public void testJavaClient() throws Exception {

        Service service = Service.create(new URL(JAXWS_URI + "?wsdl"), SERVICE_QNAME);
        Address port = service.getPort(Address.class);
        Assert.assertNotNull("Address not null", port);

        Date dob = DATE_FORMAT.parse("11.11.1968");
        Item kermit = new ItemBuilder().id(100).name("Kermit").dateOfBirth(dob).build();
        Item frog = new ItemBuilder().id(100).name("Frog").dateOfBirth(dob).build();
        
        Assert.assertEquals(0, port.listAddresses().size());
        Assert.assertEquals(100, (int) port.addAddress(kermit));
        Assert.assertEquals("Kermit", port.getAddress(100).getName());
        Assert.assertEquals(1, port.listAddresses().size());
        Assert.assertEquals(100, port.listAddresses().get(0).intValue());
        Assert.assertEquals(100, (int) port.updAddress(frog));
        Assert.assertEquals("Frog", port.delAddress(100).getName());
    }

    @Test
    public void testCamelClient() throws Exception {

        URL resourceUrl = getClass().getResource("/doclit-camel-context.xml");
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            Date dob = DATE_FORMAT.parse("11.11.1968");
            Item kermit = new ItemBuilder().id(100).name("Kermit").dateOfBirth(dob).build();
            Item frog = new ItemBuilder().id(100).name("Frog").dateOfBirth(dob).build();

            ProducerTemplate producer = camelctx.createProducerTemplate();
            
            Assert.assertEquals(0, producer.requestBody("direct:listAddresses", null, List.class).size());
            
            Assert.assertEquals(100, (int) producer.requestBody("direct:addAddress", kermit, Integer.class));
            Assert.assertEquals(1, producer.requestBody("direct:listAddresses", null, List.class).size());
            Assert.assertEquals(100, producer.requestBody("direct:listAddresses", null, List.class).get(0));
            Assert.assertEquals("Kermit", producer.requestBodyAndHeader("direct:getAddress", 100, "arg0", "100", Item.class).getName());
            Assert.assertEquals(100, (int) producer.requestBody("direct:updAddress", frog, Integer.class));
            Assert.assertEquals("Frog", producer.requestBodyAndHeader("direct:delAddress", 100, "arg0", "100", Item.class).getName());
        } finally {
            camelctx.stop();
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRestClient() throws Exception {

        URL resourceUrl = getClass().getResource("/doclit-camel-context.xml");
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
                        
            Date dob = DATE_FORMAT.parse("11.11.1968");
            Item kermit = new ItemBuilder().id(100).name("Kermit").dateOfBirth(dob).build();
            Item frog = new ItemBuilder().id(100).name("Frog").dateOfBirth(dob).build();
            
            // GET @Address#listAddresses()
            List<Integer> res1 = client.target(JAXRS_URL + "/addresses").request().get(List.class);
            Assert.assertEquals(0, res1.size());

            // POST @Address#addAddress(Item)
            String payload = new ObjectMapper().writeValueAsString(kermit);
            Integer res2 = client.target(JAXRS_URL + "/address").request().post(Entity.entity(payload, MediaType.APPLICATION_JSON), Integer.class);
            Assert.assertEquals(new Integer(100), res2);
            
            
            // GET @Address#listAddresses()
            List<Integer> res3 = client.target(JAXRS_URL + "/addresses").request().get(List.class);
            Assert.assertEquals(new Integer(100), res3.get(0));

            
            // GET @Address#getAddress(int)
            Item res4 = client.target(JAXRS_URL + "/address/100").request().get(Item.class);
            Assert.assertEquals("Kermit", res4.getName());

            // PUT @Address#updAddress(Item)
            payload = new ObjectMapper().writeValueAsString(frog);
            Integer res5 = client.target(JAXRS_URL + "/address").request().put(Entity.entity(payload, MediaType.APPLICATION_JSON), Integer.class);
            Assert.assertEquals(new Integer(100), res5);

            // GET @Address#getAddress(int)
            Item res6 = client.target(JAXRS_URL + "/address/100").request().get(Item.class);
            Assert.assertEquals("Frog", res6.getName());

            // DEL @Address#delAddress(int)
            client.target(JAXRS_URL + "/address/100").request().delete();
            
            // GET @Address#listAddresses()
            List<Integer> res7 = client.target(JAXRS_URL + "/addresses").request().get(List.class);
            Assert.assertEquals(0, res7.size());
            

        } finally {
            camelctx.stop();
        }
    }
}
