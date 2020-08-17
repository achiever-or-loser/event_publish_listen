package com.event.controller;

import com.event.event.MyEventPublish;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "事件接口")
@RequestMapping("/event")
public class PublishEventController {
    @Autowired
    private MyEventPublish register;

    @GetMapping("/registerUser")
    @ResponseBody
    public void register() {
        try {
            register.register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
