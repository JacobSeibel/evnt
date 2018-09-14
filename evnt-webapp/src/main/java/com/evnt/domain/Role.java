package com.evnt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Role {
    public static final int CREATOR = 1;
    public static final int HOST = 2;
    public static final int GUEST = 3;

    private Integer pk;
    private String name;
    private String description;
    private boolean isActive;

    public static boolean isCreator(Role role){
        return role != null && role.pk == CREATOR;
    }

    public static boolean isHost(Role role){
        return role != null && role.pk == HOST;
    }

    public static boolean isGuest(Role role){
        return role != null && role.pk == GUEST;
    }
}
