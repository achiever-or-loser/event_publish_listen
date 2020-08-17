package com.event.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

@Component
public class MyApplicationContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static void pushEvent(ApplicationEvent applicationEvent){
        //获取父容器发送事件
//        ContextLoader.getCurrentWebApplicationContext().publishEvent(applicationEvent);
        applicationContext.publishEvent(applicationEvent);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
