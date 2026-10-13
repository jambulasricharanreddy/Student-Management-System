// Mock data for Smart Student Hub

export const students = [
  { id: 'S-1001', name: 'Aarav Sharma', email: 'aarav.s@uni.edu', phone: '9876543210', dept: 'Computer Science', year: '3rd Year', gpa: 8.7, status: 'Active', dob: '2002-05-14', address: 'Hyderabad, TG' },
  { id: 'S-1002', name: 'Priya Reddy', email: 'priya.r@uni.edu', phone: '9876543211', dept: 'Electronics', year: '2nd Year', gpa: 9.1, status: 'Active', dob: '2003-08-22', address: 'Warangal, TG' },
  { id: 'S-1003', name: 'Kiran Verma', email: 'kiran.v@uni.edu', phone: '9876543212', dept: 'Mechanical', year: '4th Year', gpa: 7.8, status: 'Active', dob: '2001-01-30', address: 'Vijayawada, AP' },
  { id: 'S-1004', name: 'Sneha Patel', email: 'sneha.p@uni.edu', phone: '9876543213', dept: 'Civil', year: '1st Year', gpa: 8.2, status: 'Active', dob: '2004-11-10', address: 'Ahmedabad, GJ' },
  { id: 'S-1005', name: 'Rohit Nair', email: 'rohit.n@uni.edu', phone: '9876543214', dept: 'Computer Science', year: '3rd Year', gpa: 7.5, status: 'Inactive', dob: '2002-03-18', address: 'Kochi, KL' },
  { id: 'S-1006', name: 'Ananya Singh', email: 'ananya.s@uni.edu', phone: '9876543215', dept: 'Electronics', year: '4th Year', gpa: 9.3, status: 'Active', dob: '2001-07-05', address: 'Lucknow, UP' },
  { id: 'S-1007', name: 'Vikram Joshi', email: 'vikram.j@uni.edu', phone: '9876543216', dept: 'Mechanical', year: '2nd Year', gpa: 6.9, status: 'Active', dob: '2003-12-20', address: 'Pune, MH' },
  { id: 'S-1008', name: 'Divya Menon', email: 'divya.m@uni.edu', phone: '9876543217', dept: 'Civil', year: '3rd Year', gpa: 8.5, status: 'Active', dob: '2002-09-03', address: 'Chennai, TN' },
  { id: 'S-1009', name: 'Arjun Gupta', email: 'arjun.g@uni.edu', phone: '9876543218', dept: 'Computer Science', year: '1st Year', gpa: 8.0, status: 'Active', dob: '2004-04-25', address: 'Delhi, DL' },
  { id: 'S-1010', name: 'Pooja Iyer', email: 'pooja.i@uni.edu', phone: '9876543219', dept: 'Electronics', year: '2nd Year', gpa: 8.9, status: 'Active', dob: '2003-06-14', address: 'Bengaluru, KA' },
]

export const lecturers = [
  { id: 'L-001', name: 'Dr. Rajesh Kumar', email: 'r.kumar@uni.edu', phone: '9800000001', dept: 'Computer Science', subject: 'Data Structures', qualification: 'Ph.D', experience: '12 yrs', status: 'Active' },
  { id: 'L-002', name: 'Prof. Meena Iyer', email: 'm.iyer@uni.edu', phone: '9800000002', dept: 'Electronics', subject: 'Digital Circuits', qualification: 'M.Tech', experience: '8 yrs', status: 'Active' },
  { id: 'L-003', name: 'Dr. Suresh Babu', email: 's.babu@uni.edu', phone: '9800000003', dept: 'Mechanical', subject: 'Thermodynamics', qualification: 'Ph.D', experience: '15 yrs', status: 'Active' },
  { id: 'L-004', name: 'Ms. Kavya Rao', email: 'k.rao@uni.edu', phone: '9800000004', dept: 'Civil', subject: 'Structural Eng.', qualification: 'M.E', experience: '6 yrs', status: 'Active' },
  { id: 'L-005', name: 'Dr. Anil Mehta', email: 'a.mehta@uni.edu', phone: '9800000005', dept: 'Computer Science', subject: 'Machine Learning', qualification: 'Ph.D', experience: '10 yrs', status: 'On Leave' },
]

