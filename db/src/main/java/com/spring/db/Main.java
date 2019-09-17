package com.spring.db;

import com.spring.db.dao.JdbcMessageDao;
import org.springframework.context.support.GenericXmlApplicationContext;

import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext("classpath:datasource.xml");
        JdbcMessageDao dao = ctx.getBean("jdbcMessageDao", JdbcMessageDao.class);

        /** query() **/
//        List<Message> messages = dao.select(0,2);
//        System.out.println("messages size : "+messages.size());
//        messages.stream().forEach(System.out::println);

        /** queryForList() **/
//        List<String> names = dao.selectAllNames();
//        names.stream().forEach(System.out::println);

        /** queryForObject()**/
//        Message message = dao.selectOne(1);
//        System.out.println(message);
//        int counts = dao.counts();
//        System.out.printf("전체 데이터 개수 : %d\n",counts);

        /** insert test **/
        Message message = new Message();
        message.setName("새로운 메시지");
        message.setMessage("새로운 내용");
        message.setCreationTime(new Date());

        int result = dao.insert(message);

        if (result == 1)
            System.out.println("INSERT SUCCESS!!!");
        else
            System.out.println("INSERT FAIL!!! effected by col count is " + result);


        /** delete test **/
//        int delResult = dao.delete(4);
//
//        if (delResult == 1)
//            System.out.println("DELETE SUCCESS!!!");
//        else
//            System.out.println("DELETE FAIL!!! effected by col count is " + delResult);

        ctx.close();
    }
}
