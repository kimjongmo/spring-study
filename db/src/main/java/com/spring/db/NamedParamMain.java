package com.spring.db;

import com.spring.db.dao.NamedParameterJdbcMessageDao;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Date;
import java.util.List;

public class NamedParamMain {
    public static void main(String[] args) {
        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:datasourceAndNamed.xml");
        NamedParameterJdbcMessageDao dao = ctx.getBean(NamedParameterJdbcMessageDao.class);

        /** select **/
//        List<Message> messages = dao.select(0,3);
//        messages.stream().forEach(System.out::println);

        /** count **/
//        int counts = dao.counts();
//        System.out.println(counts);

        /** insert **/
//        Message message = new Message();
//        message.setName("새로운 메시지2");
//        message.setMessage("새로운 내용2");
//        message.setCreationTime(new Date());
//
//        dao.insert(message);

        /** delete **/
        dao.delete(5);

        ctx.close();
    }
}
