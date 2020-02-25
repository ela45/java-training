package com.edu.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.edu.domain.Person;
import com.edu.persistence.component.PersistenceManager;
import com.edu.persistence.component.PersistenceManagerImpl;
import com.edu.service.PersonService;
import com.edu.utils.PersonUtils;

public class PersonServiceImpl implements PersonService {


	private PersistenceManager persistenceManager;

	public PersonServiceImpl(String jdbcURL, String jdbcUsername, String jdbcPassword) {
	
		persistenceManager = new PersistenceManagerImpl(jdbcURL, jdbcUsername, jdbcPassword);
	}

	@Override
	public List<Person> getPersonList() {
	
		List<Person> personList = null;
		
		try {
			personList = persistenceManager.get(Person.class);
		} catch (SQLException e) {
			System.out.println("Error on getting person list "+e.getMessage());
		}
		return personList;
	}

	@Override
	public boolean deletePerson(String id) {
	
		boolean wasDeleted = false;
		Person p = new Person();
		p.setId(Integer.valueOf(id));
		try {
			wasDeleted = persistenceManager.delete(p);
		} catch (SQLException e) {
			
			System.out.println("Exception deleting a person " + e.getMessage());

		}
		return wasDeleted;
	}

	@Override
	public boolean addPerson(String name, String id, String date, String age) {
	
		boolean wasAdded = false;

		Person person = new Person(name, PersonUtils.getDate(date), Integer.valueOf(age));
		try {
			wasAdded = persistenceManager.save(person);
		} catch (SQLException e) {
		
			System.out.println("The person could not be created " + e.getMessage());
		}
		return wasAdded;
	}

	@Override
	public boolean updatePerson(String name, String id, String date, String age) {
	
		boolean wasUpdated=false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateBirth = null;
	
		try {
			dateBirth = sdf.parse(date);
		} catch (ParseException e) {
			System.out.println("Error on convertin Date "+e.getMessage());
		}
		Person person = new Person(name, Integer.valueOf(id), dateBirth, Integer.valueOf(age));
		try {
			wasUpdated=persistenceManager.update(person);
		} catch (SQLException e) {
			System.out.println("The person could not be updated "+e.getMessage());
		}
		return wasUpdated;

	}

	@Override
	public Person getPersonById(String id) {
	
		Person person = null;
		try {
			person = persistenceManager.get(Person.class,Integer.valueOf(id));
		} catch (SQLException e) {
			System.out.println("Error on getting person id "+id+ " error "+e.getMessage());
		}
		return person;
	}

}
