package com.spring.core.aop;

import org.springframework.context.support.GenericXmlApplicationContext;

public class MainQuickStart {
    public static void main(String[] args) {
        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:aop/type-aop.xml");

//        MemberService ms = ctx.getBean("memberService",MemberService.class);
//        System.out.println(ms instanceof MemberService);
//        System.out.println(ms instanceof MemberServiceImpl);
//        System.out.println(ms.getClass().getName());

        ctx.close();
    }
}
