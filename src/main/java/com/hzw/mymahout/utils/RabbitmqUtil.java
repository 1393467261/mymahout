package com.hzw.mymahout.utils;

import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author: Hzw
 * @Time: 2018/4/21 16:07
 * @Description:
 */
public class RabbitmqUtil {

    public static ConnectionFactory getrabbitmqFactory(){

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("192.168.31.2");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);
        factory.setVirtualHost("/guest");

        return factory;
    }
}
