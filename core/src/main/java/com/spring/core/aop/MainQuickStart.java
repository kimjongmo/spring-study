package com.spring.core.aop;

import com.spring.core.aop.conf.SampleConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class MainQuickStart {
    public static void main(String[] args) {
//        GenericXmlApplicationContext ctx =
//                new GenericXmlApplicationContext("classpath:aop/type-aop.xml");
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(SampleConfig.class);

        IntSample intSample = ctx.getBean("intSample",IntSample.class);
        intSample.sampleMethod(5);

        StringSample stringSample = ctx.getBean("stringSample",StringSample.class);
        stringSample.sampleMethod("hello");
//        Checker checker = ctx.getBean("checker",Checker.class);
//        checker.check();
//        MemberService ms = ctx.getBean("memberService",MemberService.class);
//        System.out.println(ms instanceof MemberService);
//        System.out.println(ms instanceof MemberServiceImpl);
//        System.out.println(ms.getClass().getName());

        ctx.close();
    }
}
