package com.event.event;

import com.event.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MyEventPublish {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Async
    public void register() {
//        publisher.publishEvent(new User("name", 1));

        for (int i = 0; i < 10; i++) {
            User user = new User("name", i);
            System.out.println("--start");
            publisher.publishEvent(new MyApplicationEvent(user));
            System.out.println("---end");
        }

//        for (int i = 10; i < 20; i++) {
//            User user = new User("name", i);
//            System.out.println("--start");
//            publisher.publishEvent(user);
//            System.out.println("---end");
//        }

//        MyApplicationContext.pushEvent(new MyApplicationEvent(user));
    }
}
