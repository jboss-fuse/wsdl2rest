package org.jboss.fuse.wsdl2rest.test.doclit;
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


import java.util.LinkedHashMap;
import java.util.Map;

public class AddressBean implements Address {

    private Map<Integer, Item> map = new LinkedHashMap<>();
    
    @Override
    public ListAddressesResponse listAddresses() {
        ListAddressesResponse res = new ListAddressesResponse();
        synchronized (map) {
            res.setReturn(map.keySet().toString());
        }
        return res;
    }

    @Override
    public GetAddressResponse getAddress(Integer id) {
        GetAddressResponse res = new GetAddressResponse();
        synchronized (map) {
            res.setReturn(map.get(id));
        }
        return res;
    }

    @Override
    public AddAddressResponse addAddress(AddAddress req) {
        AddAddressResponse res = new AddAddressResponse();
        synchronized (map) {
            int id = map.size() + 1;
            res.setReturn(id);
            map.put(res.getReturn(), new ItemBuilder().copy(req.getArg0()).id(id).build());
        }
        return res;
    }

    @Override
    public UpdAddressResponse updAddress(UpdAddress req) {
        UpdAddressResponse res = new UpdAddressResponse();
        synchronized (map) {
            Integer id = req.getArg0().getId();
            if (map.containsKey(id)) {
                map.put(id, new ItemBuilder().copy(req.getArg0()).build());
                res.setReturn(id);
            }
        }
        return res;
    }

    @Override
    public DelAddressResponse delAddress(Integer id) {
        DelAddressResponse res = new DelAddressResponse();
        synchronized (map) {
            res.setReturn(map.remove(id));
        }
        return res;
   }
}