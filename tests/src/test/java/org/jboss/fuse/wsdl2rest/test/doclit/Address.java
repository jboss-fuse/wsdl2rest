package org.jboss.fuse.wsdl2rest.test.doclit;

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
	@Path("address/{delAddress}")
	@Produces(MediaType.APPLICATION_JSON)
	public org.jboss.fuse.wsdl2rest.test.doclit.DelAddressResponse delAddress(@PathParam("delAddress") Integer delAddress);

	@POST
	@Path("address")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public org.jboss.fuse.wsdl2rest.test.doclit.AddAddressResponse addAddress(org.jboss.fuse.wsdl2rest.test.doclit.AddAddress addAddress);

	@GET
	@Path("addresses")
	@Produces(MediaType.APPLICATION_JSON)
	public org.jboss.fuse.wsdl2rest.test.doclit.ListAddressesResponse listAddresses();

	@PUT
	@Path("address")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public org.jboss.fuse.wsdl2rest.test.doclit.UpdAddressResponse updAddress(org.jboss.fuse.wsdl2rest.test.doclit.UpdAddress updAddress);

	@GET
	@Path("address/{getAddress}")
	@Produces(MediaType.APPLICATION_JSON)
	public org.jboss.fuse.wsdl2rest.test.doclit.GetAddressResponse getAddress(@PathParam("getAddress") Integer getAddress);

}

