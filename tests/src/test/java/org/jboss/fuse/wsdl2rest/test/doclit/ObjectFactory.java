
package org.jboss.fuse.wsdl2rest.test.doclit;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.jboss.fuse.wsdl2rest.test.doclit package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AddAddress_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "addAddress");
    private final static QName _AddAddressResponse_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "addAddressResponse");
    private final static QName _DelAddress_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "delAddress");
    private final static QName _DelAddressResponse_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "delAddressResponse");
    private final static QName _GetAddress_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "getAddress");
    private final static QName _GetAddressResponse_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "getAddressResponse");
    private final static QName _ListAddresses_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "listAddresses");
    private final static QName _ListAddressesResponse_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "listAddressesResponse");
    private final static QName _UpdAddress_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "updAddress");
    private final static QName _UpdAddressResponse_QNAME = new QName("http://doclit.test.wsdl2rest.fuse.jboss.org/", "updAddressResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.jboss.fuse.wsdl2rest.test.doclit
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddAddress }
     * 
     */
    public AddAddress createAddAddress() {
        return new AddAddress();
    }

    /**
     * Create an instance of {@link AddAddressResponse }
     * 
     */
    public AddAddressResponse createAddAddressResponse() {
        return new AddAddressResponse();
    }

    /**
     * Create an instance of {@link DelAddress }
     * 
     */
    public DelAddress createDelAddress() {
        return new DelAddress();
    }

    /**
     * Create an instance of {@link DelAddressResponse }
     * 
     */
    public DelAddressResponse createDelAddressResponse() {
        return new DelAddressResponse();
    }

    /**
     * Create an instance of {@link GetAddress }
     * 
     */
    public GetAddress createGetAddress() {
        return new GetAddress();
    }

    /**
     * Create an instance of {@link GetAddressResponse }
     * 
     */
    public GetAddressResponse createGetAddressResponse() {
        return new GetAddressResponse();
    }

    /**
     * Create an instance of {@link ListAddresses }
     * 
     */
    public ListAddresses createListAddresses() {
        return new ListAddresses();
    }

    /**
     * Create an instance of {@link ListAddressesResponse }
     * 
     */
    public ListAddressesResponse createListAddressesResponse() {
        return new ListAddressesResponse();
    }

    /**
     * Create an instance of {@link UpdAddress }
     * 
     */
    public UpdAddress createUpdAddress() {
        return new UpdAddress();
    }

    /**
     * Create an instance of {@link UpdAddressResponse }
     * 
     */
    public UpdAddressResponse createUpdAddressResponse() {
        return new UpdAddressResponse();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "addAddress")
    public JAXBElement<AddAddress> createAddAddress(AddAddress value) {
        return new JAXBElement<AddAddress>(_AddAddress_QNAME, AddAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "addAddressResponse")
    public JAXBElement<AddAddressResponse> createAddAddressResponse(AddAddressResponse value) {
        return new JAXBElement<AddAddressResponse>(_AddAddressResponse_QNAME, AddAddressResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DelAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "delAddress")
    public JAXBElement<DelAddress> createDelAddress(DelAddress value) {
        return new JAXBElement<DelAddress>(_DelAddress_QNAME, DelAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DelAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "delAddressResponse")
    public JAXBElement<DelAddressResponse> createDelAddressResponse(DelAddressResponse value) {
        return new JAXBElement<DelAddressResponse>(_DelAddressResponse_QNAME, DelAddressResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "getAddress")
    public JAXBElement<GetAddress> createGetAddress(GetAddress value) {
        return new JAXBElement<GetAddress>(_GetAddress_QNAME, GetAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "getAddressResponse")
    public JAXBElement<GetAddressResponse> createGetAddressResponse(GetAddressResponse value) {
        return new JAXBElement<GetAddressResponse>(_GetAddressResponse_QNAME, GetAddressResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAddresses }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "listAddresses")
    public JAXBElement<ListAddresses> createListAddresses(ListAddresses value) {
        return new JAXBElement<ListAddresses>(_ListAddresses_QNAME, ListAddresses.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListAddressesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "listAddressesResponse")
    public JAXBElement<ListAddressesResponse> createListAddressesResponse(ListAddressesResponse value) {
        return new JAXBElement<ListAddressesResponse>(_ListAddressesResponse_QNAME, ListAddressesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdAddress }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "updAddress")
    public JAXBElement<UpdAddress> createUpdAddress(UpdAddress value) {
        return new JAXBElement<UpdAddress>(_UpdAddress_QNAME, UpdAddress.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdAddressResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://doclit.test.wsdl2rest.fuse.jboss.org/", name = "updAddressResponse")
    public JAXBElement<UpdAddressResponse> createUpdAddressResponse(UpdAddressResponse value) {
        return new JAXBElement<UpdAddressResponse>(_UpdAddressResponse_QNAME, UpdAddressResponse.class, null, value);
    }

}
