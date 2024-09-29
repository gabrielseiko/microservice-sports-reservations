package com.service.auth.user.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

@CommonsLog
public class JwtTokenFilter extends OncePerRequestFilter {
//clase para filtrar las solicitudes http, extrae el token y lo valida

    @Autowired
    JwtToken jwtToken;

    @Autowired
    UserDetailsService userDetailsService;

    //manejo de la logica de autenticacion de las solicitudes http
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(">> Ingreso al filtro de token <<");
        try {
            String token = getToken(request); //extrae el token
            log.info(">> TOKEN llego =>>> " + token);

            //validacion del token
            if (token != null && jwtToken.validateToken(token)) { //valida el token de la clase validateToken
                //extrae el nombre del usuario del token
                String nameUser = jwtToken.verifyToken(token);
                log.info(">> Token filter >> nombre de usuario: " + nameUser);

                //cargar detalles del usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(nameUser); //utilizamos el nombre extraido previamente
                //creacion de objt con los detalles del usuario y roles
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                //configuracion del contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(auth); //decimos que el usuario esta autenticado y damos permiso
            }
        } catch(Exception e){
            log.error("FAIL en el metodo doFilter " + e.getMessage());
        }
        //continua el proceso
        filterChain.doFilter(request, response);
    }

    //metodo para extraer el token desde la solicitud http
    private String getToken(HttpServletRequest request){
        //obtiene los encabezados de la peticion http
        Enumeration<String> headerNames = request.getHeaderNames();

        //imprimir los encabezados en log
        //recorre cada encabezado usando getHeader
        // y se registra en cada log
        if(headerNames !=  null){
            while (headerNames.hasMoreElements()){
                String headename = headerNames.nextElement();
                log.info("HEADER: >> " + headename + ">> " + request.getHeader(headename));
            }
        }
        //obtener encabezado de AUTHORIZATION
        String header = request.getHeader("Authorization");
        log.info("<< Header >> " + header);

        //validacion del formato del token
        if(header != null && header.startsWith("Bearer"))
            return header.replace("Bearer ", "");
        return null;
    }
}
