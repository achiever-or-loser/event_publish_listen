# event_publish_listen

# 事件发布

观察者模式；发布-订阅

事件及事件源:对应于观察者模式中的主题。事件源发生某事件是特定事件监听器被触发的原因。

事件发布器可以认为是事件监听的容器，对外提供发布事件和增删事件监听器的接口,维护事件和事件监听器之间的映射关系,并在事件发生时负责通知相关监听器。

事件监听器:对应于观察者模式中的观察者。监听器监听特定事件,并在内部定义了事件发生后的响应逻辑。

事件发布器发布事件后可以继续处理之后的逻辑，事件由监听器去处理，可以认为是异步的，但是监听器默认会按照事件的发布顺序去一个一个处理这些事件。

## Spring框架的支持

发布事件的核心方法ApplicationEventPublisher#ApplicationEventPublisher.publishEvent(Object)

Object 为一个对象时，可以基于注解@EventListener标注实现handleEvent方法监听；Object 为ApplicationEvent实现类时可以绑定一个source对象，使用ApplicationListener#onApplicationEvent方法实现类实现监听需要做的事。这样会监听所有发布事件。

## 异步多播

异步多播是监听器处理的时候以多线程的方式同时去处理多个事件。

SimpleApplicationEventMulticaster中的taskExecutor默认为null，即默认是同步处理监听事件；继承SimpleApplicationEventMulticaster并调用setTaskExecutor(myThreadPool)传入自定义线程池，既可实现异步多播。同时在监听的方法onApplicationEvent或handleEvent方法上加上@Async注解

重点方法SimpleApplicationEventMulticaster#multicastEvent中taskExecutor不为null时即可以以多线程的方式运行

自定义线程池的阻塞队列长度太小的话可能会抛出 Could not bind properties to 'TaskExecutionProperties' : prefix=spring.task.execution......

> [事件机制-onApplicationEvent执行两次](https://www.iteye.com/blog/mahl1990-2403911)

在web项目中如果同时集成了spring和springMVC的话，上下文中会存在两个容器，即spring的applicationContext.xml的父容器和springMVC的applicationContext-mvc.xml的子容器。

在通过applicationContext发送通知的时候，事件会被两个容器发布。容器本身仅仅是对外提供了事件发布的接口,真正的工作其实是委托给了具体容器内部一个`ApplicationEventMulticaster`对象，其默认实现类是
SimpleApplicationEventMulticaster,该组件会在容器启动时被自动创建,并以单例的形式存在,管理了所有的事件监听器,并提供针对所有容器内事件的发布功能。

另外，实现了ApplicationListener#onApplicationEvent方法会监听所有的事件，包括容器启动的时候的事件、使用@EventListener监听的事件

## source code

### 初始化事件发布器

在容器启动的AbstractApplicationContext#refresh()方法中initApplicationEventMulticaster()方法初始化事件发布器；registerListeners()注册一个监听器--将一个监听事件注册到上面的多播器里面去

根据核心容器beanFactory中是否有id为applicationEventMulticaster的bean分两种情况:

- 容器中已有id为applicationEventMulticaster的bean
  直接从容器缓存获取或是创建该bean实例,并交由成员变量applicationEventMulticaster保存。
  当用户自定义了事件发布器并向容器注册时会执行该流程。
- 容器中不存在applicationEventMulticaster的bean
  这是容器默认的执行流程,会创建一个SimpleApplicationEventMulticaster,其仅在实现事件发布器基本功能(管理事件监听器以及发布容器事件)的前提下,增加了可以设置任务执行器Executor和错误处理器ErrorHandler的功能,当设置Executor为线程池时,则会以异步的方式对事件监听器进行回调,而ErrorHandler允许我们在回调方法执行错误时进行自定义处理。默认情况下，这两个变量都为null。

### 注册事件监听器

在容器启动的AbstractApplicationContext#registerListeners()方法中，首先遍历beanFactory中所有的bean,获取所有实现ApplicationListener接口的bean的beanName,并将这些beanName通过addApplicationListenerBean方法注册到ApplicationEventMulticaster中。

addApplicationListenerBean方法中的defaultRetriever是定义在抽象类AbstractApplicationEventMulticaster中的成员,用来保存所有事件监听器及其beanName

### 容器事件发布

跟进publishEvent，核心为getApplicationEventMulticaster().multicastEvent((ApplicationEvent)applicationEvent, eventType);通常这个eventType参数为null,因为事件的类型信息完全可以通过反射的方式从event对象中获得；

首先通过传入的参数或者通过调用resolveDefaultEventType(event)方法获取事件的事件类型信息,之后会通过
getApplicationListeners(event, type)方法得到所有和该事件类型匹配的事件监听器；根据Executor是否设置决定是同步还是异步回调。

getApplicationListeners的流程：根据事件和事件源类型查找缓存(key为事件和事件源类型，value为对应的监听器列表)；

缓存中没有时，根据监听器的泛型实际类型是否和事件类型一样或是其父类型，遍历监听器列表，返回事件类型的监听器列表，同时更新缓存；在根据是否存在任务执行器Executor决定是异步还是同步；

缓存中存在时直接以异步回调监听器



实现demo源码见：[demo 源码](https://github.com/achiever-or-loser/event_publish_listen.git)

参考：https://www.cnblogs.com/takumicx/p/9972461.html