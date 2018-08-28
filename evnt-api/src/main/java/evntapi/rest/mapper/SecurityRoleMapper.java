package evntapi.rest.mapper;

import evntapi.domain.SecurityRole;

import java.util.List;

public interface SecurityRoleMapper {
    List<SecurityRole> findAll();
    SecurityRole findByPk(int pk);
}
