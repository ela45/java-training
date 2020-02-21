package com.edu.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.service.PersonService;
import com.edu.service.impl.PersonServiceImpl;

@WebServlet(urlPatterns="/update-person.do")
public class UpdatePersonServlet extends HttpServlet{
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
		personService.updatePerson(request.getParameter("name"),request.getParameter("id"),
				request.getParameter("birthDate"),request.getParameter("age"));
		
		response.sendRedirect("/list-person.do");
	
	}
}
