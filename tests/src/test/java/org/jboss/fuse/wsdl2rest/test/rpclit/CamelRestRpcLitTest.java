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

package org.jboss.fuse.wsdl2rest.test.rpclit;

import java.io.File;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.jboss.fuse.wsdl2rest.test.doclit.Item;
import org.jboss.fuse.wsdl2rest.test.doclit.ItemBuilder;
import org.jboss.fuse.wsdl2rest.util.SpringCamelContextFactory;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class CamelRestRpcLitTest {

    static String CONTEXT_URL = "http://localhost:8080/rpclit/address";

    @Test
    public void testRestEndpoint() throws Exception {
        URL resourceUrl = new File("target/generated-wsdl2rest/rpclit-camel-context.xml").toURI().toURL();
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
            
            XMLGregorianCalendar dob = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(1968, 11, 11, 0);
            Item kermit = new ItemBuilder().id(100).name("Kermit").dateOfBirth(dob).build();
            Item frog = new ItemBuilder().id(100).name("Frog").dateOfBirth(dob).build();
            
            // GET @Address#listAddresses()
            String res1 = client.target(CONTEXT_URL + "/addresses").request().get(String.class);
            Assert.assertEquals("[]", res1);

            // POST @Address#addAddress(Item)
            String payload = new ObjectMapper().writeValueAsString(kermit);
            Integer res2 = client.target(CONTEXT_URL + "/address").request().post(Entity.entity(payload, MediaType.APPLICATION_JSON), Integer.class);
            Assert.assertEquals(new Integer(100), res2);

            // GET @Address#getAddress(int)
            Item res3 = client.target(CONTEXT_URL + "/address/" + res2).request().get(Item.class);
            Assert.assertEquals("Kermit", res3.getName());

            // PUT @Address#updAddress(Item)
            payload = new ObjectMapper().writeValueAsString(frog);
            Integer res4 = client.target(CONTEXT_URL + "/address").request().put(Entity.entity(payload, MediaType.APPLICATION_JSON), Integer.class);
            Assert.assertEquals(new Integer(100), res4);

            // DEL @Address#delAddress(int)
            Item res5 = client.target(CONTEXT_URL + "/address/100").request().delete(Item.class);
            Assert.assertEquals("Frog", res5.getName());
        } finally {
            camelctx.stop();
        }
    }

}
