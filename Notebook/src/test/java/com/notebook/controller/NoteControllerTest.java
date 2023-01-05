package com.notebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.notebook.model.Note;
import com.notebook.repository.NoteRepository;
import com.notebook.service.NoteService;
import lombok.SneakyThrows;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
public class NoteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService service;

    @MockBean
    private NoteRepository repository;

    @BeforeEach
    void setUp(){

    }

    @Test
    @SneakyThrows
    void getNotesTest(){
        List<Note> notes = new ArrayList<>(List.of(
                new Note(1L, "note", "hello"),
                new Note(2L, "note2", "text2")));

        notes.sort(Comparator.comparing(Note::getCreatedOn).reversed());

        Mockito.when(repository.findAll())
                .thenReturn(notes);

        mockMvc.perform(get("/main/notes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("All notes")))
                .andExpect(content().string(containsString("note")));
    }


    @Test
    @SneakyThrows
    void addNoteViewTest(){

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of((new Note(1L, "new", "new text"))));

        mockMvc.perform(get("/main/notes/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Add note</title>")));
    }

    @Test
    @SneakyThrows
    void editNoteViewTest(){

        Mockito.when(repository.findById(1L))
                .thenReturn(Optional.of(new Note(1L, "updated note", "updated text")));

        mockMvc.perform(get("/main/notes/edit/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>Update note</title>")));
    }

    @Test
    @SneakyThrows
    void addNoteTest(){
        Note note = new Note(1L,"new name","new text");
        mockMvc.perform(post("/main/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapToString(note)))
                .andDo(print())
                .andExpect(result -> Assertions.assertEquals(result.getResponse().getRedirectedUrl(),"/main/notes"))
                .andExpect(status().is(302));
    }


    @Test
    @SneakyThrows
    void updateNoteTest(){
        Note note = new Note(1L,"updated name","updated text");
        mockMvc.perform(put("/main/notes/note/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapToString(note)))
                .andDo(print())
                .andExpect(result -> Assertions.assertEquals(result.getResponse().getRedirectedUrl(),"/main/notes"))
                .andExpect(status().is(302));
    }

    @Test
    @SneakyThrows
    void deleteNoteTest(){

        mockMvc.perform(delete("/main/notes/1"))
                .andDo(print())
                .andExpect(result -> Assertions.assertEquals(result.getResponse().getRedirectedUrl(),"/main/notes"))
                .andExpect(status().is(302));
    }


    @SneakyThrows
    private String mapToString(Note note){
        ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
        return mapper.writeValueAsString(note);
    }
}
