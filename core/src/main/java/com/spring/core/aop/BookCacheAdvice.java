package com.spring.core.aop;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.HashMap;
import java.util.Map;

public class BookCacheAdvice {

    private Map<Integer, String> cache = new HashMap<>();

    public String cache(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer id = (Integer) joinPoint.getArgs()[0];
        String title = cache.get(id);
        if (title != null) {
            System.out.println("[BookCacheAdvice] 캐시에서 Book[" + title + "] 구함");
            return title;
        }

        String ret = (String) joinPoint.proceed();
        if (ret != null) {
            cache.put(id, ret);
            System.out.println("[BookCacheAdvice] 캐시에 Book[" + ret + "] 삽입");
        }
        return ret;
    }

    public Map<Integer, String> getCache() {
        return cache;
    }

    public void setCache(Map<Integer, String> cache) {
        this.cache = cache;
    }
}
