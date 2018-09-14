package evntapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class Role {
    public static final int CREATOR = 1;
    public static final int HOST = 2;
    public static final int GUEST = 3;

    private Integer pk;
    private String name;
    private String description;
    private boolean isActive;

    public Role(int pk){
        this.pk = pk;
    }
}
