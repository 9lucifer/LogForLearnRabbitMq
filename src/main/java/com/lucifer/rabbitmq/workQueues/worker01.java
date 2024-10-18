package com.lucifer.rabbitmq.workQueues;

import com.lucifer.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author：丁浩然
 * @Package：com.lucifer.rabbitmq.workQueues
 * @Project：rabbitMq_learning
 * @name：worker01
 * @Date：2024/10/18 19:04
 * @Filename：worker01
 * @Purpose：工作线程（消费者）
 */
public class worker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("接受到的消息："+message.getBody());
        };
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消息被取消");
        };
        //消息接受
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
