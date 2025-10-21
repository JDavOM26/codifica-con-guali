package umg.desarrolloweb.proyectobackend.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import umg.desarrolloweb.proyectobackend.TrackDto;
import umg.desarrolloweb.proyectobackend.entity.Track;
import umg.desarrolloweb.proyectobackend.repository.TrackRepository;

@RestController
@RequestMapping("/api")
public class TrackController {

	private final TrackRepository trackRepository;
	
	public TrackController(TrackRepository trackRepository) {
		this.trackRepository = trackRepository;
	}
	

	@GetMapping("/noauth/get-random-tracks")
	public List<Track> getRandomTracks() {
		return trackRepository.findRandomTracks(1);
	}
	
	
	
	@GetMapping("/admin/get-track-user")
	public List<Track> getTracksByUser(@RequestParam Integer idUser) {
		return trackRepository.findByIdUser(idUser);
	}
	
	@PostMapping("/admin/save-track")
	public ResponseEntity<Track> saveTrack(@Validated @RequestBody TrackDto trackDto) {
        try {
            Track track;
           
            if (trackDto.getIdPista() != null) {
                Optional<Track> existingTrack = trackRepository.findById(trackDto.getIdPista());
                if (existingTrack.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                track = existingTrack.get();
            } else {
                track = new Track();
            }

           
            try {
                new com.fasterxml.jackson.databind.ObjectMapper().readTree(trackDto.getConfiguracion());
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        
            track.setNombre(trackDto.getNombre());
            track.setConfiguracion(trackDto.getConfiguracion());
            track.setFechaCreacion(trackDto.getFechaCreacion() != null ? trackDto.getFechaCreacion() : new Date());
            track.setIdUser(trackDto.getIdUser());

            
            Track savedTrack = trackRepository.save(track);
            return new ResponseEntity<>(savedTrack, track.getIdPista() == null ? HttpStatus.CREATED : HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	
	@DeleteMapping("/admin/delete-track")
	public void deleteTrack(@RequestParam Integer idTrack) {
		 trackRepository.deleteById(idTrack);	
	}
}
