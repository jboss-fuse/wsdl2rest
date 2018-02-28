package org.jboss.fuse.wsdl2rest.jaxws.doclit;
/*
 * Copyright (c) 2008 SL_OpenSource Consortium
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

@WebService(endpointInterface="org.jboss.fuse.wsdl2rest.jaxws.doclit.Address")
public class AddressBean {

    private Map<Integer, Item> map = new LinkedHashMap<>();
    
    public List<Integer> listAddresses() {
        List<Integer> result;
        synchronized (map) {
            result = new ArrayList<>(map.keySet());
        }
        return result;
    }

    public Item getAddress(Integer id) {
        Item result = null;
        synchronized (map) {
            Item aux = map.get(id);
            if (aux != null) {
                result = aux;
            }
        }
        return result;
    }

    public Integer addAddress(Item item) {
        synchronized (map) {
            Integer id = item.getId();
            map.put(id, item);
            return id;
        }
    }

    public Integer updAddress(Item item) {
        Integer result;
        synchronized (map) {
            Integer id = item.getId();
            result = map.containsKey(id) ? id : null;
            if (result != null) {
                map.put(id, item);
            }
        }
        return result;
    }

    public Item delAddress(Integer id) {
        Item result;
        synchronized (map) {
            result = map.remove(id);
        }
        return result;
   }
}