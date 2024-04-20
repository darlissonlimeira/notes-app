package com.br.notesapp.notesappserver.controller;

import com.br.notesapp.notesappserver.dto.api.SuccessResponse;
import com.br.notesapp.notesappserver.dto.user.CreateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.DeleteUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UpdateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UserModelDTO;
import com.br.notesapp.notesappserver.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserModelController {

    private final UserService service;

    public UserModelController(UserService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<SuccessResponse> findAll() {
        List<UserModelDTO> users = service.findAll();
        return ResponseEntity.ok(new SuccessResponse(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> findById(@PathVariable String id) {
        UserModelDTO user = service.findById(id);
        return ResponseEntity.ok(new SuccessResponse(user));
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> create(@RequestBody @Valid CreateUserRequestDTO request) {
        UserModelDTO user = service.insert(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse(user));
    }

    @PutMapping()
    public ResponseEntity<SuccessResponse> update(@RequestBody @Valid UpdateUserRequestDTO request) {
        service.update(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity<SuccessResponse> delete(@RequestBody @Valid DeleteUserRequestDTO request) {
        service.deleteById(request.id());
        return ResponseEntity.noContent().build();
    }

}
