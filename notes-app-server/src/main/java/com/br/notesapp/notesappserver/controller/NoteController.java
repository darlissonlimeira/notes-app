package com.br.notesapp.notesappserver.controller;

import com.br.notesapp.notesappserver.api.SuccessResponse;
import com.br.notesapp.notesappserver.dto.CreateNoteRequestDTO;
import com.br.notesapp.notesappserver.dto.DeleteNoteRequestDTO;
import com.br.notesapp.notesappserver.dto.NoteDTO;
import com.br.notesapp.notesappserver.dto.UpdateNoteRequestDTO;
import com.br.notesapp.notesappserver.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @DeleteMapping()
    public ResponseEntity deleteById(@RequestBody DeleteNoteRequestDTO request) {
        service.deleteById(request.id());
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<SuccessResponse> findAll() {
        List<NoteDTO> noteDTOS = service.find();
        return ResponseEntity.ok(new SuccessResponse(noteDTOS));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> findById(@PathVariable String id) {
        NoteDTO note = service.findById(id);
        return ResponseEntity.ok(new SuccessResponse(note));
    }

    @PostMapping()
    public ResponseEntity<SuccessResponse> create(@RequestBody CreateNoteRequestDTO request) {
        NoteDTO note = service.insert(request);
        return ResponseEntity.ok(new SuccessResponse(note));
    }

    @PutMapping()
    public ResponseEntity update(@RequestBody @Valid UpdateNoteRequestDTO request) {
        service.update(request);
        return ResponseEntity.noContent().build();
    }
}
