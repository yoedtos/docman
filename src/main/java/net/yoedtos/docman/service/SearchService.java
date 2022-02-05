package net.yoedtos.docman.service;

import net.yoedtos.docman.exception.AppException;
import net.yoedtos.docman.model.Pager;
import net.yoedtos.docman.search.Field;
import net.yoedtos.docman.search.Searcher;

public class SearchService {

	private Pager pager = new Pager();
		
	public Pager search(String word, Field field,int size, int page) throws AppException {
		Searcher searcher = new Searcher();
		String[] fields;
		
		if(field.equals(Field.ALL)) {
			fields = new String[]{"title", "description", "tags", "author"};
		} else {
			fields = new String[]{field.toString().toLowerCase()};
		}
		
		pager.setPage(page);
		pager.setSize(size);
		pager.setPrevious(page -1);
		pager.setNext(page + 1);
		
		pager.setDocuments(searcher.searchPhraseByFields(word, fields, size, page));
		pager.setTotal(searcher.getTotalHits());
		
		return pager;
	}
}
