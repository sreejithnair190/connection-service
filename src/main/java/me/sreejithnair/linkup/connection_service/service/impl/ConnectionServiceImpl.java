package me.sreejithnair.linkup.connection_service.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sreejithnair.linkup.connection_service.event.AcceptConnectionRequestEvent;
import me.sreejithnair.linkup.connection_service.event.SendConnectionRequestEvent;
import me.sreejithnair.linkup.connection_service.exception.ResourceAlreadyExistsException;
import me.sreejithnair.linkup.connection_service.node.Person;
import me.sreejithnair.linkup.connection_service.repository.PersonRepository;
import me.sreejithnair.linkup.connection_service.service.ConnectionService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static me.sreejithnair.linkup.connection_service.auth.UserContextHolder.getCurrentUserId;
import static me.sreejithnair.linkup.connection_service.constants.Event.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConnectionServiceImpl implements ConnectionService {

    private final PersonRepository personRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestEventKafkaTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestEventKafkaTemplate;

    @Override
    public List<Person> getFirstDegreeConnections(Long userId) {
        log.info("Getting first degree connections of user: {}", userId);
        return personRepository.getFirstDegreeConnections(userId);
    }

    @Override
    public String sendConnectionRequest(Long receiverId) {
        Long senderId = getCurrentUserId();

        log.info("Sending Connection Request senderId: {}, receiverId: {}", senderId, receiverId);

        if (Objects.equals(senderId, receiverId)) {
            throw  new RuntimeException("Sender and receiver cannot be same");
        }

        boolean requestAlreadySent = personRepository.connectionRequestExist(senderId, receiverId);
        if (requestAlreadySent) {
            throw new ResourceAlreadyExistsException("Connection request already exists!");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new ResourceAlreadyExistsException("Connection already exists!");
        }

        personRepository.addConnectionRequest(senderId, receiverId);

        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        sendConnectionRequestEventKafkaTemplate.send(CONNECTION_REQUEST_SENT, sendConnectionRequestEvent);

        return "Connection Request Sent Successfully!";
    }

    @Override
    public String acceptConnectionRequest(Long senderId) {
        Long receiverId = getCurrentUserId();

        log.info("Accept Connection Request senderId: {}, receiverId: {}", senderId, receiverId);

        if (Objects.equals(senderId, receiverId)) {
            throw  new RuntimeException("Sender and receiver cannot be same");
        }

        boolean requestExist = personRepository.connectionRequestExist(senderId, receiverId);
        if (!requestExist) {
            throw new ResourceNotFoundException("Connection request does not exists!");
        }

        boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
        if (alreadyConnected) {
            throw new ResourceAlreadyExistsException("Connection already exists!");
        }

        personRepository.acceptConnectionRequest(senderId, receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();

        acceptConnectionRequestEventKafkaTemplate.send(CONNECTION_REQUEST_ACCEPTED, acceptConnectionRequestEvent);

        return "Connection Request Accepted Successfully!";
    }

    @Override
    public Object rejectConnectionRequest(Long senderId) {
        Long receiverId = getCurrentUserId();

        log.info("Reject Connection Request senderId: {}, receiverId: {}", senderId, receiverId);

        if (Objects.equals(senderId, receiverId)) {
            throw  new RuntimeException("Sender and receiver cannot be same");
        }

        boolean requestExist = personRepository.connectionRequestExist(senderId, receiverId);
        if (!requestExist) {
            throw new ResourceNotFoundException("Connection request does not exists!");
        }

        personRepository.rejectConnectionRequest(senderId, receiverId);

        return "Connection Request Rejected Successfully!";

    }
}
