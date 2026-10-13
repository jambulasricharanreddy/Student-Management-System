-- ============================================================================
-- SMART STUDENT HUB — Advanced Student Management System
-- Normalized MySQL 8.x Database Schema
-- ============================================================================

CREATE DATABASE IF NOT EXISTS smart_student_hub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smart_student_hub;

-- 1. Roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- 2. Users (Central authentication entity)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    phone VARCHAR(30),
    role_id BIGINT NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    profile_photo VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 3. Departments
CREATE TABLE IF NOT EXISTS departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 4. Courses
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    department_id BIGINT NOT NULL,
    duration_years INT NOT NULL DEFAULT 4,
    total_semesters INT NOT NULL DEFAULT 8,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_courses_dept FOREIGN KEY (department_id) REFERENCES departments (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Subjects
CREATE TABLE IF NOT EXISTS subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(30) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    course_id BIGINT NOT NULL,
    semester INT NOT NULL,
    credits INT NOT NULL DEFAULT 3,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subjects_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 6. Lecturers
CREATE TABLE IF NOT EXISTS lecturers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    employee_id VARCHAR(50) NOT NULL UNIQUE,
    department_id BIGINT NOT NULL,
    qualification VARCHAR(150),
    designation VARCHAR(100),
    specialization VARCHAR(150),
    joining_date DATE,
    CONSTRAINT fk_lecturers_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_lecturers_dept FOREIGN KEY (department_id) REFERENCES departments (id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 7. Lecturer Course Assignment (Join table)
CREATE TABLE IF NOT EXISTS course_lecturers (
    course_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    PRIMARY KEY (course_id, lecturer_id),
    CONSTRAINT fk_cl_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT fk_cl_lecturer FOREIGN KEY (lecturer_id) REFERENCES lecturers (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 8. Students
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    registration_number VARCHAR(50) NOT NULL UNIQUE,
    department_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    current_semester INT NOT NULL DEFAULT 1,
    date_of_birth DATE,
    gender VARCHAR(20),
    address TEXT,
    emergency_contact VARCHAR(50),
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    enrollment_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_students_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_students_dept FOREIGN KEY (department_id) REFERENCES departments (id) ON DELETE RESTRICT,
    CONSTRAINT fk_students_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE RESTRICT,
    INDEX idx_students_reg (registration_number),
    INDEX idx_students_dept_course (department_id, course_id)
) ENGINE=InnoDB;

-- 9. Course Enrollments
CREATE TABLE IF NOT EXISTS course_enrollments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    semester INT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ENROLLED',
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_student_course_year (student_id, course_id, academic_year, semester),
    CONSTRAINT fk_enroll_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_enroll_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 10. Attendance Sessions
CREATE TABLE IF NOT EXISTS attendance_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_token VARCHAR(120) NOT NULL UNIQUE,
    token_hash VARCHAR(255) NOT NULL,
    course_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    session_date DATE NOT NULL,
    start_time TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_att_sess_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT fk_att_sess_subj FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,
    CONSTRAINT fk_att_sess_lec FOREIGN KEY (lecturer_id) REFERENCES lecturers (id) ON DELETE CASCADE,
    INDEX idx_att_token (session_token),
    INDEX idx_att_status (status)
) ENGINE=InnoDB;

-- 11. Attendance Records
CREATE TABLE IF NOT EXISTS attendance_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    marked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PRESENT',
    verification_mode VARCHAR(30) NOT NULL DEFAULT 'QR_SCAN',
    remarks VARCHAR(255),
    UNIQUE KEY uk_session_student (session_id, student_id),
    CONSTRAINT fk_att_rec_sess FOREIGN KEY (session_id) REFERENCES attendance_sessions (id) ON DELETE CASCADE,
    CONSTRAINT fk_att_rec_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_att_rec_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT fk_att_rec_subj FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,
    INDEX idx_att_student_course (student_id, course_id)
) ENGINE=InnoDB;

-- 12. Assessments
CREATE TABLE IF NOT EXISTS assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    course_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL, -- INTERNAL_EXAM, SEMESTER_EXAM, ASSIGNMENT, QUIZ
    max_marks DECIMAL(5, 2) NOT NULL,
    weightage_percent DECIMAL(5, 2) DEFAULT 100.0,
    academic_year VARCHAR(20) NOT NULL,
    semester INT NOT NULL,
    is_published BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_asm_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT fk_asm_subj FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,
    CONSTRAINT fk_asm_lec FOREIGN KEY (lecturer_id) REFERENCES lecturers (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 13. Marks
CREATE TABLE IF NOT EXISTS marks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    marks_obtained DECIMAL(5, 2) NOT NULL,
    grade VARCHAR(10),
    remarks VARCHAR(255),
    graded_by VARCHAR(100),
    graded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_by VARCHAR(100),
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_assessment_student (assessment_id, student_id),
    CONSTRAINT fk_marks_asm FOREIGN KEY (assessment_id) REFERENCES assessments (id) ON DELETE CASCADE,
    CONSTRAINT fk_marks_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 14. Timetable Entries
CREATE TABLE IF NOT EXISTS timetable_entries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    classroom VARCHAR(50) NOT NULL,
    day_of_week VARCHAR(15) NOT NULL, -- MONDAY, TUESDAY, etc.
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    semester INT NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    CONSTRAINT fk_tt_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT fk_tt_subj FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,
    CONSTRAINT fk_tt_lec FOREIGN KEY (lecturer_id) REFERENCES lecturers (id) ON DELETE CASCADE,
    INDEX idx_tt_day_time (day_of_week, start_time, end_time),
    INDEX idx_tt_room (classroom, day_of_week),
    INDEX idx_tt_lec (lecturer_id, day_of_week)
) ENGINE=InnoDB;

-- 15. Assignments
CREATE TABLE IF NOT EXISTS assignments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    course_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    lecturer_id BIGINT NOT NULL,
    due_date TIMESTAMP NOT NULL,
    max_marks DECIMAL(5, 2) NOT NULL DEFAULT 100.0,
    attachment_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_asgn_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    CONSTRAINT fk_asgn_subj FOREIGN KEY (subject_id) REFERENCES subjects (id) ON DELETE CASCADE,
    CONSTRAINT fk_asgn_lec FOREIGN KEY (lecturer_id) REFERENCES lecturers (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 16. Assignment Submissions
CREATE TABLE IF NOT EXISTS assignment_submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assignment_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    marks_awarded DECIMAL(5, 2),
    feedback TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED', -- SUBMITTED, GRADED, LATE, RESUBMITTED
    is_late BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE KEY uk_asgn_student (assignment_id, student_id),
    CONSTRAINT fk_sub_asgn FOREIGN KEY (assignment_id) REFERENCES assignments (id) ON DELETE CASCADE,
    CONSTRAINT fk_sub_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 17. Notifications
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NOT NULL DEFAULT 'INFO', -- INFO, WARNING, SUCCESS, ALERT
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    action_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    INDEX idx_notif_user_read (user_id, is_read)
) ENGINE=InnoDB;

-- 18. Internships
CREATE TABLE IF NOT EXISTS internships (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    company VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,
    required_skills VARCHAR(255),
    eligibility_criteria VARCHAR(255),
    location VARCHAR(100) NOT NULL,
    work_mode VARCHAR(50) NOT NULL DEFAULT 'HYBRID', -- ONSITE, REMOTE, HYBRID
    stipend VARCHAR(100),
    deadline DATE NOT NULL,
    apply_url VARCHAR(255),
    is_internal BOOLEAN NOT NULL DEFAULT TRUE,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    posted_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_int_poster FOREIGN KEY (posted_by) REFERENCES users (id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 19. Internship Applications
CREATE TABLE IF NOT EXISTS internship_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    internship_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resume_path VARCHAR(255),
    cover_letter TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'APPLIED', -- APPLIED, SHORTLISTED, REJECTED, ACCEPTED
    review_notes TEXT,
    UNIQUE KEY uk_int_app (internship_id, student_id),
    CONSTRAINT fk_app_int FOREIGN KEY (internship_id) REFERENCES internships (id) ON DELETE CASCADE,
    CONSTRAINT fk_app_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 20. Resume Profiles
CREATE TABLE IF NOT EXISTS resume_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL UNIQUE,
    career_objective TEXT,
    skills TEXT,
    education_json JSON,
    experience_json JSON,
    projects_json JSON,
    certifications_json JSON,
    achievements_json JSON,
    template_name VARCHAR(50) DEFAULT 'modern',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_res_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 21. Campus Locations
CREATE TABLE IF NOT EXISTS campus_locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL, -- ACADEMIC, LAB, LIBRARY, ADMIN, SPORTS, DINING
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL,
    description TEXT,
    building_number VARCHAR(30),
    floor_number VARCHAR(20),
    image_url VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- 22. Events
CREATE TABLE IF NOT EXISTS events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(50) NOT NULL,
    venue VARCHAR(150) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    registration_deadline TIMESTAMP NOT NULL,
    capacity INT NOT NULL,
    registered_count INT NOT NULL DEFAULT 0,
    organizer_id BIGINT,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_org FOREIGN KEY (organizer_id) REFERENCES users (id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 23. Event Registrations
CREATE TABLE IF NOT EXISTS event_registrations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    attended BOOLEAN NOT NULL DEFAULT FALSE,
    attended_at TIMESTAMP,
    ticket_qr_code VARCHAR(120) NOT NULL UNIQUE,
    UNIQUE KEY uk_event_reg (event_id, student_id),
    CONSTRAINT fk_ereg_event FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT fk_ereg_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 24. Certificates
CREATE TABLE IF NOT EXISTS certificates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    verification_id VARCHAR(100) NOT NULL UNIQUE,
    student_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    certificate_type VARCHAR(50) NOT NULL, -- COURSE_COMPLETION, MERIT, WORKSHOP, EXCELLENCE
    issue_date DATE NOT NULL,
    issuer_name VARCHAR(150) NOT NULL,
    description TEXT,
    is_revoked BOOLEAN NOT NULL DEFAULT FALSE,
    revocation_reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cert_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    INDEX idx_cert_verif (verification_id)
) ENGINE=InnoDB;

-- 25. Books
CREATE TABLE IF NOT EXISTS books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(30) UNIQUE,
    title VARCHAR(250) NOT NULL,
    author VARCHAR(150) NOT NULL,
    category VARCHAR(80) NOT NULL,
    publisher VARCHAR(150),
    publication_year INT,
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    shelf_location VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_books_search (title, author, isbn)
) ENGINE=InnoDB;

-- 26. Library Loans
CREATE TABLE IF NOT EXISTS library_loans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    issued_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'ISSUED', -- ISSUED, RETURNED, OVERDUE, LOST
    fine_amount DECIMAL(8, 2) DEFAULT 0.00,
    issued_by VARCHAR(100),
    CONSTRAINT fk_loan_book FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE RESTRICT,
    CONSTRAINT fk_loan_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 27. Fee Categories
CREATE TABLE IF NOT EXISTS fee_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    default_amount DECIMAL(10, 2) NOT NULL
) ENGINE=InnoDB;

-- 28. Student Fees
CREATE TABLE IF NOT EXISTS student_fees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    fee_category_id BIGINT NOT NULL,
    academic_year VARCHAR(20) NOT NULL,
    semester INT NOT NULL,
    amount_due DECIMAL(10, 2) NOT NULL,
    amount_paid DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    balance DECIMAL(10, 2) NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING', -- PENDING, PARTIALLY_PAID, PAID, OVERDUE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_fee_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_fee_cat FOREIGN KEY (fee_category_id) REFERENCES fee_categories (id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- 29. Fee Payments
CREATE TABLE IF NOT EXISTS fee_payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_fee_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50) NOT NULL, -- CASH, UPI, NET_BANKING, CARD
    transaction_reference VARCHAR(100) UNIQUE,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    status VARCHAR(30) NOT NULL DEFAULT 'SUCCESS',
    recorded_by VARCHAR(100),
    CONSTRAINT fk_pay_fee FOREIGN KEY (student_fee_id) REFERENCES student_fees (id) ON DELETE CASCADE,
    CONSTRAINT fk_pay_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 30. Grievances
