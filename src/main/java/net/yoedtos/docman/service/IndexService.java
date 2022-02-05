package net.yoedtos.docman.service;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.TermQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.dao.DAOFactory;
import net.yoedtos.docman.dao.DocumentDAO;
import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Constants;
import net.yoedtos.docman.search.Indexer;
import net.yoedtos.docman.util.DocumentConverter;

public class IndexService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexService.class);
	
	private Indexer indexer;
	private IndexWriter writer;
	
	public IndexService() {
		indexer = new Indexer();
	}
	
	public void createIndex() throws AppException {
		indexer.createIndex();
	}
	
	public void reCreateIndexFromDB() throws AppException {
		long startTime = System.currentTimeMillis();
		
		DocumentDAO documentDAO = DAOFactory.getInstance().getDocumentDAO(Constants.DB_CONFIG);
		
		try {
			writer = indexer.getIndexWriter();
			try {
				List<Document> documents = documentDAO.listDocumentToIndex();
				
				for (Document document : documents) {
					writer.addDocument(document);
					LOGGER.info("Indexing " + document.get("id"));
				}
				LOGGER.info(String.format("Total indexed: %d", writer.numDocs()));
				LOGGER.info(String.format("Time elapsed: %d ms", System.currentTimeMillis() - startTime));
				indexer.closeWriter();
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
				writer.rollback();
				throw new AppException("Failed to recreate index from database!", e);
			}
		} catch (IOException e) {
			throw new AppException("IndexWriter failure!", e);
		}
	}
	
	public void addDocumentToIndex(net.yoedtos.docman.model.Document document) throws AppException {
		writer = indexer.getIndexWriter();
		
		try {
			writer.addDocument(DocumentConverter.convertToLucene(document));
			LOGGER.info("Indexed: ID="+ document.getId());
			LOGGER.info(String.format("Total indexed: %d", writer.numDocs()));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new AppException(e);
		}
	}
	
	public void updateDocumentToIndex(net.yoedtos.docman.model.Document document) throws AppException {
		writer = indexer.getIndexWriter();
		
		try {
			Term term = new Term(Constants.ID, Long.toString(document.getId())); 
			writer.updateDocument(term, DocumentConverter.convertToLucene(document));
			LOGGER.info("Index update: ID="+ document.getId());
			LOGGER.info(String.format("Total indexed: %d", writer.numDocs()));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new AppException(e);
		}	
	}
	
	public void deleteDocumentFromIndex(Integer id) throws AppException {
		writer = indexer.getIndexWriter();
		
		try {
			TermQuery termQuery = new TermQuery(new Term(Constants.ID, Integer.toString(id)));
			writer.deleteDocuments(termQuery);
			LOGGER.info("Removed from index: ID="+ id);
			LOGGER.info(String.format("Total indexed: %d", writer.numDocs()));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw new AppException(e);
		}	
	}
	
	public void commit() throws AppException {
		try {
			writer.commit();
			indexer.closeWriter();
		} catch (IOException e) {
			throw new AppException("IndexWriter failure!", e);
		}
	}
	
	public void rollBack() throws AppException {
		try {
			writer.rollback();
			indexer.closeWriter();
		} catch (IOException e) {
			throw new AppException("IndexWriter failure!", e);
		}
	}
}	

