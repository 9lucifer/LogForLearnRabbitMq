package com.lucifer.rabbitmq.workQueues;

import com.lucifer.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.sun.scenario.effect.impl.sw.java.JSWColorAdjustPeer;

import java.util.Scanner;

/**
 * @Author：丁浩然
 * @Package：com.lucifer.rabbitmq.workQueues
 * @Project：rabbitMq_learning
 * @name：task01
 * @Date：2024/10/18 19:14
 * @Filename：task01
 * @Purpose：生产者,产生大量的消息
 */
public class task01 {
    public static final String QUEUE_NAME = "hello";



    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //控制台接受代码
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String message = sc.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息："+message);
        }
    }
}
