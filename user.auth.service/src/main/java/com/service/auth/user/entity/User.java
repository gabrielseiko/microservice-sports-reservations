package com.service.auth.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser")
    private int iduser;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "lastname")
    private String lastname;
    @Column(name = "dni")
    private String dni;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tbl_user_has_role",
               joinColumns = @JoinColumn(name = "iduser"),
               inverseJoinColumns = @JoinColumn(name = "idrole"))
    private List<Role> roles;
}
