package com.edu.service;

import java.util.List;

import com.edu.domain.Person;

public interface PersonService {

	List<Person> getPersonList();
	boolean deletePerson(String id);
	boolean addPerson(String parameter, String parameter2, String parameter3, String parameter4);
	boolean updatePerson(String parameter, String parameter2, String parameter3, String parameter4);
	Person getPersonById(String id);

}
