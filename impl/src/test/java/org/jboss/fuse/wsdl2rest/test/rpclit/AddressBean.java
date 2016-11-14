package org.jboss.fuse.wsdl2rest.test.rpclit;
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
import java.util.Map;
import java.util.Set;

import org.jboss.fuse.wsdl2rest.test.Item;
import org.jboss.fuse.wsdl2rest.test.ItemBuilder;

public class AddressBean implements Address {

    private Map<Integer, Item> map = new LinkedHashMap<>();
    
    @Override
    public Integer[] listAddresses() {
        synchronized (map) {
            Set<Integer> keySet = map.keySet();
            return new ArrayList<>(keySet).toArray(new Integer[keySet.size()]);
        }
    }

    @Override
    public Item getAddress(Integer id) {
        Item result = null;
        synchronized (map) {
            Item aux = map.get(id);
            if (aux != null) {
                result = new ItemBuilder().copy(aux).build();
            }
        }
        return result;
    }

    @Override
    public Integer addAddress(Item item) {
        synchronized (map) {
            int id = map.size() + 1;
            map.put(id, new ItemBuilder().copy(item).id(id).build());
            return id;
        }
    }

    @Override
    public Integer updAddress(Item item) {
        Integer result;
        synchronized (map) {
            Integer id = item.getId();
            result = map.containsKey(id) ? id : null;
            if (result != null) {
                map.put(id, new ItemBuilder().copy(item).build());
            }
        }
        return result;
    }

    @Override
    public Item delAddress(Integer id) {
        Item result;
        synchronized (map) {
            result = map.remove(id);
        }
        return result;
   }
}