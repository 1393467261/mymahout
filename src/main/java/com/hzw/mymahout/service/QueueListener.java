package com.hzw.mymahout.service;

import com.hzw.mymahout.utils.RabbitmqUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @Author: Hzw
 * @Time: 2018/4/21 16:03
 * @Description: 监听队列，使用线程池管理线程
 */
public class QueueListener {

    public static void main(String[] args){

        ConnectionFactory connectionFactory = RabbitmqUtil.getrabbitmqFactory();

        Thread thread1 = new Thread(new MyListenerThread(connectionFactory));
        thread1.start();

        Thread thread2 = new Thread(new MyListenerThread(connectionFactory));
        thread2.start();
    }

    private static class MyListenerThread implements Runnable{

        private ConnectionFactory connectionFactory;

        public MyListenerThread(ConnectionFactory connectionFactory){
            this.connectionFactory = connectionFactory;
        }

        @Override
        public void run() {
            try {
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
                channel.basicConsume("queue_name", true, new DefaultConsumer(channel){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        //TODO 监听队列，判断消息类型，处理消息
                        System.out.println(new String(body));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
