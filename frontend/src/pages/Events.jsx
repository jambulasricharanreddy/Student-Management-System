import { useState } from 'react'
import { CalendarDays, Plus, X, Users, MapPin, Clock } from 'lucide-react'
import { events as initEvents } from '../data/mockData'

const TYPE_COLORS = { Technical: 'badge-primary', Placement: 'badge-success', Sports: 'badge-warning', Cultural: 'badge-secondary', Academic: 'badge-info' }
const STATUS_COLORS = { Upcoming: 'badge-primary', Completed: 'badge-success', Cancelled: 'badge-danger' }

export default function Events() {
  const [events, setEvents] = useState(initEvents)
  const [showModal, setShowModal] = useState(false)
  const [filter, setFilter] = useState('All')
  const [form, setForm] = useState({ title: '', date: '', venue: '', type: 'Technical', capacity: 100, registrations: 0, status: 'Upcoming' })

  const filtered = filter === 'All' ? events : events.filter(e => e.status === filter)

  const handleAdd = (e) => {
    e.preventDefault()
    setEvents(prev => [...prev, { id: 'E-' + String(prev.length + 1).padStart(3, '0'), ...form, capacity: parseInt(form.capacity), registrations: parseInt(form.registrations) }])
    setShowModal(false)
    setForm({ title: '', date: '', venue: '', type: 'Technical', capacity: 100, registrations: 0, status: 'Upcoming' })
  }

  const setF = (k, v) => setForm(p => ({ ...p, [k]: v }))

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Events</h1>
          <p className="page-subtitle">Manage campus events, registrations, and schedules</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)} id="add-event-btn"><Plus size={17} /> Create Event</button>
      </div>

      <div className="tabs" style={{ maxWidth: 380 }}>
        {['All', 'Upcoming', 'Completed'].map(s => (
          <button key={s} className={`tab-btn ${filter === s ? 'active' : ''}`} onClick={() => setFilter(s)}>{s}</button>
        ))}
      </div>

      <div className="grid-2">
        {filtered.map(event => {
          const pct = Math.round((event.registrations / event.capacity) * 100)
          return (
            <div className="card" key={event.id} style={{ borderTop: `3px solid ${pct >= 80 ? '#10b981' : '#2563eb'}` }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '0.85rem' }}>
                <div>
                  <div style={{ fontWeight: 700, fontSize: '1rem', marginBottom: 4 }}>{event.title}</div>
                  <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
                    <span className={`badge ${TYPE_COLORS[event.type] || 'badge-gray'}`}>{event.type}</span>
                    <span className={`badge ${STATUS_COLORS[event.status] || 'badge-gray'}`}>{event.status}</span>
                  </div>
                </div>
              </div>

              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.4rem', fontSize: '0.82rem', color: 'var(--text-muted)', marginBottom: '0.85rem' }}>
                <div style={{ display: 'flex', gap: '0.4rem', alignItems: 'center' }}>
                  <CalendarDays size={13} /> {event.date}
                </div>
                <div style={{ display: 'flex', gap: '0.4rem', alignItems: 'center' }}>
                  <MapPin size={13} /> {event.venue}
                </div>
              </div>

              <div>
                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.78rem', color: 'var(--text-muted)', marginBottom: '0.35rem' }}>
                  <span style={{ display: 'flex', alignItems: 'center', gap: 4 }}><Users size={12} /> {event.registrations} / {event.capacity} registered</span>
                  <span style={{ fontWeight: 700, color: pct >= 80 ? 'var(--success)' : 'var(--text-muted)' }}>{pct}%</span>
                </div>
                <div className="progress-bar">
                  <div className="progress-fill" style={{ width: `${pct}%`, background: pct >= 80 ? '#10b981' : '#2563eb' }} />
                </div>
              </div>
            </div>
          )
        })}
      </div>

      {showModal && (
        <div className="modal-overlay" onClick={e => e.target === e.currentTarget && setShowModal(false)}>
          <div className="modal-content">
            <div className="modal-header">
              <div className="modal-title">Create Event</div>
              <button className="modal-close" onClick={() => setShowModal(false)}><X size={18} /></button>
            </div>
            <form onSubmit={handleAdd} id="event-form">
              <div className="form-group">
                <label className="form-label">Event Title *</label>
                <input className="form-control" required value={form.title} onChange={e => setF('title', e.target.value)} placeholder="e.g. Annual Tech Fest 2027" />
              </div>
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Date *</label>
                  <input type="date" className="form-control" required value={form.date} onChange={e => setF('date', e.target.value)} />
                </div>
                <div className="form-group">
                  <label className="form-label">Venue *</label>
                  <input className="form-control" required value={form.venue} onChange={e => setF('venue', e.target.value)} placeholder="e.g. Main Auditorium" />
                </div>
                <div className="form-group">
                  <label className="form-label">Type</label>
                  <select className="form-control" value={form.type} onChange={e => setF('type', e.target.value)}>
                    {['Technical', 'Placement', 'Sports', 'Cultural', 'Academic'].map(t => <option key={t}>{t}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Capacity</label>
                  <input type="number" className="form-control" value={form.capacity} onChange={e => setF('capacity', e.target.value)} />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Create Event</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
