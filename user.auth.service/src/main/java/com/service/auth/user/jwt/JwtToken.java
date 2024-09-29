package com.service.auth.user.jwt;

import com.service.auth.user.security.MainUser;
import io.jsonwebtoken.security.Keys;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@CommonsLog
@Component
public class JwtToken {
    //creacion del token

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private Duration expiration = Duration.ofHours(10); //10 horas

    public String generateToken(Authentication authentication){
        log.info(">> generateToken <<");

        MainUser mainUser = (MainUser) authentication.getPrincipal();

        return Jwts.builder().setSubject(mainUser.getUsername()) //establece usuario
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration.toMillis()))//establece la expiracion
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)// FIRMA DEL TOKEN
                .compact(); //genera firma del token
    }

    //metodo para extraer informacion del token si es valido del usuario
    public String verifyToken(String token){
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    //metodo para verificar si el token es valido sin manipulaciones externas
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e){
            log.error(">> validateToken >> token mal formado");
        } catch (UnsupportedJwtException e){
            log.error((">> validateToken >> token no soportado "));
        } catch (ExpiredJwtException e){
            log.error(">> validateToken >> token expirado");
        } catch (IllegalArgumentException e) {
            log.error((">> validateToken >> token vacío"));
        } catch (SignatureException e){
            log.error(">> validateToken >> error en la firma");
        }
        return false;
    }
}
