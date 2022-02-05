package net.yoedtos.docman.service;

import org.apache.commons.fileupload.FileItem;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import net.yoedtos.docman.dao.DocumentDAO;
import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Document;

import static org.mockito.Mockito.*;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class DocumentServiceTest {

	@Mock
	private DocumentDAO documentDAOMock;
	
	@Mock
	private IndexService IndexServiceMock;
	
	@InjectMocks
	private DocumentService documentService;
	
	@Captor
	private ArgumentCaptor<Integer> captor;
	
	@SuppressWarnings("unchecked")
	@Test(expected = AppException.class)
	public void testGetDocumentThenThrowsException() throws AppException {
		when(documentDAOMock.readFile(null)).thenThrow(AppException.class);
		documentService.getDocument(null);
		verify(documentDAOMock, times(1)).readFile(null);
	}
	
	@Test
	public void testGetDocument() throws AppException {
		when(documentDAOMock.readFile(1)).thenReturn(createTestDocument());
		Document doc = documentService.getDocument(1);
 		Assert.assertEquals("dummyfile", doc.getFileName());
		verify(documentDAOMock, times(1)).readFile(1);
	}
	
	@Test
	public void testSaveDocument() throws AppException {
		when(documentDAOMock.writeDocument(isA(Document.class))).thenReturn(createTestDocument());
		Document documentMock = documentService.saveDocument(new ArrayList<FileItem>());
		Assert.assertSame("dummyfile", documentMock.getFileName());
		verify(documentDAOMock, times(1)).writeDocument(isA(Document.class));	 
	}
	
	@Test
	public void testRemoveDocumentOnce() throws AppException {
		Integer id = 1;
		documentService.deleteDocument(id);
		verify(documentDAOMock).removeDocument(captor.capture());
		Assert.assertEquals(1, captor.getValue().intValue());
		verify(documentDAOMock, times(1)).removeDocument(id);
	}
	
	private Document createTestDocument() {
		Document document = new Document();
		document.setFileName("dummyfile");		
		return document;
	}
}
