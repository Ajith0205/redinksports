/**
 * 
 */
package com.red.ink.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.red.ink.model.User;
import com.red.ink.service.UserService;

/**
 * @author ajith
 *
 */
public class UserAge {
	@Autowired
	
   private	UserService userService;
	public  int CalculateAge() throws Exception {
		List<User>users=userService.findAll();
		List<User> birthDayAge = new ArrayList<>();
		
		for(User user:users) {
			
			if(user.getDateofbirth() != null) {
			String sDate1=user.getDateofbirth(); 
		    Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1); 
		    int age=getAge(date1);
			}
		}
		return 0;
		
	}

	private static int getAge(Date date1) {
		DateFormat dateFormater=new SimpleDateFormat("dd/MM/yyyy");
		int d1 = Integer.parseInt(dateFormater.format(date1));
		int d2 = Integer.parseInt(dateFormater.format(new Date()));
		int age = (d2 - d1) / 10000;
		return age;
		
	}

}
