package com.service.auth.user.security;

import com.service.auth.user.jwt.JwtToken;
import com.service.auth.user.jwt.JwtTokenDto;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CommonsLog
@RestController
@RequestMapping("/url/auth")
public class AuthController {
//controlador para manejar las peticiones de autenticacion y permisos

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtToken jwtToken;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        log.info("> login >> " + loginRequestDto.getUsername());
        log.info("> login >> " + loginRequestDto.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

            log.info("> authentication >> " + authentication);

            // Generación de token
            log.info("> Generacion de token <");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtToken.generateToken(authentication);
            log.info(">> Jwt >> " + jwt);

            // Validación en la BD
            MainUser user = (MainUser) authentication.getPrincipal();
            log.info("> usuario >> " + user.toString());

            JwtTokenDto jwtTokenDto = new JwtTokenDto(jwt, user.getUsername(), user.getIduser(), user.getAuthorities());
            log.info(">> jwtTokenDto " + jwtTokenDto.toString());

            return new ResponseEntity<>(jwtTokenDto, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error en la autenticación: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
