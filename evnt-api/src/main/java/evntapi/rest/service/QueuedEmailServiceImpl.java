package evntapi.rest.service;

import evntapi.domain.EventUser;
import evntapi.domain.QueuedEmail;
import evntapi.rest.mapper.EventUserMapper;
import evntapi.rest.mapper.QueuedEmailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueuedEmailServiceImpl implements QueuedEmailService{
    @Autowired
    private QueuedEmailMapper mapper;

    @Override
    public List<QueuedEmail> findAll() {
        return mapper.findAll();
    }

    @Override
    public QueuedEmail find(int emailFk, int recipientFk, int eventFk) {
        return mapper.find(emailFk, recipientFk, eventFk);
    }

    @Override
    public QueuedEmail insert(QueuedEmail queuedEmail) {
        mapper.insert(queuedEmail);
        return queuedEmail;
    }

    @Override
    public void delete(int emailFk, int recipientFk, int eventFk) {
        mapper.delete(emailFk, recipientFk, eventFk);
    }

    @Override
    public void deleteByRecipientAndEvent(int recipientFk, int eventFk) {
        mapper.deleteByRecipientAndEvent(recipientFk, eventFk);
    }
}
