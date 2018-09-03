package evntapi.rest.service;

import evntapi.domain.Role;
import evntapi.domain.SecurityRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    List<Role> findAll();
    Role findByPk(int pk);
}
