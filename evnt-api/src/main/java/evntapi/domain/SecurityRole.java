package evntapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class SecurityRole{

    private int pk;
    private String name;
    private boolean isActive;
}
