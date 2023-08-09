/**
 * 
 */
package webSocket;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.red.ink.dto.ResponseDto;
import com.red.ink.model.User;
import com.red.ink.repository.UserRepository;

/**
 * @author Ajith
 *
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
	private SimpMessagingTemplate template;
	@Autowired
	private  UserRepository userRepository;

	public ChatController(SimpMessagingTemplate template, UserRepository userRepository) {
		this.template = template;
		this.userRepository = userRepository;
	}

	@PostMapping("/users/{username}/send")
	public ResponseEntity<Object> sendMessage(@RequestHeader("Authorization") String token,
			@RequestBody Message message, @PathVariable String username) {
		String jwtToken = token.replaceFirst("Bearer", "");
		ResponseDto dto = new ResponseDto();

		DecodedJWT jwt = JWT.decode(jwtToken.trim());
		Optional<User> loggedInUser = userRepository.findByUsername(jwt.getSubject());
		if (loggedInUser.isPresent()) {
			Optional<User> user = userRepository.findByUsername(username);
			if (user != null) {
				template.convertAndSendToUser(user.get().getUsername(), "/messages", message);
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.notFound().build();
			}

		}
		return null;
	}
	
}
