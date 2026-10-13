import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useState } from 'react'
import Layout from './components/Layout'
import LoginPage from './pages/LoginPage'
import Dashboard from './pages/Dashboard'
import StudentManagement from './pages/StudentManagement'
import LecturerManagement from './pages/LecturerManagement'
import CourseManagement from './pages/CourseManagement'
import AttendanceManagement from './pages/AttendanceManagement'
import QRAttendance from './pages/QRAttendance'
import MarksResults from './pages/MarksResults'
import Timetable from './pages/Timetable'
import Assignments from './pages/Assignments'
import Internships from './pages/Internships'
import FeeManagement from './pages/FeeManagement'
import Library from './pages/Library'
import Events from './pages/Events'
import Grievances from './pages/Grievances'
import Certificates from './pages/Certificates'
import Settings from './pages/Settings'
import './index.css'

function ProtectedRoute({ children, isLoggedIn }) {
  return isLoggedIn ? children : <Navigate to="/login" replace />
}

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(() => {
    return localStorage.getItem('ssh_logged_in') === 'true'
  })
  const [currentUser, setCurrentUser] = useState(() => {
    const saved = localStorage.getItem('ssh_user')
    return saved ? JSON.parse(saved) : null
  })

  const handleLogin = (user) => {
    setIsLoggedIn(true)
    setCurrentUser(user)
    localStorage.setItem('ssh_logged_in', 'true')
    localStorage.setItem('ssh_user', JSON.stringify(user))
  }

  const handleLogout = () => {
    setIsLoggedIn(false)
    setCurrentUser(null)
    localStorage.removeItem('ssh_logged_in')
    localStorage.removeItem('ssh_user')
  }

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/login"
          element={
            isLoggedIn ? (
              <Navigate to="/dashboard" replace />
            ) : (
              <LoginPage onLogin={handleLogin} />
            )
          }
        />
        <Route
          path="/"
          element={
            <ProtectedRoute isLoggedIn={isLoggedIn}>
              <Layout user={currentUser} onLogout={handleLogout} />
            </ProtectedRoute>
          }
        >
          <Route index element={<Navigate to="/dashboard" replace />} />
          <Route path="dashboard" element={<Dashboard />} />
          <Route path="students" element={<StudentManagement />} />
          <Route path="lecturers" element={<LecturerManagement />} />
          <Route path="courses" element={<CourseManagement />} />
          <Route path="attendance" element={<AttendanceManagement />} />
          <Route path="qr-attendance" element={<QRAttendance />} />
          <Route path="marks" element={<MarksResults />} />
          <Route path="timetable" element={<Timetable />} />
          <Route path="assignments" element={<Assignments />} />
          <Route path="internships" element={<Internships />} />
          <Route path="fees" element={<FeeManagement />} />
          <Route path="library" element={<Library />} />
          <Route path="events" element={<Events />} />
          <Route path="grievances" element={<Grievances />} />
          <Route path="certificates" element={<Certificates />} />
          <Route path="settings" element={<Settings />} />
        </Route>
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
