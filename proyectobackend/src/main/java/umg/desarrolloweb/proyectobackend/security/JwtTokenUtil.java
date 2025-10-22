package umg.desarrolloweb.proyectobackend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

        @Value("${jwt.secret}")
        private String jwtSecret;

        @Value("${jwt.expiration}")
        private int jwtExpirationMs;

        private SecretKey key;

        
        @PostConstruct
        public void init() {
            this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        }

      
        public String generateToken(String username) {
            return Jwts.builder()
                    .subject(username)
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                    .signWith(key, Jwts.SIG.HS256)
                    .compact();
        }

     
        public String getUsernameFromToken(String token) {
    Jwt<?, ?> jwt = Jwts.parser()
            .verifyWith(key)              
            .build()                      
            .parseSignedClaims(token);    

    Claims claims = (Claims) jwt.getPayload();
    return claims.getSubject();
}
        
        
 
    public boolean validateJwtToken(String token) {
    try {
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token); 

        return true;
    } catch (ExpiredJwtException e) {
        logger.error("JWT token expirado", e);
        return false;
    } catch (JwtException e) {
      
        logger.error("Token JWT inválido", e);
        return false;
    } catch (Exception e) {
        logger.error("Error al validar el token JWT", e);
        return false;
    }
}
}