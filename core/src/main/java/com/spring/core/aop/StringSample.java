package com.spring.core.aop;

import com.spring.core.aop.ifs.Sample;

public class StringSample implements Sample<String> {
    @Override
    public void sampleMethod(String param) {
        System.out.println("Parameter type is String");
    }
}
