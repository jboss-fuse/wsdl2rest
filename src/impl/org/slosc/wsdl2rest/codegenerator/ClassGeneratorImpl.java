package org.slosc.wsdl2rest.codegenerator;

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
import org.slosc.wsdl2rest.service.MethodInfo;
import org.slosc.wsdl2rest.service.Param;
import org.slosc.wsdl2rest.util.MessageWriter;
import org.slosc.wsdl2rest.util.MessageWriterFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.io.*;


public class ClassGeneratorImpl implements ClassGenerator {

    private static Log log = LogFactory.getLog(ClassGeneratorImpl.class);
    protected MessageWriter msgWriter = MessageWriterFactory.getMessageWriter();

    protected String outputPath;
    protected PrintWriter writer = null;
    protected List<ClassDefinition> svcClasses;
    protected ClassDefinition clazzDef;

    protected ClassGeneratorImpl(){
        
    }
    public ClassGeneratorImpl(String outputPath){
        this.outputPath = outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void generateClasses(List<ClassDefinition> svcClassesDefs) {
        this.svcClasses = svcClassesDefs;
        for(ClassDefinition classDef : svcClasses){
            this.clazzDef = classDef;
            String packageName = clazzDef.getPackageName();
            packageName = packageName.replace('.', File.separatorChar);
            File clazzFile = new File(outputPath +packageName+File.separatorChar);
            clazzFile.mkdirs();

            try {
                clazzFile = new File(outputPath +packageName+File.separatorChar+clazzDef.getClassName()+".java");
                writer = new PrintWriter(new BufferedWriter(new FileWriter(clazzFile)));
            } catch (IOException e) {
                log.warn(e.getStackTrace());
                continue;
            }

            writePackageName(classDef);
            writeImports(classDef);
            writeServiceClass(classDef);

            writer.close();
        }
    }

    protected void writePackageName(ClassDefinition clazzDef){
        final String packName = clazzDef.getPackageName();
        if( packName!= null && packName.length() != 0 ) 
            writer.println("\npackage "+packName+";");
        writer.println("\n\n");
    }

    protected void writeImports(ClassDefinition clazzDef){
        if(clazzDef.getImports() != null)
        for(String impo : clazzDef.getImports()){
          writer.println("import "+impo+";");
        }
        writer.println("\n\n");
    }

    protected void writeServiceClass(ClassDefinition clazzDef){
        if(clazzDef.getClassName() != null){
            writer.println("public class "+clazzDef.getClassName()+" {\n");
            writeMethods(clazzDef.getMethods());
            writer.println("}\n\n\n");
        }
    }

    protected void writeMethods(List<? extends  MethodInfo> methods){
        if(methods == null) return;
        for(MethodInfo mInf:methods){
            String retType = mInf.getReturnType();
            writer.print("\tpublic "+(retType!=null?retType:"void")+" ");
            writer.print(mInf.getMethodName()+"(");
            writeParams(mInf.getParams());
            String excep = mInf.getExceptionType() != null?(" throws "+ mInf.getExceptionType()):"";
            writer.println(")"+excep+";");
        }
    }

    protected void writeMethod(MethodInfo mInf){
        if(mInf == null) return;
        String retType = mInf.getReturnType();
        writer.print("\tpublic "+(retType!=null?retType:"void")+" ");
        writer.print(mInf.getMethodName()+"(");
        writeParams(mInf.getParams());
        String excep = mInf.getExceptionType() != null?(" throws "+ mInf.getExceptionType()):"";
        writer.println(")"+excep+";\n");
    }

    protected void writeParams(List<Param> params){
        if(params != null) {
            int i=0; int size = params.size();
            for(Param p : params){
                String comma = (++i != size)?", ":"";
                writer.print(p.getParamType()+" "+p.getParamName()+comma);
            }
        }
    }
}
