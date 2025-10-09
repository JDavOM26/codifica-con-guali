package umg.desarrolloweb.proyectobackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



import umg.desarrolloweb.proyectobackend.entity.User;


@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
	  User findByUsername(String username);
}
