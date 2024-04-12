package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.dto.note.NoteDTO;
import com.br.notesapp.notesappserver.dto.note.UpdateNoteRequestDTO;
import com.br.notesapp.notesappserver.dto.user.CreateNoteRequestDTO;

import java.util.List;

public interface NoteService {

    NoteDTO insert(CreateNoteRequestDTO note);

    NoteDTO findById(String id);

    List<NoteDTO> find();

    void deleteById(String id);

    void update(UpdateNoteRequestDTO request);

}
