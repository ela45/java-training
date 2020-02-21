package com.edu.persistence.component;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.edu.domain.Person;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		PERSON P=NEW PERSON();
//		P.SETAGE(29);
//		
//		P.SETNAME("ALEJANDRA");
//		p.setBirthDate(new Date());
	PersistenceManagerImpl per=new PersistenceManagerImpl("jdbc:mysql://localhost:3306/webappdb","person_usr","personpwd");
//		try {
//			per.save(p);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//	try {
//		List<Object> personlist=per.getAll(Person.class);
//		for(Object obj:personlist) {
//			Person p=(Person) obj;
//			System.out.println(p.getId());
//			System.out.println(p.getName());
//			System.out.println(p.getAge());
//			System.out.println(p.getBirthDate());
//			
//		}
//		System.out.println("holis" +personlist.size());
//	} catch (SQLException e) {
//		// todo auto-generated catch block
//		e.printStackTrace();
//	}
	System.out.println("GETTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
	
//	try {
//		Object persona=per.getPerson(Person.class, 23);
//		Person pepe=(Person) persona;
//		if(Objects.nonNull(pepe)) {
//			System.out.println(pepe.getId());
//			System.out.println(pepe.getName());
//			System.out.println(pepe.getAge());
//			System.out.println(pepe.getBirthDate());
//		}
//		
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	
	Person p=new Person();
	p.setId(33);
	p.setName("Alejandra Anaya");
	p.setAge(30);
	p.setBirthDate(new Date());
	try {
		per.update(p);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
		
	}

}
