package com.br.notesapp.notesappserver.service;

import com.br.notesapp.notesappserver.dto.user.CreateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UpdateUserRequestDTO;
import com.br.notesapp.notesappserver.dto.user.UserModelDTO;

import java.util.List;

public interface UserService {
    UserModelDTO insert(CreateUserRequestDTO request);

    UserModelDTO findById(String userId);

    List<UserModelDTO> find();

    void deleteOne(String userId);

    void update(UpdateUserRequestDTO request);

}
