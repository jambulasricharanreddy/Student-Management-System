import { useState } from 'react'
import { NavLink, Outlet } from 'react-router-dom'
import {
  LayoutDashboard, Users, UserCheck, BookOpen, ClipboardList,
  QrCode, BarChart2, Calendar, FileText, Briefcase,
  CreditCard, Library, PartyPopper, MessageSquare, Award,
  Settings, LogOut, Bell, Search, ChevronLeft, GraduationCap, Menu
} from 'lucide-react'

const navItems = [
  { path: '/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
  { path: '/students', icon: Users, label: 'Students' },
  { path: '/lecturers', icon: UserCheck, label: 'Lecturers' },
  { path: '/courses', icon: BookOpen, label: 'Courses' },
  { path: '/attendance', icon: ClipboardList, label: 'Attendance' },
  { path: '/qr-attendance', icon: QrCode, label: 'QR Attendance' },
  { path: '/marks', icon: BarChart2, label: 'Marks & Results' },
  { path: '/timetable', icon: Calendar, label: 'Timetable' },
  { path: '/assignments', icon: FileText, label: 'Assignments' },
  { path: '/internships', icon: Briefcase, label: 'Internships' },
  { path: '/fees', icon: CreditCard, label: 'Fee Management' },
  { path: '/library', icon: Library, label: 'Library' },
  { path: '/events', icon: PartyPopper, label: 'Events' },
  { path: '/grievances', icon: MessageSquare, label: 'Grievances' },
  { path: '/certificates', icon: Award, label: 'Certificates' },
  { path: '/settings', icon: Settings, label: 'Settings' },
]

const notifications = [
  { id: 1, msg: 'New student registered: Aarav Sharma', time: '2m ago', unread: true },
  { id: 2, msg: 'Fee due alert: 12 students overdue', time: '1h ago', unread: true },
  { id: 3, msg: 'Attendance report generated', time: '3h ago', unread: false },
  { id: 4, msg: 'New grievance submitted by S-1042', time: '5h ago', unread: false },
]

export default function Layout({ user, onLogout }) {
  const [collapsed, setCollapsed] = useState(false)
  const [showNotif, setShowNotif] = useState(false)
  const [showProfile, setShowProfile] = useState(false)
  const [searchVal, setSearchVal] = useState('')
  const [mobileOpen, setMobileOpen] = useState(false)

  const unreadCount = notifications.filter(n => n.unread).length

  return (
    <div className="app-container">
      {/* Mobile overlay */}
      {mobileOpen && (
        <div className="mobile-overlay" onClick={() => setMobileOpen(false)} />
      )}

      {/* Sidebar */}
      <aside className={`sidebar ${collapsed ? 'collapsed' : ''} ${mobileOpen ? 'mobile-open' : ''}`}>
        <div className="sidebar-header">
          <div className="sidebar-logo">
            <div className="logo-icon">
              <GraduationCap size={22} color="#fff" />
            </div>
            {!collapsed && (
              <div className="logo-text">
                <span className="logo-title">SmartHub</span>
                <span className="logo-sub">University Portal</span>
              </div>
            )}
          </div>
          <button
            className="collapse-btn"
            onClick={() => setCollapsed(c => !c)}
            aria-label="Toggle sidebar"
          >
            <ChevronLeft size={16} style={{ transform: collapsed ? 'rotate(180deg)' : 'none', transition: 'transform 0.3s' }} />
          </button>
        </div>

        <nav className="sidebar-nav">
          {navItems.map(({ path, icon: Icon, label }) => (
            <NavLink
              key={path}
              to={path}
              className={({ isActive }) => `nav-item ${isActive ? 'active' : ''}`}
              title={collapsed ? label : undefined}
              onClick={() => setMobileOpen(false)}
            >
              <Icon size={19} className="nav-icon" />
              {!collapsed && <span className="nav-label">{label}</span>}
            </NavLink>
          ))}
        </nav>

        <div className="sidebar-footer">
          <button className="nav-item logout-btn" onClick={onLogout} title={collapsed ? 'Logout' : undefined}>
            <LogOut size={19} className="nav-icon" />
            {!collapsed && <span className="nav-label">Logout</span>}
          </button>
        </div>
      </aside>

      {/* Main content */}
      <div className="main-content">
        {/* Top navigation */}
        <header className="topnav">
          <div className="topnav-left">
            <button
              className="mobile-menu-btn"
              onClick={() => setMobileOpen(o => !o)}
              aria-label="Open menu"
            >
              <Menu size={22} />
            </button>
            <div className="search-box">
              <Search size={16} className="search-icon" />
              <input
                type="text"
                className="search-input"
                placeholder="Search students, courses, events…"
                value={searchVal}
                onChange={e => setSearchVal(e.target.value)}
                id="global-search"
              />
            </div>
          </div>

          <div className="topnav-right">
            {/* Notifications */}
            <div className="notif-wrapper">
              <button
                className="icon-btn"
                id="notif-btn"
                onClick={() => { setShowNotif(n => !n); setShowProfile(false) }}
                aria-label="Notifications"
              >
                <Bell size={20} />
                {unreadCount > 0 && <span className="badge-dot">{unreadCount}</span>}
              </button>
              {showNotif && (
                <div className="dropdown notif-dropdown">
                  <div className="dropdown-header">
                    <span>Notifications</span>
                    <span className="badge badge-primary">{unreadCount} new</span>
                  </div>
                  {notifications.map(n => (
                    <div key={n.id} className={`notif-item ${n.unread ? 'unread' : ''}`}>
                      <div className="notif-msg">{n.msg}</div>
                      <div className="notif-time">{n.time}</div>
                    </div>
                  ))}
                  <div className="dropdown-footer">View all notifications</div>
                </div>
              )}
            </div>

            {/* Profile */}
            <div className="profile-wrapper">
              <button
                className="profile-btn"
                id="profile-btn"
                onClick={() => { setShowProfile(p => !p); setShowNotif(false) }}
              >
                <div className="avatar">{user?.name?.[0] ?? 'A'}</div>
                <div className="profile-info">
                  <span className="profile-name">{user?.name ?? 'Admin User'}</span>
                  <span className="profile-role">{user?.role ?? 'Administrator'}</span>
                </div>
              </button>
              {showProfile && (
                <div className="dropdown profile-dropdown">
                  <div className="dropdown-header">
                    <div className="avatar avatar-lg">{user?.name?.[0] ?? 'A'}</div>
                    <div>
                      <div style={{ fontWeight: 700 }}>{user?.name ?? 'Admin User'}</div>
                      <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>{user?.email ?? 'admin@university.edu'}</div>
                    </div>
                  </div>
                  <NavLink to="/settings" className="dropdown-item" onClick={() => setShowProfile(false)}>
                    <Settings size={15} /> Settings
                  </NavLink>
                  <button className="dropdown-item danger" onClick={onLogout}>
                    <LogOut size={15} /> Logout
                  </button>
                </div>
              )}
            </div>
          </div>
        </header>

        {/* Page content */}
        <main className="page-container">
          <Outlet />
        </main>
      </div>
    </div>
  )
}
