package com.service.auth.user.repository;

import com.service.auth.user.entity.Role;
import com.service.auth.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    public abstract User findByUsername(String username);

    // Consulta para obtener los roles de un usuario específico usando la relación ManyToMany
    @Query("SELECT r FROM User u JOIN u.roles r WHERE u.iduser = ?1")
    List<Role> getRolesUser(int iduser);

}
