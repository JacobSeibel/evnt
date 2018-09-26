package evntapi.rest.mapper;

import evntapi.domain.QueuedEmail;

import java.util.List;

public interface QueuedEmailMapper {
    List<QueuedEmail> findAll();
    void insert(QueuedEmail queuedEmail);
    void delete(int pk);
}
