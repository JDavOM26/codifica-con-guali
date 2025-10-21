package umg.desarrolloweb.proyectobackend.controller;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import umg.desarrolloweb.proyectobackend.entity.User;
import umg.desarrolloweb.proyectobackend.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
public class UserController {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder= passwordEncoder;
	}

	@GetMapping("/get-users")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	@PostMapping("/save-user")
	public User saveUser(@RequestBody User user) {
		if(user.getIdUser()==null) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			return userRepository.save(user);
		}
		return userRepository.save(user);
	}

	@DeleteMapping("/delete-user")
	public void deleteUser(@RequestParam Integer idUser) {
		userRepository.deleteById(idUser);
	}
}
