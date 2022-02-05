package net.yoedtos.docman.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import net.yoedtos.docman.exception.AppException;


public abstract class DAO {
	
	private DbConfig config;
	private Connection connection;

	protected DAO(String config) throws AppException {
		this.config = new DbConfig(config);
	}
	
	protected Connection getConnection() throws SQLException {
		try {
			Class.forName(config.getDriver());
		} catch (ClassNotFoundException e) {
			throw new SQLException(e.getMessage());
		}
		connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
		connection.setAutoCommit(false);
		
		return connection;
	}

	protected boolean hasTable(String table) {
		boolean exits = false;
		
		try { 
			ResultSet result = getConnection().getMetaData().getTables(null, null, table, null);
			
			if(result.next()) {
			  exits = true;
			}
		} catch (SQLException e) {
			return false;
		}
		return exits;
	}
	
	protected void closeConnection() throws SQLException {
		if(connection != null) {
			connection.close();
		}
	}
}
