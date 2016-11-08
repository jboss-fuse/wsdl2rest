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


import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.RPC)
public interface Address {
    
    /**
     * List the available resource ids.
     */
    @WebMethod
    Integer[] listAddresses();
    
    /**
     * Get the resource value for the given id.
     * @return The resource value or null
     */
    @WebMethod
    String getAddress(Integer id);

    /**
     * Add a resource with the given value.
     * @return The new resource id
     */
    @WebMethod
    Integer addAddress(String name);
    
    /**
     * Update a resource for the given id with the given value.
     * @return The new resource value or null
     */
    @WebMethod
    String updAddress(Integer id, String name);

    /**
     * Delete a resource with the given id.
     * @return The resource value or null
     */
    @WebMethod
    String delAddress(Integer id);
}