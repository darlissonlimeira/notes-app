package com.br.notesapp.notesappserver.repository;

import com.br.notesapp.notesappserver.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    @Query("{'user': ?0}")
    List<Note> findAllByUserId(String userId);

}
