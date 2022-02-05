package net.yoedtos.docman.util;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import net.yoedtos.docman.model.Constants;

public class DocumentConverter {

	private DocumentConverter() {
		
	}
	
	public static net.yoedtos.docman.model.Document convertFromLucene(Document document) {
		net.yoedtos.docman.model.Document docman = null;
		
		if(document.get("id") != null) {
			docman = new net.yoedtos.docman.model.Document();
			docman.setId(Integer.parseInt(document.get(Constants.ID)));
			docman.setTitle(document.get(Constants.TITLE));
			docman.setDescription(document.get(Constants.DESCRIPTION));
			docman.setTags(document.get(Constants.TAGS));
			docman.setAuthor(document.get(Constants.AUTHOR));
		}
		return docman;
	}
	
	public static Document convertToLucene(net.yoedtos.docman.model.Document documan) {
		
		Document document = new org.apache.lucene.document.Document();
		document.add(new StringField(Constants.ID, Integer.toString(documan.getId()), Field.Store.YES));
		document.add(new TextField(Constants.TITLE,documan.getTitle(), Field.Store.YES));
		document.add(new TextField(Constants.DESCRIPTION,documan.getDescription(), Field.Store.YES));
		document.add(new TextField(Constants.AUTHOR,documan.getAuthor(), Field.Store.YES));
		document.add(new TextField(Constants.TAGS,documan.getTags(), Field.Store.YES));
		
		return document;
	}
}
