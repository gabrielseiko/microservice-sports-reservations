package com.service.auth.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_user_has_role")
public class UserHasRole {

    @Id
    @ManyToOne
    @JoinColumn(name = "iduser")
    private User iduser;

    @Id
    @ManyToOne
    @JoinColumn(name = "idrole")
    private Role idrole;
}
