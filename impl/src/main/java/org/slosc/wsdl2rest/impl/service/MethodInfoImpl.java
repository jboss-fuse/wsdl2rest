package org.slosc.wsdl2rest.impl.service;

import java.util.ArrayList;
import java.util.List;

import org.slosc.wsdl2rest.MethodInfo;
import org.slosc.wsdl2rest.ParamInfo;



public class MethodInfoImpl extends MetaInfoImpl implements MethodInfo {

    private String style;
    private String returnType;
    private String methodName;
    private List<ParamInfo> params = new ArrayList<>();
    private String exceptionType;

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

    public List<ParamInfo> getParams() {
        return params;
    }

    public void setParams(List<ParamInfo> params) {
        this.params = params;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
    public String toString() {
        return methodName+"(): "+returnType;
    }
}
