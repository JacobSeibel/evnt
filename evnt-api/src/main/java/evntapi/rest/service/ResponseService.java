package evntapi.rest.service;

import evntapi.domain.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ResponseService {
    List<Response> findAll();
    Response findByPk(int pk);
}
