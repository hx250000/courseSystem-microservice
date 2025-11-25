package com.zjgsu.hx.enrollment_service.service;

import com.zjgsu.hx.enrollment_service.exception.ResourceConflictException;
import com.zjgsu.hx.enrollment_service.exception.ResourceNotFoundException;
import com.zjgsu.hx.enrollment_service.model.Enrollment;
import com.zjgsu.hx.enrollment_service.model.Status;
import com.zjgsu.hx.enrollment_service.model.Student;
import com.zjgsu.hx.enrollment_service.repository.EnrollmentRepository;
import com.zjgsu.hx.enrollment_service.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final RestTemplate restTemplate;
    @Value("${catalog-service.url}") // 引用 application.yml 中的配置
    private String catalogServiceUrl;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             RestTemplate restTemplate) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.restTemplate = restTemplate;

    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }
    /**
     * 创建选课记录（选课）
     */
    @Transactional
    public Enrollment createEnrollment(String studentId, String courseId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在！"));

        // 2. 调用课程目录服务验证课程是否存在
        String url = catalogServiceUrl + "/api/courses/" + courseId;
        Map<String, Object> courseResponse;
        try {
            courseResponse = restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("课程不存在："+ courseId);
        }
        // 3. 从响应中提取课程信息
        Map<String, Object> courseData = (Map<String, Object>)
                courseResponse.get("data");
        Integer capacity = (Integer) courseData.get("capacity");
        Integer enrolled = (Integer) courseData.get("enrolled");
        // 4. 检查课程容量
        if (enrolled >= capacity) {
            throw new ResourceConflictException("课程人数已满！");
        }
        // 5. 检查重复选课
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId,courseId
                )) {
            throw new ResourceConflictException("Already enrolled in this course");
        }
        // 6. 创建选课记录
        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentId(studentId);
        enrollment.setStatus(Status.ACTIVE);
        //enrollment.setEnrolledAt(LocalDateTime.now());
        Enrollment saved = enrollmentRepository.save(enrollment);
        // 7. 更新课程的已选人数（调用catalog-service）
        updateCourseEnrolledCount(courseId, enrolled + 1);

        return saved;
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        // 确保学生存在
        Student student=studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("学生不存在"));
        return enrollmentRepository.findByStudentId(student.getStudentId());
    }
    public List<Enrollment> getEnrollmentsByCourse(String courseId) {
        // 确保课程存在
//        Course course=courseRepository.findById(courseId)
//                .orElseThrow(() -> new ResourceNotFoundException("课程不存在"));
        // 调用课程目录服务验证课程是否存在
        String url = catalogServiceUrl + "/api/courses/" + courseId;
        Map<String, Object> courseResponse;
        try {
            courseResponse = restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("课程不存在："+ courseId);
        }
        return enrollmentRepository.findByCourseId(courseId);
    }
    /*
     *删除选课记录（退课）
     */
    @Transactional
    public Enrollment deleteEnrollment(String studentId, String courseId) {
        // 找到该学生的选课记录
        System.out.printf("正在删除选课记录，学生=%s,课程=%s\n",studentId,courseId);
        Student student=studentRepository.findByStudentId(studentId)
                .orElseThrow(()->new ResourceNotFoundException("学生不存在！"));
        Enrollment enrollment=enrollmentRepository.findByStudentIdAndCourseId(studentId,courseId)
                .orElseThrow(()->new ResourceNotFoundException("选课记录不存在！"));


        // 更新课程人数
        // 更新课程人数：先远程获取当前人数，再 PUT 更新
        int enrolled = getEnrolledCount(courseId);
        if (enrolled > 0) {
            updateCourseEnrolledCount(courseId, enrolled - 1); // 远程更新减少 1
        }
        else {
            throw new ResourceConflictException("课程人数已空！");
        }
// 删除选课记录
        enrollment.setStatus(Status.DROPPED);
        enrollmentRepository.save(enrollment);
        return enrollment;
    }

    //删除选课记录（byId)
    @Transactional
    public Enrollment deleteById(String id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("选课记录不存在!"));
        String courseId=enrollment.getCourseId();
        int enrolled = getEnrolledCount(courseId);
        if (enrolled > 0) {
            updateCourseEnrolledCount(courseId, enrolled - 1); // 远程更新减少 1
        }
        else {
            throw new ResourceConflictException("课程人数已空！");
        }
        enrollmentRepository.deleteById(id);
        return enrollment;
    }

    private int getEnrolledCount(String courseId) {
        String url = catalogServiceUrl + "/api/courses/" + courseId;
        try {
            // 远程获取课程信息
            Map<String, Object> courseResponse = restTemplate.getForObject(url, Map.class);

            Map<String, Object> courseData = (Map<String, Object>) courseResponse.get("data");

            Integer enrolled = (Integer) courseData.get("enrolled");
            return enrolled != null ? enrolled : 0;

        } catch (HttpClientErrorException.NotFound e) {
            // 课程不存在
            throw new ResourceNotFoundException("课程不存在："+ courseId);
        } catch (Exception e) {
            // 远程调用失败，记录日志
            System.err.println("Failed to fetch course enrolled count for " + courseId + ": " + e.getMessage());
            return 0; // 无法获取人数，返回 0
        }
    }
    private void updateCourseEnrolledCount(String courseId, int newCount) {
        String url = catalogServiceUrl + "/api/courses/" + courseId;
        Map<String,Object> resp = restTemplate.getForObject(url, Map.class);
        Map<String,Object> courseData = (Map<String,Object>) resp.get("data");

        courseData.put("enrolled", newCount);

        restTemplate.put(url, courseData);

    }
    public void validateCourseDeletion(String courseId) {
        boolean exists = enrollmentRepository.existsByCourseId(courseId);
        if (exists) {
            throw new ResourceConflictException("无法删除：该课程存在选课记录！");
        }
    }

}
