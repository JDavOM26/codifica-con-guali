package umg.desarrolloweb.proyectobackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import umg.desarrolloweb.proyectobackend.entity.User;
import umg.desarrolloweb.proyectobackend.repository.UserRepository;
import umg.desarrolloweb.proyectobackend.security.JwtTokenUtil;





@RestController
@CrossOrigin
@RequestMapping("/api/noauth")
public class JwtAuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtTokenUtil jwtUtils;
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    user.getPassword()
                )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateToken(userDetails.getUsername());
            User userTemp = userRepository.findByUsername(user.getUsername());
            LoginResponseDto response = new LoginResponseDto();
            response.setToken(jwt);
            response.setIdUser(userTemp.getIdUser());

           System.out.println("usuario no valido");
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            
            throw new RuntimeException("Usuario o password inválido"); 
        }
    }
}
