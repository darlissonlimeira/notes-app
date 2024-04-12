package com.br.notesapp.notesappserver.service.impl;

import com.br.notesapp.notesappserver.dto.note.NoteDTO;
import com.br.notesapp.notesappserver.dto.note.UpdateNoteRequestDTO;
import com.br.notesapp.notesappserver.dto.user.CreateNoteRequestDTO;
import com.br.notesapp.notesappserver.exception.NoteNotFoundException;
import com.br.notesapp.notesappserver.exception.UserNotFoundException;
import com.br.notesapp.notesappserver.model.Note;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.repository.NoteRepository;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import com.br.notesapp.notesappserver.service.NoteService;
import com.br.notesapp.notesappserver.utils.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private NoteRepository noteRepository;

    private UserModelRepository userRepository;

    private ObjectMapper mapper;

    public NoteServiceImpl(NoteRepository noteRepository, UserModelRepository userRepository, ObjectMapper mapper) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public NoteDTO insert(CreateNoteRequestDTO request) {
        Optional<UserModel> user = userRepository.findById(request.getUserId());
        if (user.isEmpty()) {
            throw new UserNotFoundException(String.format("user not found with id: %s", request.getUserId()));
        }
        Note note = mapper.map(request, Note.class);
        note.setUser(user.get());
        Note newNote = noteRepository.insert(note);
        return mapper.map(newNote, NoteDTO.class);
    }

    @Override
    public NoteDTO findById(String id) {
        Optional<Note> note = noteRepository.findById(id);
        if (note.isEmpty()) {
            throw new NoteNotFoundException(String.format("note not found with id: %s", id));
        }
        return mapper.map(note.get(), NoteDTO.class);
    }

    @Override
    public List<NoteDTO> find() {
        List<Note> notes = noteRepository.findAll();
        return notes.stream().map(note -> mapper.map(note, NoteDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        noteRepository.deleteById(id);
    }

    @Override
    public void update(UpdateNoteRequestDTO request) {
        Optional<Note> note = noteRepository.findById(request.getId());
        if (note.isEmpty()) {
            throw new NoteNotFoundException(String.format("note not found with id: %s", request.getId()));
        }
        Note updatedNote = note.get();
        updatedNote.setTitle(request.getTitle());
        updatedNote.setText(request.getText());
        updatedNote.setCompleted(request.isCompleted());
        updatedNote.setUpdatedAt(new Date());
        noteRepository.save(updatedNote);
    }
}
