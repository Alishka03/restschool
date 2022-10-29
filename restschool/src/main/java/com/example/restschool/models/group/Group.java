package com.example.restschool.models.group;

import com.example.restschool.models.student.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="Groups")
@Getter

@Setter
@NoArgsConstructor
public class Group implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    private List<Student> students;
}