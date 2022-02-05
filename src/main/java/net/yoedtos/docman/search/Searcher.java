package net.yoedtos.docman.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Document;
import net.yoedtos.docman.util.DocumentConverter;

public class Searcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(Searcher.class);
	
	private IndexReader reader;
	private int totalHits;
	
	public List<net.yoedtos.docman.model.Document> searchPhraseByFields(String phrase, String[] fields,int size, int page) throws AppException {
		StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
		MultiFieldQueryParser multiFieldParser = new MultiFieldQueryParser(Version.LUCENE_43, fields, analyzer);
		Query phraseQuery = null;
		
		try {
			phraseQuery = multiFieldParser.parse(phrase);
		} catch (ParseException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("Cannot parse query", e); 
		}
		
		return doSearch(phraseQuery, size, page);
	}
	
	public int getTotalHits() {
		return this.totalHits;
	}
	
	private List<net.yoedtos.docman.model.Document> doSearch(Query query, int size, int page) throws AppException {
		List<net.yoedtos.docman.model.Document> documents = new ArrayList<>();
		
		try {
			IndexSearcher index = createSearcher();
			TopScoreDocCollector collector = TopScoreDocCollector.create(Constants.MAX_RESULT, true);
			int start = (page -1) * size;
			index.search(query, collector);
			TopDocs hits = collector.topDocs(start, size);
			totalHits = hits.totalHits;
			ScoreDoc[] scoreDoc = hits.scoreDocs;
			
			for (int i = 0; i < scoreDoc.length; i++) {
				Document document = DocumentConverter.convertFromLucene(index.doc(scoreDoc[i].doc));
				
				if(document != null) {
					documents.add(document);
				}
			}

			closeSearcher();
		} catch (IOException | AppException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("A problem occur during search", e);
		} 
		
		return documents;
	}
		
	private synchronized void closeSearcher() throws IOException {
		if(reader != null) {
			this.reader.close();
		}
	}
	
	private synchronized IndexSearcher createSearcher() throws AppException {
		Directory directory = null;
		try {
			directory = FSDirectory.open(new File(Constants.INDEX_DIR));
			reader = DirectoryReader.open(directory);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("Failure to create IndexSearcher", e);
		} 
 
		return new IndexSearcher(reader);
	}

}
