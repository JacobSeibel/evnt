package evntapi.persistence;

import evntapi.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.EVNT_API+"/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User findByUsername(@PathVariable("username") String username){
        return userService.findByUsername(username);
    }
}
