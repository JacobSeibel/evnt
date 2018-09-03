package evntapi.rest.mapper;

import evntapi.domain.Role;
import evntapi.domain.SecurityRole;

import java.util.List;

public interface RoleMapper {
    List<Role> findAll();
    Role findByPk(int pk);
}
