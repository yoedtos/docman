package net.yoedtos.docman.service;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;

import net.yoedtos.docman.dao.DAOFactory;
import net.yoedtos.docman.dao.DocumentDAO;
import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Constants;
import net.yoedtos.docman.model.Document;
import net.yoedtos.docman.model.Pager;
import net.yoedtos.docman.model.Type;

public class DocumentService {

	private DocumentDAO documentDAO;
	private IndexService indexService;
	private Pager pager;

	public DocumentService() throws AppException {
		super();
		documentDAO = DAOFactory.getInstance().getDocumentDAO(Constants.DB_CONFIG);
		indexService = ServiceFactory.getInstance().getIndexService();
		pager = new Pager();
	}

	public Document getDocument(Integer id) throws AppException {
		return documentDAO.readFile(id);
	}
	
	public Document editDocument(Integer id) throws AppException {
		return documentDAO.loadDocument(id);
	}
	
	public void deleteDocument(Integer id) throws AppException {
		int result = 0;
		try {
			result = documentDAO.removeDocument(id);	
			indexService.deleteDocumentFromIndex(id);
		} catch (AppException e) {
			rollBack(result);
			throw e;
		}
		commit();
	}
	
	public Pager listDocuments(int size, int page) throws AppException {
		int total = documentDAO.countAllDocuments();
		
		pager.setPage(page);
		pager.setSize(size);
		pager.setPrevious(page -1);
		pager.setNext(page + 1);
		int offset = 0;
		
		if(page > 1) {
			--page;
			offset = size * page;
		}
		
		pager.setDocuments(documentDAO.listDocuments(size, offset));
		pager.setTotal(total);
		
		return pager;
	}
	
	public Document saveDocument(List<FileItem> items) throws AppException {
		
		Iterator<FileItem>iterator = items.iterator();
		Document document = new Document();
		
		while (iterator.hasNext()) {
			FileItem fileItem = iterator.next();
			
			String field = fileItem.getFieldName();
			String value = fileItem.getString();
			
			if (fileItem.isFormField()) {
				switch (field) {
					case "id":
						if(value != null && StringUtils.isNotEmpty(value)) {
							document.setId(Integer.parseInt(value));
						}
						break;
					case "title":
						document.setTitle(value);
						break;
					case "author":
						document.setAuthor(value);				
						break;
					case "description":
						document.setDescription(value);
						break;
					case "tags":
						document.setTags(value);
						break;
					case "type":
						document.setType(Type.valueOf(value));
						break;
					default:
						break;
				}
				
			} else if(document.getId() == null){
				String fileName = StringUtils.deleteSpaces(fileItem.getName());
				
				if(fileName.length() > Constants.FILENAME_SIZE) {
					String[] tokens = fileName.split(Constants.EXT_REGEX);
					fileName = tokens[0].substring(0, Constants.FILENAME_SIZE - 5) +"."+ tokens[1];
				}
				document.setFileName(fileName);
				document.setSize(fileItem.getSize());
				document.setContentType(fileItem.getContentType());
				document.setFile(fileItem.get());	
			}
		}
		
		return save(document);
	}

	private Document save(Document document) throws AppException {
		int result = 0;
		
		try {
			if(document.getId() != null) {
				result = documentDAO.updateDocument(document);
				indexService.updateDocumentToIndex(document);
			} else {
				document = documentDAO.writeDocument(document);
				indexService.addDocumentToIndex(document);
			}
		} catch (AppException e) {
			rollBack(result);
			throw e;
		}
		commit();
		
		return document;
	}

	private void rollBack(int result) throws AppException {
		if(result != 0) {
			documentDAO.rollBack();
			indexService.rollBack();
		} else {
			documentDAO.rollBack();
		}
	}
	
	private void commit() throws AppException {
		documentDAO.commit();
		indexService.commit();
	}
}
