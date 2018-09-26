package evntapi.rest.service;

import evntapi.domain.QueuedEmail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QueuedEmailService {
    List<QueuedEmail> findAll();
    QueuedEmail insert(QueuedEmail queuedEmail);
    void delete(int pk);
}