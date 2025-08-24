package me.sreejithnair.linkup.connection_service.service;

import me.sreejithnair.linkup.connection_service.node.Person;

import java.util.List;

public interface ConnectionService {
    List<Person> getFirstDegreeConnections(Long userId);
}
