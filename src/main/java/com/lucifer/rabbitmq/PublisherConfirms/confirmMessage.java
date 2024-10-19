package com.lucifer.rabbitmq.PublisherConfirms;

import com.lucifer.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import javax.smartcardio.ATR;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @Author：丁浩然
 * @Package：com.lucifer.rabbitmq.PublisherConfirms
 * @Project：rabbitMq_learning
 * @name：confirmMessage
 * @Date：2024/10/19 13:57
 * @Filename：confirmMessage
 * @Purpose：用于验证发布确认模式
 * 1.单个
 * 2.批量
 * 3.异步批量
 * 比较三者的用时
 */
public class confirmMessage {
    //批量发消息的个数
    public static final int message_count = 1000;

    public static void main(String[] args) throws Exception {
//         * 1.单个
//        publishMessageIndividually();
//        publishMessageBatch();
//         * 2.批量
        publishMessageAsync();
//         * 3.异步批量
    }
    //单个
    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queue_name = UUID.randomUUID().toString();
        channel.queueDeclare(queue_name,false,false,false,null);

        //开始时间
        long begin = System.currentTimeMillis();
        channel.confirmSelect();

        for (int i = 0; i < message_count; i++) {
            String message = i+"";
            channel.basicPublish("",queue_name,null,message.getBytes());

            //单个消息马上确认
            boolean flag = channel.waitForConfirms();
            if(flag == true){
                System.out.println("消息发送成功");
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布"+message_count+"条用时："+(end - begin)+"mills");
    }

    //批量发布确认
    public static void publishMessageBatch() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queue_name = UUID.randomUUID().toString();
        channel.queueDeclare(queue_name,false,false,false,null);
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认的长度
        int basicSize = 100;
        for (int i = 1; i <= message_count; i++) {
            String message = i+"";
            channel.basicPublish("",queue_name,null,message.getBytes());
            if(i%basicSize == 0){
                System.out.println("消息发送成功");
                channel.confirmSelect();
            }
        }


        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量发布"+message_count+"条用时："+(end - begin)+"mills");
    }


    //异步发布确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();
        String queue_name = UUID.randomUUID().toString();
        channel.queueDeclare(queue_name,false,false,false,null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的哈希表 适用于高并发
         * 1、将序号和消息关联
         * 2、轻松删除条目，主要给到序号
         * 3、支持高并发
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        //开始时间
        long begin = System.currentTimeMillis();
        //消息确认成功的回调
        ConfirmCallback ackCallBack = (deliveryTag, multiple)->{
            //2:删除确认的消息
            if(multiple){
                ConcurrentNavigableMap<Long,String> confirmed =
                        outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }

            System.out.println("确认消息："+deliveryTag);
        };
        /**
         * 1、消息类型
         * 2、是否批量
         */
        ConfirmCallback NackCallBack = (deliveryTag, multiple)->{
            //3:打印未确认的
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("发布的消息"+message+"未被确认，序列号"+deliveryTag);

        };
        //准备消息的监听器
        //1.监听成功的
        //2.监听失败的
        channel.addConfirmListener(ackCallBack,NackCallBack);

        for (int i = 1; i <= message_count; i++) {
            String message = i+"";
            channel.basicPublish("",queue_name,null,message.getBytes());

            //1:此处记录所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
        }


        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("批量发布"+message_count+"条用时："+(end - begin)+"mills");
    }

}
