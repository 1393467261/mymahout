package com.hzw.mymahout.utils;

import com.hzw.mymahout.entity.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Hzw
 * @Time: 2018/4/21 15:44
 * @Description:
 */
public class JdbcUtil {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://39.105.59.8:3306/mfshop?useUnicode=true&characterEncoding=utf-8";
    static final String MY_DB_URL = "jdbc:mysql://112.74.36.19:3306/mf?useUnicode=true&characterEncoding=utf-8";
    static final String USER = "root";
    static final String PASS = "";

    public static Connection getConnection(){

        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static Connection getMyConnection(){

        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(MY_DB_URL, "admin", "admin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void close(ResultSet rs, PreparedStatement ps, Connection con) {

        if (rs != null && ps != null && con != null) {
            try {
                rs.close();
                ps.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void close(Connection connection, PreparedStatement ps) {

        if (connection != null && ps != null) {
            try {
                ps.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void log(String event){

        Connection connection = null;
        PreparedStatement ps = null;
        String sql = "insert into log(event) values(?)";

        try{
            connection = getMyConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, event);
            ps.execute();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(connection, ps);
        }
    }

    public static ResultSet getShoppingCart(){
        /**
        *@Description: 以订单为单位查询一个订单下的所有物品id
        *@param
        *@return: java.sql.ResultSet
        */
        Connection connection = null;
        PreparedStatement ps = null;
        String sql = "select GROUP_CONCAT(goods_id) as ids " +
                        "from mf_order_goods " +
                            "GROUP BY order_id";

        try{
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            return ps.executeQuery();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static void messageToDataBase(Message message){
        /**
        *@Author: Hzw
        *@Description: 处理message中的数据，写入数据库，紧急事件，打折事件不需要写入数据库
        *@param message
        *@return: void
        */
        Connection connection = getConnection();
        String messageType = message.getMessageType();

        switch (messageType){
            case "click":
                //TODO 根据用户id和商品id获取对应的评分，根据页面停留时间加上相应的分数
                break;

            case "order":
                break;

            case "pay":
                break;
        }
    }

    public static List<Integer> getLongTimeNoOrderUsers(Integer lastOrder, Integer interval){
        /**
        *@Description: 作用是及时召回快要流失的客户
        *@param lastOrder 查找该天数内下过单的用户
        @param interval 间隔了多久没下单的用户
        *@return: java.util.List<java.lang.Integer>
        */
        List<Integer> userIdList = new ArrayList<>();
        int seconds = 86400;
        Long i = Long.valueOf(String.valueOf(System.currentTimeMillis()).substring(0, 10));
        Long t1 = i - lastOrder*seconds;
        Long t2 = i - interval*seconds;
        String sql = "select max(pay_time) as time, user_id " +
                        "from mf_order_info " +
                            "where pay_status = 2 " +
                                "GROUP BY user_id " +
                                    "having time > ? and time < ?";
        Connection connection = null;

        try{
            connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, t1);
            ps.setLong(2, t2);
            ResultSet set = ps.executeQuery();
            while (set.next()){
                userIdList.add(set.getInt("user_id"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return userIdList;
    }

    public static List<Integer> getTopTenGoods(){
        /**
        *@Description: 2017年后销量前十的商品id,
        *@param
        *@return: java.util.List<java.lang.Integer>
        */
        List<Integer> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet set = null;
        String sql = "select sum(g.goods_number) as count, g.goods_name, g.goods_id " +
                        "from mf_order_goods g, mf_order_info i " +
                            "where i.pay_time > 1483203661 and i.order_id = g.order_id and goods_id != 286 " +
                                "GROUP BY goods_id " +
                                    "order by count desc " +
                                        "limit 0, 10";

        try{
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            set = ps.executeQuery();
            while (set.next()){
                list.add(set.getInt("goods_id"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(set, ps, connection);
        }

        return list;
    }

    public static void saveTopTenGoods(List<Integer> list){
        /**
        *@Description: 将商品存入top10数据库
        *@param list
        *@return: void
        */
        Connection connection = null;
        PreparedStatement ps = null;

        try{
            connection = getMyConnection();
            ps = connection.prepareStatement("truncate top_ten");
            ps.execute();

            ps = connection.prepareStatement("insert into top_ten(goods_id) values(?)");
            for (Integer integer : list) {
                ps.setInt(1, integer);
                ps.executeUpdate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(connection, ps);
        }
    }

    public static void crontab(){
        //top10，fptree计算关联商品,存入redis
    }
}
