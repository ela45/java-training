package com.edu.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.edu.domain.Person;

public class PersonDAOImpl implements PersonDAO{
	
	private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;
     
    public PersonDAOImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }
     
    protected void connect() throws SQLException {
        if (jdbcConnection == null || jdbcConnection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(
                                        jdbcURL, jdbcUsername, jdbcPassword);
        }
    }
     
    protected void disconnect() throws SQLException {
        if (jdbcConnection != null && !jdbcConnection.isClosed()) {
            jdbcConnection.close();
        }
    }

	@Override
	public boolean insertPerson(Person person) throws SQLException {
		
		 String sql = "INSERT INTO person (name, age, birth_date) VALUES (?, ?, ?)";
	        connect();
	         
	        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
	        statement.setString(1, person.getName());
	        statement.setInt(2, person.getAge());
	        statement.setDate(3, new java.sql.Date(person.getBirthDate().getTime()));
	       
	        boolean rowInserted = statement.executeUpdate() > 0;
	        statement.close();
	        disconnect();
	        return rowInserted;
		
	}

	@Override
	public List<Person> listAllPeople() throws SQLException {
		
		List<Person> listBook = new ArrayList<>();
        
        String sql = "SELECT * FROM person";
         
        connect();
         
        Statement statement = jdbcConnection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
         
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            Date birthDate = resultSet.getDate("birth_date");
            int age = resultSet.getInt("age");
             
            Person book = new Person(name, id, new java.util.Date(birthDate.getTime()), age);
            listBook.add(book);
        }
         
        resultSet.close();
        statement.close();
         
        disconnect();
         
        return listBook;
	}

	@Override
	public boolean deletePerson(Person person) throws SQLException {
		
		String sql = "DELETE FROM person where id = ?";
        
        connect();
         
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, person.getId());
         
        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowDeleted;     
	}

	@Override
	public boolean updatePerson(Person person) throws SQLException {
		
		String sql = "UPDATE person SET name = ?, age = ?, birth_date = ?";
        sql += " WHERE id = ?";
        connect();
         
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, person.getName());
        statement.setInt(2, person.getAge());
        statement.setDate(3, new java.sql.Date(person.getBirthDate().getTime()));
        statement.setInt(4, person.getId());
         
        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;     
	}

	@Override
	public Person getPerson(Integer id) throws SQLException {
		
		Person person = null;
        String sql = "SELECT * FROM person WHERE id = ?";
         
        connect();
         
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, id);
         
        ResultSet resultSet = statement.executeQuery();
         
        if (resultSet.next()) {
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");
            Date date = resultSet.getDate("birth_date");
             
            person = new Person(name, id, date, age);
        }
         
        resultSet.close();
        statement.close();
         
        return person;
	}

}
