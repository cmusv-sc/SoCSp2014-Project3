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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.*;
import java.util.*;

public class DBLPParser {
    final static String XMLName = "dblp_500.xml";

    public static void main(String[] args) throws Exception {
      System.out.printf("\n\nDo you want to create the database? [y/n] ");
      boolean valid_input = false;
      String input = null;
      Scanner scan = new Scanner(System.in);
      while (!valid_input) {
        input = scan.next();
        if (input.equals("y")) {
          valid_input = true;
          buildDB();
        } else if (input.equals("n")) {
          valid_input = true;
        }

        if (!valid_input) {
          System.out.printf("\n\nEnter a 'y' or 'n': ");
        }

      }

      queryDB(scan);
    }

      private static void buildDB() throws Exception {
        String workingDir = System.getProperty("user.dir");
        String filename = workingDir + File.separatorChar + XMLName;
        File xmlFile = new File(filename);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        Document doc = dBuilder.parse(xmlFile);

        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB( "dblp" );
        DBCollection coll = db.getCollection("incollections");

//      BasicDBObject obj = new BasicDBObject("name", "MongoDB").
//          append("type", "database").
//          append("count", 1).
//          append("info", new BasicDBObject("x", 203).append("y", 102));

        NodeList nList = doc.getElementsByTagName("incollection");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                BasicDBObject obj = new BasicDBObject("type", "incollection");
                ArrayList<String> authorList = new ArrayList<String>();
                Element eElement = (Element) nNode;
                NodeList childList = eElement.getChildNodes();
                for (int j = 0; j < childList.getLength(); j++) {
                    Node tempNode = childList.item(j);
                    if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                        if (tempNode.getNodeName() == "author") {
                            authorList.add(tempNode.getTextContent());
                        }
                        else {
                            obj.append(tempNode.getNodeName(), tempNode.getTextContent());
                        }
                        //                  System.out.println(tempNode.getTextContent() + ", " + tempNode.getNodeName());
                    }
                }
                obj.append("author", authorList);
                coll.insert(obj);
            }
        }
        mongoClient.close();
    }

    private static void queryDB(Scanner scan) throws Exception {
      MongoClient mongoClient = new MongoClient();
      DB db = mongoClient.getDB( "dblp" );
      DBCollection coll = db.getCollection("incollections");
      DBCursor cursor = coll.find();
      
      boolean done = false;
      String author = null;
      String query = null;
      while (!done) {
        System.out.printf("\n\nEnter an author ('x' to exit): ");
        author = scan.nextLine();
        if (author.equals("x")) {
          mongoClient.close();
          done = true;
        } else {
          query = "{'author': {'$in': ['" + author + "']}}";
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

    }

}
