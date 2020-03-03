package com.edu.controllers;


import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;

import com.edu.annotations.Controller;
import com.edu.annotations.Parameters;
import com.edu.annotations.RequestMapping;
import com.edu.domain.Person;
import com.edu.proxy.DynamicProxy;
import com.edu.service.PersonService;
import com.edu.service.impl.PersonServiceImpl;
import com.edu.utils.RequestMethod;
import com.edu.utils.JsonSerializer;
import com.edu.utils.PersonUtils;

@Controller
@RequestMapping(value = "/person")
public class PersonController implements PersonControllerI {
	private final static Logger LOGGER = Logger.getLogger(DynamicProxy.class);
	
	PersonService personService=new PersonServiceImpl(
			"jdbc:mysql://localhost:3306/webappdb","person_usr","personpwd");

	@RequestMapping(method = RequestMethod.GET)
	public String getAll() {
		String response=null;
		try {
			List<Person> personList=personService.getPersonList();
			if(Objects.nonNull(personList)) {
				response=PersonUtils.getJSONStringList(personList);
			}
		}catch(Exception e) {
			LOGGER.error("Error on getting response "+e.getMessage());
		}
		LOGGER.info("Getting all people ....");
		return response;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String addPerson(
			@Parameters(name="name")String name,
			@Parameters(name="date") String date,
			@Parameters(name="age") String age) {
		LOGGER.info("Saving a person ....date"+date);
		LOGGER.info("Saving a person ....name"+name);
		LOGGER.info("Saving a person ....age"+age);
		String response=null;
		try {
			if(personService.addPerson(name, "", date, age))
				response="CREATED";
		}catch(Exception e) {
			
		}
		return response;
	}

	@Override
	@RequestMapping(method = RequestMethod.PUT)
	public String updatePerson(
			@Parameters(name="name")String name,
			@Parameters(name="id")String id,
			@Parameters(name="date") String date,
			@Parameters(name="age")String age) {
		LOGGER.info("Updating a person ....");
		String response=null;
		try {
			 if(personService.updatePerson(name, id, date, age))
				 response="UPDATED";
		}catch (Exception e) {
			LOGGER.error("Error on getting response.."+e.getMessage());
		} 
		return response;
	}

	@Override
	@RequestMapping(method = RequestMethod.DELETE)
	public String deletePerson(@Parameters(name="id")String id) {
		LOGGER.info("Deleting a person ....");
		String response=null;
		try {
			if(personService.deletePerson(id))
				response="DELETED";
		}catch (Exception e) {
			LOGGER.error("Error on getting response.."+e.getMessage());
		} 
		return response;
	}

	@Override
	@RequestMapping(value="/id", method = RequestMethod.GET)
	public String getById(@Parameters(name="id")String id) {
		LOGGER.info("Getting a person ....");
		String response=null;
		try {
			response = JsonSerializer.serilaize(personService.getPersonById(id));
		} catch (Exception e) {
			LOGGER.error("Error on getting response.."+e.getMessage());
		} 
		return response;
	}
}
