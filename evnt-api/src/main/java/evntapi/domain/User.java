package evntapi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.util.List;

@Getter @Setter @ToString
public class User {
    private int pk;
    private String username;
    private String password;
    private String email;
    private String cellNumber;
    //TODO: Figure this out
    private String profilePic;
    private boolean isActive;
    private String firstName;
    private String lastName;

    private List<SecurityRole> securityRoles;
}
