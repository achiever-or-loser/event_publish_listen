package com.event.event;

import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Description:
 * @Author: csc
 * @Create: 2020-08-14 11:22
 * @Version: 1.0
 */
@Component(value = "applicationEventMulticaster")
public class MySimpleApplicationEventMulticaster extends SimpleApplicationEventMulticaster {

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,4,10,
            TimeUnit.SECONDS,new LinkedBlockingDeque<>());

    public MySimpleApplicationEventMulticaster () {
        setTaskExecutor(threadPoolExecutor);
//        setTaskExecutor(Executors.newFixedThreadPool(8));
    }
}
