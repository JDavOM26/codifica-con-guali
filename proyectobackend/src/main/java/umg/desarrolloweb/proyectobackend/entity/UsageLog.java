package umg.desarrolloweb.proyectobackend.entity;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "USAGE_LOG")
@Data
public class UsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usage_log")
    private Integer idUsageLog;

    @Column(name = "timestamp", nullable = false)
    private LocalDate timestamp; 

    @Column(name = "ip_address", nullable = true) 
    private String ipAddress;

    @Column(name = "event_type", nullable = false)
    private EventType eventType; 

   
    public enum EventType {
        VISIT, SUCCESS, FAIL
    }
}