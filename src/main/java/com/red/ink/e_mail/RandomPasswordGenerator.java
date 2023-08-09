/**
 * 
 */
package com.red.ink.e_mail;

import java.text.DecimalFormat;
import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * @author ajith
 *
 */
@Component
public class RandomPasswordGenerator {
	private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int RANDOM_STRING_LENGTH = 10;

	public String generateRandomString() {
		StringBuffer randStr = new StringBuffer();
		for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
			int number = getRandomNumber();
			char ch = CHAR_LIST.charAt(number);
			randStr.append(ch);
		}
		return randStr.toString();
	}
	
	public int getRandomNumber() {
		int randomInt = 0;
		Random randomGenerator = new Random();
		randomInt = randomGenerator.nextInt(CHAR_LIST.length());
		if (randomInt - 1 == -1) {
			return randomInt;
		} else {
			return randomInt - 1;
		}
	}
	
	 //6 digit otp
    public String generateOTP() {
        return new DecimalFormat("000000")
                .format(new Random().nextInt(999999));
    }
	
}
