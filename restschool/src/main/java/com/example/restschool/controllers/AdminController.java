package com.example.restschool.controllers;

import com.example.restschool.models.group.Group;
import com.example.restschool.models.group.GroupService;
import com.example.restschool.models.group.exceptions.GroupNotCreatedException;
import com.example.restschool.models.group.exceptions.GroupNotFoundException;
import com.example.restschool.models.student.Student;
import com.example.restschool.models.student.StudentDTO;
import com.example.restschool.models.student.StudentService;
import com.example.restschool.models.subject.Subject;
import com.example.restschool.models.subject.SubjectDTO;
import com.example.restschool.models.subject.SubjectService;
import com.example.restschool.util.ResourseNotFoundException;
import com.example.restschool.util.ErrorResponse;
import com.example.restschool.util.StudentValidator;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final StudentService studentService;
    private final GroupService groupService;
    private final ModelMapper modelMapper;
    private final StudentValidator validation;
    private final SubjectService subjectService;

    public AdminController(StudentService studentService, GroupService groupService, ModelMapper modelMapper, StudentValidator validation, SubjectService subjectService) {
        this.studentService = studentService;
        this.groupService = groupService;
        this.modelMapper = modelMapper;
        this.validation = validation;
        this.subjectService = subjectService;
    }

    // ---------------------STUDENTS CONTROLLERS-----------------------------
    @GetMapping("/students")
    private List<Student> getStudents() {
        return studentService.getAll();
    }

    @PostMapping("/students")
    public ResponseEntity<HttpStatus> createNewStudent(@Valid @RequestBody Student student, BindingResult bind) {
        if (bind.hasErrors()) {
            String message = getErrorMessage(bind);
            throw new ResourseNotFoundException(message);
        }
        validation.validate(student, bind);
        studentService.saveStudent((student));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/students/{id}")
    public StudentDTO getStudent(@PathVariable("id") int id) {
        return convertToStudentDTO(studentService.findOne(id).get());
    }

    @RequestMapping(value = "/students/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteStudent(@PathVariable("id") int id) {
        if (studentService.findOne(id).isEmpty()) {
            return new ResponseEntity<>(new ResourseNotFoundException("Student with ID:" + id + " not found"), HttpStatus.NOT_FOUND);
        }
        studentService.deleteStudent(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/students/{id}")
    public ResponseEntity<HttpStatus> updateStudent(@Valid @RequestBody StudentDTO student, BindingResult bind, @PathVariable("id") int id) {
        if (bind.hasErrors()) {
            String message = getErrorMessage(bind);
            throw new ResourseNotFoundException(message);
        }
        studentService.updateStudent(id, convertToStudent(student));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/students/{id}/assigngroup")
    public ResponseEntity<HttpStatus> changeGroup(@RequestBody Group group, @PathVariable("id") int id) {
        if (studentService.findOne(id).isEmpty()) {
            throw new ResourseNotFoundException("STUDENT NOT FOUND");
        }
        studentService.assignGroup(id, group);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/students/{id}/setfreegroup")
    public ResponseEntity<HttpStatus> setFreeGroup(@PathVariable("id") int id) {
        if (studentService.findOne(id).isEmpty()) {
            throw new ResourseNotFoundException("STUDENT NOT FOUND");
        }
        studentService.setFreeGroup(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/students/{id}/enrolltosubject")
    public ResponseEntity<HttpStatus> enrollStudentToSubject(@RequestBody Map<String , Integer> response, @PathVariable("id") int id){
        if (studentService.findOne(id).isEmpty()) {
            throw new ResourseNotFoundException("STUDENT NOT FOUND");
        }
        int subject_id = response.get("subject_id");
        Student st = studentService.findOne(id).get();
        studentService.enrollStudent(st,subject_id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //--------------------------GROUP CONTROLLERS-----------------------------------
    @GetMapping("/groups")
    public List<Group> getGroups() {
        return groupService.findAll();
    }

    @PostMapping("/groups")
    public ResponseEntity<HttpStatus> saveGroup(@RequestBody Group group, BindingResult bind) {
        if (bind.hasErrors()) {
            String msg = getErrorMessage(bind);
            throw new GroupNotCreatedException(msg);
        }
        groupService.saveGroup(group);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/groups/{id}")
    public Group getGroupById(@PathVariable("id") int id) {
        if (groupService.findOne(id).isEmpty()) {
            throw new GroupNotFoundException("Group not found");
        }
        return groupService.findOne(id).get();
    }

    @RequestMapping(value = "/groups/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteGroup(@PathVariable("id") int id) {
        if (groupService.findOne(id).isEmpty()) {
            throw new GroupNotFoundException("Group not found");
        }
        groupService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/groups/{id}")
    public ResponseEntity<HttpStatus> editGroup(@RequestBody Group group, BindingResult bind, @PathVariable("id") int id) {
        if (bind.hasErrors()) {
            return ResponseEntity.ok(HttpStatus.BAD_GATEWAY);
        }
        groupService.updateGroup(id, group);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/groups/{id}/deletefromgroup")
    public ResponseEntity<HttpStatus> deleteStudentFromGroup(@RequestBody Map<String, Integer> response, @PathVariable("id") int id) {
        int studentid = (response.get("student_id"));
        System.out.println(studentid);
        Group group = groupService.findOne(id)
                .orElseThrow(() -> new ResourseNotFoundException("Group not exist with id :" + id));
        Student student = studentService.findOne(studentid)
                .orElseThrow(() -> new ResourseNotFoundException("Employee not exist with id :" + studentid));
        groupService.deleteStudentFromGroup(group.getId(), student);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/groups/{id}/addstudenttogroup")
    public ResponseEntity<HttpStatus> addStudentToGroup(@RequestBody Map<String, Integer> response, @PathVariable("id") int id) {
        int studentid = (response.get("student_id"));
        System.out.println(studentid);
        Group group = groupService.findOne(id)
                .orElseThrow(() -> new ResourseNotFoundException("Group not exist with id :" + id));
        Student student = studentService.findOne(studentid)
                .orElseThrow(() -> new ResourseNotFoundException("Employee not exist with id :" + studentid));
        groupService.addStudentToGroup(group.getId(), student);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    //--------------SUBJECTS----------------
    @GetMapping("/subjects")
    public List<Subject> getSubjects() {
        return subjectService.findAll();
    }

    @PostMapping("/subjects")
    public ResponseEntity<?> saveSubject(@RequestBody @Valid Subject subject, BindingResult bind) {
        if (bind.hasErrors()) {
            throw new RuntimeException("Data is wrong");
        }
        subjectService.saveSubject(subject);
        return ResponseEntity.ok("Subject saved succesfully");
    }

    @PatchMapping("/subjects/{id}")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid SubjectDTO subject, BindingResult bind, @PathVariable("id") int id) {

        if (bind.hasErrors()) {
            throw new RuntimeException("Data is wrong");
        }
        subjectService.updateSubject(id, convertToSubject(subject));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping(value = "/subjects/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteSubject(@PathVariable("id") int id) {
        Subject subject = subjectService.findOne(id)
                .orElseThrow(() -> new ResourseNotFoundException("Subject not exist with id :" + id));
        subjectService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/subjects/{id}/enrollstudent")
    public ResponseEntity<HttpStatus> addStudentSubject(@RequestBody Map<String, Integer> response, @PathVariable("id") int id) {
        int studentid = (response.get("student_id"));
        System.out.println(studentid);

        Student student = studentService.findOne(studentid)
                .orElseThrow(() -> new ResourseNotFoundException("Employee not exist with id :" + studentid));

        subjectService.enrollStudent(id ,student);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @PostMapping("/subjects/{id}/addstudent")
    public ResponseEntity<?> addStudent(@RequestParam Map<String,String> response , @PathVariable("id") int id){

        int studentid = Integer.parseInt((response.get("student_id")));
        System.out.println(studentid);
        Student student = studentService.findOne(studentid).
                orElseThrow(() -> new ResourseNotFoundException("Student doesnot exist with id :"+studentid));
        subjectService.enrollStudent(id,student);
        return ResponseEntity.ok(student.getName()+ " add to subject by ID" + id);
    }

//   ---------------------------------------METHODS--------------------------

    private String getErrorMessage(BindingResult bind) {
        List<FieldError> list = bind.getFieldErrors();
        StringBuilder response = new StringBuilder();
        for (FieldError error : list) {
            response.append(error.getField()).append("-").append(error.getDefaultMessage()).append("\n");
        }
        return response.toString();
    }

    //----------------EXCEPTION HANDLERS------------------------
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ResourseNotFoundException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(GroupNotFoundException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(GroupNotCreatedException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private StudentDTO convertToStudentDTO(Student s) {
        return modelMapper.map(s, StudentDTO.class);
    }

    ;

    private Student convertToStudent(StudentDTO s) {
        return modelMapper.map(s, Student.class);
    }

    ;

    private SubjectDTO convertToSubjectDTO(Subject s) {
        return modelMapper.map(s, SubjectDTO.class);
    }

    ;

    private Subject convertToSubject(SubjectDTO s) {
        return modelMapper.map(s, Subject.class);
    }

    ;
}
