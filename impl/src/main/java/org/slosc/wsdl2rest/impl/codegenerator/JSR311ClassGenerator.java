package org.slosc.wsdl2rest.impl.codegenerator;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.slosc.wsdl2rest.ClassDefinition;
import org.slosc.wsdl2rest.MethodInfo;
import org.slosc.wsdl2rest.Param;

public class JSR311ClassGenerator extends ClassGeneratorImpl {

    public JSR311ClassGenerator(String outputPath) {
        super(outputPath);
    }

    @Override
    protected void writeImports(PrintWriter writer, ClassDefinition clazzDef) {
        writer.println("import javax.ws.rs.GET;");
        writer.println("import javax.ws.rs.POST;");
        writer.println("import javax.ws.rs.Path;");
        writer.println("import javax.ws.rs.PathParam;");
        super.writeImports(writer, clazzDef);
    }

    @Override
    protected void writeServiceClass(PrintWriter writer, ClassDefinition clazzDef) {
        String pathName = clazzDef.getClassName().toLowerCase();
        writer.println("@Path(\"/" + pathName + "/\")");
        super.writeServiceClass(writer, clazzDef);
    }

    @Override
    protected void writeMethods(PrintWriter writer, List<? extends MethodInfo> methods) {
        if (methods != null) {
            for (MethodInfo methodInf : methods) {
                List<String> resourceInf = methodInf.getResources();
                if (methodInf.getPreferredResource() != null) {
                    resourceInf = new ArrayList<String>();
                    resourceInf.add(methodInf.getPreferredResource());
                }
                if (resourceInf != null) {
                    String httpMethod = methodInf.getPreferredHttpMethod() != null ? methodInf.getPreferredHttpMethod() : methodInf.getHttpMethod();
                    writer.println("\t@" + httpMethod);
                    StringBuilder path = new StringBuilder();
                    int loc = 0;
                    if (resourceInf.size() >= 2)
                        loc = 1;
                    for (int i = loc; i < resourceInf.size(); i++) {
                        path.append(resourceInf.get(i));
                    }
                    String pathName = path.toString().toLowerCase();
                    writer.print("\t@Path(\"" + pathName);
                    List<Param> params = methodInf.getParams();
                    if (params.size() == 1) {
                        Param param = params.get(0);
                        writer.print("/{" + param.getParamName() + "}");
                    }
                    writer.println("\")");
                }
                writeMethod(writer, methodInf);
            }
        }
    }

    @Override
    protected void writeParams(PrintWriter writer, List<Param> params) {
        int i = 0;
        for (Param p : params) {
            String paramName = p.getParamName();
            String paramType = p.getParamType();
            String pathParam = "@PathParam(\"" + paramName + "\") ";
            writer.print(i++ == 0 ? "" : ", ");
            writer.print(pathParam + paramType + " " + paramName);
        }
    }
}
