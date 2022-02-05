package net.yoedtos.docman.search;

public enum Field {
	
	ALL("All"),
	TITLE("Title"),
	DESCRIPTION("Description"),
	AUTHOR("Author"),
	TAGS("Tags");
	
	String name;

	private Field(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
