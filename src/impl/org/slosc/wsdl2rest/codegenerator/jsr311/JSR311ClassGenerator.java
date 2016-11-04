package org.slosc.wsdl2rest.codegenerator.jsr311;

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

import org.slosc.wsdl2rest.codegenerator.ClassGeneratorImpl;
import org.slosc.wsdl2rest.service.ClassDefinition;
import org.slosc.wsdl2rest.service.MethodInfo;
import org.slosc.wsdl2rest.service.Param;

import java.util.List;
import java.util.ArrayList;

public class JSR311ClassGenerator extends ClassGeneratorImpl {

    public JSR311ClassGenerator(){
    }
    public JSR311ClassGenerator(String outputPath){
        super(outputPath);
    }



    protected void writeServiceClass(ClassDefinition clazzDef){
        if(clazzDef.getClassName() != null){
            //write jsr-311 annotations
            writer.println("@Path(\"/"+clazzDef.getClassName().toLowerCase()+"/\")");
            writer.println("@ProduceMime(\"application/xml\")");
            
            super.writeServiceClass(clazzDef);
        }
    }

    protected void writeMethods(List<? extends MethodInfo> methods){
        if(methods == null) return;
        for(MethodInfo mInf:methods){
            //write jsr-311 annotations
            List<String> resouceInf = mInf.getResources();
            if(mInf.getPreferredResource() != null){
                resouceInf = new ArrayList<String>();
                resouceInf.add(mInf.getPreferredResource());
            }

            if(resouceInf != null){
               writer.println("\t@"+((mInf.getPreferredHttpMethod()==null)?mInf.getHttpMethod():mInf.getPreferredHttpMethod()));
               StringBuilder path = new StringBuilder();
               int loc = 0;
               if(resouceInf.size() >= 2) loc = 1;
               for(int i=loc;i<resouceInf.size();i++){
                   path.append(resouceInf.get(i));
               }
               writer.println("\t@Path(\""+path.toString().toLowerCase()+"\")");
            }else{

            }
            writeMethod(mInf);
        }
    }

     protected void writeParams(List<Param> params){
        if(params != null) {
            int i=0; int size = params.size();
            for(Param p : params){
                String comma = (++i != size)?", ":"";
                //write jsr-311 annotations
                writer.print(p.getParamType()+" "+p.getParamName()+comma);
            }
        }
     }
    
}
