package com.edu.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.edu.utils.RequestMethod;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
	RequestMethod[] method() default {};
	String value()default "";
}
