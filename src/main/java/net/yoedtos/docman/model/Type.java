package net.yoedtos.docman.model;

public enum Type {

	DOCUMENT("Document"),
	SPREADSHEET("Spreadsheet"),
	PRESENTATION("Presentation"),
	IMAGE("Image"),
	ZIP_FILE("Zip File");
	
	private String name;

	private Type(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
