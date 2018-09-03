package evntapi.rest.controller;

import evntapi.domain.Response;
import evntapi.rest.RestConstants;
import evntapi.rest.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RestConstants.EVNT_API+"/response")
public class ResponseController {

    @Autowired
    private ResponseService responseService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Response> findAll(){
        return responseService.findAll();
    }

    @GetMapping("/{pk}")
    @ResponseStatus(HttpStatus.OK)
    public Response findByPk(@PathVariable("pk") int pk){
        return responseService.findByPk(pk);
    }
}
