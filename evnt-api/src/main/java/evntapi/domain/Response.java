package evntapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @NoArgsConstructor
public class Response {
    public static final int GOING = 1;
    public static final int MAYBE = 2;
    public static final int CANT_GO = 3;

    private Integer pk;
    private String name;
    private String description;
    private boolean isActive;

    public Response(int pk){
        this.pk = pk;
    }
}
