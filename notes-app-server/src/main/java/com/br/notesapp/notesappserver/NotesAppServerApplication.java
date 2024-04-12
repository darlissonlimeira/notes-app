package com.br.notesapp.notesappserver;

import com.br.notesapp.notesappserver.model.Note;
import com.br.notesapp.notesappserver.model.UserModel;
import com.br.notesapp.notesappserver.model.UserModelRole;
import com.br.notesapp.notesappserver.repository.NoteRepository;
import com.br.notesapp.notesappserver.repository.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
@EnableCaching
public class NotesAppServerApplication {

    @Autowired
    private UserModelRepository userModelRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private PasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(NotesAppServerApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            var users = userModelRepository.findAll();

            if (!users.isEmpty()) return;

            var user1 = new UserModel("Darlisson", encoder.encode("123"), true, Set.of(UserModelRole.ADMIN, UserModelRole.MANAGER));
            var user2 = new UserModel("John", encoder.encode("123"), true, Set.of(UserModelRole.MANAGER));
            var user3 = new UserModel("Mark", encoder.encode("123"), true, Set.of(UserModelRole.ADMIN));
            var user4 = new UserModel("Joe", encoder.encode("123"), true, Set.of(UserModelRole.EMPLOYEE));
            userModelRepository.saveAll(List.of(user1, user2, user3, user4));

            users = userModelRepository.findAll();

            var note1 = new Note(users.get(0), "Supermarket list", "eggs, tomatos");
            var note2 = new Note(users.get(1), "tv shows ", "barry, shameless");
            var note3 = new Note(users.get(2), "game list", "forza, god of war, gta V");
            note3.setCompleted(true);
            var note4 = new Note(users.get(3), "anime list ", "jujutsu kaizen, mob psycho");
            note4.setCompleted(true);
            noteRepository.insert(List.of(note1, note2, note3, note4));

        };
    }
}
