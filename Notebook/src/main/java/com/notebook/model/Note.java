package com.notebook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.*;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalField;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty(message = "Name should be not empty")
    @Size(min = 2,max=30,message = "Name should be between 2 and 30 characters")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "Text should be not empty")
    @Size(min = 1,message = "Text should has more than one character")
    @Column(nullable = false)
    private String text;

    @Column(name = "created_on",nullable = false)
    private LocalDateTime createdOn = LocalDateTime
            .of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth(),LocalTime.now().getHour(),LocalTime.now().getMinute());

    public Note(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public Note(Long id,String name, String text) {
        this.id = id;
        this.name = name;
        this.text = text;
    }

}

