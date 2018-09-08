package evntapi.rest.mapper;

import evntapi.domain.EventUser;
import evntapi.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EventUserMapper {
    List<EventUser> findByEventFk(int pk);
    void insert(EventUser eventUser);
    void update(EventUser eventUser);
    void delete(@Param("eventFk") int eventFk, @Param("userFk") int userFk);
}
