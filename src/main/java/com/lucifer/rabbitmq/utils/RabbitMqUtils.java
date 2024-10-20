package com.lucifer.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 连接工厂创建信道的工具类
 */
public class RabbitMqUtils {
    //得到一个连接的 channel
    public static Channel getChannel() throws Exception{
       //创建一个连接工厂
       ConnectionFactory factory = new ConnectionFactory();
       factory.setHost("1.94.97.29");
       factory.setUsername("guest");
       factory.setPassword("123456");
       Connection connection = factory.newConnection();
       Channel channel = connection.createChannel();
       return channel;
    }
}