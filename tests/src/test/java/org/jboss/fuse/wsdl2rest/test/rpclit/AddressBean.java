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

public class AddressBean {

    private Map<Integer, Item> map = new LinkedHashMap<>();
    
    public Integer[] listAddresses() {
        synchronized (map) {
            Set<Integer> keySet = map.keySet();
            return new ArrayList<>(keySet).toArray(new Integer[keySet.size()]);
        }
    }

    public Item getAddress(Integer id) {
        synchronized (map) {
            return map.get(id);
        }
    }

    public Integer addAddress(Item item) {
        synchronized (map) {
            int id = map.size() + 1;
            map.put(id, new ItemBuilder().copy(item).id(id).build());
            return id;
        }
    }

    public Integer updAddress(Item item) {
        Integer result = null;
        synchronized (map) {
            Integer id = item.getId();
            if (map.get(id) != null) {
                map.put(id, new ItemBuilder().copy(item).build());
                result = id;
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