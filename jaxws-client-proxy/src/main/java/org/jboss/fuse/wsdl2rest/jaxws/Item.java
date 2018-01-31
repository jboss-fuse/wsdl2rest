package org.jboss.fuse.wsdl2rest.jaxws;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Item extends org.jboss.fuse.wsdl2rest.jaxws.doclit.Item {
    
    public static DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    
    private Integer id;
    private String name;
    private Date dateOfBirth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Item)) return false;
        return toString().equals(obj.toString());
    }

    public String toString() {
        return "Item[" + id + "," + name + "," + dateOfBirth + "]";
    }
}