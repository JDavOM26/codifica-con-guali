package umg.desarrolloweb.proyectobackend.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ACTION_LOG")
@Data
public class ActionLog {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_action_log")
    private Integer idActionLog;

	    @Column(name="timestamp", nullable = false)
	    private LocalDateTime timestamp;

	    @Column(name="action", nullable = false)
	    private String action;
	    
	    
	    @Column(name="id_user")
	    private Integer idUser;
	    
	
}


