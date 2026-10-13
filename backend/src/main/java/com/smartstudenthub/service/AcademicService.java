package com.smartstudenthub.service;

import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.BadRequestException;
import com.smartstudenthub.exception.ConflictException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AcademicService {

    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final LecturerRepository lecturerRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    public AcademicService(
            DepartmentRepository departmentRepository,
            CourseRepository courseRepository,
            SubjectRepository subjectRepository,
            LecturerRepository lecturerRepository,
            CourseEnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository) {
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
    }

    // Departments
    public List<Department> getAllDepartments() { return departmentRepository.findAll(); }

    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found"));
    }

    @Transactional
    public Department createDepartment(Department department) {
        if (departmentRepository.existsByCode(department.getCode())) {
            throw new ConflictException("Department code already exists: " + department.getCode());
        }
        return departmentRepository.save(department);
    }

    // Courses
    public List<Course> getAllCourses() { return courseRepository.findAll(); }

    public List<Course> getCoursesByDepartment(Long deptId) { return courseRepository.findByDepartmentId(deptId); }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    @Transactional
    public Course createCourse(Course course) {
        if (courseRepository.existsByCode(course.getCode())) {
            throw new ConflictException("Course code already exists: " + course.getCode());
        }
        return courseRepository.save(course);
    }

    // Subjects
    public List<Subject> getAllSubjects() { return subjectRepository.findAll(); }

    public List<Subject> getSubjectsByCourse(Long courseId) { return subjectRepository.findByCourseId(courseId); }

    public List<Subject> getSubjectsByCourseAndSemester(Long courseId, int semester) {
        return subjectRepository.findByCourseIdAndSemester(courseId, semester);
    }

    @Transactional
    public Subject createSubject(Subject subject) {
        if (subjectRepository.existsByCode(subject.getCode())) {
            throw new ConflictException("Subject code already exists: " + subject.getCode());
        }
        return subjectRepository.save(subject);
    }

    // Lecturers
    public List<Lecturer> getAllLecturers() { return lecturerRepository.findAll(); }

    public Lecturer getLecturerById(Long id) {
        return lecturerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));
    }

    public Lecturer getLecturerByUserId(Long userId) {
        return lecturerRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Lecturer not found for user ID: " + userId));
    }

    @Transactional
    public Lecturer assignLecturerToCourse(Long lecturerId, Long courseId) {
        Lecturer lecturer = getLecturerById(lecturerId);
        Course course = getCourseById(courseId);
        lecturer.getCourses().add(course);
        return lecturerRepository.save(lecturer);
    }

    // Enrollments
    public List<CourseEnrollment> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Transactional
    public CourseEnrollment enrollStudent(Long studentId, Long courseId, String academicYear, int semester) {
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndAcademicYearAndSemester(studentId, courseId, academicYear, semester)) {
            throw new ConflictException("Student is already enrolled in this course for " + academicYear + " semester " + semester);
        }
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Course course = getCourseById(courseId);

        CourseEnrollment enrollment = new CourseEnrollment(student, course, academicYear, semester);
        return enrollmentRepository.save(enrollment);
    }
}
