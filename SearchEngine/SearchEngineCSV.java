import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.*;

public class SearchEngineCSV {
     StandardAnalyzer analyzer;
     Directory index;
     IndexWriterConfig config;
     IndexWriter w;
     String filename;
     IndexReader reader;
		 IndexSearcher searcher;
      

     public SearchEngineCSV(String filename) throws IOException {
       analyzer = new StandardAnalyzer(Version.LUCENE_40);
       index = new RAMDirectory();
       config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
       w = new IndexWriter(index, config);
       this.filename = filename;
       System.out.printf("FILENAME: %s\n", filename);
       addDoc(w, filename);
       w.close();
       
    }
    
    private void searchField(String field, String value) throws IOException, ParseException { 
       System.out.println("QUERY: " + field + " = " + value);
       Query q = new QueryParser(Version.LUCENE_40, field, analyzer).parse(value);
       searchify(q);
    }

    private void searchAllFields(String value) throws IOException, ParseException { 
      System.out.println("QUERY: " + value);
      MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_40, new String[]{"name", "description", "tags"}, analyzer);
      Query q = queryParser.parse(value);
      searchify(q);
       
    }

    private void searchify(Query q) throws IOException, ParseException {
      reader = DirectoryReader.open(index);
      searcher = new IndexSearcher(reader);
      ScoreDoc[] hits = getHits(q);
      printResults(hits);
      reader.close();

    }

 
    private static void addDoc(IndexWriter w, String file) throws IOException {
      BufferedReader br = new BufferedReader(new FileReader(file));  
      String line = null;  
      while ((line = br.readLine()) != null) {
			    Document doc = new Document();
          String[] fields = line.split(";;");
          doc.add(new TextField("name", fields[0], Field.Store.YES));
          doc.add(new TextField("description", fields[1], Field.Store.YES));
          doc.add(new TextField("rating", fields[2], Field.Store.YES));
          doc.add(new TextField("tags", fields[3], Field.Store.YES));
          doc.add(new TextField("timestamp", fields[4], Field.Store.YES));
			    w.addDocument(doc);
      }
    }
  
    private Query buildQuery (String field, String value) throws ParseException {
      System.out.println("QUERY: " + field + " = " + value);
			return new QueryParser(Version.LUCENE_40, field, analyzer).parse(value);
    
    }

    private ScoreDoc[] getHits(Query q) throws IOException {
			int hitsPerPage = 10;
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(q, collector);
			return collector.topDocs().scoreDocs;
    
    }

    private void printResults(ScoreDoc[] hits) throws IOException {
      System.out.println("Found " + hits.length + " hits.");
			for(int i=0;i<hits.length;++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
          System.out.println();
					System.out.println((i + 1) + ". NAME: " + d.get("name")); 
          System.out.println("\tDESCRIPTION: " + d.get("description"));
          System.out.println("\tRATING: " + d.get("rating"));
          System.out.println("\tTAGS: " + d.get("tags"));
          System.out.println("\tTIMESTAMP: " + d.get("timestamp"));
			}
    
    }

    public static void main(String[] args) throws IOException, ParseException {
      String workingDir = System.getProperty("user.dir");
      String filename = workingDir + File.separatorChar + "python_src/PW.csv";

      SearchEngineCSV searchifier = new SearchEngineCSV(filename);


      if (args.length == 0) {
        System.out.println("ERROR: Must provide a value or a field and value to search for.");
      
      } else if (args.length == 1) {
        searchifier.searchAllFields(args[0]);

      } else {
        searchifier.searchField(args[0], args[1]);

      }

    }

}
