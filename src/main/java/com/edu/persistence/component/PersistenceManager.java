package com.edu.persistence.component;

import java.sql.SQLException;
import java.util.List;


public interface PersistenceManager {
	
	 void connect() throws SQLException;

	 void disconnect() throws SQLException;

	public boolean update(Object entity) throws SQLException ;
	public boolean save(Object entity) throws SQLException ;
	public <T> List<T> get(Class<T> entityClass) throws SQLException ;
	public <T> T get(Class<T> entityClass, Integer id) throws SQLException ;
	public boolean delete(Object entity) throws SQLException ;

}
