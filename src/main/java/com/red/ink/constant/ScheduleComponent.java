/**
 * 
 */
package com.red.ink.constant;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.red.ink.e_mail.SendMail;
import com.red.ink.model.User;
import com.red.ink.service.UserService;

/**
 * @author ajith
 *
 */
@Component
public class ScheduleComponent {
	
	
	
	private static final int Int = 0;
	@Autowired
	UserService userService;
	private static String dateFormat="MM-dd";
	
	
@Scheduled(fixedRate = 86400000) // One Day
//@Scheduled(fixedRate = 100000)
	private void BirthDayWishes() {
		List<User>users=userService.findAll();
		List<User> happyBirthDay = new ArrayList<>();
		 
//	    int age =AgeCalculate(users);
		String Wishes=Constants.BIRTHDAYWISHES;
		 
	
		for(User user:users) {
			try {
				if (isItBirthday(user)) {
						happyBirthDay.add(user);
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		
		}
		
		try {
			if(happyBirthDay.size()>0) {
				for(User user1:happyBirthDay) {

		
					
					String emailLoginContent = "Website: " + "<a href = " + Constants.URL
							+ ">www.redinksports.com</a><br/>";
					String[] receivers = new String[] { 
							user1.getEmail()
							};
					SendMail.sendMail(receivers, "Hi " + user1.getName() + ","
							+ "<br /><br />"
							+ "Happy Birthday  To You ."
							+ "<br />" + emailLoginContent +"<br />" 
							+ "Wish You: " +Wishes
							
							+ "<br /><br />" + "Thanks & Regards," + "<br />" + "REDINK Sports Club",
							"All Ways Good Relationship with You ;", null);
					
				}
				
			}
			
		}catch(Exception e ) {
			e.printStackTrace();
		}
			
	}


private boolean isItBirthday(User user) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		
		
		if(user.getDateofbirth() !=null) {
			Date today = new Date();
			String sDate1=user.getDateofbirth(); 
		  Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);  
            
		  if(date1 ==null) {
				return false;
			}
		  
		  return (formatter.format(today).equals(formatter.format(date1)));
		}
		return false;
		}

//	private int getAge(Date date1) {
//		DateFormat dateFormater=new SimpleDateFormat("dd/MM/yyyy");
//		int d1 = Integer.parseInt(dateFormater.format(date1));
//		int d2 = Integer.parseInt(dateFormater.format(new Date()));
//		int age = (d2 - d1) / 10000;
//		return age;
//	}

	


	
	
}
