import { Users, UserCheck, BookOpen, CreditCard, TrendingUp, TrendingDown, ClipboardList, PartyPopper } from 'lucide-react'
import {
  AreaChart, Area, BarChart, Bar, PieChart, Pie, Cell,
  XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Legend
} from 'recharts'
import { students, recentActivity, monthlyAttendance, deptDistribution, events, fees } from '../data/mockData'

const COLORS = ['#2563eb', '#7c3aed', '#10b981', '#f59e0b', '#ef4444']

const stats = [
  { label: 'Total Students', value: '1,140', icon: Users, iconBg: '#dbeafe', iconColor: '#2563eb', change: '+12 this month', up: true },
  { label: 'Faculty Members', value: '48', icon: UserCheck, iconBg: '#ede9fe', iconColor: '#7c3aed', change: '+2 this month', up: true },
  { label: 'Active Courses', value: '24', icon: BookOpen, iconBg: '#dcfce7', iconColor: '#10b981', change: '6 depts', up: true },
  { label: 'Fees Collected', value: '₹51.3L', icon: CreditCard, iconBg: '#fef3c7', iconColor: '#f59e0b', change: '78% of target', up: true },
]

export default function Dashboard() {
  const unpaidCount = fees.filter(f => f.status === 'Unpaid').length
  const upcomingEvents = events.filter(e => e.status === 'Upcoming').length
  const avgAttendance = Math.round(monthlyAttendance.reduce((s, m) => s + m.attendance, 0) / monthlyAttendance.length)

  return (
    <div>
      {/* Welcome Banner */}
      <div className="dash-welcome">
        <div className="dash-welcome-title">Good morning, Admin 👋</div>
        <div className="dash-welcome-sub">Here's what's happening at your university today — {new Date().toLocaleDateString('en-IN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' })}</div>
        <div className="dash-welcome-stats">
          <div>
            <div className="dash-welcome-stat-val">{avgAttendance}%</div>
            <div className="dash-welcome-stat-lbl">Avg Attendance</div>
          </div>
          <div>
            <div className="dash-welcome-stat-val">{unpaidCount}</div>
            <div className="dash-welcome-stat-lbl">Pending Fees</div>
          </div>
          <div>
            <div className="dash-welcome-stat-val">{upcomingEvents}</div>
            <div className="dash-welcome-stat-lbl">Upcoming Events</div>
          </div>
          <div>
            <div className="dash-welcome-stat-val">4</div>
            <div className="dash-welcome-stat-lbl">Open Grievances</div>
          </div>
        </div>
      </div>

      {/* Stat Cards */}
      <div className="grid-4" style={{ marginBottom: '1.5rem' }}>
        {stats.map((s) => (
          <div className="stat-card" key={s.label}>
            <div className="stat-icon" style={{ background: s.iconBg }}>
              <s.icon size={24} color={s.iconColor} />
            </div>
            <div className="stat-body">
              <div className="stat-label">{s.label}</div>
              <div className="stat-value">{s.value}</div>
              <div className={`stat-change ${s.up ? '' : 'down'}`}>
                {s.up ? <TrendingUp size={12} style={{ display: 'inline', marginRight: 3 }} /> : <TrendingDown size={12} style={{ display: 'inline', marginRight: 3 }} />}
                {s.change}
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Charts row */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 320px', gap: '1.25rem', marginBottom: '1.5rem' }}>
        {/* Attendance trend */}
        <div className="chart-card">
          <div className="chart-card-title">Monthly Attendance (%)</div>
          <ResponsiveContainer width="100%" height={200}>
            <AreaChart data={monthlyAttendance}>
              <defs>
                <linearGradient id="attGrad" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#2563eb" stopOpacity={0.18} />
                  <stop offset="95%" stopColor="#2563eb" stopOpacity={0} />
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
              <XAxis dataKey="month" tick={{ fontSize: 12 }} />
              <YAxis domain={[80, 100]} tick={{ fontSize: 12 }} />
              <Tooltip />
              <Area type="monotone" dataKey="attendance" stroke="#2563eb" strokeWidth={2.5} fill="url(#attGrad)" dot={{ fill: '#2563eb', r: 4 }} />
            </AreaChart>
          </ResponsiveContainer>
        </div>

        {/* Department distribution */}
        <div className="chart-card">
          <div className="chart-card-title">Students by Department</div>
          <ResponsiveContainer width="100%" height={200}>
            <BarChart data={deptDistribution} barSize={28}>
              <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
              <XAxis dataKey="name" tick={{ fontSize: 12 }} />
              <YAxis tick={{ fontSize: 12 }} />
              <Tooltip />
              <Bar dataKey="value" fill="#2563eb" radius={[4, 4, 0, 0]}>
                {deptDistribution.map((_, i) => (
                  <Cell key={i} fill={COLORS[i % COLORS.length]} />
                ))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>

        {/* Pie chart */}
        <div className="chart-card">
          <div className="chart-card-title">Dept Distribution</div>
          <ResponsiveContainer width="100%" height={180}>
            <PieChart>
              <Pie data={deptDistribution} cx="50%" cy="50%" innerRadius={50} outerRadius={80} dataKey="value" paddingAngle={2}>
                {deptDistribution.map((_, i) => (
                  <Cell key={i} fill={COLORS[i % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip />
              <Legend iconType="circle" iconSize={8} wrapperStyle={{ fontSize: '0.78rem' }} />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>

      {/* Bottom row: Recent Activity + Quick Actions */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 340px', gap: '1.25rem' }}>
        {/* Recent Activity */}
        <div className="card">
          <div className="section-header">
            <div className="section-title">Recent Activity</div>
            <span className="badge badge-primary">Live</span>
          </div>
          {recentActivity.map(a => (
            <div className="activity-item" key={a.id}>
              <div className="activity-dot" style={{ background: a.color }} />
              <div>
                <div className="activity-text">{a.text}</div>
                <div className="activity-time">{a.time}</div>
              </div>
            </div>
          ))}
        </div>

        {/* Quick Actions */}
        <div className="card">
          <div className="section-title" style={{ marginBottom: '1rem' }}>Quick Actions</div>
          <div style={{ display: 'flex', flexDirection: 'column', gap: '0.6rem' }}>
            {[
              { label: 'Add New Student', icon: Users, color: '#2563eb', link: '/students' },
              { label: 'Mark Attendance', icon: ClipboardList, color: '#10b981', link: '/attendance' },
              { label: 'Post Assignment', icon: BookOpen, color: '#7c3aed', link: '/assignments' },
              { label: 'Create Event', icon: PartyPopper, color: '#f59e0b', link: '/events' },
              { label: 'View Fee Reports', icon: CreditCard, color: '#ef4444', link: '/fees' },
            ].map(q => (
              <a
                key={q.label}
                href={q.link}
                style={{
                  display: 'flex', alignItems: 'center', gap: '0.75rem',
                  padding: '0.75rem', borderRadius: '10px', border: '1px solid var(--border)',
                  textDecoration: 'none', color: 'var(--text-main)', transition: 'all 0.2s',
                  fontSize: '0.875rem', fontWeight: 500
                }}
                onMouseOver={e => { e.currentTarget.style.background = '#f8fafc'; e.currentTarget.style.borderColor = '#cbd5e1' }}
                onMouseOut={e => { e.currentTarget.style.background = ''; e.currentTarget.style.borderColor = 'var(--border)' }}
              >
                <div style={{ width: 36, height: 36, borderRadius: 9, background: q.color + '18', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                  <q.icon size={18} color={q.color} />
                </div>
                {q.label}
              </a>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}
