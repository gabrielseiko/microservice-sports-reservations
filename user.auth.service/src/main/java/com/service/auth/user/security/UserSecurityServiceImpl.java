package com.service.auth.user.security;

import com.service.auth.user.entity.Role;
import com.service.auth.user.entity.User;
import com.service.auth.user.repository.UserRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CommonsLog
@Qualifier("userSecurityServiceImpl")
public class UserSecurityServiceImpl implements UserDetailsService {
//service para gestionar la autenticacion y carga de usuarios
    //implementacion de userdetailsservice, cargar y proporciona detalles del un usuario durante la autenticacion

    @Autowired
    private UserRepository userRepository;

    //metodo para cargar los detalles del usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(">> loadUserByUsername >> " + username);
        try {
            // Búsqueda del usuario en la BD usando el repositorio
            User objUser = userRepository.findByUsername(username);

            if (objUser != null) {
                log.info("=> Login == " + objUser);

                List<Role> listRole = userRepository.getRolesUser(objUser.getIduser());
                log.info("=> Role == " + listRole);

                // Construye y retorna el UserDetails usando MainUser
                return MainUser.build(objUser, listRole);
            } else {
                throw new UsernameNotFoundException("Usuario no encontrado con el nombre de usuario: " + username);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new UsernameNotFoundException("Usuario incorrecto: " + username);
        } catch (DataAccessException e) {
            log.error("Error de acceso a datos", e);
            throw new UsernameNotFoundException("Error en la base de datos");
        } catch (Exception e) {
            log.error("Error desconocido", e);
            throw new UsernameNotFoundException("Error desconocido");
        }
    }

}
