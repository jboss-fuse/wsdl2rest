package org.jboss.fuse.wsdl2rest.jaxws;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class ItemBuilder {

    private org.jboss.fuse.wsdl2rest.jaxws.doclit.Item result = new org.jboss.fuse.wsdl2rest.jaxws.doclit.Item();
    
    private static DatatypeFactory df = null;
    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException(
                "Exception while obtaining DatatypeFactory instance", dce);
        }
    }  
    
    public ItemBuilder copy(org.jboss.fuse.wsdl2rest.jaxws.doclit.Item obj) {
        result.setId(obj.getId());
        result.setName(obj.getName());
        result.setDateOfBirth(obj.getDateOfBirth());
        return this;
    }
    
    public ItemBuilder id(Integer id) {
        result.setId(id);
        return this;
    }
    
    public ItemBuilder name(String name) {
        result.setName(name);
        return this;
    }
    
    public ItemBuilder dateOfBirth(XMLGregorianCalendar dob) {
        result.setDateOfBirth(dob);
        return this;
    }
    
    public org.jboss.fuse.wsdl2rest.jaxws.doclit.Item build() {
        return result;
    }
    
    public static XMLGregorianCalendar asXMLGregorianCalendar(java.util.Date date) {
        if (date == null) {
            return null;
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return df.newXMLGregorianCalendar(gc);
        }
    }
}