package com.lucifer.rabbitmq.PublisherConfirms;

import com.lucifer.rabbitmq.utils.RabbitMqUtils;
import com.lucifer.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author：丁浩然
 * @Package：com.lucifer.rabbitmq.MessageAcknowledgment
 * @Project：rabbitMq_learning
 * @name：consumer1
 * @Date：2024/10/18 23:39
 * @Filename：consumer1
 * @Purpose：消费者，手动应答
 */
public class consumer2 {
    //    队列名
    public static final String QUEUE_NAME = "ack_queue2";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("C2等待接受消息，时间短");

        DeliverCallback deliverCallback = (consumerTag,message)->{
            //sleep 1s
            SleepUtils.sleep(1);
            System.out.println("接受到的消息："+new String(message.getBody(),"UTF-8"));
            /**
             * 手动应答
             * 1.消息的标记tag
             * 2.是否批量应答
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //不公平分发
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        boolean autoAck = false;

        channel.basicConsume(QUEUE_NAME,autoAck,deliverCallback,(consumerTag->{
            System.out.println(consumerTag+"消费者取消接口回调");
        }));

    }
}
