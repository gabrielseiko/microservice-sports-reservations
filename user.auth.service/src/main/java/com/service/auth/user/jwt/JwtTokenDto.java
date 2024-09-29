package com.service.auth.user.jwt;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
@Data
@ToString
public class JwtTokenDto {

    private String token;
    private String bearer = "Bearer";
    private int iduser;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtTokenDto(String token, String login, int iduser, Collection<? extends GrantedAuthority> authorities){
        this.token = token;
        this.username = login;
        this.authorities = authorities;
        this.iduser = iduser;
    }
}
