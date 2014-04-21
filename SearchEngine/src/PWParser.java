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

public class PWParser {

    public static void main(String[] args) throws Exception {
        System.setProperty("javax.net.ssl.trustStore", "src/wso2carbon.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        System.setProperty("javax.net.ssl.trustStoreType","JKS");
        String REGISTRY_URL = "https://localhost:9443/registry/atom";
        String USER = "admin";
        String PASSWORD = "admin";
        RemoteRegistry registry = new RemoteRegistry(REGISTRY_URL, USER, PASSWORD);
        Scanner stdin = new Scanner(System.in);
        String line = "";
        while(stdin.hasNextLine()) {
            line = stdin.nextLine();
            String[] tokens = line.split(";;");
            System.out.println(tokens[0]);
            if (!registry.resourceExists("/c1/pw")) {
                Collection collection = registry.newCollection();
                registry.put("/c1/pw", collection);
            }
        
            if (!registry.resourceExists("/c1/pw" + tokens[0])) {
                Resource r1 = registry.newResource();
                r1.setContent(line.getBytes());
                registry.put("/c1/pw" + tokens[0], r1);
            }
        }
//        if (!registry.resourceExists("/c1/pw")) {
//            System.out.println("Resource Exists");
//           Collection collection = registry.newCollection();
//           registry.put("/c1/pw", collection);
//        }
//        
//        if (!registry.resourceExists("/c1/pw/r1")) {
//            System.out.println("Set r1");
//            Resource r1 = registry.newResource();
//            String r1_str = "My File Content";
//            r1.setContent(r1_str.getBytes());
//            registry.put("/c1/pw/r1", r1);
//        }

        
//        try {
//            if (registry.getComments("/c1/c2/r1")[0] != null) {
//                Comment c1 = new Comment();
//                c1.setText("This is my comment");
//                registry.addComment("/c1/c2/r1", c1);
//            }
//        }
//        catch (Exception e) {
//        }
    }
}
