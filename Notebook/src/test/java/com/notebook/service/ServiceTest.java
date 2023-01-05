package com.notebook.service;

import com.notebook.exception.NotFoundException;
import com.notebook.model.Note;
import com.notebook.repository.NoteRepository;
import com.notebook.service.NoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
public class ServiceTest {
    @Autowired
    private NoteService service;
    @Autowired
    private NoteRepository repository;

    private Note note;

    @BeforeEach
    public void setUp(){
        note = new Note("new name","new text");
        repository.save(note);
    }

    @Test
    public void updateNoteSuccessfullyTest(){
        Note newNote = new Note("updated name","updated text");
        service.updateNote(note.getId(), newNote);
        repository.flush();

        assertThat(note.getName()).isEqualTo("updated name");
        assertThat(note.getText()).isEqualTo("updated text");
    }

    @Test
    public void updateNoteThrowsNotFoundException(){
        Note newNote = new Note("updated name","updated text");

        Long wrongId = note.getId()+1;
        assertThatThrownBy(()->{
            service.updateNote(wrongId,newNote);

        }).isInstanceOf(NotFoundException.class)
                        .hasMessage("Can not find note by id: "+wrongId);

    }
}
