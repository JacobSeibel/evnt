package evntapi.rest.service;

import evntapi.domain.Role;
import evntapi.domain.SecurityRole;
import evntapi.rest.mapper.RoleMapper;
import evntapi.rest.mapper.SecurityRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleMapper mapper;

    @Override
    public List<Role> findAll() {
        return mapper.findAll();
    }

    @Override
    public Role findByPk(int pk) {
        return mapper.findByPk(pk);
    }
}
