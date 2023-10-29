package com.br.notesapp.notesappserver.utils;

public interface ObjectMapper {
    <D> D map(Object o, Class<D> c);
}
