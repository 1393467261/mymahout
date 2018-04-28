package com.hzw.mymahout.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Author: Hzw
 * @Time: 2018/4/28 10:24
 * @Description:
 */
public class DataUtil {

    public static void collectDataAndSaveToFile(){
        /**
        *@Description: 收集每个订单下的商品，每个订单为一条记录
        *@param
        *@return: void
        */
        String sql = "select GROUP_CONCAT(goods_id) " +
                        "from mf_order_goods " +
                            "group by order_id";
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet set = null;

        try{
            connection = JdbcUtil.getConnection();
            ps = connection.prepareStatement(sql);
            set = ps.executeQuery();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
