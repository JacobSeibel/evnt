package evntapi.rest.service;

import evntapi.domain.QueuedEmail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QueuedEmailService {
    List<QueuedEmail> findAll();
    QueuedEmail find(int emailFk, int recipientFk, int eventFk);
    QueuedEmail insert(QueuedEmail queuedEmail);
    void delete(int emailFk, int recipientFk, int eventFk);
    void deleteByRecipientAndEvent(int recipientFk, int eventFk);
}