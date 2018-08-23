package com.evnt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@Getter @Setter @ToString
public class SecurityRole implements GrantedAuthority{

    private int pk;
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
