import { useState } from 'react'
import { Plus, Search, Edit2, Trash2, X, Eye } from 'lucide-react'
import { students as initStudents } from '../data/mockData'

const DEPTS = ['All', 'Computer Science', 'Electronics', 'Mechanical', 'Civil']
const YEARS = ['All', '1st Year', '2nd Year', '3rd Year', '4th Year']
const STATUSES = ['All', 'Active', 'Inactive']

const COLORS = ['#2563eb', '#7c3aed', '#10b981', '#f59e0b', '#ef4444', '#0284c7', '#ec4899', '#06b6d4']

function getColor(name) {
  let h = 0; for (let i = 0; i < name.length; i++) h = name.charCodeAt(i) + ((h << 5) - h)
  return COLORS[Math.abs(h) % COLORS.length]
}

export default function StudentManagement() {
  const [students, setStudents] = useState(initStudents)
  const [search, setSearch] = useState('')
  const [deptFilter, setDeptFilter] = useState('All')
  const [yearFilter, setYearFilter] = useState('All')
  const [statusFilter, setStatusFilter] = useState('All')
  const [showModal, setShowModal] = useState(false)
  const [editStudent, setEditStudent] = useState(null)
  const [viewStudent, setViewStudent] = useState(null)
  const [form, setForm] = useState({ id: '', name: '', email: '', phone: '', dept: 'Computer Science', year: '1st Year', gpa: '', status: 'Active', dob: '', address: '' })

  const filtered = students.filter(s => {
    const q = search.toLowerCase()
    const matchSearch = !q || s.name.toLowerCase().includes(q) || s.id.toLowerCase().includes(q) || s.email.toLowerCase().includes(q)
    const matchDept = deptFilter === 'All' || s.dept === deptFilter
    const matchYear = yearFilter === 'All' || s.year === yearFilter
    const matchStatus = statusFilter === 'All' || s.status === statusFilter
    return matchSearch && matchDept && matchYear && matchStatus
  })

  const openAdd = () => {
    setEditStudent(null)
    const nextId = 'S-' + (1000 + students.length + 1)
    setForm({ id: nextId, name: '', email: '', phone: '', dept: 'Computer Science', year: '1st Year', gpa: '', status: 'Active', dob: '', address: '' })
    setShowModal(true)
  }

  const openEdit = (s) => {
    setEditStudent(s)
    setForm({ ...s })
    setShowModal(true)
  }

  const handleDelete = (id) => {
    if (window.confirm('Are you sure you want to delete this student?')) {
      setStudents(prev => prev.filter(s => s.id !== id))
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault()
    if (editStudent) {
      setStudents(prev => prev.map(s => s.id === editStudent.id ? { ...form, gpa: parseFloat(form.gpa) } : s))
    } else {
      setStudents(prev => [...prev, { ...form, gpa: parseFloat(form.gpa) }])
    }
    setShowModal(false)
  }

  const setF = (k, v) => setForm(p => ({ ...p, [k]: v }))

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Student Management</h1>
          <p className="page-subtitle">Manage student records, profiles, and enrollment</p>
        </div>
        <button className="btn btn-primary" onClick={openAdd} id="add-student-btn">
          <Plus size={17} /> Add Student
        </button>
      </div>

      {/* Summary chips */}
      <div style={{ display: 'flex', gap: '0.75rem', marginBottom: '1.25rem', flexWrap: 'wrap' }}>
        {[
          { label: 'Total', val: students.length, bg: '#dbeafe', color: '#1e40af' },
          { label: 'Active', val: students.filter(s => s.status === 'Active').length, bg: '#dcfce7', color: '#166534' },
          { label: 'Inactive', val: students.filter(s => s.status === 'Inactive').length, bg: '#fee2e2', color: '#991b1b' },
        ].map(c => (
          <div key={c.label} style={{ background: c.bg, color: c.color, padding: '0.4rem 1rem', borderRadius: 9999, fontSize: '0.82rem', fontWeight: 700 }}>
            {c.label}: {c.val}
          </div>
        ))}
      </div>

      <div className="card">
        {/* Toolbar */}
        <div className="table-toolbar">
          <div className="table-search">
            <Search size={15} color="#94a3b8" />
            <input
              placeholder="Search by name, ID, email…"
              value={search}
              onChange={e => setSearch(e.target.value)}
              id="student-search"
            />
          </div>
          <div style={{ display: 'flex', gap: '0.6rem', flexWrap: 'wrap' }}>
            {[['deptFilter', DEPTS, deptFilter, setDeptFilter], ['yearFilter', YEARS, yearFilter, setYearFilter], ['statusFilter', STATUSES, statusFilter, setStatusFilter]].map(([id, opts, val, setter]) => (
              <select key={id} id={id} className="form-control" style={{ width: 'auto', fontSize: '0.82rem' }} value={val} onChange={e => setter(e.target.value)}>
                {opts.map(o => <option key={o}>{o}</option>)}
              </select>
            ))}
          </div>
        </div>

        {/* Table */}
        <div className="data-table-container">
          <table className="data-table">
            <thead>
              <tr>
                <th>Student</th>
                <th>ID</th>
                <th>Department</th>
                <th>Year</th>
                <th>GPA</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filtered.map(s => (
                <tr key={s.id}>
                  <td>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                      <div className="student-avatar" style={{ background: getColor(s.name) }}>{s.name[0]}</div>
                      <div>
                        <div style={{ fontWeight: 600 }}>{s.name}</div>
                        <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>{s.email}</div>
                      </div>
                    </div>
                  </td>
                  <td><code style={{ background: '#f1f5f9', padding: '2px 8px', borderRadius: 5, fontSize: '0.8rem' }}>{s.id}</code></td>
                  <td>{s.dept}</td>
                  <td>{s.year}</td>
                  <td>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                      <span style={{ fontWeight: 700, color: s.gpa >= 8.5 ? 'var(--success)' : s.gpa >= 7 ? 'var(--warning)' : 'var(--danger)' }}>{s.gpa}</span>
                    </div>
                  </td>
                  <td>
                    <span className={`badge ${s.status === 'Active' ? 'badge-success' : 'badge-danger'}`}>{s.status}</span>
                  </td>
                  <td>
                    <div style={{ display: 'flex', gap: '0.35rem' }}>
                      <button className="btn btn-secondary btn-sm" onClick={() => setViewStudent(s)} title="View"><Eye size={14} /></button>
                      <button className="btn btn-secondary btn-sm" onClick={() => openEdit(s)} title="Edit"><Edit2 size={14} /></button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(s.id)} title="Delete"><Trash2 size={14} /></button>
                    </div>
                  </td>
                </tr>
              ))}
              {filtered.length === 0 && (
                <tr><td colSpan={7} style={{ textAlign: 'center', padding: '2rem', color: 'var(--text-muted)' }}>No students found</td></tr>
              )}
            </tbody>
          </table>
        </div>
        <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: '0.75rem' }}>
          Showing {filtered.length} of {students.length} students
        </div>
      </div>

      {/* Add/Edit Modal */}
      {showModal && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-title">{editStudent ? 'Edit Student' : 'Add New Student'}</div>
              <button className="modal-close" onClick={() => setShowModal(false)}><X size={18} /></button>
            </div>
            <form onSubmit={handleSubmit} id="student-form">
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Student ID</label>
                  <input className="form-control" value={form.id} readOnly style={{ background: '#f8fafc', color: 'var(--text-muted)' }} />
                </div>
                <div className="form-group">
                  <label className="form-label">Full Name *</label>
                  <input className="form-control" required value={form.name} onChange={e => setF('name', e.target.value)} placeholder="e.g. Aarav Sharma" />
                </div>
                <div className="form-group">
                  <label className="form-label">Email *</label>
                  <input className="form-control" type="email" required value={form.email} onChange={e => setF('email', e.target.value)} placeholder="student@uni.edu" />
                </div>
                <div className="form-group">
                  <label className="form-label">Phone</label>
                  <input className="form-control" value={form.phone} onChange={e => setF('phone', e.target.value)} placeholder="9876543210" />
                </div>
                <div className="form-group">
                  <label className="form-label">Department *</label>
                  <select className="form-control" value={form.dept} onChange={e => setF('dept', e.target.value)}>
                    {['Computer Science', 'Electronics', 'Mechanical', 'Civil'].map(d => <option key={d}>{d}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Year *</label>
                  <select className="form-control" value={form.year} onChange={e => setF('year', e.target.value)}>
                    {['1st Year', '2nd Year', '3rd Year', '4th Year'].map(y => <option key={y}>{y}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">GPA</label>
                  <input className="form-control" type="number" step="0.1" min="0" max="10" value={form.gpa} onChange={e => setF('gpa', e.target.value)} placeholder="8.5" />
                </div>
                <div className="form-group">
                  <label className="form-label">Status</label>
                  <select className="form-control" value={form.status} onChange={e => setF('status', e.target.value)}>
                    {['Active', 'Inactive'].map(s => <option key={s}>{s}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Date of Birth</label>
                  <input className="form-control" type="date" value={form.dob} onChange={e => setF('dob', e.target.value)} />
                </div>
                <div className="form-group">
                  <label className="form-label">Address</label>
                  <input className="form-control" value={form.address} onChange={e => setF('address', e.target.value)} placeholder="City, State" />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">{editStudent ? 'Save Changes' : 'Add Student'}</button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* View Modal */}
      {viewStudent && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setViewStudent(null)}>
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-title">Student Profile</div>
              <button className="modal-close" onClick={() => setViewStudent(null)}><X size={18} /></button>
            </div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1.5rem', padding: '1rem', background: '#f8fafc', borderRadius: 12 }}>
              <div style={{ width: 60, height: 60, borderRadius: 16, background: getColor(viewStudent.name), display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontSize: '1.5rem', fontWeight: 700 }}>
                {viewStudent.name[0]}
              </div>
              <div>
                <div style={{ fontFamily: 'var(--font-heading)', fontSize: '1.15rem', fontWeight: 700 }}>{viewStudent.name}</div>
                <div style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>{viewStudent.id} • {viewStudent.dept}</div>
                <span className={`badge ${viewStudent.status === 'Active' ? 'badge-success' : 'badge-danger'}`} style={{ marginTop: 4 }}>{viewStudent.status}</span>
              </div>
            </div>
            <div className="grid-2">
              {[['Email', viewStudent.email], ['Phone', viewStudent.phone], ['Year', viewStudent.year], ['GPA', viewStudent.gpa], ['Date of Birth', viewStudent.dob], ['Address', viewStudent.address]].map(([k, v]) => (
                <div key={k} style={{ padding: '0.65rem 0', borderBottom: '1px solid var(--border)' }}>
                  <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '0.05em' }}>{k}</div>
                  <div style={{ fontWeight: 600, marginTop: 2 }}>{v || '—'}</div>
                </div>
              ))}
            </div>
            <div className="modal-footer">
              <button className="btn btn-secondary" onClick={() => setViewStudent(null)}>Close</button>
              <button className="btn btn-primary" onClick={() => { openEdit(viewStudent); setViewStudent(null) }}>Edit Profile</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
