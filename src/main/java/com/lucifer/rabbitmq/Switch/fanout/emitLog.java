package com.lucifer.rabbitmq.Switch.fanout;

import com.lucifer.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @Author：丁浩然
 * @Package：com.lucifer.rabbitmq.Switch.fanout
 * @Project：rabbitMq_learning
 * @name：emitlOG
 * @Date：2024/10/19 19:50
 * @Filename：emitlOG
 * @Purpose：发送方，给交换机
 */
public class emitLog {
    //    交换机的名字
    public static final String exchange_name = "logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();
        channel.exchangeDeclare(exchange_name,"fanout");

        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            String message = sc.next();
            channel.basicPublish(exchange_name,"",null,message.getBytes());
            System.out.println("生产者发送消息："+message);
        }



    }
}
