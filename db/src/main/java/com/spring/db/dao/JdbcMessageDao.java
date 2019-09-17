package com.spring.db.dao;

import com.spring.db.Message;
import com.spring.db.MessageDao;
import com.spring.db.mapper.MessageRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class JdbcMessageDao implements MessageDao {

    private JdbcTemplate jdbcTemplate;
    private MessageRowMapper messageRowMapper = new MessageRowMapper();

    public JdbcMessageDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Message> select(int start, int size) {

        String sql = "select * from guestmessage order by id desc limit ?,?";
        Object[] args = new Object[]{start, size};
//        RowMapper<Message> rowMapper = new RowMapper<Message>() {
//            public Message mapRow(ResultSet rs, int i) throws SQLException {
//                Message message = new Message();
//                message.setId(rs.getInt(1));
//                message.setName(rs.getString(2));
//                message.setMessage(rs.getString(3));
//                message.setCreationTime(rs.getTimestamp(4));
//                return message;
//            }
//        };
        List<Message> messages = jdbcTemplate.query(sql, messageRowMapper, args);
        return messages;
    }

    public List<String> selectAllNames() {
        String sql = "select name from guestmessage";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public Message selectOne(int id){
        String sql = "select * from guestmessage where id = ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{id},messageRowMapper);
    }

    public int counts() {
        String sql = "select count(*) from guestmessage";
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    public int insert(Message message) {
        String sql = "insert into guestmessage(name,message,creation_time) value(?,?,?)";
        Object[] args = new Object[]{message.getName(),message.getMessage(),message.getCreationTime()};
        return jdbcTemplate.update(sql,args);
    }

    public int delete(int id) {
        String sql = "delete from guestmessage where id = ?";
        return jdbcTemplate.update(sql,new Object[]{id});
    }
}
