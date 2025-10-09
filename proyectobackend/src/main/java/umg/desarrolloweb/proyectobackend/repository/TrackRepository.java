package umg.desarrolloweb.proyectobackend.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import umg.desarrolloweb.proyectobackend.entity.Track;

@Repository("trackRepository")
public interface TrackRepository extends JpaRepository<Track, Integer> {
	
	@Query(value = "SELECT * FROM TRACK ORDER BY RAND() LIMIT ?1", nativeQuery = true)
    List<Track> findRandomTracks(int limit);
	
	 List<Track> findByIdUser(Integer idUser);

}
