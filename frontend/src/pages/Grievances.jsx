import { useState } from 'react'
import { MessageSquare, Plus, X, AlertCircle, CheckCircle, Clock } from 'lucide-react'
import { grievances as initGrievances } from '../data/mockData'

const PRIORITY_COLORS = { High: 'badge-danger', Medium: 'badge-warning', Low: 'badge-info' }
const STATUS_COLORS = { Pending: 'badge-warning', 'In Progress': 'badge-primary', Resolved: 'badge-success' }

export default function Grievances() {
  const [grievances, setGrievances] = useState(initGrievances)
  const [showModal, setShowModal] = useState(false)
  const [filter, setFilter] = useState('All')
  const [form, setForm] = useState({ student: '', sId: '', type: 'Academic', subject: '', priority: 'Medium' })

  const filtered = filter === 'All' ? grievances : grievances.filter(g => g.status === filter)

  const updateStatus = (id, status) => {
    setGrievances(prev => prev.map(g => g.id === id ? { ...g, status } : g))
  }

  const handleAdd = (e) => {
    e.preventDefault()
    setGrievances(prev => [...prev, {
      id: 'GR-' + String(prev.length + 1).padStart(3, '0'),
      ...form,
      date: new Date().toISOString().split('T')[0],
      status: 'Pending'
    }])
    setShowModal(false)
    setForm({ student: '', sId: '', type: 'Academic', subject: '', priority: 'Medium' })
  }

  const setF = (k, v) => setForm(p => ({ ...p, [k]: v }))

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Grievances</h1>
          <p className="page-subtitle">Track and resolve student complaints and grievances</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)} id="add-grievance-btn"><Plus size={17} /> New Grievance</button>
      </div>

      {/* Summary */}
      <div className="grid-3" style={{ marginBottom: '1.25rem' }}>
        {[
          { label: 'Pending', val: grievances.filter(g => g.status === 'Pending').length, icon: Clock, color: '#f59e0b' },
          { label: 'In Progress', val: grievances.filter(g => g.status === 'In Progress').length, icon: AlertCircle, color: '#2563eb' },
          { label: 'Resolved', val: grievances.filter(g => g.status === 'Resolved').length, icon: CheckCircle, color: '#10b981' },
        ].map(s => (
          <div className="stat-card" key={s.label}>
            <div className="stat-icon" style={{ background: s.color + '18' }}><s.icon size={22} color={s.color} /></div>
            <div className="stat-body">
              <div className="stat-label">{s.label}</div>
              <div className="stat-value" style={{ fontSize: '1.5rem' }}>{s.val}</div>
            </div>
          </div>
        ))}
      </div>

      <div className="tabs" style={{ maxWidth: 440 }}>
        {['All', 'Pending', 'In Progress', 'Resolved'].map(s => (
          <button key={s} className={`tab-btn ${filter === s ? 'active' : ''}`} onClick={() => setFilter(s)}>{s}</button>
        ))}
      </div>

      <div className="card">
        <div className="data-table-container">
          <table className="data-table">
            <thead>
              <tr><th>ID</th><th>Student</th><th>Type</th><th>Subject</th><th>Date</th><th>Priority</th><th>Status</th><th>Actions</th></tr>
            </thead>
            <tbody>
              {filtered.map(g => (
                <tr key={g.id}>
                  <td><code style={{ background: '#f1f5f9', padding: '2px 7px', borderRadius: 5, fontSize: '0.78rem' }}>{g.id}</code></td>
                  <td>
                    <div style={{ fontWeight: 600 }}>{g.student}</div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{g.sId}</div>
                  </td>
                  <td><span className="badge badge-gray">{g.type}</span></td>
                  <td style={{ maxWidth: 200 }}>
                    <div style={{ fontWeight: 500, whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: 200 }}>{g.subject}</div>
                  </td>
                  <td style={{ color: 'var(--text-muted)', fontSize: '0.82rem' }}>{g.date}</td>
                  <td><span className={`badge ${PRIORITY_COLORS[g.priority]}`}>{g.priority}</span></td>
                  <td><span className={`badge ${STATUS_COLORS[g.status]}`}>{g.status}</span></td>
                  <td>
                    <div style={{ display: 'flex', gap: '0.3rem' }}>
                      {g.status === 'Pending' && <button className="btn btn-primary btn-sm" onClick={() => updateStatus(g.id, 'In Progress')}>Start</button>}
                      {g.status === 'In Progress' && <button className="btn btn-success btn-sm" onClick={() => updateStatus(g.id, 'Resolved')}>Resolve</button>}
                      {g.status === 'Resolved' && <span style={{ color: 'var(--success)', fontSize: '0.82rem', fontWeight: 600 }}>✓ Done</span>}
                    </div>
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr><td colSpan={8} style={{ textAlign: 'center', padding: '2rem', color: 'var(--text-muted)' }}>No grievances found</td></tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-title">Submit Grievance</div>
              <button className="modal-close" onClick={() => setShowModal(false)}><X size={18} /></button>
            </div>
            <form onSubmit={handleAdd} id="grievance-form">
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Student Name *</label>
                  <input className="form-control" required value={form.student} onChange={e => setF('student', e.target.value)} />
                </div>
                <div className="form-group">
                  <label className="form-label">Student ID</label>
                  <input className="form-control" value={form.sId} onChange={e => setF('sId', e.target.value)} placeholder="S-1001" />
                </div>
                <div className="form-group">
                  <label className="form-label">Type</label>
                  <select className="form-control" value={form.type} onChange={e => setF('type', e.target.value)}>
                    {['Academic', 'Fee', 'Infrastructure', 'Hostel', 'Other'].map(t => <option key={t}>{t}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Priority</label>
                  <select className="form-control" value={form.priority} onChange={e => setF('priority', e.target.value)}>
                    {['Low', 'Medium', 'High'].map(p => <option key={p}>{p}</option>)}
                  </select>
                </div>
                <div className="form-group" style={{ gridColumn: '1 / -1' }}>
                  <label className="form-label">Subject / Description *</label>
                  <input className="form-control" required value={form.subject} onChange={e => setF('subject', e.target.value)} placeholder="Briefly describe the issue" />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Submit Grievance</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
