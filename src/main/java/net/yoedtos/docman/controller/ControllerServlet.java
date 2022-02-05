package net.yoedtos.docman.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Document;
import net.yoedtos.docman.service.DocumentService;
import net.yoedtos.docman.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControllerServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerServlet.class);
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String action = request.getParameter("action");
			String id = request.getParameter("id");
			DocumentService service = ServiceFactory.getInstance().getDocumentService();
			
			if(action != null) {
				if(action.equals("drop")) {
					service.deleteDocument(Integer.parseInt(id));
					response.sendRedirect("success.jsp");
				} else if(action.equals("edit")) {
					Document document = service.editDocument(Integer.parseInt(id));
					request.setAttribute("document", document);
					request.getRequestDispatcher("document.jsp").forward(request, response);
				} 
			} 
		} catch (NumberFormatException | AppException e) {
			LOGGER.error(e.getMessage());
			sendErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), response);
		}
	}

	protected void sendErrorMessage(int error, String message, HttpServletResponse response) {
			try {
				response.sendError(error, message);
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
	   }	
}
