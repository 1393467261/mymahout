package com.hzw.mymahout.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hzw
 * @Time: 2018/4/28 15:17
 * @Description:
 */
public class RedisUtil {

    private static String REDIS_HOST = "localhost";
    private static int REDIS_PORT = 6379;
    public static JedisPool pool = null;

    static{
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(80);
        config.setMaxTotal(128);
        config.setMaxWaitMillis(2001);

        pool = new JedisPool(config, REDIS_HOST, REDIS_PORT);
    }

    public static void saveTopTen(List<Integer> list){
        /**
        *@Description: 将元素插入到列表尾部
        *@param key
        @param strings
        *@return: java.lang.Long list的大小
        */
        Jedis jedis = null;
        try{
            jedis = pool.getResource();
            jedis.del("top_ten");
            for (Integer integer : list) {
                jedis.rpush("top_ten".getBytes(),intToBytes(integer));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    public static List<Integer> getTopTen(){
        /**
        *@Description: 获取热销前十商品id
        *@param
        *@return: java.util.List<java.lang.Integer>
        */
        Jedis jedis = null;
        List<Integer> list = new ArrayList<>();

        try{
            jedis = RedisUtil.pool.getResource();
            List<byte[]> lrange = jedis.lrange("top_ten".getBytes(), 0, -1);
            for (byte[] bytes : lrange) {
                int i = bytesToInt(bytes);
                list.add(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public static byte[] intToBytes(int value){
        /**
        *@Description: int转字节数组
        *@param value
        *@return: byte[]
        */
        byte[] src = new byte[4];
        src[3] =  (byte) ((value>>24) & 0xFF);
        src[2] =  (byte) ((value>>16) & 0xFF);
        src[1] =  (byte) ((value>>8) & 0xFF);
        src[0] =  (byte) (value & 0xFF);
        return src;
    }

    public static int bytesToInt(byte[] src) {

        int value;
        int offset = 0;
        value = (int) ((src[offset] & 0xFF)
                | ((src[offset+1] & 0xFF)<<8)
                | ((src[offset+2] & 0xFF)<<16)
                | ((src[offset+3] & 0xFF)<<24));
        return value;
    }

}
