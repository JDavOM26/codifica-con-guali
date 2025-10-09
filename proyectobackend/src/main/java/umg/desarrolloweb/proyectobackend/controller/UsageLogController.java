package umg.desarrolloweb.proyectobackend.controller;

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
        // Validar que el eventType no sea nulo
        if (eventType == null) {
            return ResponseEntity.badRequest().body("Event type is required");
        }

        // Crear un nuevo log
        UsageLog newLog = new UsageLog();
        newLog.setIpAddress(request.getRemoteAddr()); // Obtener IP del cliente
        newLog.setTimestamp(LocalDateTime.now()); // Establecer la hora actual
        newLog.setEventType(eventType);

        // Guardar en la base de datos
        usageLogRepository.save(newLog);

        // Devolver respuesta exitosa
        return ResponseEntity.ok("Usage log entered successfully");
    }
    
    
    @PostMapping("/auth/view-stats")  // Cambiar de GET a POST
    public ResponseEntity<Map<String, Object>> viewUsageStats(@RequestBody DateRangeRequest dateRange) {
        
        // Validate date range
        if (dateRange.getRangoInferior() == null || dateRange.getRangoSuperior() == null || 
            dateRange.getRangoInferior().isAfter(dateRange.getRangoSuperior())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid date range"));
        }

        // Convert LocalDate to LocalDateTime (start of day and end of day)
        LocalDateTime startDateTime = dateRange.getRangoInferior().atStartOfDay();
        LocalDateTime endDateTime = dateRange.getRangoSuperior().atTime(23, 59, 59);

        // Query the repository
        List<Object[]> results = usageLogRepository.countEventsByTypeInDateRange(startDateTime, endDateTime);

        // Process results into a structured response
        Map<Integer, Long> eventCounts = new HashMap<>();
        eventCounts.put(0, 0L); // VISIT
        eventCounts.put(1, 0L); // SUCCESS
        eventCounts.put(2, 0L); // FAIL

        for (Object[] result : results) {
            Integer eventType = ((Number) result[0]).intValue();
            Long count = ((Number) result[1]).longValue();
            eventCounts.put(eventType, count);
        }

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("visits", eventCounts.get(0));
        response.put("successes", eventCounts.get(1));
        response.put("failures", eventCounts.get(2));
        response.put("startDate", dateRange.getRangoInferior());
        response.put("endDate", dateRange.getRangoSuperior());

        return ResponseEntity.ok(response);
    }
}