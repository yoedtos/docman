package net.yoedtos.docman.search;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.exception.AppException;

public class Indexer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Indexer.class);
	
	private IndexWriter writer;
		
	public IndexWriter createWriter() throws AppException {
		
		Directory directory = null;
		try {
			directory = FSDirectory.open(new File(Constants.INDEX_DIR));
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
			IndexWriterConfig configuration = new IndexWriterConfig(Version.LUCENE_43, analyzer);
			configuration.setOpenMode(IndexWriterConfig.OpenMode.APPEND);
			this.writer = new IndexWriter(directory, configuration);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("Failure to create IndexWriter", e);
		} 
		return writer;
	}
	
	public void createIndex() throws AppException {
		boolean hasIndexDirectory = false;
		File indexDirectory = new File(Constants.INDEX_DIR);
		
		if(indexDirectory.exists() && indexDirectory.isDirectory()) {
			hasIndexDirectory = true;
		}
		
		try {
			Directory directory = FSDirectory.open(indexDirectory);
			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);
			IndexWriterConfig configuration = new IndexWriterConfig(Version.LUCENE_43, analyzer);
			
			if(!hasIndexDirectory) {
				configuration.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
				LOGGER.info("Index Directory is ready to use");
			}
			
			IndexWriter writer = new IndexWriter(directory, configuration);
			writer.commit();
			writer.close(true);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("Failure to create index", e);
		} 
	}
	
	public synchronized IndexWriter getIndexWriter() throws AppException {
		if(this.writer == null) {
			createWriter();
		}
		return this.writer;
	}
	
	public synchronized void closeWriter() throws IOException {
		if(this.writer != null) {
			this.writer.close();
		}
	}
}
