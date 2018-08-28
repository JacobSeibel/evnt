package com.evnt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter @Setter @ToString
public class SecurityRole implements GrantedAuthority{

    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_USER = 2;

    private int pk;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
