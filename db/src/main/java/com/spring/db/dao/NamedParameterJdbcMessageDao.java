package com.spring.db.dao;

import com.spring.db.Message;
import com.spring.db.MessageDao;
import com.spring.db.mapper.MessageRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NamedParameterJdbcMessageDao implements MessageDao {
    private NamedParameterJdbcTemplate template;
    private MessageRowMapper messageRowMapper = new MessageRowMapper();

    public NamedParameterJdbcMessageDao(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Message> select(int start, int size) {
        String sql = "select * from guestmessage order by id desc limit :start,:size";

        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("size", size);

        List<Message> messages = template.query(sql, params, messageRowMapper);

        return messages;
    }

    @Override
    public int counts() {
        String sql = "select count(*) from guestmessage";
        return template.queryForObject(sql,new HashMap<>(),Integer.class);
    }

    @Override
    public int insert(Message message) {
        String sql = "insert into guestmessage(name,message,creation_time) value(:name,:message,:creationTime)";
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(message);
        return template.update(sql,parameterSource);
    }

    @Override
    public int delete(int id) {
        String sql = "delete from guestmessage where id = :id";
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id",id);
        return template.update(sql,parameterSource);
    }
}
