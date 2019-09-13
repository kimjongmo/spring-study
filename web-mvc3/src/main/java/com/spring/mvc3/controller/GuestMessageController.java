package com.spring.mvc3.controller;

import com.spring.mvc3.form.GuestMessage;
import com.spring.mvc3.form.GuestMessageList;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class GuestMessageController {

    @RequestMapping(value = "/guestmessage/post.xml", method = RequestMethod.POST)
    @ResponseBody
    public GuestMessageList postXml(@RequestBody GuestMessageList messageList) {
        return messageList;
    }

    @RequestMapping(value = "/guestmessage/list.xml")
    @ResponseBody
    public GuestMessageList listXml() {
        return getMessageList();
    }

    private GuestMessageList getMessageList() {
        List<GuestMessage> messages = Arrays.asList(
                new GuestMessage(1, "메시지", new Date()),
                new GuestMessage(2, "메시지2", new Date())
        );
        return new GuestMessageList(messages);
    }
}