export const courses = [
  { id: 'CS301', name: 'Data Structures & Algorithms', dept: 'Computer Science', credits: 4, lecturer: 'Dr. Rajesh Kumar', students: 58, semester: '5th', status: 'Active' },
  { id: 'EC201', name: 'Digital Circuits & Logic Design', dept: 'Electronics', credits: 3, lecturer: 'Prof. Meena Iyer', students: 45, semester: '3rd', status: 'Active' },
  { id: 'ME301', name: 'Thermodynamics', dept: 'Mechanical', credits: 4, lecturer: 'Dr. Suresh Babu', students: 52, semester: '5th', status: 'Active' },
  { id: 'CV201', name: 'Structural Engineering', dept: 'Civil', credits: 3, lecturer: 'Ms. Kavya Rao', students: 38, semester: '3rd', status: 'Active' },
  { id: 'CS401', name: 'Machine Learning', dept: 'Computer Science', credits: 4, lecturer: 'Dr. Anil Mehta', students: 62, semester: '7th', status: 'Active' },
  { id: 'CS201', name: 'Operating Systems', dept: 'Computer Science', credits: 4, lecturer: 'Dr. Rajesh Kumar', students: 55, semester: '4th', status: 'Active' },
]

export const attendanceData = [
  { date: '2026-10-07', course: 'CS301', total: 58, present: 52, absent: 6 },
  { date: '2026-10-08', course: 'EC201', total: 45, present: 40, absent: 5 },
  { date: '2026-10-09', course: 'ME301', total: 52, present: 49, absent: 3 },
  { date: '2026-10-10', course: 'CV201', total: 38, present: 35, absent: 3 },
  { date: '2026-10-11', course: 'CS401', total: 62, present: 58, absent: 4 },
]

export const marks = [
  { student: 'Aarav Sharma', id: 'S-1001', course: 'CS301', mid1: 87, mid2: 91, assignment: 85, final: 88, total: 88.5, grade: 'A' },
  { student: 'Priya Reddy', id: 'S-1002', course: 'EC201', mid1: 92, mid2: 89, assignment: 95, final: 93, total: 92.5, grade: 'A+' },
  { student: 'Kiran Verma', id: 'S-1003', course: 'ME301', mid1: 74, mid2: 78, assignment: 70, final: 76, total: 75.5, grade: 'B+' },
  { student: 'Sneha Patel', id: 'S-1004', course: 'CV201', mid1: 82, mid2: 80, assignment: 88, final: 83, total: 83.0, grade: 'A' },
  { student: 'Ananya Singh', id: 'S-1006', course: 'CS401', mid1: 95, mid2: 93, assignment: 97, final: 94, total: 94.5, grade: 'A+' },
]

export const assignments = [
  { id: 'A-001', title: 'Binary Search Tree Implementation', course: 'CS301', dueDate: '2026-10-20', submissions: 42, total: 58, status: 'Open' },
  { id: 'A-002', title: 'Karnaugh Map Simplification', course: 'EC201', dueDate: '2026-10-18', submissions: 38, total: 45, status: 'Open' },
  { id: 'A-003', title: 'Heat Engine Cycle Analysis', course: 'ME301', dueDate: '2026-10-15', submissions: 52, total: 52, status: 'Closed' },
  { id: 'A-004', title: 'Beam Load Calculations', course: 'CV201', dueDate: '2026-10-22', submissions: 10, total: 38, status: 'Open' },
  { id: 'A-005', title: 'Neural Network from Scratch', course: 'CS401', dueDate: '2026-10-25', submissions: 20, total: 62, status: 'Open' },
]

export const fees = [
  { id: 'F-001', student: 'Aarav Sharma', sId: 'S-1001', semester: '5th', amount: 45000, paid: 45000, due: 0, status: 'Paid', date: '2026-08-10' },
  { id: 'F-002', student: 'Priya Reddy', sId: 'S-1002', semester: '3rd', amount: 45000, paid: 25000, due: 20000, status: 'Partial', date: '2026-08-12' },
  { id: 'F-003', student: 'Kiran Verma', sId: 'S-1003', semester: '7th', amount: 45000, paid: 0, due: 45000, status: 'Unpaid', date: '-' },
  { id: 'F-004', student: 'Sneha Patel', sId: 'S-1004', semester: '1st', amount: 45000, paid: 45000, due: 0, status: 'Paid', date: '2026-08-05' },
  { id: 'F-005', student: 'Ananya Singh', sId: 'S-1006', semester: '7th', amount: 45000, paid: 45000, due: 0, status: 'Paid', date: '2026-08-09' },
  { id: 'F-006', student: 'Rohit Nair', sId: 'S-1005', semester: '5th', amount: 45000, paid: 0, due: 45000, status: 'Unpaid', date: '-' },
]

