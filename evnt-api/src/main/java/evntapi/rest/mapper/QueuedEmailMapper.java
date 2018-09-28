package evntapi.rest.mapper;

import evntapi.domain.QueuedEmail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QueuedEmailMapper {
    List<QueuedEmail> findAll();
    QueuedEmail find(@Param("emailFk") int emailFk, @Param("recipientFk") int recipientFk, @Param("eventFk") int eventFk);
    void insert(QueuedEmail queuedEmail);
    void delete(@Param("emailFk") int emailFk, @Param("recipientFk") int recipientFk, @Param("eventFk") int eventFk);
    void deleteByRecipientAndEvent(@Param("recipientFk") int recipientFk, @Param("eventFk") int eventFk);
}
