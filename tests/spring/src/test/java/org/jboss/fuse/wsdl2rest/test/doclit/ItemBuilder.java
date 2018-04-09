package org.jboss.fuse.wsdl2rest.test.doclit;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jboss.fuse.wsdl2rest.jaxws.doclit.Item;

public class ItemBuilder {

    private Item result = new Item();

    private static DatatypeFactory df = null;
    static {
        try {
            df = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException dce) {
            throw new IllegalStateException("Exception while obtaining DatatypeFactory instance", dce);
        }
    }

    public ItemBuilder id(Integer id) {
        result.setId(id);
        return this;
    }

    public ItemBuilder name(String name) {
        result.setName(name);
        return this;
    }

    public ItemBuilder dateOfBirth(Date date) {
        result.setDateOfBirth(asXMLGregorianCalendar(date));
        return this;
    }

    public Item build() {
        return result;
    }

    private static XMLGregorianCalendar asXMLGregorianCalendar(Date date) {
        if (date == null) {
            return null;
        } else {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            return df.newXMLGregorianCalendar(gc);
        }
    }
}