package org.jboss.fuse.wsdl2rest.impl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.fuse.wsdl2rest.MethodInfo;
import org.jboss.fuse.wsdl2rest.ParamInfo;

public class MethodInfoImpl extends MetaInfoImpl implements MethodInfo {

    private String style;
    private String returnType;
    private String methodName;
    private String exceptionType;
    private String httpMethod;
    private List<ParamInfo> params = new ArrayList<>();
    
    private String wrappedReturnType;
    private List<ParamInfo> wrappedParams = new ArrayList<>();

    public MethodInfoImpl(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getWrappedReturnType() {
        return wrappedReturnType != null ? wrappedReturnType : returnType;
    }

    public void setWrappedReturnType(String wrappedReturnType) {
        this.wrappedReturnType = wrappedReturnType;
    }

    public List<ParamInfo> getWrappedParams() {
        List<ParamInfo> result = wrappedParams.size() > 0 ? wrappedParams : params;
        return Collections.unmodifiableList(result);
    }

    public void setWrappedParams(List<ParamInfo> wrappedParams) {
        this.wrappedParams.addAll(wrappedParams);
    }

    @Override
    public List<ParamInfo> getParams() {
        return Collections.unmodifiableList(params);
    }

    public void setParams(List<ParamInfo> params) {
        this.params.addAll(params);
    }

    @Override
    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public String getPath() {
        String result = null;
        List<String> resources = getResources();
        if (getPreferredResource() != null) {
            resources = new ArrayList<String>();
            resources.add(getPreferredResource());
        }
        if (resources != null) {
            int loc = resources.size() >= 2 ? 1 : 0;
            StringBuilder path = new StringBuilder();
            for (int i = loc; i < resources.size(); i++) {
                path.append(resources.get(i));
            }
            result = path.toString().toLowerCase();

        }
        if (result != null && getParams().size() > 0) {
            ParamInfo pinfo = getParams().get(0);
            if (hasPathParam(pinfo)) {
                result += "/{" + pinfo.getParamName() + "}";
            }
        }
        return result;
    }

    private boolean hasPathParam(ParamInfo pinfo) {
        boolean pathParam =
        		//null httpMethod is supposed to default to GET based on ResourceMapperImpl.mapResources(String)
        		httpMethod == null
        		|| "GET".equals(httpMethod)
        		|| "DELETE".equals(httpMethod);
        return pathParam && pinfo.getParamType() != null;
    }

    @Override
    public String toString() {
        return methodName + "(): " + returnType;
    }

}
