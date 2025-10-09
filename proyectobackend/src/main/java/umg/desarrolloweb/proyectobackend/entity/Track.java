package umg.desarrolloweb.proyectobackend.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "TRACK")
@Data
public class Track {
	
	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_track")
    private Integer idPista;

	    @Column(name="name", nullable = false)
	    private String nombre;

	    @Column(name="configuration", nullable = false)
	    private String configuracion;
	    
	    @Column(name = "creation_date")
	    private Date fechaCreacion;
	    
	    @Column(name="id_user")
	    private Integer idUser;
	    
	    
}
