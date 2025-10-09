package umg.desarrolloweb.proyectobackend;

import java.util.Date;

import lombok.Data;

@Data
public class TrackDto {
	private Integer idPista;
	private String nombre;
	private String configuracion;
	private Date fechaCreacion;
	private Integer idUser;

}
