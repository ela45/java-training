package com.edu.controllers;


public interface PersonControllerI {
	String getAll();
	
	String addPerson(String name,String date, String age);
	
	String updatePerson(String name,String id,String date, String age);
	
	String deletePerson(String id);
	
	String getById(String id);
	
}
