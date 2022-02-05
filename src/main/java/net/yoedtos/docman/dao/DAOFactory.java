package net.yoedtos.docman.dao;

import net.yoedtos.docman.exception.AppException;

public class DAOFactory {

	private static DAOFactory instance;
	
	private DAOFactory() {
		
	}
	
	public static DAOFactory getInstance() {
		if(instance == null) {
			instance = new DAOFactory();
		}
		return instance;
	}
	
	public DocumentDAO getDocumentDAO(String config) throws AppException {
		return new DocumentDAO(config);
	}
}
