package org.jboss.fuse.wsdl2rest.impl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.fuse.wsdl2rest.EndpointInfo;
import org.jboss.fuse.wsdl2rest.MethodInfo;

public class ClassDefinitionImpl extends MetaInfoImpl implements EndpointInfo {

    private String packageName;
    private List<String> imports;
    private String className;
    private Map<String, MethodInfo> methods = new LinkedHashMap<>();

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public List<String> getImports() {
        return imports;
    }

    public void setImports(List<String> imports) {
        this.imports = imports;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public List<MethodInfo> getMethods() {
        List<MethodInfo> result = new ArrayList<>(methods.values());
        result = result.stream().sorted(new Comparator<MethodInfo>() {
            public int compare(MethodInfo o1, MethodInfo o2) {
                return o1.getMethodName().compareTo(o2.getMethodName());
            }
        }).collect(Collectors.toList());
        return Collections.unmodifiableList(result);
    }

    @Override
   public MethodInfo getMethod(String methodName) {
        return methods.get(methodName);
    }

    public void addMethod(MethodInfo method) {
        methods.put(method.getMethodName(), method);
    }
    
    @Override
    public String getFQN() {
        return packageName + "." + className;
    }

    @Override
    public String toString() {
        return getFQN();
    }
}
