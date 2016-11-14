package org.slosc.wsdl2rest.test.rpclit;

import javax.xml.datatype.XMLGregorianCalendar;

public class ItemBuilder {

    private Item result = new Item();
    
    public ItemBuilder copy(Item obj) {
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
    
    public Item build() {
        return result;
    }
}