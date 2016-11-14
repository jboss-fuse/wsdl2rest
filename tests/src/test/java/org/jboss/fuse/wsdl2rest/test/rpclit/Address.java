package org.jboss.fuse.wsdl2rest.test.rpclit;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

@Path("/address/")
public interface Address {

	@DELETE
	@Path("address/{arg0}")
	@Produces(MediaType.APPLICATION_JSON)
	public org.jboss.fuse.wsdl2rest.test.rpclit.Item delAddress(@PathParam("arg0") int arg0);

	@POST
	@Path("address")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int addAddress(org.jboss.fuse.wsdl2rest.test.rpclit.Item arg0);

	@GET
	@Path("addresses")
	@Produces(MediaType.APPLICATION_JSON)
	public int[] listAddresses();

	@PUT
	@Path("address")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int updAddress(org.jboss.fuse.wsdl2rest.test.rpclit.Item arg0);

	@GET
	@Path("address/{arg0}")
	@Produces(MediaType.APPLICATION_JSON)
	public org.jboss.fuse.wsdl2rest.test.rpclit.Item getAddress(@PathParam("arg0") int arg0);

}

