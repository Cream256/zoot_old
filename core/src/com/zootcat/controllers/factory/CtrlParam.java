package com.zootcat.controllers.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CtrlParam 
{
	boolean required() default false;
	boolean global() default false;
	boolean debug() default false;
}
