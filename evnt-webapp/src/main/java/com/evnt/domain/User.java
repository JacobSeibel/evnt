package com.evnt.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.beans.Transient;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Getter @Setter @ToString
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    private static final String USE_APP_ROLE = "USE-APP-ROLE";

    private int pk;
    private String username;
    private String password;
    private String email;
    private String cellNumber;
    //TODO: Figure this out
    private File profilePic;
    private boolean isActive;
    private String firstName;
    private String lastName;

    private boolean enabled = true;
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;

    @SuppressWarnings("serial")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> result = new ArrayList<>();
        result.add((GrantedAuthority) () -> USE_APP_ROLE);

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Transient
    public void setUnencryptedPassword(String password) {
        setPassword(new BCryptPasswordEncoder().encode(password));
    }

    public String getFullName(){
        return firstName + " " + lastName;
    }
}
