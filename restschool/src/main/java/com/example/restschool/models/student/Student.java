package com.example.restschool.models.student;


import com.example.restschool.models.group.Group;
import com.example.restschool.models.subject.Subject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Valid
@Table(name = "Student")
@Getter
@Setter
public class Student  {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    @NotEmpty
    @Length(min = 2, message = "Length error")
    @NotNull
    @Min(2)
    private String name;
    @NotEmpty
    @Column(name = "lastname")
    @NotNull(message = "lastname mustn't be empty!")
    @Min(2)
    private String lastname;
    @NotEmpty
    @Column(name = "email")
    @NotNull(message = "email mustn't be empty!")
    @Email(message = "In correct email form!")
    private String email;
    @NotNull(message = "Name mustn't be empty!")
    @Column(name = "year")
    @Min(value = 1950, message = "Year must be more than 1950")
    @Max(value = 2015, message = "Your age must be more than 7")
    private int year;
    @NotNull(message = "Username mustn't be empty!")
    @Column(name = "username")
    @NotEmpty
    @Length(min = 3)
    private String username;
    @NotNull(message = "Name mustn't be empty!")
    @Column(name = "password")
    @NotEmpty

    @Length(min = 6, message = "Password have to be more than 6 symbols")
    private String password;

    @ManyToMany(mappedBy = "enrolledStudents", fetch = FetchType.EAGER)
    private Set<Subject> subjects = new HashSet<Subject>();
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public Student() {
    }

    public Student(int id, String name, String lastname, String email, int year, String username, String password, Set<Subject> subjects, Group group) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.year = year;
        this.username = username;
        this.password = password;
        this.subjects = subjects;
        this.group = group;
    }
}