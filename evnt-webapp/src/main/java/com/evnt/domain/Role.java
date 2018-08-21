package com.evnt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Role {
    private Integer pk;
    private String name;
    private String description;
    private boolean isActive;
}
