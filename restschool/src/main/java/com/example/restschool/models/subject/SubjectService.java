package com.example.restschool.models.subject;

import com.example.restschool.models.student.Student;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }
    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> findOne(int id) {
        return subjectRepository.findById(id);
    }

    @Transactional
    public void saveSubject(Subject subject) {
        subjectRepository.save(subject);
    }

    @Transactional
    public void updateSubject(int id, Subject subject) {
        subject.setId(id);
        subjectRepository.save(subject);
    }
    @Transactional
    public void deleteById(int id){
        subjectRepository.deleteById(id);
    }
    @Transactional
    public void enrollStudent(int id, Student student) {
        Subject sb = findOne(id).get();
        sb.getEnrolledStudents().add(student);
        student.getSubjects().add(sb);
    }
}
