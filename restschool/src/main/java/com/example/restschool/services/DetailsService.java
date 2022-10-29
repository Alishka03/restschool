package com.example.restschool.services;

import com.example.restschool.models.student.Student;
import com.example.restschool.models.student.StudentRepository;
import com.example.restschool.security.StudentDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DetailsService implements UserDetailsService {
    private final StudentRepository studentRepository;

    public DetailsService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Student> student = studentRepository.findByUsername(username);
        if(student.isEmpty()){
            throw new UsernameNotFoundException("Student not found");
        }
        return new StudentDetails(student.get());
    }
}
