package com.notebook.controller;

import com.notebook.model.Note;
import com.notebook.repository.NoteRepository;
import com.notebook.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class NoteController {
    private final NoteRepository repository;
    private final NoteService service;

    @GetMapping
    public String main() {
        return "index";
    }

    @GetMapping("/notes")
    public String getAll(Model model){
        List<Note> notes = repository.findAll();
        notes.sort(Comparator.comparing(Note::getCreatedOn).reversed());

        model.addAttribute("notes",notes);
        return "notes";
    }

    @GetMapping("/notes/add")
    public String addNote(Model model){
        model.addAttribute("note",new Note());
        return "new";
    }

    //@ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/notes")
    public String save(@ModelAttribute("note")@Valid Note note, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            System.out.println("Hello");
            return "new";
        }
        repository.save(note);
        return "redirect:/main/notes";
    }

    @GetMapping("/notes/{id}")
    public String getNote(@PathVariable("id")Long id,Model model){
        model.addAttribute("note",repository.findById(id).orElseThrow());
        return "note";
    }


    @RequestMapping(value = "/notes/{id}",method = {RequestMethod.POST,RequestMethod.DELETE})
    public String delete(@PathVariable("id")Long id){
        repository.deleteById(id);
        return "redirect:/main/notes";
    }


    @GetMapping("/notes/edit/{id}")
    public String edit(@PathVariable("id")Long id,Model model){
        Note foundNote = repository.findById(id).orElseThrow();
        model.addAttribute("note",foundNote);
        return "edit";
    }

    @RequestMapping(value= "/notes/note/{id}",method = {RequestMethod.POST,RequestMethod.PUT})
    public String update(@ModelAttribute("note")Note note, @PathVariable("id")Long id){
        service.updateNote(id,note);
        return "redirect:/main/notes";
    }
}
