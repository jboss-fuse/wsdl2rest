package org.slosc.wsdl2rest.test.rpclit;
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

    private Map<Integer, String> map = new LinkedHashMap<>();
    
    public Integer[] listAddresses() {
        synchronized (map) {
            Set<Integer> keySet = map.keySet();
            return new ArrayList<>(keySet).toArray(new Integer[keySet.size()]);
        }
    }

    public String getAddress(Integer id) {
        synchronized (map) {
            return map.get(id);
        }
    }

    public Integer addAddress(String name) {
        synchronized (map) {
            map.put(map.size() + 1, name);
            return map.size();
        }
    }

    public String updAddress(Integer id, String name) {
        String result;
        synchronized (map) {
            result = map.get(id);
            if (result != null) {
                map.put(id, name);
            }
        }
        return result;
    }

    public String delAddress(Integer id) {
        String result;
        synchronized (map) {
            result = map.remove(id);
        }
        return result;
   }
}