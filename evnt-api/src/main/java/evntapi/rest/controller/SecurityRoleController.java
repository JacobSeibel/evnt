package evntapi.rest.controller;

import evntapi.domain.SecurityRole;
import evntapi.rest.RestConstants;
import evntapi.rest.service.SecurityRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.EVNT_API+"/securityRole")
public class SecurityRoleController {

    @Autowired
    private SecurityRoleService securityRoleService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SecurityRole> findAll(){
        return securityRoleService.findAll();
    }

    @GetMapping("/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public SecurityRole findByPk(@PathVariable("pk") int pk){
        return securityRoleService.findByPk(pk);
    }
}
