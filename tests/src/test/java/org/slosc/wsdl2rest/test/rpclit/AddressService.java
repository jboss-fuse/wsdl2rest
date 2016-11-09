package org.slosc.wsdl2rest.test.rpclit;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/addressservice/")
public interface AddressService {

	@POST
	@Path("address/{arg0}")
	public void addAddress(@PathParam("arg0") String arg0);

	@GET
	@Path("address/{arg0}")
	public String getAddress(@PathParam("arg0") String arg0);

}

