package net.yoedtos.docman.model;

public class Constants {

	private Constants() {
		
	}
	
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String TAGS = "tags";
	public static final String AUTHOR = "author";
	
	//5MB
	public static final long MAX_FILE_SIZE = 1024 * 1024 * 5; 
	public static final int FILENAME_SIZE = 40;
	public static final String EXT_REGEX = "\\.(?=[^\\.]+$)";
	
	public static final String DB_CONFIG = "db.properties";
}
