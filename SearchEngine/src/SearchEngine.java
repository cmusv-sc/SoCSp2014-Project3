package SearchEngine;

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

    public static RemoteRegistry getRegistry() throws Exception {
        System.setProperty("javax.net.ssl.trustStore", "src/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType","JKS");
        String REGISTRY_URL = "https://localhost:9443/registry/atom";
        String USER = "admin";
        String PASSWORD = "admin";
        return new RemoteRegistry(REGISTRY_URL, USER, PASSWORD);
    }

    public static String[] getAllPaths(RemoteRegistry registry) throws Exception {
        if (!registry.resourceExists(RegistryConstants.CONFIG_REGISTRY_BASE_PATH + RegistryConstants.QUERIES_COLLECTION_PATH + "/custom-queries")) {
            String sql1 = "SELECT REG_PATH_ID, REG_NAME FROM REG_RESOURCE WHERE REG_DESCRIPTION LIKE?";
            Resource q1 = registry.newResource();
            q1.setContent(sql1);
            q1.setMediaType(RegistryConstants.SQL_QUERY_MEDIA_TYPE);
            q1.addProperty(RegistryConstants.RESULT_TYPE_PROPERTY_NAME, RegistryConstants.RESOURCES_RESULT_TYPE);
            registry.put(RegistryConstants.CONFIG_REGISTRY_BASE_PATH + RegistryConstants.QUERIES_COLLECTION_PATH + "/custom-queries", q1);
        }

        Map parameters = new HashMap();
        if (parameters.isEmpty()) { 
            //            parameters.put("1", "%service%");
        }
        
        Resource result = registry.executeQuery(RegistryConstants.CONFIG_REGISTRY_BASE_PATH + RegistryConstants.QUERIES_COLLECTION_PATH + "/custom-queries", parameters);
        return (String[])result.getContent();
    }

    public static void getStuff() throws Exception {
        RemoteRegistry registry = SearchEngine.getRegistry();
//        if (!registry.resourceExists("/c1/c2")) {
//           Collection collection = registry.newCollection();
//           registry.put("/c1/c2", collection);
//        }
//        
//        if (!registry.resourceExists("/c1/c2/r1")) {
//            Resource r1 = registry.newResource();
//            String r1_str = "My File Content";
//            r1.setContent(r1_str.getBytes());
//            registry.put("/c1/c2/r1", r1);
//        }
//
//        if (!registry.resourceExists("c1/c2/r2")) {
//            Resource r2 = registry.newResource();
//            String r2_str = "My File Content";
//            r2.setContent(r2_str.getBytes());
//            registry.put("/c1/c2/r2", r2);
//        }
//        
//        try {
//            if (registry.getComments("/c1/c2/r1")[0] != null) {
//                Comment c1 = new Comment();
//                c1.setText("This is my comment");
//                registry.addComment("/c1/c2/r1", c1);
//            }
//        }
//        catch (Exception e) {
//        }

        String[] paths = SearchEngine.getAllPaths(registry);

        //        System.out.println(((String[])registry.get(paths[1000]).getContent())[0]);
//        int i = 1000;
//        String path = paths[i];
//        path = path.substring(path.indexOf("/",1), path.length());
//        if (registry.resourceExists(path)) {
//            System.out.println(registry.get(path));
//        }
//        else {
//            System.out.println("Resource doesn't exist " + paths[i] + registry.resourceExists("/c1/pw/api/bitcoin-charts-markets"));
//        }
//        for (int i = 0; i < paths.length; i++) {
//            String path = paths[i];
//            try {
//                path = path.substring(path.indexOf("/",1), path.length());
//                if (registry.resourceExists(path)) {
//                    System.out.println("" + ((String[]) registry.get(path).getContent()).length + path );
//                }
//            }
//            catch (Exception e) {
//                System.out.println("Error " + path + " " + e);
//                continue;
//            }
//        }

        for (int i = 0; i < paths.length; i++) System.out.printf("RESULTS: %s\n\n", paths[i]);

    }

    public static void main(String[] args) throws Exception {
        SearchEngine.getStuff();
    }
}
