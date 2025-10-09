package umg.desarrolloweb.proyectobackend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umg.desarrolloweb.proyectobackend.entity.ActionLog;


@Repository("actionLogRepository")
public interface ActionLogRepository extends JpaRepository<ActionLog, Integer> {

}