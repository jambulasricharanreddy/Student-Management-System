import { useState } from 'react'
import { Check, X, CheckCircle2, XCircle, Calendar } from 'lucide-react'
import { students, courses, attendanceData } from '../data/mockData'

export default function AttendanceManagement() {
  const [selectedCourse, setSelectedCourse] = useState(courses[0].id)
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0])
  const [attendance, setAttendance] = useState(() => {
    const init = {}
    students.forEach(s => { init[s.id] = 'present' })
    return init
  })
  const [saved, setSaved] = useState(false)

  const toggle = (id) => {
    setAttendance(prev => ({ ...prev, [id]: prev[id] === 'present' ? 'absent' : 'present' }))
    setSaved(false)
  }

  const markAll = (val) => {
    const next = {}
    students.forEach(s => { next[s.id] = val })
    setAttendance(next)
    setSaved(false)
  }

  const presentCount = Object.values(attendance).filter(v => v === 'present').length
  const absentCount = students.length - presentCount
  const pct = Math.round((presentCount / students.length) * 100)

  const handleSave = () => { setSaved(true) }

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Attendance Management</h1>
          <p className="page-subtitle">Mark and track daily student attendance</p>
        </div>
      </div>

      {/* Controls */}
      <div className="card" style={{ marginBottom: '1.25rem' }}>
        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap', alignItems: 'flex-end' }}>
          <div className="form-group" style={{ marginBottom: 0, flex: 1, minWidth: 180 }}>
            <label className="form-label">Course</label>
            <select className="form-control" id="att-course" value={selectedCourse} onChange={e => setSelectedCourse(e.target.value)}>
              {courses.map(c => <option key={c.id} value={c.id}>{c.id} — {c.name}</option>)}
            </select>
          </div>
          <div className="form-group" style={{ marginBottom: 0 }}>
            <label className="form-label">Date</label>
            <input type="date" className="form-control" id="att-date" value={selectedDate} onChange={e => setSelectedDate(e.target.value)} />
          </div>
          <div style={{ display: 'flex', gap: '0.5rem' }}>
            <button className="btn btn-success btn-sm" onClick={() => markAll('present')}><CheckCircle2 size={15} /> All Present</button>
            <button className="btn btn-danger btn-sm" onClick={() => markAll('absent')}><XCircle size={15} /> All Absent</button>
          </div>
        </div>
      </div>

      {/* Summary */}
      <div className="grid-3" style={{ marginBottom: '1.25rem' }}>
        {[
          { label: 'Present', val: presentCount, color: '#10b981', bg: '#dcfce7' },
          { label: 'Absent', val: absentCount, color: '#ef4444', bg: '#fee2e2' },
          { label: 'Attendance %', val: pct + '%', color: pct >= 75 ? '#10b981' : '#f59e0b', bg: pct >= 75 ? '#dcfce7' : '#fef3c7' },
        ].map(s => (
          <div className="card" key={s.label} style={{ textAlign: 'center', padding: '1rem', borderTop: `3px solid ${s.color}` }}>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: s.color, fontFamily: 'var(--font-heading)' }}>{s.val}</div>
            <div style={{ fontSize: '0.82rem', color: 'var(--text-muted)', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.05em' }}>{s.label}</div>
          </div>
        ))}
      </div>

      {/* Attendance table */}
      <div className="card">
        <div className="section-header" style={{ marginBottom: '1rem' }}>
          <div className="section-title">Student Attendance — {selectedDate}</div>
          <button className={`btn ${saved ? 'btn-success' : 'btn-primary'}`} onClick={handleSave} id="save-att-btn">
            {saved ? <><Check size={16} /> Saved!</> : 'Save Attendance'}
          </button>
        </div>

        {/* Progress bar */}
        <div style={{ marginBottom: '1rem' }}>
          <div className="progress-bar">
            <div className="progress-fill" style={{ width: `${pct}%` }} />
          </div>
          <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', marginTop: 4 }}>{pct}% attendance rate</div>
        </div>

        <div className="data-table-container">
          <table className="data-table">
            <thead>
              <tr><th>#</th><th>Student</th><th>ID</th><th>Department</th><th>Status</th><th>Toggle</th></tr>
            </thead>
            <tbody>
              {students.map((s, i) => (
                <tr key={s.id} style={{ background: attendance[s.id] === 'absent' ? '#fff5f5' : '' }}>
                  <td style={{ color: 'var(--text-muted)', fontWeight: 600 }}>{i + 1}</td>
                  <td>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.55rem' }}>
                      <div style={{ width: 30, height: 30, borderRadius: 8, background: attendance[s.id] === 'present' ? '#dcfce7' : '#fee2e2', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '0.8rem', fontWeight: 700, color: attendance[s.id] === 'present' ? '#166534' : '#991b1b' }}>{s.name[0]}</div>
                      <span style={{ fontWeight: 600 }}>{s.name}</span>
                    </div>
                  </td>
                  <td><code style={{ background: '#f1f5f9', padding: '2px 7px', borderRadius: 5, fontSize: '0.78rem' }}>{s.id}</code></td>
                  <td>{s.dept}</td>
                  <td>
                    {attendance[s.id] === 'present'
                      ? <span className="badge badge-success"><Check size={11} style={{ marginRight: 3 }} />Present</span>
                      : <span className="badge badge-danger"><X size={11} style={{ marginRight: 3 }} />Absent</span>
                    }
                  </td>
                  <td>
                    <button
                      className={`btn btn-sm ${attendance[s.id] === 'present' ? 'btn-danger' : 'btn-success'}`}
                      onClick={() => toggle(s.id)}
                      id={`att-toggle-${s.id}`}
                    >
                      {attendance[s.id] === 'present' ? 'Mark Absent' : 'Mark Present'}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Historical records */}
      <div className="card" style={{ marginTop: '1.25rem' }}>
        <div className="section-title" style={{ marginBottom: '1rem' }}>Recent Attendance Records</div>
        <div className="data-table-container">
          <table className="data-table">
            <thead>
              <tr><th>Date</th><th>Course</th><th>Total</th><th>Present</th><th>Absent</th><th>Attendance %</th></tr>
            </thead>
            <tbody>
              {attendanceData.map((r, i) => {
                const pct = Math.round((r.present / r.total) * 100)
                return (
                  <tr key={i}>
                    <td style={{ display: 'flex', alignItems: 'center', gap: 6 }}><Calendar size={13} color="#94a3b8" />{r.date}</td>
                    <td><code style={{ background: '#f1f5f9', padding: '2px 7px', borderRadius: 5, fontSize: '0.8rem' }}>{r.course}</code></td>
                    <td>{r.total}</td>
                    <td style={{ color: 'var(--success)', fontWeight: 700 }}>{r.present}</td>
                    <td style={{ color: 'var(--danger)', fontWeight: 700 }}>{r.absent}</td>
                    <td>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                        <div style={{ flex: 1, height: 6, background: '#e2e8f0', borderRadius: 99, overflow: 'hidden', minWidth: 80 }}>
                          <div style={{ height: '100%', width: `${pct}%`, background: pct >= 75 ? '#10b981' : '#f59e0b', borderRadius: 99, transition: 'width 0.5s' }} />
                        </div>
                        <span style={{ fontWeight: 700, fontSize: '0.82rem', color: pct >= 75 ? 'var(--success)' : 'var(--warning)' }}>{pct}%</span>
                      </div>
                    </td>
                  </tr>
                )
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
