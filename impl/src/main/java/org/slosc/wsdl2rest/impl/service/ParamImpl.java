package org.slosc.wsdl2rest.impl.service;

import org.slosc.wsdl2rest.ParamInfo;

public class ParamImpl extends MetaInfoImpl implements ParamInfo {

    private String paramType;
    private String paramName;

    public ParamImpl(String paramType, String paramName) {
        this.paramType = paramType;
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public String getParamName() {
        return paramName;
    }
}
