package com.example.restschool.models.subject;



import com.example.restschool.models.student.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


public class SubjectDTO {


    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    public SubjectDTO() {
    }

    public SubjectDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}