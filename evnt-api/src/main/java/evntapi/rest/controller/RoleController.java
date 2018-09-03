package evntapi.rest.controller;

import evntapi.domain.Role;
import evntapi.domain.SecurityRole;
import evntapi.rest.RestConstants;
import evntapi.rest.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.EVNT_API+"/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Role> findAll(){
        return roleService.findAll();
    }

    @GetMapping("/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public Role findByPk(@PathVariable("pk") int pk){
        return roleService.findByPk(pk);
    }
}
