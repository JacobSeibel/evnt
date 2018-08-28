package evntapi.rest.service;

import evntapi.domain.SecurityRole;
import evntapi.rest.mapper.SecurityRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecurityRoleServiceImpl implements SecurityRoleService{
    @Autowired
    private SecurityRoleMapper mapper;

    @Override
    public List<SecurityRole> findAll() {
        return mapper.findAll();
    }

    @Override
    public SecurityRole findByPk(int pk) {
        return mapper.findByPk(pk);
    }
}
