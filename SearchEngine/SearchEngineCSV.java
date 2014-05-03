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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.*;

public class SearchEngineCSV {
    public static void main(String[] args) throws IOException, ParseException {
			// 0. Specify the analyzer for tokenizing text.
			//    The same analyzer should be used for indexing and searching
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

			// 1. create the index
			Directory index = new RAMDirectory();

			IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);

      IndexWriter w = new IndexWriter(index, config);
      String workingDir = System.getProperty("user.dir");
      String filename = workingDir + File.separatorChar + "python_src/PW.csv";
      System.out.printf("FILENAME: %s\n", filename);
      //System.out.println(workingDir + File.separatorChar + "pw.csv");
      addDoc(w, filename);
      w.close();

			// 2. query
      String field = "tags";
      String querystr = "tag1";
      if (args.length == 1) {
        querystr = args[0];
      } else if (args.length == 2) {
        field = args[0];
        querystr = args[1];
      }
      System.out.println("QUERY: " + field + " = " + querystr);
			Query q = new QueryParser(Version.LUCENE_40, field, analyzer).parse(querystr);

			// 3. search
			int hitsPerPage = 10;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			// 4. display results
			System.out.println("Found " + hits.length + " hits.");
			for(int i=0;i<hits.length;++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					System.out.println((i + 1) + ". NAME: " + d.get("name") + "\tDESCRIPTION" + d.get("description") + "\tRATING: " + d.get("rating") + "\tTAGS: " + d.get("tags") + "\tTIMESTAMP: " + d.get("timestamp"));
			}

			// reader can only be closed when there
			// is no need to access the documents any more.
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
}
