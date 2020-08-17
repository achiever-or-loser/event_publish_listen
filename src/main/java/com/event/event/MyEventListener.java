package com.event.event;

import com.event.po.User;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class MyEventListener {
    @Async
    @EventListener(condition = "#user.name!=null")
    public void handleEvent(User user) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(user);
    }
}
