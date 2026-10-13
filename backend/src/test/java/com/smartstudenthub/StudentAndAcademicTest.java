package com.smartstudenthub;

import com.smartstudenthub.dto.StudentRequest;
import com.smartstudenthub.dto.TimetableRequest;
import com.smartstudenthub.entity.Course;
import com.smartstudenthub.entity.Department;
import com.smartstudenthub.entity.Lecturer;
import com.smartstudenthub.entity.Subject;
import com.smartstudenthub.exception.ConflictException;
import com.smartstudenthub.repository.CourseRepository;
import com.smartstudenthub.repository.DepartmentRepository;
import com.smartstudenthub.repository.LecturerRepository;
import com.smartstudenthub.repository.SubjectRepository;
import com.smartstudenthub.service.AcademicService;
import com.smartstudenthub.service.StudentService;
import com.smartstudenthub.service.TimetableService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StudentAndAcademicTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AcademicService academicService;

    @Autowired
    private TimetableService timetableService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Test
    public void testDuplicateEmailThrowsConflict() {
        Department dept = departmentRepository.findAll().get(0);
        Course course = courseRepository.findAll().get(0);

        String email = "unique_test_" + UUID.randomUUID() + "@smartstudenthub.edu";

        StudentRequest r1 = new StudentRequest();
        r1.setFullName("Test Student 1");
        r1.setEmail(email);
        r1.setDepartmentId(dept.getId());
        r1.setCourseId(course.getId());
        r1.setAcademicYear("2025-2026");
        r1.setCurrentSemester(1);
        r1.setEnrollmentDate(LocalDate.now());

        studentService.createStudent(r1);

        StudentRequest r2 = new StudentRequest();
        r2.setFullName("Test Student 2");
        r2.setEmail(email);
        r2.setDepartmentId(dept.getId());
        r2.setCourseId(course.getId());
        r2.setAcademicYear("2025-2026");
        r2.setCurrentSemester(1);
        r2.setEnrollmentDate(LocalDate.now());

        assertThrows(ConflictException.class, () -> studentService.createStudent(r2));
    }

    @Test
    public void testTimetableClashDetection() {
        Course course = courseRepository.findAll().get(0);
        Subject subject = subjectRepository.findByCourseId(course.getId()).get(0);
        Lecturer lecturer = lecturerRepository.findAll().get(0);

        TimetableRequest req1 = new TimetableRequest();
        req1.setCourseId(course.getId());
        req1.setSubjectId(subject.getId());
        req1.setLecturerId(lecturer.getId());
        req1.setClassroom("TEST-ROOM-101");
        req1.setDayOfWeek("WEDNESDAY");
        req1.setStartTime(LocalTime.of(10, 0));
        req1.setEndTime(LocalTime.of(11, 0));
        req1.setSemester(1);
        req1.setAcademicYear("2025-2026");

        timetableService.createTimetableEntry(req1);

        // Attempt overlapping slot in the same classroom
        TimetableRequest req2 = new TimetableRequest();
        req2.setCourseId(course.getId());
        req2.setSubjectId(subject.getId());
        req2.setLecturerId(lecturer.getId());
        req2.setClassroom("TEST-ROOM-101"); // same room
        req2.setDayOfWeek("WEDNESDAY");
        req2.setStartTime(LocalTime.of(10, 30)); // overlaps!
        req2.setEndTime(LocalTime.of(11, 30));
        req2.setSemester(1);
        req2.setAcademicYear("2025-2026");

        assertThrows(ConflictException.class, () -> timetableService.createTimetableEntry(req2));
    }
}
