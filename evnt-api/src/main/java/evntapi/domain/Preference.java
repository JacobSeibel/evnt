package evntapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class Preference {
    private Integer pk;
    private String name;
    private String description;
    private boolean isActive;
}