export const books = [
  { id: 'B-001', title: 'Introduction to Algorithms', author: 'Cormen et al.', category: 'CS', total: 10, available: 3, status: 'Available' },
  { id: 'B-002', title: 'Digital Electronics', author: 'Morris Mano', category: 'EC', total: 8, available: 0, status: 'Checked Out' },
  { id: 'B-003', title: 'Engineering Mechanics', author: 'Beer & Johnston', category: 'ME', total: 12, available: 7, status: 'Available' },
  { id: 'B-004', title: 'Structural Analysis', author: 'Hibbeler', category: 'CV', total: 6, available: 2, status: 'Available' },
  { id: 'B-005', title: 'Machine Learning', author: 'Mitchell', category: 'CS', total: 5, available: 1, status: 'Available' },
  { id: 'B-006', title: 'Database Systems', author: 'Ramakrishnan', category: 'CS', total: 7, available: 4, status: 'Available' },
]

export const events = [
  { id: 'E-001', title: 'Annual Tech Fest 2026', date: '2026-11-15', venue: 'Main Auditorium', type: 'Technical', registrations: 320, capacity: 500, status: 'Upcoming' },
  { id: 'E-002', title: 'Campus Placement Drive - TCS', date: '2026-10-25', venue: 'Conference Hall A', type: 'Placement', registrations: 150, capacity: 200, status: 'Upcoming' },
  { id: 'E-003', title: 'Sports Day', date: '2026-10-08', venue: 'Sports Ground', type: 'Sports', registrations: 480, capacity: 600, status: 'Completed' },
  { id: 'E-004', title: 'Freshers Welcome Party', date: '2026-09-20', venue: 'Open Air Theatre', type: 'Cultural', registrations: 250, capacity: 350, status: 'Completed' },
  { id: 'E-005', title: 'Research Paper Symposium', date: '2026-11-28', venue: 'Seminar Hall', type: 'Academic', registrations: 80, capacity: 150, status: 'Upcoming' },
]

export const grievances = [
  { id: 'GR-001', student: 'Kiran Verma', sId: 'S-1003', type: 'Academic', subject: 'Marks not updated correctly', date: '2026-10-09', status: 'Pending', priority: 'High' },
  { id: 'GR-002', student: 'Rohit Nair', sId: 'S-1005', type: 'Infrastructure', subject: 'Broken chairs in Lab 3', date: '2026-10-08', status: 'In Progress', priority: 'Medium' },
  { id: 'GR-003', student: 'Sneha Patel', sId: 'S-1004', type: 'Fee', subject: 'Wrong amount charged', date: '2026-10-07', status: 'Resolved', priority: 'High' },
  { id: 'GR-004', student: 'Pooja Iyer', sId: 'S-1010', type: 'Academic', subject: 'Attendance mismatch', date: '2026-10-06', status: 'Pending', priority: 'Medium' },
]

export const internships = [
  { id: 'INT-001', student: 'Aarav Sharma', sId: 'S-1001', company: 'Infosys', role: 'Software Intern', startDate: '2026-06-01', endDate: '2026-07-31', stipend: '₹15,000/mo', status: 'Completed' },
  { id: 'INT-002', student: 'Ananya Singh', sId: 'S-1006', company: 'Google India', role: 'ML Intern', startDate: '2026-05-15', endDate: '2026-08-15', stipend: '₹40,000/mo', status: 'Completed' },
  { id: 'INT-003', student: 'Priya Reddy', sId: 'S-1002', company: 'Texas Instruments', role: 'Hardware Intern', startDate: '2026-10-01', endDate: '2026-12-31', stipend: '₹20,000/mo', status: 'Ongoing' },
  { id: 'INT-004', student: 'Vikram Joshi', sId: 'S-1007', company: 'L&T', role: 'Mechanical Intern', startDate: '2026-11-01', endDate: '2027-01-31', stipend: '₹12,000/mo', status: 'Upcoming' },
]

export const certificates = [
  { id: 'CERT-001', student: 'Aarav Sharma', sId: 'S-1001', type: 'Bonafide', issued: '2026-09-15', status: 'Issued', reason: 'Bank Loan' },
  { id: 'CERT-002', student: 'Priya Reddy', sId: 'S-1002', type: 'Transfer', issued: '-', status: 'Pending', reason: 'Institute Change' },
  { id: 'CERT-003', student: 'Kiran Verma', sId: 'S-1003', type: 'Character', issued: '2026-10-01', status: 'Issued', reason: 'Job Application' },
  { id: 'CERT-004', student: 'Divya Menon', sId: 'S-1008', type: 'Bonafide', issued: '2026-10-05', status: 'Issued', reason: 'Scholarship' },
]

