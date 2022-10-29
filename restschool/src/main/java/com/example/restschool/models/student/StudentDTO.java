package com.example.restschool.models.student;

import com.example.restschool.models.group.Group;
import com.example.restschool.models.subject.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

public class StudentDTO {
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
    @JsonIgnore
    @ManyToMany(mappedBy = "enrolledStudents", fetch = FetchType.EAGER)
    private Set<Subject> subjects = new HashSet<Subject>();
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public StudentDTO() {
    }

    public StudentDTO(String name, String lastname, String email, int year, String username, String password, Set<Subject> subjects, Group group) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.year = year;
        this.username = username;
        this.password = password;
        this.subjects = subjects;
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
