package org.slosc.wsdl2rest.impl.service;

import java.util.ArrayList;
import java.util.List;

import org.slosc.wsdl2rest.MethodInfo;
import org.slosc.wsdl2rest.Param;



public class MethodInfoImpl extends MetaInfoImpl implements MethodInfo {

    private String returnType;
    private String methodName;
    private List<Param> params = new ArrayList<>();
    private String exceptionType;

    public MethodInfoImpl() {
    }

    public MethodInfoImpl(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String toString() {
        return methodName+"(): "+returnType;
    }
}
