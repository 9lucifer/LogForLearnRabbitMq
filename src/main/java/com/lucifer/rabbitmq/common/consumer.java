package com.lucifer.rabbitmq.common;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author：丁浩然
 * @Package：com.lucifer.rabbitmq.one
 * @Project：rabbitMq_learning
 * @name：consumer
 * @Date：2024/10/18 18:26
 * @Filename：consumer
 * 用于接受消息
 */
public class consumer {
    //    队列名
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("1.94.97.29");
        factory.setUsername("guest");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //声明
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println(new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消费消息被中断");
        };
        /**
         * 消费者消费消息
         * 1.消费对象
         * 2.消费成功后是否自动应答
         * 3.消费者没有成功消费的回调内容
         * 4.消费者取消消费的回调
         * 5.
         */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
