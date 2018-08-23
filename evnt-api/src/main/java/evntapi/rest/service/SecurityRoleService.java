package evntapi.rest.service;

import evntapi.domain.SecurityRole;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SecurityRoleService {
    List<SecurityRole> findAll();
}
