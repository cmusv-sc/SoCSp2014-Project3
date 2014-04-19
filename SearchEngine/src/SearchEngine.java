import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", "src/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType","JKS");
        String REGISTRY_URL = "https://localhost:9443/registry/atom";
        String USER = "admin";
        String PASSWORD = "admin";
        RemoteRegistry registry = new RemoteRegistry(REGISTRY_URL, USER, PASSWORD);
        Collection collection = registry.newCollection();
        //registry.put("/c1/c2", collection);
        
        Resource r1 = registry.newResource();
        String r1_str = "My File Content";
        r1.setContent(r1_str.getBytes());
        //registry.put("/c1/c2/r1", r1);

        Resource r2 = registry.newResource();
        String r2_str = "My File Content";
        r2.setContent(r2_str.getBytes());
        //registry.put("/c1/c2/r2", r2);

        Comment c1 = new Comment();
        c1.setText("This is my comment");
        //registry.addComment("/c1/c2/r1", c1);
        System.out.println(registry.getComments("/c1/c2/r1")[0].getText());

        //registry.searchContent("Content");


        String sql1 = "SELECT REG_PATH_ID, REG_NAME FROM REG_RESOURCE";
        Resource q2 = registry.newResource();
        q2.setContent(sql1);
        q2.setMediaType(RegistryConstants.SQL_QUERY_MEDIA_TYPE);
        q2.addProperty(RegistryConstants.RESULT_TYPE_PROPERTY_NAME, RegistryConstants.RESOURCES_RESULT_TYPE);
        //registry.put(RegistryConstants.CONFIG_REGISTRY_BASE_PATH + RegistryConstants.QUERIES_COLLECTION_PATH + "/custom-queries", q2);
        Map parameters = new HashMap();
        //parameters.put("1", "%service%");
        Resource result = registry.executeQuery(RegistryConstants.CONFIG_REGISTRY_BASE_PATH + RegistryConstants.QUERIES_COLLECTION_PATH + "/custom-queries", parameters);

        String[] paths = (String[])result.getContent();

        for (int i = 0; i < paths.length; i++) System.out.printf("RESULTS: %s\n\n", paths[i]);








//        String line;
//        Scanner stdin = new Scanner(System.in);
        
//        if (args.length != 2) {
//            System.out.printf("\n\nUsage:\nSearchEngine <key> <value>\n");
//        }
//        else {
//            String key = args[0];
//            String value = args[1];
//            searchDB(key, value);
//        }
        
    }

}
