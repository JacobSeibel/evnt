package evntapi.rest.mapper;

import evntapi.domain.Response;
import evntapi.domain.Role;

import java.util.List;

public interface ResponseMapper {
    List<Response> findAll();
    Response findByPk(int pk);
}
