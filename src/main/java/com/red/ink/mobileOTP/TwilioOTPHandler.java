/**
 * 
 */
package com.red.ink.mobileOTP;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;

/**
 * @author ajith
 *
 */
@Component
public class TwilioOTPHandler {
	 @Autowired
	    private TwilioOTPService service;
	 
	

	  

	

}
