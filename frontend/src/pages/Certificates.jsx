import { useState } from 'react'
import { Award, Download, X, Plus, FileCheck } from 'lucide-react'
import { certificates as initCerts } from '../data/mockData'

const TYPE_COLORS = { Bonafide: 'badge-primary', Transfer: 'badge-warning', Character: 'badge-success', Migration: 'badge-info' }

export default function Certificates() {
  const [certs, setCerts] = useState(initCerts)
  const [showModal, setShowModal] = useState(false)
  const [filter, setFilter] = useState('All')
  const [form, setForm] = useState({ student: '', sId: '', type: 'Bonafide', reason: '' })

  const filtered = filter === 'All' ? certs : certs.filter(c => c.type === filter)

  const issue = (id) => {
    setCerts(prev => prev.map(c => c.id === id ? { ...c, status: 'Issued', issued: new Date().toISOString().split('T')[0] } : c))
  }

  const handleAdd = (e) => {
    e.preventDefault()
    setCerts(prev => [...prev, {
      id: 'CERT-' + String(prev.length + 1).padStart(3, '0'),
      ...form,
      issued: '-',
      status: 'Pending'
    }])
    setShowModal(false)
    setForm({ student: '', sId: '', type: 'Bonafide', reason: '' })
  }

  const setF = (k, v) => setForm(p => ({ ...p, [k]: v }))

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Certificates</h1>
          <p className="page-subtitle">Manage and issue student certificates</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)} id="request-cert-btn"><Plus size={17} /> Request Certificate</button>
      </div>

      {/* Summary */}
      <div style={{ display: 'flex', gap: '0.75rem', marginBottom: '1.25rem', flexWrap: 'wrap' }}>
        {[
          { label: 'Total Requests', val: certs.length, bg: '#dbeafe', color: '#1e40af' },
          { label: 'Issued', val: certs.filter(c => c.status === 'Issued').length, bg: '#dcfce7', color: '#166534' },
          { label: 'Pending', val: certs.filter(c => c.status === 'Pending').length, bg: '#fef3c7', color: '#92400e' },
        ].map(s => (
          <div key={s.label} style={{ background: s.bg, color: s.color, padding: '0.4rem 1rem', borderRadius: 9999, fontSize: '0.82rem', fontWeight: 700 }}>
            {s.label}: {s.val}
          </div>
        ))}
      </div>

      {/* Type filter */}
      <div className="tabs" style={{ maxWidth: 500, marginBottom: '1.25rem' }}>
        {['All', 'Bonafide', 'Transfer', 'Character', 'Migration'].map(t => (
          <button key={t} className={`tab-btn ${filter === t ? 'active' : ''}`} onClick={() => setFilter(t)}>{t}</button>
        ))}
      </div>

      <div className="grid-2">
        {filtered.map(c => (
          <div className="card" key={c.id}>
            <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', marginBottom: '1rem' }}>
              <div style={{ display: 'flex', gap: '0.75rem', alignItems: 'center' }}>
                <div style={{ width: 46, height: 46, borderRadius: 12, background: '#ede9fe', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  <Award size={22} color="#7c3aed" />
                </div>
                <div>
                  <div style={{ fontWeight: 700, fontSize: '0.95rem' }}>{c.type} Certificate</div>
                  <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>{c.id}</div>
                </div>
              </div>
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end', gap: 4 }}>
                <span className={`badge ${TYPE_COLORS[c.type] || 'badge-gray'}`}>{c.type}</span>
                <span className={`badge ${c.status === 'Issued' ? 'badge-success' : 'badge-warning'}`}>{c.status}</span>
              </div>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '0.4rem', fontSize: '0.85rem', marginBottom: '1rem' }}>
              <div>
                <span style={{ fontWeight: 700 }}>{c.student}</span>
                <code style={{ background: '#f1f5f9', padding: '1px 7px', borderRadius: 5, fontSize: '0.75rem', marginLeft: 6 }}>{c.sId}</code>
              </div>
              <div style={{ color: 'var(--text-muted)' }}>Reason: {c.reason}</div>
              {c.status === 'Issued' && <div style={{ color: 'var(--text-muted)' }}>Issued on: <strong>{c.issued}</strong></div>}
            </div>

            <div style={{ display: 'flex', gap: '0.5rem' }}>
              {c.status === 'Pending' && (
                <button className="btn btn-primary btn-sm" onClick={() => issue(c.id)} id={`issue-cert-${c.id}`}>
                  <FileCheck size={14} /> Issue Certificate
                </button>
              )}
              {c.status === 'Issued' && (
                <button className="btn btn-secondary btn-sm" id={`download-cert-${c.id}`} onClick={() => alert(`Downloading ${c.type} certificate for ${c.student}…`)}>
                  <Download size={14} /> Download PDF
                </button>
              )}
            </div>
          </div>
        ))}
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-title">Request Certificate</div>
              <button className="modal-close" onClick={() => setShowModal(false)}><X size={18} /></button>
            </div>
            <form onSubmit={handleAdd} id="cert-form">
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
                  <label className="form-label">Certificate Type</label>
                  <select className="form-control" value={form.type} onChange={e => setF('type', e.target.value)}>
                    {['Bonafide', 'Transfer', 'Character', 'Migration'].map(t => <option key={t}>{t}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Reason *</label>
                  <input className="form-control" required value={form.reason} onChange={e => setF('reason', e.target.value)} placeholder="e.g. Bank Loan, Job Application" />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Submit Request</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
