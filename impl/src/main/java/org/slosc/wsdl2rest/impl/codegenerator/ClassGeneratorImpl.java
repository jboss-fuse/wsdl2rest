package org.slosc.wsdl2rest.impl.codegenerator;

import org.slosc.wsdl2rest.ClassDefinition;
import org.slosc.wsdl2rest.ClassGenerator;
import org.slosc.wsdl2rest.MethodInfo;
import org.slosc.wsdl2rest.Param;
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

    public ClassGeneratorImpl(String outputPath) {
        this.outputPath = outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void generateClasses(List<ClassDefinition> svcClassesDefs) {
        this.svcClasses = svcClassesDefs;
        for (ClassDefinition classDef : svcClasses) {
            this.clazzDef = classDef;
            String packageName = clazzDef.getPackageName();
            packageName = packageName.replace('.', File.separatorChar);
            File packageDir = new File(outputPath + File.separatorChar + packageName);
            packageDir.mkdirs();

            try {
                File clazzFile = new File(packageDir, clazzDef.getClassName() + ".java");
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

    protected void writePackageName(ClassDefinition clazzDef) {
        final String packName = clazzDef.getPackageName();
        if (packName != null && packName.length() != 0) {
            writer.println("package " + packName + ";");
        }
        writer.println();
    }

    protected void writeImports(ClassDefinition clazzDef) {
        if (clazzDef.getImports() != null) {
            for (String impo : clazzDef.getImports()) {
                writer.println("import " + impo + ";");
            }
        }
        writer.println();
    }

    protected void writeServiceClass(ClassDefinition clazzDef) {
        if (clazzDef.getClassName() != null) {
            writer.println("public interface " + clazzDef.getClassName() + " {\n");
            writeMethods(clazzDef.getMethodInfos());
            writer.println("}");
            writer.println();
        }
    }

    protected void writeMethods(List<? extends MethodInfo> methods) {
        if (methods != null) {
            for (MethodInfo mInf : methods) {
                String retType = mInf.getReturnType();
                writer.print("\tpublic " + (retType != null ? retType : "void") + " ");
                writer.print(mInf.getMethodName() + "(");
                writeParams(mInf.getParams());
                String excep = mInf.getExceptionType() != null ? (" throws " + mInf.getExceptionType()) : "";
                writer.println(")" + excep + ";");
                writer.println();
            }
        }
    }

    protected void writeMethod(MethodInfo mInf) {
        if (mInf != null) {
            String retType = mInf.getReturnType();
            writer.print("\tpublic " + (retType != null ? retType : "void") + " ");
            writer.print(mInf.getMethodName() + "(");
            writeParams(mInf.getParams());
            String excep = mInf.getExceptionType() != null ? (" throws " + mInf.getExceptionType()) : "";
            writer.println(")" + excep + ";");
            writer.println();
        }
    }

    protected void writeParams(List<Param> params) {
        if (params != null) {
            int i = 0;
            int size = params.size();
            for (Param p : params) {
                String comma = (++i != size) ? ", " : "";
                writer.print(p.getParamType() + " " + p.getParamName() + comma);
            }
        }
    }
}
