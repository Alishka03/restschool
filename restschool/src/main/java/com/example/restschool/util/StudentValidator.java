package com.example.restschool.util;

import com.example.restschool.models.student.Student;
import com.example.restschool.models.student.StudentRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.Column;

@Component
public class StudentValidator implements Validator {
    private final StudentRepository studentRepository;

    public StudentValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return Student.class.equals(clazz);
    }
    @Override
    public void validate(Object target, Errors errors) {
        Student student = (Student) target;
        if(studentRepository.findByUsername(student.getUsername()).isPresent()){
            errors.rejectValue("username" , "","Username is taken by another student");
        }
    }
}
