package com.br.notesapp.notesappserver.repository;

import com.br.notesapp.notesappserver.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserModelRepository extends MongoRepository<UserModel, String> {

    Optional<UserModel> findByUsername(String username);


}
