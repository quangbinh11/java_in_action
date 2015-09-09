/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.Enumeration;
import java.util.Properties;
import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.hive.HiveContext;

/**
 *
 * @author lqbinh
 */
public class SparkHiveApp {

    public static void main(String[] args) {
        // sc is an existing JavaSparkContext.
        JavaSparkContext sc = new JavaSparkContext(new SparkConf().setAppName("tes").setMaster("local"));
        
        HiveContext sqlContext = new org.apache.spark.sql.hive.HiveContext(sc.sc());
//        sqlContext.hiveconf().set("hive.metastore.warehouse.dir", "hdfs:///user/hive/warehouse");
//        sqlContext.hiveconf().set("hadoop.tmp.dir", "/data");
        
        Enumeration e = sqlContext.hiveconf().getAllProperties().keys();
        
        
        while (e.hasMoreElements()) {
            Object nextElement = e.nextElement();
            
            System.out.println(String.format("Key : %s - %s", nextElement.toString(), sqlContext.hiveconf().getAllProperties().get(nextElement)));
            
        }
        
        
        sqlContext.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)");
        sqlContext.sql("LOAD DATA LOCAL INPATH 'file:/home/lqbinh/tmp/kv1.txt' INTO TABLE src");
       Row[] results = sqlContext.sql("FROM src SELECT key, value").collect();
       
        for (Row result : results) {
            System.out.println(result.toString());
        }
        
    }
}
