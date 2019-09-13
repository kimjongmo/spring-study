package com.spring.mvc3.form;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

@XmlAccessorType(XmlAccessType.FIELD)
public class GuestMessage {
    @XmlElement(name = "id")
    private Integer id;
    @XmlElement(name = "message")
    private String message;
    @XmlElement(name = "created")
    private Date creationTime;

    public GuestMessage(Integer id, String message, Date creationTime) {
        this.id = id;
        this.message = message;
        this.creationTime = creationTime;
    }

    public Integer getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreationTime() {
        return creationTime;
    }
}
