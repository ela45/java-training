package com.edu.dao;

import java.sql.SQLException;
import java.util.List;

import com.edu.domain.Person;

public interface PersonDAO {
	public boolean insertPerson(Person book) throws SQLException;
	public List<Person> listAllPeople() throws SQLException ;
	public boolean deletePerson(Person person) throws SQLException ;
	public boolean updatePerson(Person person) throws SQLException;
	Person getPerson(Integer id) throws SQLException;
}
