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
import com.edu.utils.PersonUtils;

@WebServlet(urlPatterns="/people.do")
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
			if(!PersonUtils.validatePersonFields(name, date,age)) {	
				response.sendRedirect("/add-person.do");
			}else {
				personService.addPerson(name,id,
						date,age);
				response.sendRedirect("/list-person.do");
			}
			
		
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
		
		protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException,IOException {
			personService.deletePerson(request.getParameter("id"));
			response.sendRedirect("/list-person.do");
		}	
		
		protected void doPut(HttpServletRequest request, HttpServletResponse response) 
				throws ServletException,IOException {
			personService.updatePerson(request.getParameter("name"),request.getParameter("id"),
					request.getParameter("birthDate"),request.getParameter("age"));
			
			response.sendRedirect("/list-person.do");
		
		}
}
