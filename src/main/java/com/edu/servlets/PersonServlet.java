package com.edu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.domain.Person;
import com.edu.service.PersonService;
import com.edu.service.impl.PersonServiceImpl;


public class PersonServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private PersonService personService;
	
	 public void init() {
	        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
	        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
	        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
	 
	        personService=new PersonServiceImpl(jdbcURL, jdbcUsername, jdbcPassword);
	        
	 
	    }
	 
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException,IOException {	
			String name=request.getParameter("name");
			String id= request.getParameter("id");
			String date=request.getParameter("birthDate");
			String age=request.getParameter("age");

			personService.addPerson(name,id,
						date,age);

			
		
		}
	 
		
		protected void doGet(HttpServletRequest request, HttpServletResponse response)	
				throws ServletException, IOException {
			String path = request.getServletPath();
			
	            switch (path) {
	            case "/list":
	                showList(request, response);
	                break;
	            case "/insert":
	                insert(request, response);
	                break;

	            default:
	            	showList(request, response);
	                break;
	            }
	       
			
			
		}

		
		private void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			String id=request.getParameter("id");
			
			if(id!=null) {
				//Get Person by id
				Person person=personService.getPersonById(id);
				//set person attribute
				request.setAttribute("person", person);
				
			}
			request.getRequestDispatcher("/WEB-INF/views/add-person.jsp").forward(request, response);
		}

		private void showList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			List<Person> personList=personService.getPersonList();
			if(personList!=null && !personList.isEmpty()) {
				request.setAttribute("personList", personService.getPersonList());
			}else {
				request.setAttribute("errorEmptyList", "There is no records.");
			}
			
			request.getRequestDispatcher("/WEB-INF/views/person.jsp").forward(request, response);
		}

		protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException,IOException {
		
			personService.deletePerson(request.getParameter("id"));
		

		}	
		
		protected void doPut(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException,IOException {
		
			String name=request.getParameter("name");
			String id= request.getParameter("id");
			String date=request.getParameter("birthDate");
			String age=request.getParameter("age");
	
			
			personService.updatePerson(name,id,date,age);
	
		}
}
