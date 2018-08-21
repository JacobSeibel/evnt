package evntapi.persistence.mapper;

import evntapi.domain.User;

import java.util.List;

public interface UserMapper {
    List<User> findAll();
}