CREATE TABLE IF NOT EXISTS grievances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_number VARCHAR(50) NOT NULL UNIQUE,
    student_id BIGINT NOT NULL,
    category VARCHAR(50) NOT NULL, -- ACADEMIC, HOSTEL, FEE, INFRASTRUCTURE, OTHER
    subject VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM', -- LOW, MEDIUM, HIGH, URGENT
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED', -- SUBMITTED, UNDER_REVIEW, IN_PROGRESS, RESOLVED, REJECTED
    assigned_to BIGINT,
    attachment_path VARCHAR(255),
    resolution_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP,
    CONSTRAINT fk_grv_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT fk_grv_assignee FOREIGN KEY (assigned_to) REFERENCES users (id) ON DELETE SET NULL,
    INDEX idx_grv_status (status)
) ENGINE=InnoDB;

-- 31. Grievance Comments
CREATE TABLE IF NOT EXISTS grievance_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grievance_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    comment TEXT NOT NULL,
    is_internal BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_gc_grv FOREIGN KEY (grievance_id) REFERENCES grievances (id) ON DELETE CASCADE,
    CONSTRAINT fk_gc_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 32. Password Reset Tokens
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token VARCHAR(100) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_prt_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 33. Audit Logs
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(100) NOT NULL,
    entity_name VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100),
    details TEXT,
    ip_address VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_action (action),
    INDEX idx_audit_entity (entity_name, entity_id)
) ENGINE=InnoDB;
