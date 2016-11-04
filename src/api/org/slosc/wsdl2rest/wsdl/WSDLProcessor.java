package org.slosc.wsdl2rest.wsdl;

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

import org.slosc.wsdl2rest.service.ClassDefinition;

import java.util.Map;
import java.util.List;

public interface WSDLProcessor {
    Map process(String wsdlURI, String username, String password);

    Map process(String wsdlURI);

    List<ClassDefinition> getTypeDefs();

    Map<String, ClassDefinition> getServiceDef();
}
