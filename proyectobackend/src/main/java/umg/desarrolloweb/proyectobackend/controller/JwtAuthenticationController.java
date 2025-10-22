package umg.desarrolloweb.proyectobackend.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import umg.desarrolloweb.proyectobackend.entity.ActionLog;
import umg.desarrolloweb.proyectobackend.entity.User;
import umg.desarrolloweb.proyectobackend.repository.ActionLogRepository;
import umg.desarrolloweb.proyectobackend.repository.UserRepository;
import umg.desarrolloweb.proyectobackend.security.JwtTokenUtil;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/api/noauth")
public class JwtAuthenticationController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private JwtTokenUtil jwtUtils;
    private final ActionLogRepository actionLogRepository;

    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_DURATION_MINUTES = 30;

    public JwtAuthenticationController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            JwtTokenUtil jwtUtils,
            ActionLogRepository actionLogRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.actionLogRepository = actionLogRepository;
    }


	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody User user) {

		User userFromDb = userRepository.findByUsername(user.getUsername());

		if (userFromDb == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new ErrorResponse("Usuario o contraseña inválidos"));
		}

		if (userFromDb.getUserStatus() == User.EventType.BLOCKED) {
			Date blockedUntil = userFromDb.getBlockedUntil();
			Date now = new Date();

			if (blockedUntil != null && now.before(blockedUntil)) {
				long minutesRemaining = (blockedUntil.getTime() - now.getTime()) / (1000 * 60);
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
						new ErrorResponse("Cuenta bloqueada. Intente nuevamente en " + minutesRemaining + " minutos."));
			} else {

				userFromDb.setUserStatus(User.EventType.ACTIVE);
				userFromDb.setAccessAttempts(0);
				userFromDb.setBlockedUntil(null);
				userRepository.save(userFromDb);
			}
		}

		try {
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

			if (userFromDb.getAccessAttempts() != null && userFromDb.getAccessAttempts() > 0) {
				userFromDb.setAccessAttempts(0);
				userRepository.save(userFromDb);
			}

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String jwt = jwtUtils.generateToken(userDetails.getUsername());
			
			 ActionLog newLog = new ActionLog();
			    newLog.setIdUser(userFromDb.getIdUser());
			    newLog.setTimestamp(LocalDateTime.now());
			    newLog.setAction("LOGIN ID_USER "+ userFromDb.getIdUser());
			    actionLogRepository.save(newLog);

			LoginResponseDto response = new LoginResponseDto();
			response.setToken(jwt);
			response.setIdUser(userFromDb.getIdUser());

			return ResponseEntity.ok(response);

		} catch (BadCredentialsException e) {

			int attempts = (userFromDb.getAccessAttempts() != null) ? userFromDb.getAccessAttempts() : 0;
			attempts++;
			userFromDb.setAccessAttempts(attempts);

			if (attempts >= MAX_ATTEMPTS) {
				userFromDb.setUserStatus(User.EventType.BLOCKED);

				LocalDateTime blockTime = LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES);
				Date blockedUntil = Date.from(blockTime.atZone(ZoneId.systemDefault()).toInstant());
				userFromDb.setBlockedUntil(blockedUntil);

				userRepository.save(userFromDb);

				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(new ErrorResponse("Cuenta bloqueada por múltiples intentos fallidos. "
								+ "Intente nuevamente en " + BLOCK_DURATION_MINUTES + " minutos."));
			}

			userRepository.save(userFromDb);

			int remainingAttempts = MAX_ATTEMPTS - attempts;
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
					new ErrorResponse("Usuario o contraseña inválidos. " + "Intentos restantes: " + remainingAttempts));
		}
	}

	public static class ErrorResponse {
		private String message;

		public ErrorResponse(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
