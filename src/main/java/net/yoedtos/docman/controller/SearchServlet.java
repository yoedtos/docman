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
import net.yoedtos.docman.search.Field;
import net.yoedtos.docman.service.SearchService;
import net.yoedtos.docman.service.ServiceFactory;

@WebServlet(description = "Search Document", urlPatterns = { "/search" })
public class SearchServlet extends ControllerServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(SearchServlet.class);
	
	private static final int HITS_PER_PAGE = 5;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		SearchService searchService = ServiceFactory.getInstance().getSearchService();
		int page;
		
		try {
			String keyword = request.getParameter("keyword");
			Field field = Field.values()[Integer.parseInt(request.getParameter("field"))];
			
			if(request.getParameter("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(request.getParameter("page"));
			}
			Pager pager = searchService.search(keyword, field, HITS_PER_PAGE, page);
			request.setAttribute("pager", pager);
			request.setAttribute("keyword", keyword);
			request.setAttribute("field", field);
			request.getRequestDispatcher("search.jsp").forward(request, response);
		} catch (NumberFormatException | AppException e) {
			LOGGER.error(e.getMessage());
			sendErrorMessage(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage(), response);
		}
	}	
}
