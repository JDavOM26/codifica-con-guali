package umg.desarrolloweb.proyectobackend.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import umg.desarrolloweb.proyectobackend.entity.ActionLog;
import umg.desarrolloweb.proyectobackend.repository.ActionLogRepository;

@RestController
@RequestMapping("/api/admin")
public class ActionLogController {

	private final ActionLogRepository actionLogRepository;
	
	public ActionLogController ( ActionLogRepository actionLogRepository) {
		this.actionLogRepository = actionLogRepository;
	}
	@PostMapping("/enter-action-log")
	public ResponseEntity<?> enterUsageLog(@RequestParam Integer idUser, @RequestParam String action) {
	    ActionLog newLog = new ActionLog();
	    newLog.setIdUser(idUser);
	    newLog.setTimestamp(LocalDateTime.now());
	    newLog.setAction(action);
	    actionLogRepository.save(newLog);
	    return ResponseEntity.ok("Usage log entered successfully");
	}
	
	@GetMapping("/get-all-historics")
	public List<ActionLog> getAllHistorics(){
		return actionLogRepository.findAll();
	}
}
