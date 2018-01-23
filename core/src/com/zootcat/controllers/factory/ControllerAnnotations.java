package com.zootcat.controllers.factory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.zootcat.controllers.Controller;

public class ControllerAnnotations
{
	public static List<Field> getAnnotatedFields(Controller controller)
	{
		List<Field> allClassFields = new ArrayList<Field>();    	
		Class<?> currentClass = controller.getClass();
		while(currentClass != null)
		{
			allClassFields.addAll(Arrays.asList(currentClass.getDeclaredFields()));
			currentClass = currentClass.getSuperclass();
		}					
		return allClassFields.stream().filter(field -> field.isAnnotationPresent(CtrlParam.class)).collect(Collectors.toList());
	}
}
