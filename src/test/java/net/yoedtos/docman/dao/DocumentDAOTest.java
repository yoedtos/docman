package net.yoedtos.docman.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Document;
import net.yoedtos.docman.model.Type;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentDAOTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentDAOTest.class);
	
	private static DocumentDAO documentDAO;
	private Document document;
	private byte[] fileBytes;
	
	@BeforeClass
	public static void setUp() throws SQLException, AppException {
		LOGGER.info("startup - creating DB connection");
		
		documentDAO = DAOFactory.getInstance().getDocumentDAO("db-test.properties");
		try {
			documentDAO.checkTable();
		} catch (SQLException e) {
			LOGGER.error("Check Table failed: "+ e.getMessage());
			throw e;
		}
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		LOGGER.info("closing DB connection");
		try {
			documentDAO.closeConnection();
		} catch (SQLException e) {
			LOGGER.error("Connection failed: "+ e.getMessage());
			throw e;
		}
	}
	
	@Before
	public void create() throws IOException {
		LOGGER.info("init - creating objects");
		
		String fileName = "duke-java.png";
		
		document = new Document();
		document.setTitle("Duke Java mascot");
		document.setType(Type.IMAGE);
		document.setFileName(fileName);
		document.setAuthor("Java User");
		document.setTags("Java, Logo");
		document.setDescription("Logo Image of Java Mascote - Duke");
		InputStream input = DocumentDAOTest.class.getClassLoader().getResourceAsStream(fileName);
		fileBytes = IOUtils.toByteArray(input);
		document.setFile(fileBytes);
		document.setContentType("image/png");
	}
	
	@After
	public void destroy() {
		LOGGER.info("destroy - destroy objects");
	}
	
	@Test
	public void test1SaveDocumentThenAssertId() throws AppException {
		Document documentDb = documentDAO.writeDocument(document);
		Assert.assertNotNull(documentDb.getId());
	}
	
	@Test
	public void test2ReadDocumentThenAssertFile() throws AppException {
		Document documentDb = documentDAO.writeDocument(document);
		Assert.assertArrayEquals(fileBytes, documentDb.getFile());
	}
	
	@Test(expected = AppException.class)
	public void test3DropDocumentThenThrowException() throws AppException {
		Document documentDb = documentDAO.writeDocument(document);
		documentDAO.removeDocument(documentDb.getId());
		documentDb = documentDAO.loadDocument(documentDb.getId());
	}
	
	@Test
	public void test4ListDocuments() throws AppException {
		Assert.assertEquals(2, documentDAO.countAllDocuments());
		List<Document> listDocuments = documentDAO.listDocuments(10, 0);
		Assert.assertEquals(2, listDocuments.size());
		
		for (Document document : listDocuments) {
			LOGGER.info("Document ID: " + document.getId());
			Assert.assertNotNull(document.getId());
		}
	}
}
