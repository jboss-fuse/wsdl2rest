package org.slosc.wsdl2rest.test.rpclit;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("/addressservice/")
public interface AddressService {

	@GET
	@Path("addresses")
	@Produces(MediaType.APPLICATION_JSON)
	public int[] listAddresses();

	@PUT
	@Path("address/{arg0}")
	@Produces(MediaType.APPLICATION_JSON)
	public String updAddress(@PathParam("arg0") int arg0, String arg1);

	@POST
	@Path("address")
	@Consumes(MediaType.APPLICATION_JSON)
	public int addAddress(String arg0);

	@DELETE
	@Path("address/{arg0}")
	@Produces(MediaType.APPLICATION_JSON)
	public String delAddress(@PathParam("arg0") int arg0);

	@GET
	@Path("address/{arg0}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAddress(@PathParam("arg0") int arg0);

}

