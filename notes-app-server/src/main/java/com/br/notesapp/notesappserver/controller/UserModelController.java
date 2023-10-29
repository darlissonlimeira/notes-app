package com.br.notesapp.notesappserver.controller;

import com.br.notesapp.notesappserver.api.SuccessResponse;
import com.br.notesapp.notesappserver.dto.CreateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.DeleteUserRequestDTO;
import com.br.notesapp.notesappserver.dto.UpdateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.UserModelDTO;
import com.br.notesapp.notesappserver.service.UserModelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserModelController {

    private final UserModelService service;

    public UserModelController(UserModelService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<SuccessResponse> findAll() {
        List<UserModelDTO> users = service.find();
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
        service.deleteOne(request.id());
        return ResponseEntity.noContent().build();
    }

}
