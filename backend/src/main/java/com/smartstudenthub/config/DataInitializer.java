package com.smartstudenthub.config;

import com.smartstudenthub.entity.*;
import com.smartstudenthub.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final LecturerRepository lecturerRepository;
    private final StudentRepository studentRepository;
    private final CourseEnrollmentRepository enrollmentRepository;
    private final CampusLocationRepository locationRepository;
    private final BookRepository bookRepository;
    private final FeeCategoryRepository feeCategoryRepository;
    private final StudentFeeRepository studentFeeRepository;
    private final EventRepository eventRepository;
    private final InternshipRepository internshipRepository;
    private final TimetableEntryRepository timetableRepository;
    private final AssessmentRepository assessmentRepository;
    private final MarkRepository markRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            RoleRepository roleRepository,
            UserRepository userRepository,
            DepartmentRepository departmentRepository,
            CourseRepository courseRepository,
            SubjectRepository subjectRepository,
            LecturerRepository lecturerRepository,
            StudentRepository studentRepository,
            CourseEnrollmentRepository enrollmentRepository,
            CampusLocationRepository locationRepository,
            BookRepository bookRepository,
            FeeCategoryRepository feeCategoryRepository,
            StudentFeeRepository studentFeeRepository,
            EventRepository eventRepository,
            InternshipRepository internshipRepository,
            TimetableEntryRepository timetableRepository,
            AssessmentRepository assessmentRepository,
            MarkRepository markRepository,
            PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.subjectRepository = subjectRepository;
        this.lecturerRepository = lecturerRepository;
        this.studentRepository = studentRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.locationRepository = locationRepository;
        this.bookRepository = bookRepository;
        this.feeCategoryRepository = feeCategoryRepository;
        this.studentFeeRepository = studentFeeRepository;
        this.eventRepository = eventRepository;
        this.internshipRepository = internshipRepository;
        this.timetableRepository = timetableRepository;
        this.assessmentRepository = assessmentRepository;
        this.markRepository = markRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        logger.info("Checking database initialization status...");

        // 1. Initialize Roles
        Role adminRole = roleRepository.findByName(RoleType.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(RoleType.ROLE_ADMIN)));
        Role lecturerRole = roleRepository.findByName(RoleType.ROLE_LECTURER)
                .orElseGet(() -> roleRepository.save(new Role(RoleType.ROLE_LECTURER)));
        Role studentRole = roleRepository.findByName(RoleType.ROLE_STUDENT)
                .orElseGet(() -> roleRepository.save(new Role(RoleType.ROLE_STUDENT)));

        // 2. Initialize Default Administrator
        User admin = userRepository.findByUsername("admin").orElse(null);
        if (admin == null) {
            admin = new User(
                    "admin",
                    "admin@smartstudenthub.edu",
                    passwordEncoder.encode("Admin@123"),
                    "System Administrator",
                    adminRole
            );
            admin.setPhone("+1-555-0199");
            admin = userRepository.save(admin);
            logger.info("Created default administrator: admin / Admin@123");
        }

        // 3. Departments
        if (departmentRepository.count() == 0) {
            Department cse = departmentRepository.save(new Department("CSE", "Computer Science & Engineering", "Department of Computer Science and Engineering"));
            Department ece = departmentRepository.save(new Department("ECE", "Electronics & Communication", "Department of Electronics and Communication Engineering"));
            Department mba = departmentRepository.save(new Department("MBA", "School of Business Administration", "Graduate School of Business and Management"));

            // 4. Courses
            Course btechCse = courseRepository.save(new Course("BTECH-CSE", "B.Tech in Computer Science", cse, 4, 8));
            Course btechEce = courseRepository.save(new Course("BTECH-ECE", "B.Tech in Electronics & Comm", ece, 4, 8));
            Course mbaCourse = courseRepository.save(new Course("MBA-GEN", "Master of Business Administration", mba, 2, 4));

            // 5. Subjects
            Subject dsa = subjectRepository.save(new Subject("CS201", "Data Structures & Algorithms", btechCse, 3, 4));
            Subject dbms = subjectRepository.save(new Subject("CS202", "Database Management Systems", btechCse, 3, 4));
            Subject cn = subjectRepository.save(new Subject("CS301", "Computer Networks", btechCse, 5, 3));
            Subject se = subjectRepository.save(new Subject("CS302", "Software Engineering", btechCse, 5, 3));
            Subject sigSys = subjectRepository.save(new Subject("EC201", "Signals & Systems", btechEce, 3, 4));

            // 6. Lecturers
            User lecUser1 = userRepository.save(new User(
                    "alan_turing",
                    "alan.turing@smartstudenthub.edu",
                    passwordEncoder.encode("Lecturer@123"),
                    "Dr. Alan Turing",
                    lecturerRole
            ));
            Lecturer lec1 = new Lecturer(lecUser1, "EMP-CSE-101", cse, "Ph.D. Computer Science", "Professor", "Algorithms & Theory", LocalDate.of(2020, 8, 1));
            lec1.getCourses().add(btechCse);
            lec1 = lecturerRepository.save(lec1);

            User lecUser2 = userRepository.save(new User(
                    "ada_lovelace",
                    "ada.lovelace@smartstudenthub.edu",
                    passwordEncoder.encode("Lecturer@123"),
                    "Prof. Ada Lovelace",
                    lecturerRole
            ));
            Lecturer lec2 = new Lecturer(lecUser2, "EMP-CSE-102", cse, "M.Tech Software Engineering", "Associate Professor", "Software Engineering & Architecture", LocalDate.of(2021, 1, 15));
            lec2.getCourses().add(btechCse);
            lec2 = lecturerRepository.save(lec2);

            // 7. Students
            User stuUser1 = userRepository.save(new User(
                    "john_doe",
                    "john.doe@smartstudenthub.edu",
                    passwordEncoder.encode("Student@123"),
                    "John Doe",
                    studentRole
            ));
            Student stu1 = new Student(
                    stuUser1,
                    "STU-2026-001",
                    cse,
                    btechCse,
                    "2025-2026",
                    3,
                    LocalDate.of(2004, 5, 12),
                    "MALE",
                    "Room 204, North Campus Hostel",
                    LocalDate.of(2024, 8, 10)
            );
            stu1.setEmergencyContact("+1-555-8821");
            stu1 = studentRepository.save(stu1);
            enrollmentRepository.save(new CourseEnrollment(stu1, btechCse, "2025-2026", 3));

            User stuUser2 = userRepository.save(new User(
                    "jane_smith",
                    "jane.smith@smartstudenthub.edu",
                    passwordEncoder.encode("Student@123"),
                    "Jane Smith",
                    studentRole
            ));
            Student stu2 = new Student(
                    stuUser2,
                    "STU-2026-002",
                    cse,
                    btechCse,
                    "2025-2026",
                    3,
                    LocalDate.of(2004, 9, 22),
                    "FEMALE",
                    "Room 108, South Campus Hostel",
                    LocalDate.of(2024, 8, 10)
            );
            stu2.setEmergencyContact("+1-555-9932");
            stu2 = studentRepository.save(stu2);
            enrollmentRepository.save(new CourseEnrollment(stu2, btechCse, "2025-2026", 3));

            // 8. Timetable Entries
            TimetableEntry tt1 = new TimetableEntry();
            tt1.setCourse(btechCse);
            tt1.setSubject(dsa);
            tt1.setLecturer(lec1);
            tt1.setClassroom("LH-101");
            tt1.setDayOfWeek("MONDAY");
            tt1.setStartTime(LocalTime.of(9, 30));
            tt1.setEndTime(LocalTime.of(10, 30));
            tt1.setSemester(3);
            tt1.setAcademicYear("2025-2026");
            timetableRepository.save(tt1);

            TimetableEntry tt2 = new TimetableEntry();
            tt2.setCourse(btechCse);
            tt2.setSubject(dbms);
            tt2.setLecturer(lec2);
            tt2.setClassroom("LAB-202");
            tt2.setDayOfWeek("MONDAY");
            tt2.setStartTime(LocalTime.of(11, 0));
            tt2.setEndTime(LocalTime.of(12, 30));
            tt2.setSemester(3);
            tt2.setAcademicYear("2025-2026");
            timetableRepository.save(tt2);

            // 9. Assessment & Sample Marks
            Assessment asm = new Assessment();
            asm.setTitle("Mid-Term Exam — Data Structures");
            asm.setCourse(btechCse);
            asm.setSubject(dsa);
            asm.setLecturer(lec1);
            asm.setType("INTERNAL_EXAM");
            asm.setMaxMarks(new BigDecimal("50.00"));
            asm.setAcademicYear("2025-2026");
            asm.setSemester(3);
            asm.setPublished(true);
            asm = assessmentRepository.save(asm);

            Mark m1 = new Mark();
            m1.setAssessment(asm);
            m1.setStudent(stu1);
            m1.setMarksObtained(new BigDecimal("44.50"));
            m1.setGrade("A");
            m1.setRemarks("Excellent algorithmic performance");
            m1.setGradedBy(lecUser1.getFullName());
            markRepository.save(m1);

            // 10. Campus Locations
            locationRepository.save(new CampusLocation("Academic Block Alpha", "ACADEMIC", new BigDecimal("17.385044"), new BigDecimal("78.486671"), "Central classrooms and lecture theaters", "Block A"));
            locationRepository.save(new CampusLocation("Turing Computing Lab", "LAB", new BigDecimal("17.385200"), new BigDecimal("78.487100"), "Advanced High Performance Computing Labs", "Block B"));
            locationRepository.save(new CampusLocation("University Central Library", "LIBRARY", new BigDecimal("17.384800"), new BigDecimal("78.486200"), "Four-story library with digital media center", "Block C"));
            locationRepository.save(new CampusLocation("Administrative Headquarters", "ADMIN", new BigDecimal("17.384500"), new BigDecimal("78.485900"), "Offices of Dean, Registrar, and Student Affairs", "Admin Block"));
            locationRepository.save(new CampusLocation("Campus Arena & Sports Complex", "SPORTS", new BigDecimal("17.385600"), new BigDecimal("78.487800"), "Indoor basketball court and track stadium", "Sports Arena"));

            // 11. Library Books
            bookRepository.save(new Book("9780262033848", "Introduction to Algorithms (4th Edition)", "Thomas H. Cormen", "Computer Science", "MIT Press", 2022, 5, "Shelf CS-01"));
            bookRepository.save(new Book("9780132350884", "Clean Code: A Handbook of Agile Craftsmanship", "Robert C. Martin", "Software Engineering", "Prentice Hall", 2008, 4, "Shelf SE-04"));
            bookRepository.save(new Book("9780073523323", "Database System Concepts", "Abraham Silberschatz", "Database Systems", "McGraw-Hill", 2019, 6, "Shelf DB-02"));

            // 12. Fee Categories & Sample Fee Assignment
            FeeCategory tuitionCat = feeCategoryRepository.save(new FeeCategory("Semester Tuition Fee", "Standard semester tuition fees", new BigDecimal("50000.00")));
            FeeCategory examCat = feeCategoryRepository.save(new FeeCategory("Examination Fee", "Semester end examination registration fee", new BigDecimal("2500.00")));

            StudentFee sf = new StudentFee(stu1, tuitionCat, "2025-2026", 3, new BigDecimal("50000.00"), LocalDate.of(2026, 11, 30));
            sf.setAmountPaid(new BigDecimal("20000.00"));
            sf.recalculateBalance();
            studentFeeRepository.save(sf);

            // 13. Events
            Event evt = new Event();
            evt.setTitle("Smart University Hackathon 2026");
            evt.setDescription("48-hour collaborative engineering hackathon tackling smart campus challenges.");
            evt.setCategory("HACKATHON");
            evt.setVenue("Auditorium Hall 1");
            evt.setEventDate(LocalDateTime.now().plusDays(10));
            evt.setRegistrationDeadline(LocalDateTime.now().plusDays(8));
            evt.setCapacity(120);
            evt.setRegisteredCount(1);
            evt.setOrganizer(admin);
            evt.setActive(true);
            eventRepository.save(evt);

            // 14. Internships
            Internship int1 = new Internship();
            int1.setTitle("Full-Stack Software Engineering Intern");
            int1.setCompany("TechNova Cloud Systems");
            int1.setDescription("Build scalable microservices and dynamic user interfaces using Spring Boot and React.");
            int1.setRequiredSkills("Java, Spring Boot, React, SQL, Git");
            int1.setEligibilityCriteria("B.Tech CSE/IT, minimum GPA 7.0/10.0");
            int1.setLocation("Silicon Valley / Remote");
            int1.setWorkMode("HYBRID");
            int1.setStipend("$2,500 / month");
            int1.setDeadline(LocalDate.now().plusDays(25));
            int1.setInternal(true);
            int1.setStatus("OPEN");
            int1.setPostedBy(admin);
            internshipRepository.save(int1);

            logger.info("Fictional academic demo data seeded successfully!");
        }
    }
}
