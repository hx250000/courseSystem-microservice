package com.zjgsu.hx.enrollment_service.service;

import com.zjgsu.hx.enrollment_service.exception.ResourceConflictException;
import com.zjgsu.hx.enrollment_service.exception.ResourceNotFoundException;
import com.zjgsu.hx.enrollment_service.model.Enrollment;
import com.zjgsu.hx.enrollment_service.model.Status;
import com.zjgsu.hx.enrollment_service.repository.EnrollmentRepository;
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
    private final RestTemplate restTemplate;
    //@Value("${catalog-service.url}") // 引用 application.yml 中的配置
    private final String catalogServiceUrl="http://catalog-service";
    //@Value("${user-service.url}")
    private final String userServiceUrl="http://user-service";

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             RestTemplate restTemplate) {
        this.enrollmentRepository = enrollmentRepository;
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
        
        Map<?,?> student=getStudentByStudentId(studentId);

        // 2. 调用课程目录服务验证课程是否存在
        String courseurl = catalogServiceUrl + "/api/courses/" + courseId;
        Map<String, Object> courseResponse;
        try {
            courseResponse = restTemplate.getForObject(courseurl, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("课程不存在："+ courseId);
        }
        // 3. 从响应中提取课程信息
        /*Map<String, Object> courseData = (Map<String, Object>)
                courseResponse.get("data");
        Integer capacity = (Integer) courseData.get("capacity");
        Integer enrolled = (Integer) courseData.get("enrolled");
        // 4. 检查课程容量
        if (enrolled >= capacity) {
            throw new ResourceConflictException("课程人数已满！");
        }*/
        // 5. 检查重复选课
        // 6. 创建选课记录
        Enrollment enrollment;
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId,courseId
                )) {
            enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId,courseId).get();
            if(enrollment.getStatus().equals(Status.ACTIVE)){
                throw new ResourceConflictException("该学生已选择该课程！");
            }
            else{
                enrollment.setStatus(Status.ACTIVE);
            }
        }
        else{
            enrollment = new Enrollment();
            enrollment.setCourseId(courseId);
            enrollment.setStudentId(studentId);
            enrollment.setStatus(Status.ACTIVE);
        }

        //enrollment.setEnrolledAt(LocalDateTime.now());
        Enrollment saved = enrollmentRepository.save(enrollment);
        // 7. 更新课程的已选人数（调用catalog-service）
        //updateCourseEnrolledCount(courseId, enrolled + 1);
        increaseCourseEnrolledCount(courseId);
        return saved;
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        // 确保学生存在
        Map<?,?> student=getStudentByStudentId(studentId);
        return enrollmentRepository.findByStudentId(studentId);
    }
    public List<Enrollment> getEnrollmentsByCourse(String courseId) {
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
        Map<?,?> student=getStudentByStudentId(studentId);

        Enrollment enrollment=enrollmentRepository.findByStudentIdAndCourseId(studentId,courseId)
                .orElseThrow(()->new ResourceNotFoundException("选课记录不存在！"));


        // 更新课程人数
        // 更新课程人数：先远程获取当前人数，再 PUT 更新
        /*int enrolled = getEnrolledCount(courseId);
        if (enrolled > 0) {
            updateCourseEnrolledCount(courseId, enrolled - 1); // 远程更新减少 1
        }
        else {
            throw new ResourceConflictException("课程人数已空！");
        }*/
        decreaseCourseEnrolledCount(courseId);
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
        /*int enrolled = getEnrolledCount(courseId);
        if (enrolled > 0) {
            updateCourseEnrolledCount(courseId, enrolled - 1); // 远程更新减少 1
        }
        else {
            throw new ResourceConflictException("课程人数已空！");
        }*/
        decreaseCourseEnrolledCount(courseId);
        enrollmentRepository.deleteById(id);
        return enrollment;
    }

    private void increaseCourseEnrolledCount(String courseId){
        String url=catalogServiceUrl+"/api/courses/"+courseId+"/increment";
        restTemplate.put(url, null);

    }
    private void decreaseCourseEnrolledCount(String courseId){
        String url=catalogServiceUrl+"/api/courses/"+courseId+"/decrement";
        restTemplate.put(url, null);

    }

    public Map<String,Object> getStudentByStudentId(String studentId){
        String url = userServiceUrl + "/api/users/students/" + studentId;
        Map<String, Object> studentResp;
        try {
            studentResp = restTemplate.getForObject(url, Map.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("学生不存在："+ studentId);
        }
        return studentResp;
    }
}
