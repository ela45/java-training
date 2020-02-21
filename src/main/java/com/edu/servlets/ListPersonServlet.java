package com.edu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.domain.Person;
import com.edu.service.PersonService;
import com.edu.service.impl.PersonServiceImpl;

@WebServlet(urlPatterns="/list-person.do")
public class ListPersonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PersonService personService;
	
	 public void init() {

	        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
	        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
	        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
	 
	        personService=new PersonServiceImpl(jdbcURL, jdbcUsername, jdbcPassword);
	        
	 
	    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)	
			throws ServletException, IOException {
		List<Person> personList=personService.getPersonList();
		if(personList!=null && !personList.isEmpty()) {
			request.setAttribute("personList", personService.getPersonList());
		}else {
			request.setAttribute("errorEmptyList", "There is no records.");
		}
		
		request.getRequestDispatcher("/WEB-INF/views/person.jsp").forward(request, response);
	}
	

	  
}
