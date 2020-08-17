package com.event.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {

    @Override
    @Async
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println(Thread.currentThread().getName());
        System.out.println("MyApplicationListener onApplicationEvent");
        if (!Objects.isNull(applicationEvent.getSource())) {
            System.out.println(applicationEvent.getSource().toString());
        }else {
            System.out.println("null");
        }

    }
}
