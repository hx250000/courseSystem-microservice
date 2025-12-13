package com.zjgsu.hx.enrollment_service.service;

import com.zjgsu.hx.enrollment_service.client.CatalogClient;
import com.zjgsu.hx.enrollment_service.client.UserClient;
import com.zjgsu.hx.enrollment_service.common.ApiResponse;
import com.zjgsu.hx.enrollment_service.dto.CourseDto;
import com.zjgsu.hx.enrollment_service.dto.StudentDto;
import com.zjgsu.hx.enrollment_service.exception.ResourceConflictException;
import com.zjgsu.hx.enrollment_service.exception.ResourceNotFoundException;
import com.zjgsu.hx.enrollment_service.model.Enrollment;
import com.zjgsu.hx.enrollment_service.model.Status;
import com.zjgsu.hx.enrollment_service.repository.EnrollmentRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private CatalogClient catalogClient;

    @Value("${catalog-service.url}") // 引用 application-docker.yml 中的配置
    private String catalogServiceUrl;

    @Value("${user-service.url}")
    private String userServiceUrl;

    public EnrollmentService(EnrollmentRepository enrollmentRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }
    /**
     * 创建选课记录（选课）
     */
    @Transactional
    public Enrollment createEnrollment(String studentId, String courseId) {
        //System.out.println(restTemplate.getClass());
        // 1. 调用用户服务验证学生是否存在
        try
        {
            ApiResponse<StudentDto> stu = userClient.getStudent(studentId);
            if(stu.getData()==null){
                throw new ResourceNotFoundException("[Feign]学生不存在："+ studentId);
            }
        }
        catch (FeignException.NotFound e){
            throw new ResourceNotFoundException("[Feign]学生不存在："+ studentId);
        }
        catch (Exception e){
            throw new ResourceNotFoundException("无法访问用户服务！"+ e);
        }
        //Map<?,?> student=getStudentByStudentId(studentId);

        System.out.println("[Feign]正在查询记录，学生="+studentId+",课程="+courseId);
        try{
            ApiResponse<CourseDto> c=catalogClient.getCourse(courseId);
            if(c.getData()==null){
                throw new ResourceNotFoundException("课程不存在："+ courseId);
            }
            System.out.println("[Feign]查询到课程："+c.toString());
        }
        catch (FeignException.NotFound e){
            throw new ResourceNotFoundException("[Feign]课程不存在："+ courseId);
        }
        catch (Exception e){
            throw new ResourceNotFoundException("无法访问课程服务！"+ e);
        }

        Enrollment enrollment;
        // 3. 检查是否已选该课程
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId,courseId
                )) {
            enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId,courseId).get();
            if(enrollment.getStatus().equals(Status.ACTIVE)){
                throw new ResourceConflictException("该学生已选择该课程！");
            }
            // 4. 如果之前退选过该课程，则重新激活选课记录
            else{
                enrollment.setStatus(Status.ACTIVE);
            }
        }
        // 5. 保存选课记录
        else{
            enrollment = new Enrollment();
            enrollment.setCourseId(courseId);
            enrollment.setStudentId(studentId);
            enrollment.setStatus(Status.ACTIVE);
        }

        Enrollment saved = enrollmentRepository.save(enrollment);
        // 7. 更新课程的已选人数（调用catalog-service）
        increaseCourseEnrolledCount(courseId);
        return saved;
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) {
        // 确保学生存在
        System.out.println("[Feign]正在查询学生选课记录，学生="+studentId);
        try
        {
            ApiResponse<StudentDto> stu = userClient.getStudent(studentId);
            if(stu.getData()==null){
                throw new ResourceNotFoundException("[Feign]学生不存在："+ studentId);
            }
            System.out.println("[Feign]查询到学生："+stu.toString());
        }
        catch (FeignException.NotFound e){
            throw new ResourceNotFoundException("[Feign]学生不存在："+ studentId);
        }
        catch (Exception e){
            throw new ResourceNotFoundException("无法访问用户服务！"+ e);
        }

        return enrollmentRepository.findByStudentId(studentId);
    }
    public List<Enrollment> getEnrollmentsByCourse(String courseId) {
        // 调用课程目录服务验证课程是否存在
        System.out.println("[Feign]正在查询课程选课记录，课程="+courseId);
        try{
            ApiResponse<CourseDto> c=catalogClient.getCourse(courseId);
            System.out.println("[Feign]查询到课程："+c.toString());
            if(c.getData()==null){
                throw new ResourceNotFoundException("[Feign]课程不存在："+ courseId);
            }
        }
        catch (FeignException.NotFound e){
            throw new ResourceNotFoundException("[Feign]课程不存在："+ courseId);
        }
        catch (Exception e){
            throw new ResourceNotFoundException("无法访问课程服务！"+ e);
        }

        return enrollmentRepository.findByCourseId(courseId);
    }
    /*
     *删除选课记录（退课）
     */
    @Transactional
    public Enrollment deleteEnrollment(String studentId, String courseId) {
        // 找到该学生的选课记录
        System.out.printf("[Feign]正在删除选课记录，学生=%s,课程=%s\n",studentId,courseId);
        try
        {
            ApiResponse<StudentDto> stu = userClient.getStudent(studentId);
            if(stu.getData()==null){
                throw new ResourceNotFoundException("[Feign]学生不存在："+ studentId);
            }
            System.out.println("[Feign]查询到学生："+stu.toString());
        }
        catch (FeignException.NotFound e){
            throw new ResourceNotFoundException("[Feign]学生不存在："+ studentId);
        }
        catch (Exception e){
            throw new ResourceNotFoundException("无法访问用户服务！"+ e);
        }

        Enrollment enrollment=enrollmentRepository.findByStudentIdAndCourseId(studentId,courseId)
                .orElseThrow(()->new ResourceNotFoundException("选课记录不存在！"));

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
        decreaseCourseEnrolledCount(courseId);
        enrollmentRepository.deleteById(id);
        return enrollment;
    }

    private void increaseCourseEnrolledCount(String courseId){

        catalogClient.increaseEnrolledCount(courseId);

    }
    private void decreaseCourseEnrolledCount(String courseId){

        catalogClient.decreaseEnrolledCount(courseId);

    }

}

