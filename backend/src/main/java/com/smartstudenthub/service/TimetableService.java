package com.smartstudenthub.service;

import com.smartstudenthub.dto.TimetableRequest;
import com.smartstudenthub.entity.*;
import com.smartstudenthub.exception.ConflictException;
import com.smartstudenthub.exception.ResourceNotFoundException;
import com.smartstudenthub.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TimetableService {

    private final TimetableEntryRepository timetableRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;

    public TimetableService(
            TimetableEntryRepository timetableRepository,
            CourseRepository courseRepository,
            SubjectRepository subjectRepository,
            LecturerRepository lecturerRepository,
            StudentRepository studentRepository) {
        this.timetableRepository = timetableRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
    }

    public List<TimetableEntry> getCourseTimetable(Long courseId, int semester) {
        return timetableRepository.findByCourseIdAndSemester(courseId, semester);
    }

    public List<TimetableEntry> getLecturerTimetable(Long lecturerId) {
        return timetableRepository.findByLecturerId(lecturerId);
    }

    public List<TimetableEntry> getStudentTimetable(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return timetableRepository.findByCourseIdAndSemester(student.getCourse().getId(), student.getCurrentSemester());
    }

    @Transactional
    public TimetableEntry createTimetableEntry(TimetableRequest request) {
        validateNoClashes(request, null);

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Lecturer lecturer = lecturerRepository.findById(request.getLecturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));

        TimetableEntry entry = new TimetableEntry();
        entry.setCourse(course);
        entry.setSubject(subject);
        entry.setLecturer(lecturer);
        entry.setClassroom(request.getClassroom());
        entry.setDayOfWeek(request.getDayOfWeek().toUpperCase());
        entry.setStartTime(request.getStartTime());
        entry.setEndTime(request.getEndTime());
        entry.setSemester(request.getSemester());
        entry.setAcademicYear(request.getAcademicYear());

        return timetableRepository.save(entry);
    }

    @Transactional
    public TimetableEntry updateTimetableEntry(Long id, TimetableRequest request) {
        TimetableEntry entry = timetableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Timetable entry not found"));

        validateNoClashes(request, id);

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));
        Lecturer lecturer = lecturerRepository.findById(request.getLecturerId())
                .orElseThrow(() -> new ResourceNotFoundException("Lecturer not found"));

        entry.setCourse(course);
        entry.setSubject(subject);
        entry.setLecturer(lecturer);
        entry.setClassroom(request.getClassroom());
        entry.setDayOfWeek(request.getDayOfWeek().toUpperCase());
        entry.setStartTime(request.getStartTime());
        entry.setEndTime(request.getEndTime());
        entry.setSemester(request.getSemester());
        entry.setAcademicYear(request.getAcademicYear());

        return timetableRepository.save(entry);
    }

    @Transactional
    public void deleteTimetableEntry(Long id) {
        timetableRepository.deleteById(id);
    }

    private void validateNoClashes(TimetableRequest request, Long excludeId) {
        List<TimetableEntry> clashes = timetableRepository.findClashes(
                request.getDayOfWeek().toUpperCase(),
                request.getStartTime(),
                request.getEndTime(),
                request.getClassroom(),
                request.getLecturerId(),
                request.getCourseId(),
                request.getSemester(),
                excludeId
        );

        if (!clashes.isEmpty()) {
            TimetableEntry conflict = clashes.get(0);
            if (conflict.getClassroom().equalsIgnoreCase(request.getClassroom())) {
                throw new ConflictException("Schedule conflict: Classroom " + request.getClassroom() + " is already occupied on " + request.getDayOfWeek() + " between " + conflict.getStartTime() + " and " + conflict.getEndTime());
            }
            if (conflict.getLecturer().getId().equals(request.getLecturerId())) {
                throw new ConflictException("Schedule conflict: Lecturer " + conflict.getLecturer().getUser().getFullName() + " already has a class on " + request.getDayOfWeek() + " between " + conflict.getStartTime() + " and " + conflict.getEndTime());
            }
            if (conflict.getCourse().getId().equals(request.getCourseId()) && conflict.getSemester() == request.getSemester()) {
                throw new ConflictException("Schedule conflict: Course batch (" + conflict.getCourse().getName() + " Sem " + conflict.getSemester() + ") already has a class scheduled on " + request.getDayOfWeek() + " between " + conflict.getStartTime() + " and " + conflict.getEndTime());
            }
            throw new ConflictException("Timetable schedule conflict detected.");
        }
    }
}
