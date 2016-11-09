package org.slosc.wsdl2rest.impl.service;

import java.util.List;

import org.slosc.wsdl2rest.ClassDefinition;
import org.slosc.wsdl2rest.MethodInfo;

import java.util.ArrayList;
import java.net.URL;
import java.net.MalformedURLException;

public class ClassDefinitionImpl extends MetaInfoImpl implements ClassDefinition {

    private String packageName;
    private List<String> imports;
    private String className;
    private List<MethodInfo> methods = new ArrayList<MethodInfo>();

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
        if (packageName != null && packageName.startsWith("http")) {
            URL loc;
            try {
                loc = new URL(packageName);
            } catch (MalformedURLException ex) {
                throw new IllegalArgumentException("Invalid package name", ex);
            }
            String host = loc.getHost();
            String[] pk = host.split("\\.");
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < pk.length; i++) {
                s.append(pk[pk.length - i - 1]).append((i + 1 < pk.length) ? "." : "");
            }
            packageName = s.toString();
            String path = loc.getPath();
            if (path.length() > 1) {
                packageName += path.replace('/', '.');
            }
            this.packageName = packageName;
        } else {
            this.packageName = "";
        }
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<MethodInfo> getMethodInfos() {
        return methods;
    }

    public void addMethodInfo(MethodInfo method) {
        for (int i = 0; i < methods.size(); i++) {
            MethodInfo mi = methods.get(i);
            if (mi.getMethodName().equals(method.getMethodName())) {
                methods.set(i, method);
                return;
            }
        }
        methods.add(method);
    }

    public MethodInfo getMethodInfo(String methodName) {
        for (MethodInfo mi : methods) {
            if (mi.getMethodName().equals(methodName))
                return mi;
        }
        return null;
    }

    public MethodInfo addMethod(String methodName) {
        MethodInfo mi = null;
        for (int i = 0; i < methods.size(); i++) {
            mi = methods.get(i);
            if (mi.getMethodName().equals(methodName)) {
                return mi;
            }
        }
        mi = new MethodInfoImpl(methodName);
        methods.add(mi);
        return mi;
    }

    public String toString() {
        return packageName + "." + className;
    }
}
