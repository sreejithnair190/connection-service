package me.sreejithnair.linkup.connection_service.service.impl;


import lombok.RequiredArgsConstructor;
import me.sreejithnair.linkup.connection_service.node.Person;
import me.sreejithnair.linkup.connection_service.repository.PersonRepository;
import me.sreejithnair.linkup.connection_service.service.ConnectionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    private final PersonRepository personRepository;

    @Override
    public List<Person> getFirstDegreeConnections(Long userId) {
        return personRepository.getFirstDegreeConnections(userId);
    }
}
