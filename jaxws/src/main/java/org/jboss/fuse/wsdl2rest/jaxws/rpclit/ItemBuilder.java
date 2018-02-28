package org.jboss.fuse.wsdl2rest.jaxws.rpclit;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
        result.setDateOfBirth(toXMLGregorianCalendar(date));
        return this;
    }

    public Item build() {
        return result;
    }

    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        XMLGregorianCalendar result = null;
        if (date != null) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTimeInMillis(date.getTime());
            result = df.newXMLGregorianCalendar(gc);
        }
        return result;
    }

    public static Date toDate(XMLGregorianCalendar cal) {
        Date result = null;
        if (cal != null) {
            result = cal.toGregorianCalendar().getTime();
        }
        return result;
    }
}