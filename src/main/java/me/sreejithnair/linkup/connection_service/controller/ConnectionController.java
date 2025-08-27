package me.sreejithnair.linkup.connection_service.controller;

import lombok.RequiredArgsConstructor;
import me.sreejithnair.linkup.connection_service.common.ApiResponse;
import me.sreejithnair.linkup.connection_service.node.Person;
import me.sreejithnair.linkup.connection_service.service.ConnectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/send-connection/{userId}")
    public ResponseEntity<ApiResponse<Object>> sendConnectionRequest(@PathVariable Long userId) {
        Object message = connectionService.sendConnectionRequest(userId);
        ApiResponse<Object> apiResponse = new ApiResponse<>(message);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/accept-connection/{userId}")
    public ResponseEntity<ApiResponse<Object>> acceptConnectionRequest(@PathVariable Long userId) {
        Object message = connectionService.acceptConnectionRequest(userId);
        ApiResponse<Object> apiResponse = new ApiResponse<>(message);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/reject-connection/{userId}")
    public ResponseEntity<ApiResponse<Object>> rejectConnectionRequest(@PathVariable Long userId) {
        Object message = connectionService.rejectConnectionRequest(userId);
        ApiResponse<Object> apiResponse = new ApiResponse<>(message);
        return ResponseEntity.ok(apiResponse);
    }
}
