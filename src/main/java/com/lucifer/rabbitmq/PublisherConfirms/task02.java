package com.lucifer.rabbitmq.PublisherConfirms;

import com.lucifer.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @Author：丁浩然
 * @Package：com.lucifer.rabbitmq.MessageAcknowledgment
 * @Project：rabbitMq_learning
 * @name：task02
 * @Date：2024/10/18 23:36
 * @Filename：task02
 * @Purpose：生产者
 */
public class task02 {
    //    队列名
    public static final String QUEUE_NAME = "ack_queue2";

    public static void main(String[] args) throws Exception {
       Channel channel = RabbitMqUtils.getChannel();

        //queue持久化
        boolean durable = true;
       //声明一个队列
       channel.queueDeclare(QUEUE_NAME,durable,false,false,null);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            String message = sc.next();
            channel.basicPublish("",QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("发送成功:"+message);
        }
    }
}
