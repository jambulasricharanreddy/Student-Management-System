import { useState } from 'react'
import { Plus, Search, Edit2, Trash2, X, Users } from 'lucide-react'
import { courses as initCourses } from '../data/mockData'

export default function CourseManagement() {
  const [courses, setCourses] = useState(initCourses)
  const [search, setSearch] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [editItem, setEditItem] = useState(null)
  const [form, setForm] = useState({ id: '', name: '', dept: 'Computer Science', credits: 3, lecturer: '', students: 0, semester: '1st', status: 'Active' })

  const filtered = courses.filter(c => {
    const q = search.toLowerCase()
    return !q || c.name.toLowerCase().includes(q) || c.id.toLowerCase().includes(q) || c.dept.toLowerCase().includes(q)
  })

  const SEMESTER_COLORS = { '1st': 'badge-info', '2nd': 'badge-primary', '3rd': 'badge-secondary', '4th': 'badge-warning', '5th': 'badge-success', '6th': 'badge-danger', '7th': 'badge-info', '8th': 'badge-gray' }

  const openAdd = () => {
    setEditItem(null)
    const depts = { 'Computer Science': 'CS', 'Electronics': 'EC', 'Mechanical': 'ME', 'Civil': 'CV' }
    setForm({ id: '', name: '', dept: 'Computer Science', credits: 3, lecturer: '', students: 0, semester: '1st', status: 'Active' })
    setShowModal(true)
  }
  const openEdit = (c) => { setEditItem(c); setForm({ ...c, credits: String(c.credits), students: String(c.students) }); setShowModal(true) }
  const handleDelete = (id) => { if (window.confirm('Delete this course?')) setCourses(prev => prev.filter(c => c.id !== id)) }
  const handleSubmit = (e) => {
    e.preventDefault()
    const course = { ...form, credits: parseInt(form.credits), students: parseInt(form.students) }
    if (editItem) setCourses(prev => prev.map(c => c.id === editItem.id ? course : c))
    else setCourses(prev => [...prev, { ...course, id: form.dept.substring(0,2).toUpperCase() + Date.now().toString().slice(-3) }])
    setShowModal(false)
  }
  const setF = (k, v) => setForm(p => ({ ...p, [k]: v }))

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Course Management</h1>
          <p className="page-subtitle">Manage curriculum, courses, and faculty assignments</p>
        </div>
        <button className="btn btn-primary" onClick={openAdd} id="add-course-btn"><Plus size={17} /> Add Course</button>
      </div>

      {/* Summary */}
      <div style={{ display: 'flex', gap: '0.75rem', marginBottom: '1.25rem', flexWrap: 'wrap' }}>
        {[
          { label: 'Total Courses', val: courses.length, bg: '#dbeafe', color: '#1e40af' },
          { label: 'Total Students', val: courses.reduce((a, c) => a + c.students, 0), bg: '#dcfce7', color: '#166534' },
          { label: 'Avg Credits', val: (courses.reduce((a, c) => a + c.credits, 0) / courses.length).toFixed(1), bg: '#ede9fe', color: '#5b21b6' },
        ].map(c => (
          <div key={c.label} style={{ background: c.bg, color: c.color, padding: '0.4rem 1rem', borderRadius: 9999, fontSize: '0.82rem', fontWeight: 700 }}>
            {c.label}: {c.val}
          </div>
        ))}
      </div>

      <div className="card">
        <div className="table-toolbar">
          <div className="table-search">
            <Search size={15} color="#94a3b8" />
            <input placeholder="Search courses…" value={search} onChange={e => setSearch(e.target.value)} id="course-search" />
          </div>
        </div>
        <div className="data-table-container">
          <table className="data-table">
            <thead>
              <tr><th>Course</th><th>Code</th><th>Department</th><th>Credits</th><th>Lecturer</th><th>Students</th><th>Semester</th><th>Status</th><th>Actions</th></tr>
            </thead>
            <tbody>
              {filtered.map(c => (
                <tr key={c.id}>
                  <td style={{ fontWeight: 600, maxWidth: 220 }}>{c.name}</td>
                  <td><code style={{ background: '#f1f5f9', padding: '2px 7px', borderRadius: 5, fontSize: '0.8rem' }}>{c.id}</code></td>
                  <td>{c.dept}</td>
                  <td>
                    <div style={{ display: 'flex', gap: 3 }}>
                      {Array.from({ length: c.credits }).map((_, i) => (
                        <div key={i} style={{ width: 8, height: 8, borderRadius: '50%', background: '#2563eb' }} />
                      ))}
                    </div>
                  </td>
                  <td>{c.lecturer}</td>
                  <td>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.35rem' }}>
                      <Users size={13} color="#64748b" />
                      <span>{c.students}</span>
                    </div>
                  </td>
                  <td><span className={`badge ${SEMESTER_COLORS[c.semester] || 'badge-gray'}`}>{c.semester}</span></td>
                  <td><span className={`badge ${c.status === 'Active' ? 'badge-success' : 'badge-gray'}`}>{c.status}</span></td>
                  <td>
                    <div style={{ display: 'flex', gap: '0.35rem' }}>
                      <button className="btn btn-secondary btn-sm" onClick={() => openEdit(c)}><Edit2 size={14} /></button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleDelete(c.id)}><Trash2 size={14} /></button>
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
              <div className="modal-title">{editItem ? 'Edit Course' : 'Add Course'}</div>
              <button className="modal-close" onClick={() => setShowModal(false)}><X size={18} /></button>
            </div>
            <form onSubmit={handleSubmit} id="course-form">
              <div className="grid-2">
                <div className="form-group" style={{ gridColumn: '1 / -1' }}>
                  <label className="form-label">Course Name *</label>
                  <input className="form-control" required value={form.name} onChange={e => setF('name', e.target.value)} placeholder="e.g. Data Structures & Algorithms" />
                </div>
                <div className="form-group">
                  <label className="form-label">Department *</label>
                  <select className="form-control" value={form.dept} onChange={e => setF('dept', e.target.value)}>
                    {['Computer Science', 'Electronics', 'Mechanical', 'Civil'].map(d => <option key={d}>{d}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Credits</label>
                  <select className="form-control" value={form.credits} onChange={e => setF('credits', e.target.value)}>
                    {[1,2,3,4,5].map(n => <option key={n}>{n}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Assigned Lecturer</label>
                  <input className="form-control" value={form.lecturer} onChange={e => setF('lecturer', e.target.value)} placeholder="Dr. Name" />
                </div>
                <div className="form-group">
                  <label className="form-label">Semester</label>
                  <select className="form-control" value={form.semester} onChange={e => setF('semester', e.target.value)}>
                    {['1st','2nd','3rd','4th','5th','6th','7th','8th'].map(s => <option key={s}>{s}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Enrolled Students</label>
                  <input className="form-control" type="number" value={form.students} onChange={e => setF('students', e.target.value)} />
                </div>
                <div className="form-group">
                  <label className="form-label">Status</label>
                  <select className="form-control" value={form.status} onChange={e => setF('status', e.target.value)}>
                    {['Active', 'Inactive'].map(s => <option key={s}>{s}</option>)}
                  </select>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">{editItem ? 'Save Changes' : 'Add Course'}</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
