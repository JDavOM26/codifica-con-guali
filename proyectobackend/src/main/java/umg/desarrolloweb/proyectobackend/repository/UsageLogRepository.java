package umg.desarrolloweb.proyectobackend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import umg.desarrolloweb.proyectobackend.entity.UsageLog;

@Repository("userLogRepository")
public interface UsageLogRepository extends JpaRepository<UsageLog, Integer> {
	@Query(value = "SELECT t.event_type, COALESCE(COUNT(u.event_type), 0) AS count " +
	         "FROM (SELECT 0 AS event_type UNION SELECT 1 UNION SELECT 2) t " +
	         "LEFT JOIN USAGE_LOG u ON u.event_type = t.event_type " +
	         "AND u.timestamp BETWEEN :startDate AND :endDate " +
	         "GROUP BY t.event_type " +
	         "ORDER BY t.event_type", nativeQuery = true)
	List<Object[]> countEventsByTypeInDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
