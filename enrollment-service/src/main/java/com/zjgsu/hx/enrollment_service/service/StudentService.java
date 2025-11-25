package com.zjgsu.hx.enrollment_service.service;

import com.zjgsu.hx.enrollment_service.exception.ResourceConflictException;
import com.zjgsu.hx.enrollment_service.exception.ResourceNotFoundException;
import com.zjgsu.hx.enrollment_service.model.Student;
import com.zjgsu.hx.enrollment_service.repository.EnrollmentRepository;
import com.zjgsu.hx.enrollment_service.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    public StudentService(StudentRepository studentRepository, EnrollmentRepository enrollmentRepository) {
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(String id) {
        return studentRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
    }

    public Student findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId).
                orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
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
                .filter(s -> !s.getId().equals(student.getId()))
                .ifPresent(s -> {
                    throw new ResourceConflictException("学号已存在！");
                });
        // 保存更新
        existing.setStudentId(student.getStudentId());
        existing.setName(student.getName());
        existing.setEmail(student.getEmail());
        existing.setMajor(student.getMajor());
        return studentRepository.save(existing);
    }

    @Transactional
    public Student deleteById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在!"));
        boolean hasEnrollments = !enrollmentRepository
                .findByStudentId(student.getStudentId())
                .isEmpty();
        if (hasEnrollments) {
            throw new ResourceConflictException("无法删除：该学生存在选课记录！");
        }
        studentRepository.deleteById(id);
        return student;
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
