package me.sreejithnair.linkup.connection_service.controller;

import lombok.RequiredArgsConstructor;
import me.sreejithnair.linkup.connection_service.node.Person;
import me.sreejithnair.linkup.connection_service.service.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class ConnectionController {
    private final ConnectionService connectionService;

    @GetMapping("/{userId}/first-connections")
    public ResponseEntity<List<Person>> getFirstConnections(@PathVariable Long userId) {
        List<Person> people = connectionService.getFirstDegreeConnections(userId);
        return ResponseEntity.ok(people);
    }
}
