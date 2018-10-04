package org.jboss.fuse.wsdl2rest.jaxws.rpclit;
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

import javax.jws.WebService;

import net.java.dev.jaxb.array.IntArray;

@WebService(endpointInterface = "org.jboss.fuse.wsdl2rest.jaxws.rpclit.Address")
public class AddressBean implements Address {

    private Map<Integer, Item> map = new LinkedHashMap<>();

    public IntArray listAddresses() {
        synchronized (map) {
            Set<Integer> keySet = map.keySet();
            return new IntArray() {
                {
                    item = new ArrayList<>(keySet);
                }
            };
        }
    }

    public Item getAddress(int id) {
        Item result = null;
        synchronized (map) {
            Item aux = map.get(id);
            if (aux != null) {
                result = aux;
            }
        }
        return result;
    }

    public int addAddress(Item item) {
        synchronized (map) {
            Integer id = item.getId();
            map.put(id, item);
            return id;
        }
    }

    public int updAddress(Item item) {
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

    public Item delAddress(int id) {
        Item result;
        synchronized (map) {
            result = map.remove(id);
        }
        return result;
    }
}