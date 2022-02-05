package net.yoedtos.docman.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Document;
import net.yoedtos.docman.service.DocumentService;

@WebServlet(description = "Download Document", urlPatterns = { "/download" })
public class DownloadServlet extends ControllerServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String documentId = request.getParameter("id");
	   
	    Document document;
		
	    try {
	    	if (documentId == null) {
			    sendErrorMessage(HttpServletResponse.SC_BAD_REQUEST, "Invalid content", response);
			    return;
			}

			DocumentService documentService = new DocumentService();
			document = documentService.getDocument(Integer.parseInt(documentId));
			
			if(document.getFileName() != null) {
				String fileName = document.getFileName();
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
				response.setContentType(document.getContentType());
				sendDocument(response, document.getFile());			
			} 
		} catch (NumberFormatException | AppException e) {
			LOGGER.error(e.getMessage());
			sendErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), response);
		}
	}
	
	private void sendDocument(HttpServletResponse response, byte[] byteArray) throws IOException {
		
		try (OutputStream output = response.getOutputStream()) {
			output.write(byteArray);
			output.flush();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
	}
}
