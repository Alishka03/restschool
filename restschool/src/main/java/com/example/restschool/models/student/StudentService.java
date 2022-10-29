package com.example.restschool.models.student;

import com.example.restschool.models.group.Group;
import com.example.restschool.models.group.GroupService;
import com.example.restschool.models.subject.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupService groupService;
    private final SubjectRepository subjectRepository;

    public StudentService(StudentRepository studentRepository, GroupService groupService, SubjectRepository subjectRepository) {
        this.studentRepository = studentRepository;
        this.groupService = groupService;
        this.subjectRepository = subjectRepository;
    }

    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Transactional
    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    @Transactional
    public void updateStudent(int id, Student student) {
        student.setId(id);
        studentRepository.save(student);
    }

    public Optional<Student> findOne(int id) {
        return studentRepository.findById(id);
    }

    @Transactional
    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    @Transactional
    public void assignGroup(int id, Group group) {
        if (groupService.findOne(group.getId()).isPresent()) {
            Student st = findOne(id).get();
            st.setGroup(group);
        } else {
            throw new RuntimeException("Group not found");
        }
    }

    @Transactional
    public void setFreeGroup(int id) {
        Student st = findOne(id).get();
        Group gr = st.getGroup();
        gr.getStudents().remove(st);
        st.setGroup(null);
    }
    @Transactional
    public void enrollStudent(Student st, int subject_id) {
        st.getSubjects().add(subjectRepository.findById(subject_id).get());
        subjectRepository.findById(subject_id).get().getEnrolledStudents().add(st);
    }
}
