package net.yoedtos.docman.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.yoedtos.docman.exception.AppException;

public class DbConfig {
	
	private String driver;
	private String url;
	private String username;
	private String password;
	
	
	public DbConfig(String config) throws AppException {
		InputStream input = DbConfig.class.getClassLoader().getResourceAsStream(config);
		Properties properties = new Properties();
		try {
			properties.load(input);
		} catch (IOException e) {
			throw new AppException(e.getMessage());
		}
		driver = properties.getProperty("jdbc.driver");
		url = properties.getProperty("jdbc.url");
		username = properties.getProperty("jdbc.username");
		password = properties.getProperty("jdbc.password");
	}


	public String getDriver() {
		return driver;
	}


	public String getUrl() {
		return url;
	}


	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}
	
}
