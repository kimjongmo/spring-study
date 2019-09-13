package com.spring.mvc3.form;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "message-list")
public class GuestMessageList {
    @XmlElement(name = "message")
    private List<GuestMessage> messages;

    public GuestMessageList(){

    }
    public GuestMessageList(List<GuestMessage> messages) {
        this.messages = messages;
    }

    public List<GuestMessage> getMessages() {
        return messages;
    }
}
