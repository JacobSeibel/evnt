package com.evnt.util;

import com.evnt.ui.EvntWebappUI;

import java.util.HashMap;
import java.util.Map;

public class ParamUtils {

    public static String getStringParam(String paramName){
        return getParamMap().get(paramName);
    }

    public static Integer getIntegerParam(String paramName){
        return Integer.parseInt(getParamMap().get(paramName));
    }

    private static Map<String, String> getParamMap(){
        String fragment = EvntWebappUI.getCurrent().getPage().getLocation().getFragment();
        Map<String, String> paramMap = new HashMap<>();
        if(fragment.indexOf('?') > -1) {
            String paramString = fragment.substring(fragment.indexOf('?')+1);
            for(String param : paramString.split("&")){
                paramMap.put(param.substring(0, param.indexOf('=')), param.substring(param.indexOf('=')+1));
            }
        }

        return paramMap;
    }

}
