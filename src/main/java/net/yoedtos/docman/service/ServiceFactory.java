package net.yoedtos.docman.service;

import net.yoedtos.docman.exception.AppException;

public class ServiceFactory {

	private static ServiceFactory instance;
	
	private ServiceFactory() {
		
	}
	
	public static ServiceFactory getInstance() {
		if(instance == null) {
			instance = new ServiceFactory();
		}
		return instance;
	}
	
	public DocumentService getDocumentService() throws AppException {
		return new DocumentService();
	}
	
	public SearchService getSearchService() {
		return new SearchService();
	}
	
	public IndexService getIndexService() {
		return new IndexService();
	}
}
