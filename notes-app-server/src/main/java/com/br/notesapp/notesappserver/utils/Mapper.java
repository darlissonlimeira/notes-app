package com.br.notesapp.notesappserver.utils;

import com.br.notesapp.notesappserver.dto.note.NoteDTO;
import com.br.notesapp.notesappserver.model.Note;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class Mapper implements ObjectMapper {

    private ModelMapper mapper;

    public Mapper() {
    }

    @PostConstruct
    private void init() {
        mapper = new ModelMapper();
        mapper.typeMap(Note.class, NoteDTO.class).addMapping(note -> note.getUser().getUsername(), (noteDTO, value) -> noteDTO.setUser(toString()));
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public <D> D map(Object source, Class<D> c) {
        return mapper.map(source, c);
    }
}
