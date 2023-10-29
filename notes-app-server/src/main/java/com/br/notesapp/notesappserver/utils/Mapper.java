package com.br.notesapp.notesappserver.utils;

import com.br.notesapp.notesappserver.dto.NoteDTO;
import com.br.notesapp.notesappserver.model.Note;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class Mapper implements ObjectMapper {

    private ModelMapper mapper;

    public Mapper() {
        this.mapper = this.init();
    }

    private ModelMapper init() {
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(Note.class, NoteDTO.class).addMapping(note -> note.getUser().getUsername(), (noteDTO, value) -> noteDTO.setUser(toString()));
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }

    @Override
    public <D> D map(Object source, Class<D> c) {
        return mapper.map(source, c);
    }
}
