package com.notebook.service;

import com.notebook.exception.NotFoundException;
import com.notebook.model.Note;
import com.notebook.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository repository;

    public void updateNote(Long id, Note note){
        Note foundNote = repository.findById(id)
                .orElseThrow(()-> new NotFoundException("Can not find note by id: "+id));

        foundNote.setName(note.getName());
        foundNote.setText(note.getText());
        foundNote.setCreatedOn(note.getCreatedOn());
    }
}
