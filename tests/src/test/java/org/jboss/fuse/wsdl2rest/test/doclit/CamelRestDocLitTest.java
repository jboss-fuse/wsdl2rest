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
import org.jboss.fuse.wsdl2rest.jaxws.doclit.AddAddress;
import org.jboss.fuse.wsdl2rest.jaxws.doclit.AddAddressResponse;
import org.jboss.fuse.wsdl2rest.jaxws.doclit.GetAddressResponse;
import org.jboss.fuse.wsdl2rest.jaxws.doclit.Item;
import org.jboss.fuse.wsdl2rest.jaxws.doclit.ListAddressesResponse;
import org.jboss.fuse.wsdl2rest.jaxws.doclit.UpdAddress;
import org.jboss.fuse.wsdl2rest.jaxws.doclit.UpdAddressResponse;
import org.jboss.fuse.wsdl2rest.util.SpringCamelContextFactory;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class CamelRestDocLitTest {

    static String CONTEXT_URL = "http://localhost:8080";

    @Test
    public void testRestEndpoint() throws Exception {
        URL resourceUrl = new File("target/generated-wsdl2rest/doclit-camel-context.xml").toURI().toURL();
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
            
            XMLGregorianCalendar dob = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(1968, 11, 11, 0);
            Item kermit = new ItemBuilder().id(100).name("Kermit").dateOfBirth(dob).build();
            Item frog = new ItemBuilder().id(100).name("Frog").dateOfBirth(dob).build();
            
            // GET @Address#listAddresses()
            ListAddressesResponse res1 = client.target(CONTEXT_URL + "/addresses").request().get(ListAddressesResponse.class);
            Assert.assertEquals("[]", res1.getReturn());

            // POST @Address#addAddress(String)
            AddAddress req2 = new AddAddress();
            req2.setArg0(kermit);
            String payload = new ObjectMapper().writeValueAsString(req2);
            AddAddressResponse res2 = client.target(CONTEXT_URL + "/address").request().post(Entity.entity(payload, MediaType.APPLICATION_JSON), AddAddressResponse.class);
            Assert.assertEquals(new Integer(100), res2.getReturn());
            
            // GET @Address#listAddresses()
            ListAddressesResponse res3 = client.target(CONTEXT_URL + "/addresses").request().get(ListAddressesResponse.class);
            Assert.assertEquals("[100]", res3.getReturn());

            // GET @Address#getAddress(int)
            GetAddressResponse res4 = client.target(CONTEXT_URL + "/address/100").request().get(GetAddressResponse.class);
            Assert.assertEquals("Kermit", res4.getReturn().getName());

            // PUT @Address#updAddress(int, String)
            UpdAddress req5 = new UpdAddress();
            req5.setArg0(frog);
            payload = new ObjectMapper().writeValueAsString(req5);
            UpdAddressResponse res5 = client.target(CONTEXT_URL + "/address").request().put(Entity.entity(payload, MediaType.APPLICATION_JSON), UpdAddressResponse.class);
            Assert.assertEquals(new Integer(100), res5.getReturn());

            // GET @Address#getAddress(int)
            GetAddressResponse res6 = client.target(CONTEXT_URL + "/address/100").request().get(GetAddressResponse.class);
            Assert.assertEquals("Frog", res6.getReturn().getName());

            // DEL @Address#delAddress(int)
            client.target(CONTEXT_URL + "/address/100").request().delete();

            // GET @Address#listAddresses()
            ListAddressesResponse res7 = client.target(CONTEXT_URL + "/addresses").request().get(ListAddressesResponse.class);
            Assert.assertEquals("[]", res7.getReturn());

        } finally {
            camelctx.stop();
        }
    }

}
