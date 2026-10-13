import { useState } from 'react'
import { Plus, FileText, X, Clock, Check } from 'lucide-react'
import { assignments as initAssignments, courses } from '../data/mockData'

export default function Assignments() {
  const [assignments, setAssignments] = useState(initAssignments)
  const [showModal, setShowModal] = useState(false)
  const [form, setForm] = useState({ title: '', course: courses[0].id, dueDate: '', status: 'Open' })

  const handleAdd = (e) => {
    e.preventDefault()
    const newA = { id: 'A-' + String(assignments.length + 1).padStart(3, '0'), ...form, submissions: 0, total: courses.find(c => c.id === form.course)?.students || 0 }
    setAssignments(prev => [...prev, newA])
    setShowModal(false)
    setForm({ title: '', course: courses[0].id, dueDate: '', status: 'Open' })
  }

  const toggle = (id) => {
    setAssignments(prev => prev.map(a => a.id === id ? { ...a, status: a.status === 'Open' ? 'Closed' : 'Open' } : a))
  }

  const setF = (k, v) => setForm(p => ({ ...p, [k]: v }))

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Assignments</h1>
          <p className="page-subtitle">Manage course assignments and track submissions</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)} id="add-assignment-btn"><Plus size={17} /> Post Assignment</button>
      </div>

      {/* Summary */}
      <div style={{ display: 'flex', gap: '0.75rem', marginBottom: '1.25rem', flexWrap: 'wrap' }}>
        {[
          { label: 'Total', val: assignments.length, bg: '#dbeafe', color: '#1e40af' },
          { label: 'Open', val: assignments.filter(a => a.status === 'Open').length, bg: '#dcfce7', color: '#166534' },
          { label: 'Closed', val: assignments.filter(a => a.status === 'Closed').length, bg: '#f1f5f9', color: '#475569' },
          { label: 'Total Submissions', val: assignments.reduce((s, a) => s + a.submissions, 0), bg: '#ede9fe', color: '#5b21b6' },
        ].map(c => (
          <div key={c.label} style={{ background: c.bg, color: c.color, padding: '0.4rem 1rem', borderRadius: 9999, fontSize: '0.82rem', fontWeight: 700 }}>
            {c.label}: {c.val}
          </div>
        ))}
      </div>

      <div className="grid-2" style={{ gap: '1rem' }}>
        {assignments.map(a => {
          const pct = Math.round((a.submissions / a.total) * 100)
          const isOverdue = new Date(a.dueDate) < new Date() && a.status === 'Open'
          return (
            <div className="card" key={a.id}>
              <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', gap: '0.5rem', marginBottom: '0.75rem' }}>
                <div style={{ display: 'flex', gap: '0.65rem', flex: 1 }}>
                  <div style={{ width: 40, height: 40, borderRadius: 10, background: '#ede9fe', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                    <FileText size={20} color="#7c3aed" />
                  </div>
                  <div>
                    <div style={{ fontWeight: 700, fontSize: '0.95rem' }}>{a.title}</div>
                    <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', marginTop: 2 }}>
                      <code style={{ background: '#f1f5f9', padding: '1px 6px', borderRadius: 4 }}>{a.course}</code>
                    </div>
                  </div>
                </div>
                <span className={`badge ${a.status === 'Open' ? 'badge-success' : 'badge-gray'}`}>{a.status}</span>
              </div>

              <div style={{ marginBottom: '0.75rem' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.78rem', color: 'var(--text-muted)', marginBottom: '0.4rem' }}>
                  <span>Submissions: {a.submissions}/{a.total}</span>
                  <span style={{ fontWeight: 700, color: pct >= 80 ? 'var(--success)' : 'var(--text-muted)' }}>{pct}%</span>
                </div>
                <div className="progress-bar">
                  <div className="progress-fill" style={{ width: `${pct}%`, background: pct >= 80 ? '#10b981' : '#2563eb' }} />
                </div>
              </div>

              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', fontSize: '0.78rem', color: isOverdue ? 'var(--danger)' : 'var(--text-muted)' }}>
                  <Clock size={13} />
                  Due: {a.dueDate}
                  {isOverdue && <span className="badge badge-danger" style={{ fontSize: '0.65rem' }}>Overdue</span>}
                </div>
                <button className="btn btn-secondary btn-sm" onClick={() => toggle(a.id)}>
                  {a.status === 'Open' ? 'Close' : 'Reopen'}
                </button>
              </div>
            </div>
          )
        })}
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-title">Post Assignment</div>
              <button className="modal-close" onClick={() => setShowModal(false)}><X size={18} /></button>
            </div>
            <form onSubmit={handleAdd} id="assignment-form">
              <div className="form-group">
                <label className="form-label">Assignment Title *</label>
                <input className="form-control" required value={form.title} onChange={e => setF('title', e.target.value)} placeholder="e.g. Binary Tree Implementation" />
              </div>
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Course *</label>
                  <select className="form-control" value={form.course} onChange={e => setF('course', e.target.value)}>
                    {courses.map(c => <option key={c.id} value={c.id}>{c.id} — {c.name}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Due Date *</label>
                  <input type="date" className="form-control" required value={form.dueDate} onChange={e => setF('dueDate', e.target.value)} />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary"><Check size={16} /> Post Assignment</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
