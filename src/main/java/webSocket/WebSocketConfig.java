/**
 * 
 */
package webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.red.ink.serviceimpl.UserPrincipalDetailsServiceImpl;

/**
 * @author Ajith A
 *
 */
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private PasswordEncoder encoder;
	@Autowired
	private UserPrincipalDetailsServiceImpl userPrincipalDetailsServiceImpl;
	
	public WebSocketConfig(UserPrincipalDetailsServiceImpl userPrincipalDetailsServiceImpl, PasswordEncoder passwordEncoder) {
        this.userPrincipalDetailsServiceImpl = userPrincipalDetailsServiceImpl;
        this.encoder = encoder;
    }
	
	

}
