package com.edu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class PersonUtils {

	public static boolean validatePersonFields(String name, String date,String age) {
		boolean isValid=false;
		if(Objects.nonNull(name) && !name.isEmpty()   
				&& Objects.nonNull(date) && !date.isEmpty()  && validateDate(date) 
				&& Objects.nonNull(age) && !age.isEmpty()) {
		
			isValid=true;
			try {
				Integer.valueOf(age);
			} catch (NumberFormatException e) {
				isValid=false;
			}
		}
		
		
		return isValid;
	}
	
	public static boolean validateDate(String date) {
		return getDate(date)==null?false:true;
	}
	
	public static Date getDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date dateBirth = null;
		try {
			dateBirth = sdf.parse(date);
		} catch (ParseException e) {
		System.out.println("The format is not valid "+e.getMessage());
		}
		return dateBirth;
	}
}
