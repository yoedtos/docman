package net.yoedtos.docman.dao;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Constants;

@WebListener
public class DBContextListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBContextListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.debug("Started");
		try {
			DocumentDAO documentDAO = DAOFactory.getInstance().getDocumentDAO(Constants.DB_CONFIG);
			documentDAO.checkTable();
		} catch (SQLException | AppException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.debug("Shutdown");
		try {
			DocumentDAO documentDAO = DAOFactory.getInstance().getDocumentDAO(Constants.DB_CONFIG);
			documentDAO.closeConnection();
		} catch (SQLException | AppException e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
	}
}
