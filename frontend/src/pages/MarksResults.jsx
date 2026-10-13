import { useState } from 'react'
import { Search, Award, TrendingUp } from 'lucide-react'
import { marks, courses } from '../data/mockData'
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell } from 'recharts'

const GRADE_COLORS = { 'A+': '#10b981', 'A': '#2563eb', 'B+': '#7c3aed', 'B': '#f59e0b', 'C': '#ef4444' }

export default function MarksResults() {
  const [search, setSearch] = useState('')
  const [tab, setTab] = useState('marks')

  const filtered = marks.filter(m => {
    const q = search.toLowerCase()
    return !q || m.student.toLowerCase().includes(q) || m.id.toLowerCase().includes(q) || m.course.toLowerCase().includes(q)
  })

  const courseAvg = courses.slice(0, 5).map(c => {
    const courseMarks = marks.filter(m => m.course === c.id)
    const avg = courseMarks.length ? Math.round(courseMarks.reduce((s, m) => s + m.total, 0) / courseMarks.length) : 72
    return { name: c.id, avg, full: c.name }
  })

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Marks & Results</h1>
          <p className="page-subtitle">View and manage student examination results</p>
        </div>
      </div>

      <div className="tabs">
        <button className={`tab-btn ${tab === 'marks' ? 'active' : ''}`} onClick={() => setTab('marks')}>Mark Sheet</button>
        <button className={`tab-btn ${tab === 'analytics' ? 'active' : ''}`} onClick={() => setTab('analytics')}>Analytics</button>
      </div>

      {tab === 'marks' && (
        <div className="card">
          <div className="table-toolbar">
            <div className="table-search">
              <Search size={15} color="#94a3b8" />
              <input placeholder="Search by student, ID, course…" value={search} onChange={e => setSearch(e.target.value)} id="marks-search" />
            </div>
          </div>
          <div className="data-table-container">
            <table className="data-table">
              <thead>
                <tr>
                  <th>Student</th><th>Course</th>
                  <th>Mid-1 <span style={{ fontWeight: 400 }}>/30</span></th>
                  <th>Mid-2 <span style={{ fontWeight: 400 }}>/30</span></th>
                  <th>Assignment <span style={{ fontWeight: 400 }}>/20</span></th>
                  <th>Final <span style={{ fontWeight: 400 }}>/100</span></th>
                  <th>Total %</th>
                  <th>Grade</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map((m, i) => (
                  <tr key={i}>
                    <td>
                      <div style={{ fontWeight: 600 }}>{m.student}</div>
                      <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{m.id}</div>
                    </td>
                    <td><code style={{ background: '#f1f5f9', padding: '2px 7px', borderRadius: 5, fontSize: '0.8rem' }}>{m.course}</code></td>
                    <td>{m.mid1}</td>
                    <td>{m.mid2}</td>
                    <td>{m.assignment}</td>
                    <td style={{ fontWeight: 700 }}>{m.final}</td>
                    <td>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                        <div style={{ flex: 1, height: 6, background: '#e2e8f0', borderRadius: 99, overflow: 'hidden', minWidth: 60 }}>
                          <div style={{ height: '100%', width: `${m.total}%`, background: m.total >= 80 ? '#10b981' : m.total >= 60 ? '#f59e0b' : '#ef4444', borderRadius: 99 }} />
                        </div>
                        <span style={{ fontWeight: 700, fontSize: '0.82rem' }}>{m.total}%</span>
                      </div>
                    </td>
                    <td>
                      <div style={{ display: 'flex', alignItems: 'center', gap: 4 }}>
                        <Award size={14} color={GRADE_COLORS[m.grade] || '#64748b'} />
                        <span style={{ fontWeight: 800, color: GRADE_COLORS[m.grade] || '#64748b' }}>{m.grade}</span>
                      </div>
                    </td>
                  </tr>
                ))}
                {filtered.length === 0 && (
                  <tr><td colSpan={8} style={{ textAlign: 'center', padding: '2rem', color: 'var(--text-muted)' }}>No results found</td></tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {tab === 'analytics' && (
        <div>
          <div className="grid-3" style={{ marginBottom: '1.25rem' }}>
            {[
              { label: 'Class Average', val: (marks.reduce((s, m) => s + m.total, 0) / marks.length).toFixed(1) + '%', icon: TrendingUp, color: '#2563eb' },
              { label: 'Highest Score', val: Math.max(...marks.map(m => m.total)) + '%', icon: Award, color: '#10b981' },
              { label: "Pass Rate", val: Math.round(marks.filter(m => m.total >= 50).length / marks.length * 100) + '%', icon: TrendingUp, color: '#7c3aed' },
            ].map(s => (
              <div className="stat-card" key={s.label}>
                <div className="stat-icon" style={{ background: s.color + '18' }}>
                  <s.icon size={22} color={s.color} />
                </div>
                <div className="stat-body">
                  <div className="stat-label">{s.label}</div>
                  <div className="stat-value" style={{ fontSize: '1.5rem' }}>{s.val}</div>
                </div>
              </div>
            ))}
          </div>

          <div className="chart-card">
            <div className="chart-card-title">Average Score by Course</div>
            <ResponsiveContainer width="100%" height={280}>
              <BarChart data={courseAvg} barSize={40}>
                <CartesianGrid strokeDasharray="3 3" stroke="#f1f5f9" />
                <XAxis dataKey="name" tick={{ fontSize: 12 }} />
                <YAxis domain={[0, 100]} tick={{ fontSize: 12 }} />
                <Tooltip formatter={(val, _, props) => [`${val}%`, props.payload.full]} />
                <Bar dataKey="avg" radius={[6, 6, 0, 0]}>
                  {courseAvg.map((_, i) => (
                    <Cell key={i} fill={['#2563eb', '#7c3aed', '#10b981', '#f59e0b', '#ef4444'][i % 5]} />
                  ))}
                </Bar>
              </BarChart>
            </ResponsiveContainer>
          </div>

          {/* Grade distribution */}
          <div className="card" style={{ marginTop: '1.25rem' }}>
            <div className="section-title" style={{ marginBottom: '1rem' }}>Grade Distribution</div>
            <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
              {Object.entries(GRADE_COLORS).map(([grade, color]) => {
                const count = marks.filter(m => m.grade === grade).length
                const pct = Math.round(count / marks.length * 100)
                return (
                  <div key={grade} style={{ flex: 1, minWidth: 80, textAlign: 'center', padding: '1rem', borderRadius: 12, background: color + '12', border: `2px solid ${color}30` }}>
                    <div style={{ fontFamily: 'var(--font-heading)', fontSize: '1.4rem', fontWeight: 800, color }}>{grade}</div>
                    <div style={{ fontWeight: 700, marginTop: 4 }}>{count} students</div>
                    <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>{pct}%</div>
                  </div>
                )
              })}
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
