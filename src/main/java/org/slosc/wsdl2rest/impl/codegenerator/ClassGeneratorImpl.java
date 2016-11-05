package org.slosc.wsdl2rest.impl.codegenerator;

import org.slosc.wsdl2rest.api.codegenerator.ClassGenerator;
import org.slosc.wsdl2rest.api.service.ClassDefinition;
import org.slosc.wsdl2rest.api.service.MethodInfo;
import org.slosc.wsdl2rest.api.service.Param;
import org.slosc.wsdl2rest.impl.util.MessageWriter;
import org.slosc.wsdl2rest.impl.util.MessageWriterFactory;
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
