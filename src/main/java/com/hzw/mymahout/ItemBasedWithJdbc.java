package com.hzw.mymahout;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import java.util.List;

/**
 * @Author: Hzw
 * @Time: 2018/4/21 11:53
 * @Description: 分析数据库中的数据，推荐相应的商品
 */
public class ItemBasedWithJdbc {

    public static void main(String[] args) throws TasteException {

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("112.74.36.19");
        dataSource.setDatabaseName("wx");
        dataSource.setUser("admin");
        dataSource.setPassword("admin");

        JDBCDataModel model = new MySQLJDBCDataModel(dataSource, "preferences",
                "user_id", "item_id", "preference", "time");

        ReloadFromJDBCDataModel dataModel = new ReloadFromJDBCDataModel(model);
        ItemSimilarity similarity = new EuclideanDistanceSimilarity(dataModel);
        Recommender r = new GenericItemBasedRecommender(dataModel, similarity);

        List<RecommendedItem> recommendedItemList = r.recommend(1, 1);
        for (RecommendedItem recommendedItem : recommendedItemList) {
            System.out.println(recommendedItem.getItemID());
        }
    }
}