import com.mongodb.util.JSON;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.io.*;
import java.util.*;

public class SearchEngine {
    final static String DB_NAME = "PW_2012_09_02_09_03_35";
    final static String API_COL = "apis";

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.printf("\n\nUsage:\nSearchEngine <key> <value>\n");
        }
        else {
            String key = args[0];
            String value = args[1];
            searchDB(key, value);
        }
    }

    private static void searchDB(String key, String value) throws Exception {
        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB(DB_NAME);
        DBCollection coll = db.getCollection(API_COL);
        DBCursor cursor = coll.find();
        String query = "{'" + key + "': {'$in': ['" + value + "']}}";
        DBObject queryObj = (DBObject) JSON.parse(query);
        cursor = coll.find(queryObj);

        try {
            while(cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }
}
