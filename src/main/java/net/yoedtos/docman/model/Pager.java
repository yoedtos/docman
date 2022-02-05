package net.yoedtos.docman.model;

import java.util.List;

public class Pager {

	private int page;
	private int size;
	private int next;
	private int previous;
	private int total;
	private int pages;
	// pages total/size
	//   0 =  2/2 
	//     =  3/2
	// total page size
    // 
	
	// 2 =  1 + 2 -> 3
	
	// 3 =  1 + 2 -> 3
	// 3 =  2 + 2 -> 4
	
	// 3 =  1 + 3 -> 4
	
	// 3 =  1 + 3 -> 4
	// 3 =  2 + 3 -> 5
	
	// 6 =  1 + 5 -> 6
	// 6 =  2 + 5 -> 7
	
	// 5 =  1 + 3 -> 4
	// 5 =  2 + 3 -> 5
	
	// 4 =  1 + 2 -> 3
	// 4 =  2 + 2 -> 4
	// 4 =  3 + 2 -> 5
	
	// 5 =  1 + 2 -> 3
	// 5 =  2 + 2 -> 4
	// 5 =  3 + 2 -> 5

	// 6 =  1 + 2 -> 3
	// 6 =  2 + 2 -> 4
	// 6 =  3 + 2 -> 5
	// 6 =  4 + 2 -> 6
	// 6 =  5 + 2 -> 7
	
	// 6 =  1 + 3 -> 4
	// 6 =  2 + 3 -> 5
	// 6 =  3 + 3 -> 6
	// 6 =  4 + 3 -> 7
	
	// 7 =  1 + 4 -> 5
	// 7 =  2 + 4 -> 6
	// 7 =  3 + 4 -> 7
	// 7 =  4 + 4 -> 8
	
	//  -1 =  1 - (4/2)
	//  
	//  
	private List<Document> documents;
	
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	public int getPrevious() {
		return previous;
	}
	public void setPrevious(int previous) {
		this.previous = previous;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPages() {
		this.pages = total/size;
		if(pages % size > 0) {
			++pages;
		}
		return pages;
	}
	public List<Document> getDocuments() {
		return documents;
	}
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
}
