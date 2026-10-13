package com.smartstudenthub.service;

import com.smartstudenthub.dto.StudentRequest;
import com.smartstudenthub.dto.StudentResponse;
import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ConflictException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import com.smartstudenthub.util.CsvHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentService(
            StudentRepository studentRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            DepartmentRepository departmentRepository,
            CourseRepository courseRepository,
            PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Page<StudentResponse> searchStudents(String query, Long deptId, Long courseId, Integer semester, String academicYear, String status, Pageable pageable) {
        return studentRepository.searchStudents(query, deptId, courseId, semester, academicYear, status, pageable)
                .map(this::mapToResponse);
    }

    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + id));
        return mapToResponse(student);
    }

    public StudentResponse getStudentByUserId(Long userId) {
        Student student = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found for user ID: " + userId));
        return mapToResponse(student);
    }

    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already registered: " + request.getEmail());
        }

        String regNum = request.getRegistrationNumber();
        if (regNum == null || regNum.trim().isEmpty()) {
            regNum = "STU-" + LocalDate.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        } else if (studentRepository.existsByRegistrationNumber(regNum)) {
            throw new ConflictException("Registration number already exists: " + regNum);
        }

        String username = request.getUsername();
        if (username == null || username.trim().isEmpty()) {
            username = regNum.toLowerCase().replace("-", "_");
        }
        if (userRepository.existsByUsername(username)) {
            username = username + "_" + UUID.randomUUID().toString().substring(0, 4);
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Role role = roleRepository.findByName(RoleType.ROLE_STUDENT)
                .orElseGet(() -> roleRepository.save(new Role(RoleType.ROLE_STUDENT)));

        String rawPassword = (request.getPassword() != null && !request.getPassword().trim().isEmpty())
                ? request.getPassword() : "Student@123";

        User user = new User(username, request.getEmail(), passwordEncoder.encode(rawPassword), request.getFullName(), role);
        user.setPhone(request.getPhone());
        user = userRepository.save(user);

        Student student = new Student(
                user,
                regNum,
                department,
                course,
                request.getAcademicYear() != null ? request.getAcademicYear() : String.valueOf(LocalDate.now().getYear()),
                request.getCurrentSemester(),
                request.getDateOfBirth(),
                request.getGender(),
                request.getAddress(),
                request.getEnrollmentDate() != null ? request.getEnrollmentDate() : LocalDate.now()
        );
        student.setEmergencyContact(request.getEmergencyContact());
        student.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");

        student = studentRepository.save(student);
        return mapToResponse(student);
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        User user = student.getUser();
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        userRepository.save(user);

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            student.setDepartment(dept);
        }
        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
            student.setCourse(course);
        }
        if (request.getAcademicYear() != null) student.setAcademicYear(request.getAcademicYear());
        if (request.getCurrentSemester() > 0) student.setCurrentSemester(request.getCurrentSemester());
        if (request.getDateOfBirth() != null) student.setDateOfBirth(request.getDateOfBirth());
        if (request.getGender() != null) student.setGender(request.getGender());
        if (request.getAddress() != null) student.setAddress(request.getAddress());
        if (request.getEmergencyContact() != null) student.setEmergencyContact(request.getEmergencyContact());
        if (request.getStatus() != null) student.setStatus(request.getStatus());

        student = studentRepository.save(student);
        return mapToResponse(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        student.setStatus("INACTIVE");
        student.getUser().setEnabled(false);
        studentRepository.save(student);
    }

    public String exportStudentsCsv() {
        List<Student> list = studentRepository.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Registration Number,Full Name,Email,Phone,Department,Course,Semester,Academic Year,Status\n");
        for (Student s : list) {
            sb.append(s.getId()).append(",")
              .append(CsvHelper.escapeCsv(s.getRegistrationNumber())).append(",")
              .append(CsvHelper.escapeCsv(s.getUser().getFullName())).append(",")
              .append(CsvHelper.escapeCsv(s.getUser().getEmail())).append(",")
              .append(CsvHelper.escapeCsv(s.getUser().getPhone())).append(",")
              .append(CsvHelper.escapeCsv(s.getDepartment().getName())).append(",")
              .append(CsvHelper.escapeCsv(s.getCourse().getName())).append(",")
              .append(s.getCurrentSemester()).append(",")
              .append(CsvHelper.escapeCsv(s.getAcademicYear())).append(",")
              .append(CsvHelper.escapeCsv(s.getStatus())).append("\n");
        }
        return sb.toString();
    }

    public StudentResponse mapToResponse(Student s) {
        StudentResponse r = new StudentResponse();
        r.setId(s.getId());
        r.setUserId(s.getUser().getId());
        r.setRegistrationNumber(s.getRegistrationNumber());
        r.setFullName(s.getUser().getFullName());
        r.setEmail(s.getUser().getEmail());
        r.setUsername(s.getUser().getUsername());
        r.setPhone(s.getUser().getPhone());
        r.setProfilePhoto(s.getUser().getProfilePhoto());
        r.setDepartmentId(s.getDepartment().getId());
        r.setDepartmentName(s.getDepartment().getName());
        r.setCourseId(s.getCourse().getId());
        r.setCourseName(s.getCourse().getName());
        r.setAcademicYear(s.getAcademicYear());
        r.setCurrentSemester(s.getCurrentSemester());
        r.setDateOfBirth(s.getDateOfBirth());
        r.setGender(s.getGender());
        r.setAddress(s.getAddress());
        r.setEmergencyContact(s.getEmergencyContact());
        r.setStatus(s.getStatus());
        r.setEnrollmentDate(s.getEnrollmentDate());
        return r;
    }
}