export const timetable = {
  Monday:    { '9:00':  { sub: 'CS301', room: 'LH-1', lecturer: 'Dr. R.Kumar' }, '10:00': { sub: 'EC201', room: 'LH-3', lecturer: 'Prof. M.Iyer' }, '11:00': { sub: '-' }, '12:00': { sub: 'CS401', room: 'LH-2', lecturer: 'Dr. A.Mehta' }, '14:00': { sub: 'ME301', room: 'LH-4', lecturer: 'Dr. S.Babu' } },
  Tuesday:   { '9:00':  { sub: 'ME301', room: 'LH-4', lecturer: 'Dr. S.Babu' }, '10:00': { sub: '-' }, '11:00': { sub: 'CS301', room: 'Lab-1', lecturer: 'Dr. R.Kumar' }, '12:00': { sub: '-' }, '14:00': { sub: 'CV201', room: 'LH-5', lecturer: 'Ms. K.Rao' } },
  Wednesday: { '9:00':  { sub: 'EC201', room: 'Lab-2', lecturer: 'Prof. M.Iyer' }, '10:00': { sub: 'CS201', room: 'LH-1', lecturer: 'Dr. R.Kumar' }, '11:00': { sub: 'ME301', room: 'LH-4', lecturer: 'Dr. S.Babu' }, '12:00': { sub: 'CS301', room: 'LH-2', lecturer: 'Dr. R.Kumar' }, '14:00': { sub: '-' } },
  Thursday:  { '9:00':  { sub: '-' }, '10:00': { sub: 'CS401', room: 'LH-2', lecturer: 'Dr. A.Mehta' }, '11:00': { sub: 'CV201', room: 'LH-5', lecturer: 'Ms. K.Rao' }, '12:00': { sub: 'EC201', room: 'LH-3', lecturer: 'Prof. M.Iyer' }, '14:00': { sub: 'CS201', room: 'LH-1', lecturer: 'Dr. R.Kumar' } },
  Friday:    { '9:00':  { sub: 'CS201', room: 'LH-1', lecturer: 'Dr. R.Kumar' }, '10:00': { sub: 'ME301', room: 'Lab-3', lecturer: 'Dr. S.Babu' }, '11:00': { sub: 'CS401', room: 'LH-2', lecturer: 'Dr. A.Mehta' }, '12:00': { sub: 'CV201', room: 'Lab-4', lecturer: 'Ms. K.Rao' }, '14:00': { sub: 'EC201', room: 'LH-3', lecturer: 'Prof. M.Iyer' } },
}

export const subjectColors = {
  'CS301': { bg: '#dbeafe', color: '#1e40af' },
  'EC201': { bg: '#fce7f3', color: '#9d174d' },
  'ME301': { bg: '#dcfce7', color: '#166534' },
  'CV201': { bg: '#fef3c7', color: '#92400e' },
  'CS401': { bg: '#ede9fe', color: '#5b21b6' },
  'CS201': { bg: '#e0f2fe', color: '#0369a1' },
}

export const monthlyAttendance = [
  { month: 'Jun', attendance: 92 },
  { month: 'Jul', attendance: 88 },
  { month: 'Aug', attendance: 91 },
  { month: 'Sep', attendance: 86 },
  { month: 'Oct', attendance: 89 },
]

export const deptDistribution = [
  { name: 'CS', value: 320 },
  { name: 'EC', value: 280 },
  { name: 'ME', value: 240 },
  { name: 'CV', value: 180 },
  { name: 'Others', value: 120 },
]

export const recentActivity = [
  { id: 1, text: 'S-1009 Arjun Gupta registered successfully', time: '5 min ago', color: '#10b981' },
  { id: 2, text: 'Attendance marked for CS301 — 52/58 present', time: '22 min ago', color: '#2563eb' },
  { id: 3, text: 'GR-001 grievance assigned to department head', time: '1h ago', color: '#f59e0b' },
  { id: 4, text: 'Fee payment received from S-1004 Sneha Patel', time: '2h ago', color: '#10b981' },
  { id: 5, text: 'New assignment posted: Neural Network from Scratch', time: '3h ago', color: '#7c3aed' },
  { id: 6, text: 'Event: Placement Drive TCS — 150 registrations', time: '5h ago', color: '#ef4444' },
]
