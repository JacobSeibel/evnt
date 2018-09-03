package evntapi.rest.service;

import evntapi.domain.Response;
import evntapi.domain.SecurityRole;
import evntapi.rest.mapper.ResponseMapper;
import evntapi.rest.mapper.SecurityRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseServiceImpl implements ResponseService{
    @Autowired
    private ResponseMapper mapper;

    @Override
    public List<Response> findAll() {
        return mapper.findAll();
    }

    @Override
    public Response findByPk(int pk) {
        return mapper.findByPk(pk);
    }
}
