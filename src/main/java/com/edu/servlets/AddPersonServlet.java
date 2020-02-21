package com.edu.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.domain.Person;
import com.edu.service.PersonService;
import com.edu.service.impl.PersonServiceImpl;
import com.edu.utils.PersonUtils;

@WebServlet(urlPatterns="/add-person.do")
public class AddPersonServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PersonService personService;
	
	 public void init() {
	        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
	        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
	        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
	 
	        personService=new PersonServiceImpl(jdbcURL, jdbcUsername, jdbcPassword);
	        
	 
	    }
	 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException,IOException {
		String id=request.getParameter("id");
	
		if(id!=null) {
			//Get Person by id
			Person person=personService.getPersonById(id);
			//set person attribute
			request.setAttribute("person", person);
			
		}
		request.getRequestDispatcher("/WEB-INF/views/add-person.jsp").forward(request, response);
	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException,IOException {
		String name=request.getParameter("name");
		String id= request.getParameter("id");
		String date=request.getParameter("birthDate");
		String age=request.getParameter("age");
		if(!PersonUtils.validatePersonFields(name, date,age)) {	
			response.sendRedirect("/add-person.do");
		}else {
			personService.addPerson(name,id,
					date,age);
			response.sendRedirect("/list-person.do");
		}
		
	
	}
}
