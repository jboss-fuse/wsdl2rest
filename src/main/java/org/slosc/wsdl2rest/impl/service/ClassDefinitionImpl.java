package org.slosc.wsdl2rest.impl.service;

import java.util.List;

import org.slosc.wsdl2rest.api.service.ClassDefinition;
import org.slosc.wsdl2rest.api.service.MethodInfo;

import java.util.ArrayList;
import java.net.URL;
import java.net.MalformedURLException;



public class ClassDefinitionImpl extends MetaInfoImpl implements ClassDefinition {
    
    private String packageName;
    private List<String> imports;
    private String className;
    private List<MethodInfoImpl> methods = new ArrayList<MethodInfoImpl>();


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
        try {
            if(packageName == null || packageName.length() == 0 || !packageName.startsWith("http")) {
                this.packageName ="";
                return;
            }
            URL loc = new URL(packageName);
            if(loc.getProtocol().equals("http")){
                String host = loc.getHost();
                String [] pk = host.split("\\.");
                StringBuilder s = new StringBuilder();
                for(int i=0;i<pk.length;i++)
                    s.append(pk[pk.length - i - 1]).append((i+1 < pk.length)?".":"");
                packageName = s.toString()+loc.getPath().replace('/','.');
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<? extends MethodInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfoImpl> methods) {
        this.methods = methods;
    }

    public void addMethodInfo(MethodInfoImpl method){
        for(int i=0;i<methods.size();i++){
            MethodInfoImpl mi = methods.get(i);
            if(mi.getMethodName().equals(method.getMethodName())){
                methods.set(i, method);
                return;
            }
        }
        methods.add(method);
    }

    public MethodInfoImpl getMethodInfo(String methodName){
        for(MethodInfoImpl mi : methods){
            if(mi.getMethodName().equals(methodName))
                return mi;
        }
        return null;
    }

    public MethodInfoImpl addMethod(String methodName){
        MethodInfoImpl mi = null;
        for(int i=0;i<methods.size();i++){
            mi = methods.get(i);
            if(mi.getMethodName().equals(methodName)){
                return mi;
            }
        }
        mi = new MethodInfoImpl(methodName);
        methods.add(mi);
        return mi;
    }

    public String toString() {
        return packageName+"."+className;
    }
}
