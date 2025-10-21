package umg.desarrolloweb.proyectobackend.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;

import umg.desarrolloweb.proyectobackend.DateRangeRequest;
import umg.desarrolloweb.proyectobackend.entity.UsageLog;
import umg.desarrolloweb.proyectobackend.entity.UsageLog.EventType;
import umg.desarrolloweb.proyectobackend.repository.UsageLogRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UsageLogController {

	private final UsageLogRepository usageLogRepository;

	public UsageLogController(UsageLogRepository usageLogRepository) {
		this.usageLogRepository = usageLogRepository;
	}

	@PostMapping("/noauth/enter-usage-log")
	public ResponseEntity<?> enterUsageLog(@RequestParam EventType eventType, HttpServletRequest request) {
		try {
			
			if (eventType == null) {
				return ResponseEntity.badRequest().body("Event type is required");
			}

			
			UsageLog newLog = new UsageLog();
			newLog.setIpAddress(request.getRemoteAddr()); 
			newLog.setTimestamp(LocalDate.now());
			newLog.setEventType(eventType);

			
			usageLogRepository.saveAndFlush(newLog);

			
			return ResponseEntity.ok("Usage log entered successfully");

		} catch (IllegalArgumentException e) {
			
			return ResponseEntity.badRequest().body("Invalid data: " + e.getMessage());

		} catch (DataAccessException e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Database error: Unable to save usage log");

		} catch (Exception e) {
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An unexpected error occurred: " + e.getMessage());
		}
	}

	@PostMapping("/auth/view-stats") 
	public ResponseEntity<Map<String, Object>> viewUsageStats(@RequestBody DateRangeRequest dateRange) {

		
		if (dateRange.getRangoInferior() == null || dateRange.getRangoSuperior() == null
				|| dateRange.getRangoInferior().isAfter(dateRange.getRangoSuperior())) {
			return ResponseEntity.badRequest().body(Map.of("error", "Invalid date range"));
		}

		
		LocalDateTime startDateTime = dateRange.getRangoInferior().atStartOfDay();
		LocalDateTime endDateTime = dateRange.getRangoSuperior().atTime(23, 59, 59);

		
		List<Object[]> results = usageLogRepository.countEventsByTypeInDateRange(startDateTime, endDateTime);

		
		Map<Integer, Long> eventCounts = new HashMap<>();
		eventCounts.put(0, 0L); 
		eventCounts.put(1, 0L); 
		eventCounts.put(2, 0L); 

		for (Object[] result : results) {
			Integer eventType = ((Number) result[0]).intValue();
			Long count = ((Number) result[1]).longValue();
			eventCounts.put(eventType, count);
		}

		
		Map<String, Object> response = new HashMap<>();
		response.put("visits", eventCounts.get(0));
		response.put("successes", eventCounts.get(1));
		response.put("failures", eventCounts.get(2));
		response.put("startDate", dateRange.getRangoInferior());
		response.put("endDate", dateRange.getRangoSuperior());

		return ResponseEntity.ok(response);
	}
}
