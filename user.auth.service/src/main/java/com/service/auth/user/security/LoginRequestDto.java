package com.service.auth.user.security;

import lombok.Data;

@Data
public class LoginRequestDto {
    //DTO para encapsular los datos de la solicitud de autenticacion (login)

    private String username;
    private String password;
}
