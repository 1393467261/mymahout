package com.hzw.mymahout;

import com.hzw.mymahout.utils.*;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author: Hzw
 * @Time: 2018/4/27 21:46
 * @Description:
 */
public class Test {

    @org.junit.Test
    public void test(){

        ConnectionFactory factory = RabbitmqUtil.getrabbitmqFactory();
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.basicPublish("", "queue_name", null, "hello".getBytes());

            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void test2(){

        RedisUtil.saveTopTen(JdbcUtil.getTopTenGoods());

        List<Integer> topTen = RedisUtil.getTopTen();
        System.out.println(topTen);
    }

    @org.junit.Test
    public void test3() throws Exception {

        FpTreeUtil fpTreeUtil = new FpTreeUtil();
        Map<String, Integer> frequenctItem = fpTreeUtil.getFrequenctItem();
        for (String s : frequenctItem.keySet()) {
            System.out.println(s + " => " + frequenctItem.get(s));
        }

        Jedis jedis = RedisUtil.pool.getResource();
        jedis.set("map".getBytes(), ObjectTranscoder.serialize(frequenctItem));
    }

    @org.junit.Test
    public void test4(){

        Jedis jedis = RedisUtil.pool.getResource();
        byte[] bytes = jedis.get("map".getBytes());
        Object obj = ObjectTranscoder.deserialize(bytes);
        Map<String, Integer> map = (Map<String, Integer>) obj;

        System.out.println(map);

        for (String key : map.keySet()) {
            System.out.println(key + "  " + map.get(key));
        }
    }

    @org.junit.Test
    public void test5(){

        byte[] src = new byte[4];
        src[3] =  (byte) ((1>>24) & 0xFF);
        src[2] =  (byte) ((1>>16) & 0xFF);
        src[1] =  (byte) ((1>>8) & 0xFF);
        src[0] =  (byte) (1 & 0xFF);
    }
}
