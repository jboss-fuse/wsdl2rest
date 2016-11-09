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

package org.slosc.wsdl2rest.test.rpclit;

import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.junit.Assert;
import org.junit.Test;
import org.slosc.wsdl2rest.util.SpringCamelContextFactory;

public class CamelRestRpcLitTest {

    static String CONTEXT_URL = "http://localhost:8080/rpclit/address";

    @Test
    public void testRestEndpoint() throws Exception {
        URL resourceUrl = getClass().getResource("/rpclit/rpclit-camel-context.xml");
        CamelContext camelctx = SpringCamelContextFactory.createSingleCamelContext(resourceUrl, null);
        camelctx.start();
        try {
            Assert.assertEquals(ServiceStatus.Started, camelctx.getStatus());

            Client client = ClientBuilder.newClient();
            
            // GET @Address#listAddresses()
            String result = client.target(CONTEXT_URL + "/addresses").request().get(String.class);
            Assert.assertEquals("[]", result);

            // POST @Address#addAddress(String)
            String payload = "{\"name\":\"Kermit\"}";
            result = client.target(CONTEXT_URL + "/address").request().post(Entity.entity(payload, MediaType.APPLICATION_JSON), String.class);
            Assert.assertEquals("1", result);

            // GET @Address#getAddress(int)
            result = client.target(CONTEXT_URL + "/address/1").request().get(String.class);
            Assert.assertEquals("Kermit", result);

            // PUT @Address#updAddress(int, String)
            payload = "{\"name\":\"Frog\"}";
            result = client.target(CONTEXT_URL + "/address/1").request().put(Entity.entity(payload, MediaType.APPLICATION_JSON), String.class);
            Assert.assertEquals("Kermit", result);

            // DEL @Address#delAddress(int)
            result = client.target(CONTEXT_URL + "/address/1").request().delete(String.class);
            Assert.assertEquals("Frog", result);
        } finally {
            camelctx.stop();
        }
    }

}
