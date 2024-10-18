package com.lucifer.rabbitmq.common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author：丁浩然
 * @Package：com.lucifer.rabbitmq.one
 * @Project：rabbitMq_learning
 * @name：producer
 * @Date：2024/10/18 18:12
 * @Filename：producer
 */
public class producer {

//    队列名
    public static final String QUEUE_NAME = "hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip
        factory.setHost("1.94.97.29");
        //设置用户名和密码
        factory.setUsername("guest");
        factory.setPassword("123456");

        //创建连接
        Connection connection =  factory.newConnection();

        //获取信道
        Channel channel = connection.createChannel();

        //生成一个队列
        /**
         * 1.队列名称
         * 2.队列内的消息是否要持久化
         * 3.该队列是否只供一个消费者消费
         * 4.是否自动删除（消费者断链之后）
         * 5.其他参数
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "hello word";
        /**
         * 1.发送到哪个交换机
         * 2.路由的key是啥
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());

    }
}
