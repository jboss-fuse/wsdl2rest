package org.jboss.fuse.wsdl2rest.jaxws.doclit;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ListProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        ArrayList list = new ArrayList();
        Integer[] array = (Integer[])exchange.getIn().getBody();
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }
        ArrayList wrapperList = new ArrayList();
        wrapperList.add(list); 
        exchange.getOut().setBody(wrapperList);
         
    }

}
