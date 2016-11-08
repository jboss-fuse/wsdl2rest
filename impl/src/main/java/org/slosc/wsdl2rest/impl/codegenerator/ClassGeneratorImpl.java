package org.slosc.wsdl2rest.impl.codegenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slosc.wsdl2rest.ClassDefinition;
import org.slosc.wsdl2rest.ClassGenerator;
import org.slosc.wsdl2rest.MethodInfo;
import org.slosc.wsdl2rest.Param;
import org.slosc.wsdl2rest.impl.writer.MessageWriter;
import org.slosc.wsdl2rest.impl.writer.MessageWriterFactory;

public class ClassGeneratorImpl implements ClassGenerator {

    protected MessageWriter msgWriter = MessageWriterFactory.getMessageWriter();

    protected String outputPath;
    protected List<ClassDefinition> svcClasses;
    protected ClassDefinition clazzDef;

    public ClassGeneratorImpl(String outputPath) {
        this.outputPath = outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void generateClasses(List<ClassDefinition> svcClassesDefs) throws IOException {
        this.svcClasses = svcClassesDefs;
        for (ClassDefinition classDef : svcClasses) {
            this.clazzDef = classDef;
            String packageName = clazzDef.getPackageName();
            packageName = packageName.replace('.', File.separatorChar);
            File packageDir = new File(outputPath + File.separatorChar + packageName);
            packageDir.mkdirs();

            File clazzFile = new File(packageDir, clazzDef.getClassName() + ".java");
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(clazzFile)))) {
                writePackageName(writer, classDef);
                writeImports(writer, classDef);
                writeServiceClass(writer, classDef);
            }
        }
    }

    protected void writePackageName(PrintWriter writer, ClassDefinition clazzDef) {
        final String packName = clazzDef.getPackageName();
        if (packName != null && packName.length() != 0) {
            writer.println("package " + packName + ";");
        }
        writer.println();
    }

    protected void writeImports(PrintWriter writer, ClassDefinition clazzDef) {
        if (clazzDef.getImports() != null) {
            for (String impo : clazzDef.getImports()) {
                writer.println("import " + impo + ";");
            }
        }
        writer.println();
    }

    protected void writeServiceClass(PrintWriter writer, ClassDefinition clazzDef) {
        if (clazzDef.getClassName() != null) {
            writer.println("public interface " + clazzDef.getClassName() + " {\n");
            writeMethods(writer, clazzDef.getMethodInfos());
            writer.println("}");
            writer.println();
        }
    }

    protected void writeMethods(PrintWriter writer, List<? extends MethodInfo> methods) {
        if (methods != null) {
            for (MethodInfo minfo : methods) {
                String retType = minfo.getReturnType();
                writer.print("\tpublic " + (retType != null ? retType : "void") + " ");
                writer.print(minfo.getMethodName() + "(");
                writeParams(writer, minfo);
                String excep = minfo.getExceptionType() != null ? (" throws " + minfo.getExceptionType()) : "";
                writer.println(")" + excep + ";");
                writer.println();
            }
        }
    }

    protected void writeMethod(PrintWriter writer, MethodInfo minfo) {
        if (minfo != null) {
            String retType = minfo.getReturnType();
            writer.print("\tpublic " + (retType != null ? retType : "void") + " ");
            writer.print(minfo.getMethodName() + "(");
            writeParams(writer, minfo);
            String excep = minfo.getExceptionType() != null ? (" throws " + minfo.getExceptionType()) : "";
            writer.println(")" + excep + ";");
            writer.println();
        }
    }

    protected void writeParams(PrintWriter writer, MethodInfo minfo) {
        List<Param> params = minfo.getParams();
        int i = 0;
        for (Param p : params) {
            writer.print(i++ == 0 ? "" : ", ");
            writer.print(p.getParamType() + " " + p.getParamName());
        }
    }
}
