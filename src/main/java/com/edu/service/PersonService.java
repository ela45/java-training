package com.edu.service;

import java.util.List;

import com.edu.domain.Person;

public interface PersonService {

	List<Person> getPersonList();
	boolean deletePerson(String id);
	boolean addPerson(String name, String id, String date, String age);
	boolean updatePerson(String name, String id, String date, String age);
	Person getPersonById(String id);

}
