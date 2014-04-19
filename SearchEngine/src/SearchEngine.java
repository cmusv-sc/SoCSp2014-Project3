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

//It begins
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.jdbc.handlers.RequestContext;
import org.wso2.carbon.registry.core.*;
import org.wso2.carbon.registry.app.RemoteRegistry;

import org.wso2.carbon.registry.core.jdbc.handlers.Handler;


import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.util.Iterator;

public class SearchEngine {
    final static String DB_NAME = "PW_2012_09_02_09_03_35";
    final static String API_COL = "apis";

    public static void main(String[] args) throws Exception {
        String REGISTRY_URL = "https://localhost:9443/registry/atom";
        String USER = "admin";
        String PASSWORD = "admin";
        RemoteRegistry registry = new RemoteRegistry(REGISTRY_URL, USER, PASSWORD);
        Collection collection = registry.newCollection();
        registry.put("/c1/c2", collection);
//        if (args.length != 2) {
//            System.out.printf("\n\nUsage:\nSearchEngine <key> <value>\n");
//        }
//        else {
//            String key = args[0];
//            String value = args[1];
//            searchDB(key, value);
//        }
        
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
