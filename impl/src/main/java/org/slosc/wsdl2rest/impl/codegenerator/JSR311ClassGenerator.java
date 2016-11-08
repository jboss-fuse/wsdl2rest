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
        writer.println("import javax.ws.rs.Consumes;");
        writer.println("import javax.ws.rs.Produces;");
        writer.println("import javax.ws.rs.DELETE;");
        writer.println("import javax.ws.rs.GET;");
        writer.println("import javax.ws.rs.POST;");
        writer.println("import javax.ws.rs.PUT;");
        writer.println("import javax.ws.rs.Path;");
        writer.println("import javax.ws.rs.PathParam;");
        writer.println("import javax.ws.rs.core.MediaType;");
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
        for (MethodInfo minfo : methods) {
            List<String> resourceInf = minfo.getResources();
            if (minfo.getPreferredResource() != null) {
                resourceInf = new ArrayList<String>();
                resourceInf.add(minfo.getPreferredResource());
            }
            if (resourceInf != null) {
                String httpMethod = minfo.getPreferredHttpMethod() != null ? minfo.getPreferredHttpMethod() : minfo.getHttpMethod();
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
                if (httpMethod.equals("GET") || httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
                    if (minfo.getParams().size() >= 1) {
                        writer.print("/{" + minfo.getParams().get(0).getParamName() + "}");
                    }
                }
                writer.println("\")");
                if (httpMethod.equals("GET") || httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
                    writer.println("\t@Produces(MediaType.APPLICATION_JSON)");
                } else if (httpMethod.equals("PUT") || httpMethod.equals("POST")) {
                    writer.println("\t@Consumes(MediaType.APPLICATION_JSON)");
                }
            }
            writeMethod(writer, minfo);
        }
    }

    @Override
    protected void writeParams(PrintWriter writer, MethodInfo minfo) {
        for (int i = 0; i < minfo.getParams().size(); i++) {
            Param p = minfo.getParams().get(i);
            String paramName = p.getParamName();
            String paramType = p.getParamType();
            String httpMethod = minfo.getHttpMethod();
            writer.print(i == 0 ? "" : ", ");
            if (httpMethod.equals("GET") || httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
                if (i == 0) {
                    writer.print("@PathParam(\"" + paramName + "\") ");
                }
            }
            writer.print(paramType + " " + paramName);
        }
    }
}
