package umg.desarrolloweb.proyectobackend;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DateRangeRequest {
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rangoInferior;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate rangoSuperior;

}