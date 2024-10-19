package com.lucifer.rabbitmq.Switch.fanout;

import com.lucifer.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @Author：丁浩然
 * @Package：com.lucifer.rabbitmq.Switch.fanout
 * @Project：rabbitMq_learning
 * @name：ReceiveLog
 * @Date：2024/10/19 18:00
 * @Filename：ReceiveLog
 * @Purpose：消息接受01
 */
public class ReceiveLog01 {
//    交换机的名字
    public static final String exchange_name = "logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(exchange_name,"fanout");

        //声明一个队列
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机和队列
        channel.queueBind(queueName,exchange_name,"");
        System.out.println("01接受到的消息：");
        DeliverCallback deliverCallback = (consumerTag,message)->{
            System.out.println("接收到的消息："+new String(message.getBody(),"UTF-8"));
        };
        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});
        


    }

}
