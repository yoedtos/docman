package net.yoedtos.docman.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Constants;
import net.yoedtos.docman.model.Document;
import net.yoedtos.docman.model.Type;
import net.yoedtos.docman.util.FormatConverter;

public class DocumentDAO extends DAO {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDAO.class);
	
	private ResultSet result = null;
	private Connection connection;
	
	protected DocumentDAO(String config) throws AppException {
		super(config);
		try {
			connection = getConnection();
		} catch (SQLException e) {
			throw new AppException("Unable do get a connection");
		}
	}
	
	public Document writeDocument(Document document) throws AppException {
		
		String sql = "INSERT INTO documents" +
				"(file_name, content_type, size, file, title, author, description, tags, type) VALUES (?,?,?,?,?,?,?,?,?)";
		
		
		try(PreparedStatement preparedStatment = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			preparedStatment.setString(1, document.getFileName());
			preparedStatment.setString(2, document.getContentType());
			preparedStatment.setLong(3, document.getSize());
			InputStream stream = FormatConverter.byteToInputStream(document.getFile());
			preparedStatment.setBinaryStream(4, stream);
			preparedStatment.setString(5, document.getTitle());
			preparedStatment.setString(6, document.getAuthor());
			preparedStatment.setString(7, document.getDescription());
			preparedStatment.setString(8, document.getTags());
			preparedStatment.setString(9, document.getType().toString());
			
			int affectedRows = preparedStatment.executeUpdate();
			if(affectedRows == 0) {
				throw new SQLException("Failed to write document");
			}
			
			ResultSet generatedKeys = preparedStatment.getGeneratedKeys();
			if(generatedKeys.next()) {
				document.setId(generatedKeys.getInt(1));
			}
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
		return document;
	}
	
public int updateDocument(Document document) throws AppException {
		int count;
		
		String sql = "UPDATE documents SET title=?, author=?, description=?, tags=?, type=? WHERE id=?";
		
		try(PreparedStatement preparedStatment = connection.prepareStatement(sql)) {
			preparedStatment.setString(1, document.getTitle());
			preparedStatment.setString(2, document.getAuthor());
			preparedStatment.setString(3, document.getDescription());
			preparedStatment.setString(4, document.getTags());
			preparedStatment.setString(5, document.getType().toString());
			preparedStatment.setInt(6, document.getId());
			count = preparedStatment.executeUpdate();

		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
		return count;
	}
	
	public int removeDocument(Integer id) throws AppException {
		String sql = "DELETE FROM documents WHERE id = ?";
		int count;
		
		try(PreparedStatement preparedStatment = connection.prepareStatement(sql)) {
			preparedStatment.setInt(1, id);
			count = preparedStatment.executeUpdate();
			LOGGER.debug("Dropped: " + Integer.toString(count));
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
		return count;
	}
	
	public void commit() throws AppException {
		try {
			connection.commit();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
	}
	
	public void rollBack() throws AppException {
		try {
			connection.rollback();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
	}
	
	public Document loadDocument(Integer id) throws AppException {
		Document document = null;
		
		String sql = "SELECT id, title, description, author, tags, type  FROM documents WHERE id = ?";
		
		try(PreparedStatement preparedStatment = connection.prepareStatement(sql)) {
			preparedStatment.setInt(1, id);
			result = preparedStatment.executeQuery();
			while (result.next()) {
				document = new Document();
				document.setId(result.getInt(1));
				document.setTitle(result.getString(2));
				document.setDescription(result.getString(3));
				document.setAuthor(result.getString(4));
				document.setTags(result.getString(5));
				document.setType(Type.valueOf(result.getString(6)));
			}
			if(document == null) {
				throw new AppException("Document not found");
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
		
		return document;
	}
	
	public List<org.apache.lucene.document.Document> listDocumentToIndex() throws AppException {
		
		List<org.apache.lucene.document.Document> documents = new ArrayList<>();
		org.apache.lucene.document.Document document = null;
		
		String sql = "SELECT id, title, description, author, tags FROM documents";
		
		try(PreparedStatement preparedStatment = connection.prepareStatement(sql)) {
			result = preparedStatment.executeQuery();
			while (result.next()) {
				document = new org.apache.lucene.document.Document();	
				document.add(new StringField(Constants.ID, Integer.toString(result.getInt(1)), Store.YES));
				document.add(new TextField(Constants.TITLE,result.getString(2), Field.Store.YES));
				document.add(new TextField(Constants.DESCRIPTION, result.getString(3), Field.Store.YES));
				document.add(new TextField(Constants.AUTHOR,result.getString(4), Field.Store.YES));
				document.add(new TextField(Constants.TAGS,result.getString(5), Field.Store.YES));
				
				documents.add(document);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
		return documents;
	}
	
	public int countAllDocuments() throws AppException {
		int count = 0;
		try (Statement statement = getStatement()) {
			ResultSet resultSet = statement.executeQuery("SELECT COUNT (id) FROM documents");
			if(resultSet.next()) {
				count = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
		return count;
	}
	
	public List<Document> listDocuments(int size, int offset) throws AppException {
		
		List<Document> documents = new ArrayList<>();
		Document document = null;
		
		String sql = "SELECT id, title, size, author FROM documents LIMIT ? OFFSET ?";
		
		try(PreparedStatement preparedStatment = connection.prepareStatement(sql)) {
			preparedStatment.setInt(1, size);
			preparedStatment.setInt(2, offset);
			result = preparedStatment.executeQuery();
			while (result.next()) {

				document = new Document();
				document.setId(result.getInt(1));
				document.setTitle(result.getString(2));
				document.setSize(result.getLong(3));
				document.setAuthor(result.getString(4));

				documents.add(document);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
		return documents;
	}
	
	public Document readFile(Integer id) throws AppException {
		
		Document document = new Document();
		
		String sql = "SELECT file_name, content_type, file FROM documents WHERE id = ?";
		
		try(PreparedStatement preparedStatment = connection.prepareStatement(sql)) {
			preparedStatment.setInt(1, id);
			result = preparedStatment.executeQuery();
			if (result.next()) {
				document.setFileName(result.getString(1));
				document.setContentType(result.getString(2));
				document.setFile(result.getBytes(3));
			} else {
				throw new AppException("Document not found");
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			throw new AppException("There's a problem in database!", e);
		}
		
		return document;
	}
	
	public void checkTable() throws SQLException {

		if(!hasTable("DOCUMENTS")) {
				createTable();
		}
	}
	
	public void dropTable() throws SQLException {
		String sql = "DROP TABLE documents";
		getStatement().execute(sql);	
	}
	
	private void createTable() throws SQLException  {
		String sql = "CREATE TABLE documents ("
				+ "id IDENTITY NOT NULL PRIMARY KEY,"
				+ "file_name VARCHAR(40) NOT NULL,"
				+ "content_type VARCHAR(40) NOT NULL,"
				+ "size INTEGER,"
				+ "file BLOB NOT NULL,"
				+ "title VARCHAR(50) NOT NULL, "
				+ "author VARCHAR(30) NOT NULL, "
				+ "description VARCHAR(300) NOT NULL,"
				+ "tags VARCHAR(100) NOT NULL, "
				+ "type VARCHAR(20) NOT NULL)";
			getStatement().execute(sql);
	}
	
	
	private Statement getStatement() throws SQLException {
		return connection.createStatement();
	}
}
