package evntapi.rest.mapper;

import evntapi.domain.Email;

import java.util.List;

public interface EmailMapper {
    List<Email> findAll();
    Email findByPk(int pk);
}
