package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.dto.CreateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.UpdateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.UserModelDTO;

import java.util.List;

public interface UserModelService {
    UserModelDTO insert(CreateUserRequestDTO request);

    UserModelDTO findById(String userId);

    List<UserModelDTO> find();

    void deleteOne(String userId);

    void update(UpdateUserRequestDTO request);

}
