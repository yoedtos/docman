package net.yoedtos.docman.search;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.service.IndexService;
import net.yoedtos.docman.service.ServiceFactory;

@WebListener
public class SearchInitListener implements ServletContextListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(SearchInitListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOGGER.debug("Started SearchListener");
		
		IndexService indexService = ServiceFactory.getInstance().getIndexService();
		try {
			Directory indexDirectory = FSDirectory.open(new File(Constants.INDEX_DIR));
			if(!DirectoryReader.indexExists(indexDirectory)) {
				indexService.createIndex();
				indexService.reCreateIndexFromDB();
			}
		} catch (AppException | IOException e) {
			LOGGER.error("Init Error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		LOGGER.debug("Shutdown SearchListener");
	}
}
