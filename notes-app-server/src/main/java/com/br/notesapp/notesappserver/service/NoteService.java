package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.dto.note.NoteDTO;
import com.br.notesapp.notesappserver.dto.note.UpdateNoteRequestDTO;
import com.br.notesapp.notesappserver.dto.user.CreateNoteRequestDTO;
import com.br.notesapp.notesappserver.exception.NoteNotFoundException;
import com.br.notesapp.notesappserver.exception.UserNotFoundException;
import com.br.notesapp.notesappserver.model.Note;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.repository.NoteRepository;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import com.br.notesapp.notesappserver.utils.Mapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoteService {

    private NoteRepository noteRepository;

    private UserModelRepository userRepository;

    private Mapper mapper;

    public NoteService(NoteRepository noteRepository, UserModelRepository userRepository, Mapper mapper) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }


    public NoteDTO insert(CreateNoteRequestDTO request) {
        UserModel user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.format("user not found with id: %s", request.getUserId())));

        Note note = mapper.map(request, Note.class);
        note.setUser(user);
        Note newNote = noteRepository.insert(note);

        return mapper.map(newNote, NoteDTO.class);
    }


    public NoteDTO findById(String id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException(String.format("note not found with id: %s", id)));

        return mapper.map(note, NoteDTO.class);
    }

    public List<NoteDTO> find() {
        List<Note> notes = noteRepository.findAll();

        return notes.stream().map(note -> mapper.map(note, NoteDTO.class)).toList();
    }

    public void deleteById(String id) {
        noteRepository.deleteById(id);
    }

    public void update(UpdateNoteRequestDTO request) {
        Note note = noteRepository.findById(request.getId())
                .orElseThrow(() -> new NoteNotFoundException(String.format("note not found with id: %s", request.getId())));

        note.setTitle(request.getTitle());
        note.setText(request.getText());
        note.setCompleted(request.isCompleted());
        note.setUpdatedAt(new Date());
        noteRepository.save(note);
    }
}
