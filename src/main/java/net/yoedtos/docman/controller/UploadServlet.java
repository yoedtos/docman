package net.yoedtos.docman.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Constants;
import net.yoedtos.docman.service.DocumentService;
import net.yoedtos.docman.service.ServiceFactory;

@WebServlet(description = "Upload Document", urlPatterns = { "/upload" })
public class UploadServlet extends ControllerServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadServlet.class);
	

    @SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		if(isMultipart) {
			try {
				ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
				upload.setFileSizeMax(Constants.MAX_FILE_SIZE);
				
				List<FileItem> items = upload.parseRequest(request);
				
				DocumentService service = ServiceFactory.getInstance().getDocumentService();
				service.saveDocument(items);				
				response.sendRedirect("success.jsp");
			} catch (FileUploadException e) {
				LOGGER.error(e.getMessage());
				sendErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File size is invalid", response);
			} catch (IOException | AppException e1) {
				LOGGER.error(e1.getMessage());
				sendErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e1.getMessage(), response);
			}
		}
	}

}
