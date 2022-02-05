package net.yoedtos.docman.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Pager;
import net.yoedtos.docman.service.DocumentService;
import net.yoedtos.docman.service.ServiceFactory;

@WebServlet(description = "List Documents", urlPatterns = { "/list" })
public class ListServlet extends ControllerServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(ListServlet.class);
	
	private static final int PAGE_SIZE = 10;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int page = 1;
		try {
			page = Integer.parseInt(request.getParameter("page"));
			DocumentService service = ServiceFactory.getInstance().getDocumentService();
			Pager pager = service.listDocuments(PAGE_SIZE, page);
			
			request.setAttribute("pager", pager);
			request.getRequestDispatcher("list.jsp").forward(request, response);
		} catch (NumberFormatException | AppException e) {
			LOGGER.error(e.getMessage());
			sendErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), response);
		}
	}	
}
