package org.slosc.wsdl2rest.impl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slosc.wsdl2rest.EndpointInfo;
import org.slosc.wsdl2rest.MethodInfo;

public class ClassDefinitionImpl extends MetaInfoImpl implements EndpointInfo {

    private String packageName;
    private List<String> imports;
    private String className;
    private Map<String, MethodInfo> methods = new LinkedHashMap<>();

    public String getPackageName() {
        return packageName;
    }

    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<MethodInfo> getMethods() {
        ArrayList<MethodInfo> result = new ArrayList<>(methods.values());
        return Collections.unmodifiableList(result);
    }

   public MethodInfo getMethod(String methodName) {
        return methods.get(methodName);
    }

    public void addMethod(MethodInfo method) {
        methods.put(method.getMethodName(), method);
    }

    public String toString() {
        return packageName + "." + className;
    }
}
