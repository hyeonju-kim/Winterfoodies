package com.winterfoodies.winterfoodies_project.exception;


import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;

public class ExceptionUtils {
    public static boolean isForView(HttpServletRequest request){
        return request.getMethod().toString().equals(HttpMethod.GET.toString());
    }
}
