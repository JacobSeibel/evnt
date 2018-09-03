package com.evnt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter @Setter @ToString
public class SecurityRole implements GrantedAuthority{

    public static final int ROLE_ADMIN_PK = 1;
    public static final int ROLE_USER_PK = 2;

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    private int pk;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
