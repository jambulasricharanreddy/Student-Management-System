import { useState } from 'react'
import { Plus, Search, Edit2, Trash2, X } from 'lucide-react'
import { lecturers as initLecturers } from '../data/mockData'

export default function LecturerManagement() {
  const [lecturers, setLecturers] = useState(initLecturers)
  const [search, setSearch] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editItem, setEditItem] = useState(null)
  const [form, setForm] = useState({ id: '', name: '', email: '', phone: '', dept: 'Computer Science', subject: '', qualification: 'Ph.D', experience: '', status: 'Active' })

  const filtered = lecturers.filter(l => {
    const q = search.toLowerCase()
    return !q || l.name.toLowerCase().includes(q) || l.id.toLowerCase().includes(q) || l.dept.toLowerCase().includes(q) || l.subject.toLowerCase().includes(q)
  })

  const openAdd = () => {
    setEditItem(null)
    setForm({ id: 'L-' + String(lecturers.length + 1).padStart(3, '0'), name: '', email: '', phone: '', dept: 'Computer Science', subject: '', qualification: 'Ph.D', experience: '', status: 'Active' })
    setShowModal(true)
  }

  const openEdit = (l) => { setEditItem(l); setForm({ ...l }); setShowModal(true) }

  const handleDelete = (id) => {
    if (window.confirm('Delete this lecturer?')) setLecturers(prev => prev.filter(l => l.id !== id))
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    if (editItem) setLecturers(prev => prev.map(l => l.id === editItem.id ? form : l))
    else setLecturers(prev => [...prev, form])
    setShowModal(false)
  }

  const setF = (k, v) => setForm(p => ({ ...p, [k]: v }))

  const QUAL = ['Ph.D', 'M.Tech', 'M.E', 'M.Sc', 'MBA']

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Lecturer Management</h1>
          <p className="page-subtitle">Manage faculty profiles and course assignments</p>
        </div>
        <button className="btn btn-primary" onClick={openAdd} id="add-lecturer-btn"><Plus size={17} /> Add Lecturer</button>
      </div>

      <div className="card">
        <div className="table-toolbar">
          <div className="table-search">
            <Search size={15} color="#94a3b8" />
            <input placeholder="Search by name, subject, department…" value={search} onChange={e => setSearch(e.target.value)} id="lecturer-search" />
          </div>
          <span style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>{filtered.length} faculty members</span>
        </div>
        <div className="data-table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Lecturer</th><th>ID</th><th>Department</th><th>Subject</th><th>Qualification</th><th>Experience</th><th>Status</th><th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(l => (
                <tr key={l.id}>
                  <td>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                      <div style={{ width: 36, height: 36, borderRadius: 10, background: 'linear-gradient(135deg,#2563eb,#7c3aed)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontWeight: 700, fontSize: '0.9rem' }}>{l.name[0]}</div>
                      <div>
                        <div style={{ fontWeight: 600 }}>{l.name}</div>
                        <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{l.email}</div>
                      </div>
                    </div>
                  </td>
                  <td><code style={{ background: '#f1f5f9', padding: '2px 7px', borderRadius: 5, fontSize: '0.8rem' }}>{l.id}</code></td>
                  <td>{l.dept}</td>
                  <td>{l.subject}</td>
                  <td><span className="badge badge-info">{l.qualification}</span></td>
                  <td>{l.experience}</td>
                  <td><span className={`badge ${l.status === 'Active' ? 'badge-success' : 'badge-warning'}`}>{l.status}</span></td>
                  <td>
                    <div style={{ display: 'flex', gap: '0.35rem' }}>
                      <button className="btn btn-secondary btn-sm" onClick={() => openEdit(l)}><Edit2 size={14} /></button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(l.id)}><Trash2 size={14} /></button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-title">{editItem ? 'Edit Lecturer' : 'Add Lecturer'}</div>
              <button className="modal-close" onClick={() => setShowModal(false)}><X size={18} /></button>
            </div>
            <form onSubmit={handleSubmit} id="lecturer-form">
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Lecturer ID</label>
                  <input className="form-control" value={form.id} readOnly style={{ background: '#f8fafc', color: 'var(--text-muted)' }} />
                </div>
                <div className="form-group">
                  <label className="form-label">Full Name *</label>
                  <input className="form-control" required value={form.name} onChange={e => setF('name', e.target.value)} placeholder="Dr. Full Name" />
                </div>
                <div className="form-group">
                  <label className="form-label">Email *</label>
                  <input className="form-control" type="email" required value={form.email} onChange={e => setF('email', e.target.value)} />
                </div>
                <div className="form-group">
                  <label className="form-label">Phone</label>
                  <input className="form-control" value={form.phone} onChange={e => setF('phone', e.target.value)} />
                </div>
                <div className="form-group">
                  <label className="form-label">Department *</label>
                  <select className="form-control" value={form.dept} onChange={e => setF('dept', e.target.value)}>
                    {['Computer Science', 'Electronics', 'Mechanical', 'Civil'].map(d => <option key={d}>{d}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Subject *</label>
                  <input className="form-control" required value={form.subject} onChange={e => setF('subject', e.target.value)} placeholder="e.g. Data Structures" />
                </div>
                <div className="form-group">
                  <label className="form-label">Qualification</label>
                  <select className="form-control" value={form.qualification} onChange={e => setF('qualification', e.target.value)}>
                    {QUAL.map(q => <option key={q}>{q}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Experience</label>
                  <input className="form-control" value={form.experience} onChange={e => setF('experience', e.target.value)} placeholder="e.g. 8 yrs" />
                </div>
                <div className="form-group">
                  <label className="form-label">Status</label>
                  <select className="form-control" value={form.status} onChange={e => setF('status', e.target.value)}>
                    {['Active', 'On Leave', 'Inactive'].map(s => <option key={s}>{s}</option>)}
                  </select>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">{editItem ? 'Save Changes' : 'Add Lecturer'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
