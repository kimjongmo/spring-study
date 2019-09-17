package com.spring.db.mapper;

import com.spring.db.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRowMapper implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int i) throws SQLException {
        Message message = new Message();
        message.setId(rs.getInt(1));
        message.setName(rs.getString(2));
        message.setMessage(rs.getString(3));
        message.setCreationTime(rs.getTimestamp(4));
        return message;
    }
}
