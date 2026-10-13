import { useState } from 'react'
import { Briefcase, MapPin, Calendar, DollarSign, Plus, X } from 'lucide-react'
import { internships as initInternships } from '../data/mockData'

const STATUS_COLORS = { Completed: 'badge-success', Ongoing: 'badge-primary', Upcoming: 'badge-warning' }

export default function Internships() {
  const [internships, setInternships] = useState(initInternships)
  const [showModal, setShowModal] = useState(false)
  const [filter, setFilter] = useState('All')
  const [form, setForm] = useState({ student: '', sId: '', company: '', role: '', startDate: '', endDate: '', stipend: '', status: 'Upcoming' })

  const filtered = filter === 'All' ? internships : internships.filter(i => i.status === filter)

  const handleAdd = (e) => {
    e.preventDefault()
    setInternships(prev => [...prev, { id: 'INT-' + String(prev.length + 1).padStart(3, '0'), ...form }])
    setShowModal(false)
    setForm({ student: '', sId: '', company: '', role: '', startDate: '', endDate: '', stipend: '', status: 'Upcoming' })
  }

  const setF = (k, v) => setForm(p => ({ ...p, [k]: v }))

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Internships</h1>
          <p className="page-subtitle">Track student internship records and placements</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)} id="add-internship-btn"><Plus size={17} /> Add Internship</button>
      </div>

      {/* Status filter tabs */}
      <div className="tabs" style={{ maxWidth: 380 }}>
        {['All', 'Ongoing', 'Upcoming', 'Completed'].map(s => (
          <button key={s} className={`tab-btn ${filter === s ? 'active' : ''}`} onClick={() => setFilter(s)}>{s}</button>
        ))}
      </div>

      {/* Summary */}
      <div className="grid-3" style={{ marginBottom: '1.25rem' }}>
        {[
          { label: 'Total Internships', val: internships.length, color: '#2563eb' },
          { label: 'Ongoing', val: internships.filter(i => i.status === 'Ongoing').length, color: '#10b981' },
          { label: 'Completed', val: internships.filter(i => i.status === 'Completed').length, color: '#7c3aed' },
        ].map(s => (
          <div className="card" key={s.label} style={{ textAlign: 'center', borderTop: `3px solid ${s.color}` }}>
            <div style={{ fontSize: '1.8rem', fontWeight: 800, color: s.color, fontFamily: 'var(--font-heading)' }}>{s.val}</div>
            <div style={{ fontSize: '0.82rem', color: 'var(--text-muted)', fontWeight: 600 }}>{s.label}</div>
          </div>
        ))}
      </div>

      <div className="grid-2">
        {filtered.map(item => (
          <div className="card" key={item.id}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1rem' }}>
              <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center' }}>
                <div style={{ width: 46, height: 46, borderRadius: 12, background: '#dbeafe', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Briefcase size={22} color="#2563eb" />
                </div>
                <div>
                  <div style={{ fontWeight: 700, fontSize: '1rem' }}>{item.company}</div>
                  <div style={{ fontSize: '0.82rem', color: 'var(--text-muted)' }}>{item.role}</div>
                </div>
              </div>
              <span className={`badge ${STATUS_COLORS[item.status] || 'badge-gray'}`}>{item.status}</span>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.45rem', fontSize: '0.85rem' }}>
              <div style={{ display: 'flex', gap: '0.5rem', alignItems: 'center', color: 'var(--text-muted)' }}>
                <span style={{ fontWeight: 600, color: 'var(--text-main)' }}>{item.student}</span>
                <code style={{ background: '#f1f5f9', padding: '1px 7px', borderRadius: 5, fontSize: '0.75rem' }}>{item.sId}</code>
              </div>
              <div style={{ display: 'flex', gap: '0.4rem', alignItems: 'center', color: 'var(--text-muted)' }}>
                <Calendar size={13} />
                {item.startDate} → {item.endDate}
              </div>
              <div style={{ display: 'flex', gap: '0.4rem', alignItems: 'center', color: 'var(--success)', fontWeight: 700 }}>
                <DollarSign size={13} />
                {item.stipend}
              </div>
            </div>
          </div>
        ))}
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-title">Add Internship Record</div>
              <button className="modal-close" onClick={() => setShowModal(false)}><X size={18} /></button>
            </div>
            <form onSubmit={handleAdd} id="internship-form">
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Student Name *</label>
                  <input className="form-control" required value={form.student} onChange={e => setF('student', e.target.value)} placeholder="Full Name" />
                </div>
                <div className="form-group">
                  <label className="form-label">Student ID</label>
                  <input className="form-control" value={form.sId} onChange={e => setF('sId', e.target.value)} placeholder="S-1001" />
                </div>
                <div className="form-group">
                  <label className="form-label">Company *</label>
                  <input className="form-control" required value={form.company} onChange={e => setF('company', e.target.value)} placeholder="Company Name" />
                </div>
                <div className="form-group">
                  <label className="form-label">Role *</label>
                  <input className="form-control" required value={form.role} onChange={e => setF('role', e.target.value)} placeholder="e.g. Software Intern" />
                </div>
                <div className="form-group">
                  <label className="form-label">Start Date</label>
                  <input type="date" className="form-control" value={form.startDate} onChange={e => setF('startDate', e.target.value)} />
                </div>
                <div className="form-group">
                  <label className="form-label">End Date</label>
                  <input type="date" className="form-control" value={form.endDate} onChange={e => setF('endDate', e.target.value)} />
                </div>
                <div className="form-group">
                  <label className="form-label">Stipend</label>
                  <input className="form-control" value={form.stipend} onChange={e => setF('stipend', e.target.value)} placeholder="₹15,000/mo" />
                </div>
                <div className="form-group">
                  <label className="form-label">Status</label>
                  <select className="form-control" value={form.status} onChange={e => setF('status', e.target.value)}>
                    {['Upcoming', 'Ongoing', 'Completed'].map(s => <option key={s}>{s}</option>)}
                  </select>
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Add Record</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
