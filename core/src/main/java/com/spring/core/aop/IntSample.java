package com.spring.core.aop;

import com.spring.core.aop.ifs.Sample;

public class IntSample implements Sample<Integer> {
    @Override
    public void sampleMethod(Integer param) {
        System.out.println("Parameter type is Integer");
    }
}
