package umg.desarrolloweb.proyectobackend.service;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import umg.desarrolloweb.proyectobackend.entity.User;
import umg.desarrolloweb.proyectobackend.repository.UserRepository;






@Service
public class JwtUserDetailsService  implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username){
        User user = userRepository.findByUsername(username);
        
     
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.emptyList()
        );
        }
        	
        
    
}
