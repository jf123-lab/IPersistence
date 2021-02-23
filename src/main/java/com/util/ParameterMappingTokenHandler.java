package com.util;

import java.util.ArrayList;
import java.util.List;

public class ParameterMappingTokenHandler implements TokenHandler {

    private List<ParameterMapping> parameterMappingList = new ArrayList<>();

    //content是#{username} 参数名称

    @Override
    public String handlerToken(String content) {
        parameterMappingList.add(buildParameterMapping(content));
        return "?";
    }

    private ParameterMapping buildParameterMapping(String content){
        ParameterMapping parameterMapping = new ParameterMapping(content);
        return parameterMapping;

    }

    public List<ParameterMapping> getParameterMappingList() {
        return parameterMappingList;
    }

    public void setParameterMappingList(List<ParameterMapping> parameterMappingList) {
        this.parameterMappingList = parameterMappingList;
    }
}
