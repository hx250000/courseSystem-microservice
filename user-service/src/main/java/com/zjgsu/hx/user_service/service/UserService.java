package com.zjgsu.hx.user_service.service;

import com.zjgsu.hx.user_service.exception.ResourceConflictException;
import com.zjgsu.hx.user_service.exception.ResourceNotFoundException;
import com.zjgsu.hx.user_service.model.Student;
import com.zjgsu.hx.user_service.model.Teacher;
import com.zjgsu.hx.user_service.repository.StudentRepository;
import com.zjgsu.hx.user_service.repository.TeacherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate;
    private final String enrollmentServiceUrl="http://enrollment-service";

    public UserService(StudentRepository studentRepository, TeacherRepository teacherRepository, DiscoveryClient discoveryClient, RestTemplate restTemplate) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    public List<Teacher> findAllTeachers() {return teacherRepository.findAll();}

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Student findStudentById(String id) {
        return studentRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
    }

    public Teacher findTeacherById(String id) {
        return teacherRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("教师不存在！"));
    }

    public Student findStudentByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId).
                orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
    }

    public Teacher findTeacherByTeacherId(String teacherId) {
        return teacherRepository.findByTeacherId(teacherId).
                orElseThrow(()->new ResourceNotFoundException("教师不存在！"));
    }

    @Transactional
    public Student createStudent(Student student) {
        String studentId = student.getStudentId();
        String email = student.getEmail();
        boolean exists = studentRepository.existsByStudentId(studentId);
        if (exists) {
            throw new ResourceConflictException("该学号已存在！");
        }
        validateEmail(email);
        if (studentRepository.existsByEmail(email)) {
            throw new ResourceConflictException("该邮箱已被注册！");
        }
        return studentRepository.save(student);
    }

    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        String teacherId = teacher.getTeacherId();
        String email = teacher.getEmail();
        boolean exists = teacherRepository.existsByTeacherId(teacherId);
        if (exists) {
            throw new ResourceConflictException("该教师号已存在！");
        }
        validateEmail(email);
        if (teacherRepository.existsByEmail(email)) {
            throw new ResourceConflictException("该邮箱已被注册！");
        }
        return teacherRepository.save(teacher);
    }

    @Transactional
    public Student updateStudent(String id,Student student) {
        // 检查是否存在
        Student existing = studentRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
        // 保留创建时间
        //student.setCreateAt(existing.getCreateAt());
        // 校验邮箱
        validateEmail(student.getEmail());
        // 检查学号是否与其他学生重复
        studentRepository.findByStudentId(student.getStudentId())
                .filter(s -> !s.getId().equals(existing.getId()))
                .ifPresent(s -> {
                    throw new ResourceConflictException("学号已存在！");
                });
        // 保存更新
        existing.setStudentId(student.getStudentId());
        existing.setName(student.getName());
        existing.setEmail(student.getEmail());
        existing.setMajor(student.getMajor());
        existing.setGrade(student.getGrade());
        return studentRepository.save(existing);
    }

    @Transactional
    public Teacher updateTeacher(String id,Teacher teacher) {
        // 检查是否存在
        Teacher existing = teacherRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("教师不存在！"));
        // 保留创建时间
        //student.setCreateAt(existing.getCreateAt());
        // 校验邮箱
        validateEmail(teacher.getEmail());
        // 检查工号是否与其他老师重复
        teacherRepository.findByTeacherId(teacher.getTeacherId())
                .filter(s -> !s.getId().equals(existing.getId()))
                .ifPresent(s -> {
                    throw new ResourceConflictException("工号已存在！");
                });
        // 保存更新
        existing.setTeacherId(teacher.getTeacherId());
        existing.setName(teacher.getName());
        existing.setEmail(teacher.getEmail());
        existing.setDepartment(teacher.getDepartment());
        return teacherRepository.save(existing);
    }

    @Transactional
    public Student deleteStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在!"));
        String studentId = student.getStudentId();

        String url = enrollmentServiceUrl + "/api/student/" + studentId;

        Map<String, Object> studentEnrolledResponse = restTemplate.getForObject(url, Map.class);

        Map<String, Object> studentEnrolledData = (Map<String, Object>) studentEnrolledResponse.get("data");

        boolean hasEnrollments = !(studentEnrolledData.isEmpty());
        if (hasEnrollments) {
            throw new ResourceConflictException("无法删除：该学生存在选课记录！");
        }
        studentRepository.deleteById(id);
        return student;
    }
    @Transactional
    public Teacher deleteTeacherById(String id){
        Teacher teacher=teacherRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("教师不存在！"));
        teacherRepository.deleteById(id);
        return teacher;
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("邮箱不能为空！");
        }
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(regex)) {
            throw new IllegalArgumentException("邮箱格式不正确！");
        }

    }
}
