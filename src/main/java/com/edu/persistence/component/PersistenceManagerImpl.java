package com.edu.persistence.component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.edu.annotations.Attribute;
import com.edu.annotations.Id;
import com.edu.annotations.Table;
import com.edu.utils.DBOperations;

public class PersistenceManagerImpl implements PersistenceManager{
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;

	public PersistenceManagerImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		super();
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}

	public void connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
		}
	}

	public void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}

	
	
	public boolean update(Object entity) throws SQLException {

		PreparedStatement statement = null;
		boolean rowUpdated = false;
		try {

			Set<Field> fieldsToUpdate = new LinkedHashSet<Field>();
			
			connect();
			String query=getQuery(DBOperations.UPDATE,entity,fieldsToUpdate);
			
			System.out.println("QUERY " + query.toString());
			statement = jdbcConnection.prepareStatement(query.toString());
			int index=setStatementParameters(statement, fieldsToUpdate, entity);

			setIdValueStatement(statement, entity, index);
			rowUpdated = statement.executeUpdate() > 0;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {//** Refactor using try with resources item 8 efective java **
			statement.close();
			disconnect();
		}

		return rowUpdated;
	}

	public int setStatementParameters(PreparedStatement statement,Set<Field>fieldsToUpdate,Object entity) throws IllegalArgumentException, IllegalAccessException, SQLException {
		int index = 1;
		for (Field field : fieldsToUpdate) {
			field.setAccessible(true);
			statement.setObject(index, field.get(entity));
			index++;
		}
		return index;
	}
	
	

	public boolean save(Object entity) throws SQLException {

		// Create insert query

		boolean rowInserted = false;
		PreparedStatement statement = null;
		// get fields
		Set<Field> fieldsToInsert = new LinkedHashSet<>();
		try {

			String query=getQuery(DBOperations.CREATE,entity,fieldsToInsert);
			System.out.println("QUERY " + query);
			connect();
			statement = jdbcConnection.prepareStatement(query.toString());
			// get values for fields
			setStatementParameters(statement, fieldsToInsert, entity);
			rowInserted = statement.executeUpdate() > 0;
		} catch (IllegalArgumentException e) {
			System.out.println("The arguments are not correct");
		} catch (IllegalAccessException e) {
			System.out.println("Can not access to the field");
		} finally { //** Refactor using try with resources item 8 efective java
			statement.close();
			disconnect();
		}
		return rowInserted;
	}

	public <T> List<T> get(Class<T> entityClass) throws SQLException {

		List<T> resultsList =null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connect();
			String query=getQuery(DBOperations.GET,entityClass);
			statement = jdbcConnection.createStatement();
			
			resultSet = statement.executeQuery(query);

			Field[] fields = entityClass.getDeclaredFields();

			resultsList=getResultsList(resultSet, entityClass, fields);

		} catch (InstantiationException e) {
			System.out.println("The object could not be instantiated " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException " + e.getMessage());
		} finally {
			resultSet.close();
			statement.close();
			disconnect();
		}

		return resultsList;
	}


	
	public <T> T get (Class<T> entityClass, Integer id) throws SQLException {

		PreparedStatement statement = null;
		ResultSet resultSet = null;
		T entityObj = null;
		try {

			String query=getQuery(DBOperations.GETBYID,entityClass);
			System.out.println(query.toString());
			connect();
			statement = jdbcConnection.prepareStatement(query.toString());
			statement.setObject(1, id);
	
			resultSet = statement.executeQuery();
			Field[] fields = entityClass.getDeclaredFields();
			List <T> entitiesList=getResultsList(resultSet,entityClass,fields);
			if(Objects.nonNull(entitiesList)&& !entitiesList.isEmpty()) {
				entityObj=entitiesList.get(0);
			}else if(entitiesList.size()>1) {
				System.out.println("The result not contains an unic result");
			}
		} catch (InstantiationException e) {
			System.out.println("The object could not be instantiated " + e.getMessage());
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException " + e.getMessage());
		} finally {
			resultSet.close();
			statement.close();
		}
		return entityObj;
	}

	
	
	public boolean delete(Object entity) throws SQLException {

		PreparedStatement statement = null;
		boolean rowDeleted = false;
		try {

			String query=getQuery(DBOperations.DELETE,entity,null);
			connect();
			statement = jdbcConnection.prepareStatement(query.toString());

			setIdValueStatement(statement,entity,1);
			rowDeleted = statement.executeUpdate() > 0;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			statement.close();
			disconnect();
		}
		return rowDeleted;
	}
	
	public void setIdValueStatement(PreparedStatement statement,Object entity,int index) throws IllegalArgumentException, IllegalAccessException, SQLException {
	
		Field idField=null;
		Field[] fields = entity.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {

			if (Objects.nonNull(fields[i].getAnnotation(Id.class))) {
				idField = fields[i];
			} 
		}
		
		idField.setAccessible(true);
		statement.setObject(index, idField.get(entity));

	}
	
	private String getQuery(DBOperations operation,Object entity,Set<Field> fieldsToManage) throws IllegalArgumentException, IllegalAccessException {
		StringBuffer query=new StringBuffer();
		Class<?> entityClass=entity.getClass();
		String annotationTableName=entityClass.getAnnotation(Table.class).name();
		Field[] fields = entityClass.getDeclaredFields();

		Field idField=null;
		switch(operation) {
		case CREATE:
			query.append("INSERT INTO ");
			query.append(annotationTableName);
			query.append("(");
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (Objects.nonNull(fields[i].get(entity))) {
					query.append(fields[i].getAnnotation(Attribute.class).name());
					fieldsToManage.add(fields[i]);
					if (i < fields.length - 1) {
						query.append(",");
					}
				}
			}
			query.append(") VALUES (?,?,?)");
				break;
		case UPDATE:
			query.append("UPDATE ");
			query.append(annotationTableName);
			query.append(" SET ");

			for (int i = 0; i < fields.length; i++) {

				if (Objects.nonNull(fields[i].getAnnotation(Id.class))) {
					idField = fields[i];
				} else {
					query.append(fields[i].getAnnotation(Attribute.class).name()).append(" =?");
					fieldsToManage.add(fields[i]);
					if (i < fields.length - 1) {
						query.append(",");
					}
				}
			}
			query.append(" WHERE ").append(idField.getAnnotation(Attribute.class).name()).append(" = ?");

				break;
		case DELETE:
			query.append("DELETE FROM ");
			query.append(annotationTableName);
			for (Field field : fields) {
				if (Objects.nonNull(field.getAnnotation(Id.class))) {
					query.append(" WHERE " + field.getAnnotation(Attribute.class).name()).append(" =?");
					idField = field;
				}
			}
				break;
		default:
			
			break;

		}
		return query.toString();
	}
	
	private String getQuery(DBOperations operation,Class<?> entityClass) throws IllegalArgumentException, IllegalAccessException {
		StringBuffer query=new StringBuffer();
		String annotationTableName=entityClass.getAnnotation(Table.class).name();
		Field[] fields = entityClass.getDeclaredFields();
	
		switch(operation) {
		
		case GET:
			query.append("SELECT * FROM ");
			query.append(annotationTableName);
			
				break;
		case GETBYID:
			query.append("SELECT * FROM ");
			query.append(annotationTableName);
			for (Field field : fields) {
			if (Objects.nonNull(field.getAnnotation(Id.class))) {
				query.append(" WHERE " + field.getAnnotation(Attribute.class).name());
				query.append(" =?");

			}
		}
				break;
		default:
			break;	
		}
		return query.toString();
	}
	
	private  <T> List<T> getResultsList(ResultSet resultSet,Class<T> entityClass,Field[] fields) throws InstantiationException, IllegalAccessException, SecurityException, IllegalArgumentException, SQLException {
		List<T> resultsList = new ArrayList<>();
		T entityObj=null;
		while (resultSet.next()) {
			entityObj = entityClass.newInstance();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				fields[i].set(entityObj, resultSet.getObject(fields[i].getAnnotation(Attribute.class).name()));
			}
			resultsList.add(entityObj);
		}
		return resultsList;
	}

}
