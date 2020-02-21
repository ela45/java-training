package com.edu.domain;

import java.util.Date;

import com.edu.annotations.Attribute;
import com.edu.annotations.Id;
import com.edu.annotations.Table;

@Table(name="person")
public class Person {
	
	@Attribute(name="name")
	private String name;
	
	@Id
	@Attribute(name="id")
	private Integer id;
	
	@Attribute(name="birth_date")
	private Date birthDate;
	
	@Attribute(name="age")
	private Integer age;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Person(String name, Date birthDay, int age) {
		super();
		this.name = name;
		this.birthDate = birthDay;
		this.age = age;
	}
	public Person(String name, Integer id,Date birthDay, int age) {
		super();
		this.name = name;
		this.birthDate = birthDay;
		this.id=id;
		this.age = age;
	}
	public Person() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
}
