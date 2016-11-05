package org.slosc.wsdl2rest.impl.codegenerator.jsr311;

import org.slosc.wsdl2rest.api.service.ClassDefinition;
import org.slosc.wsdl2rest.api.service.MethodInfo;
import org.slosc.wsdl2rest.api.service.Param;
import org.slosc.wsdl2rest.impl.codegenerator.ClassGeneratorImpl;

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
