package com.example.restschool.models.group;

import com.example.restschool.models.student.Student;
import com.example.restschool.models.student.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    public GroupService(GroupRepository groupRepository, StudentRepository studentRepository) {
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Optional<Group> findOne(int id) {
        return groupRepository.findById(id);
    }

    @Transactional
    public void saveGroup(Group group) {
        groupRepository.save(group);
    }

    @Transactional
    public void updateGroup(int id, Group group) {
        group.setId(id);
        groupRepository.save(group);
    }
    @Transactional
    public void deleteById(int id){
        groupRepository.deleteById(id);
    }
    @Transactional
    public void deleteStudentFromGroup(int group_id,Student student){
        Group group = findOne(group_id).get();
        student.setGroup(null);
        group.getStudents().remove(student);
    }
    @Transactional
    public void addStudentToGroup(int group_id,Student student){
        Group group = findOne(group_id).get();
        student.setGroup(group);
        group.getStudents().add(student);
    }
}