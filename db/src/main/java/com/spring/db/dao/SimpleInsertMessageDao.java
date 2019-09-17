package com.spring.db.dao;

import com.spring.db.Message;
import com.spring.db.MessageDao;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleInsertMessageDao implements MessageDao {

    private SimpleJdbcInsert simpleJdbcInsert;

    public SimpleInsertMessageDao(DataSource dataSource){
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
        this.simpleJdbcInsert
                .withTableName("guestmessage")
                .usingColumns("name","message","creationTime");
    }


    @Override
    public List<Message> select(int start, int size) {
        return null;
    }

    @Override
    public int counts() {
        return 0;
    }

    @Override
    public int insert(Message message) {
//        Map<String,Object> params = new HashMap<>();
//        params.put("NAME",message.getName());
//        params.put("MESSAGE",message.getMessage());
//        params.put("CREATIONTIME",message.getCreationTime());
//        return simpleJdbcInsert.execute(params);
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(message);
        return simpleJdbcInsert.execute(parameterSource);
    }

    @Override
    public int delete(int id) {
        return 0;
    }
}
