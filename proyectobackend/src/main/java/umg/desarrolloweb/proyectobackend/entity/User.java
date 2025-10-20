package umg.desarrolloweb.proyectobackend.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "USER")
@Data
public class User {

	
	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_user")
    private Integer idUser;

	    @Column(name="username", nullable = false)
	    private String username;

	    @Column(name="password", nullable = false)
	    private String password;
	    
	    @Column(name = "rol", nullable = false, length = 50)
	    private String rol;
	    @Column(name = "access_attempts")
	    private Integer accessAttempts = 0;

	    @Enumerated(EnumType.STRING)
	    @Column(name = "user_status", nullable = false)
	    private EventType userStatus = EventType.ACTIVE;

	    @Column(name = "blocked_until")
	    private Date blockedUntil;

	    public enum EventType {
	        ACTIVE, BLOCKED
	    }
	}
