package net.yoedtos.docman.model;

import org.apache.commons.io.FileUtils;

public class Document {
	
	private Integer id;
	private String fileName;
	private String contentType;
	private byte[] file;
	private long size;
	private String title;
	private String author;
	private String description;
	private String tags;
	private Type type;
	
	public Document() {
		super();	
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public String getFormatedSize() {
		return FileUtils.byteCountToDisplaySize(this.size);
	}
}
