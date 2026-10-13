import { useState } from 'react'
import { BookOpen, Search, Plus, X } from 'lucide-react'
import { books as initBooks } from '../data/mockData'

export default function Library() {
  const [books, setBooks] = useState(initBooks)
  const [search, setSearch] = useState('')
  const [showModal, setShowModal] = useState(false)
  const [form, setForm] = useState({ title: '', author: '', category: 'CS', total: 5, available: 5 })

  const filtered = books.filter(b => {
    const q = search.toLowerCase()
    return !q || b.title.toLowerCase().includes(q) || b.author.toLowerCase().includes(q) || b.category.toLowerCase().includes(q)
  })

  const checkout = (id) => {
    setBooks(prev => prev.map(b => b.id === id && b.available > 0 ? { ...b, available: b.available - 1, status: b.available - 1 === 0 ? 'Checked Out' : 'Available' } : b))
  }

  const returnBook = (id) => {
    setBooks(prev => prev.map(b => b.id === id ? { ...b, available: b.available + 1, status: 'Available' } : b))
  }

  const handleAdd = (e) => {
    e.preventDefault()
    setBooks(prev => [...prev, { id: 'B-' + String(prev.length + 1).padStart(3, '0'), ...form, available: parseInt(form.available), total: parseInt(form.total), status: parseInt(form.available) > 0 ? 'Available' : 'Checked Out' }])
    setShowModal(false)
    setForm({ title: '', author: '', category: 'CS', total: 5, available: 5 })
  }

  const setF = (k, v) => setForm(p => ({ ...p, [k]: v }))

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Library</h1>
          <p className="page-subtitle">Manage library catalog, book availability, and checkouts</p>
        </div>
        <button className="btn btn-primary" onClick={() => setShowModal(true)} id="add-book-btn"><Plus size={17} /> Add Book</button>
      </div>

      {/* Summary chips */}
      <div style={{ display: 'flex', gap: '0.75rem', marginBottom: '1.25rem', flexWrap: 'wrap' }}>
        {[
          { label: 'Total Books', val: books.reduce((s, b) => s + b.total, 0), bg: '#dbeafe', color: '#1e40af' },
          { label: 'Available', val: books.reduce((s, b) => s + b.available, 0), bg: '#dcfce7', color: '#166534' },
          { label: 'Checked Out', val: books.reduce((s, b) => s + (b.total - b.available), 0), bg: '#fef3c7', color: '#92400e' },
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
            <input placeholder="Search by title, author, category…" value={search} onChange={e => setSearch(e.target.value)} id="book-search" />
          </div>
        </div>
        <div className="data-table-container">
          <table className="data-table">
            <thead>
              <tr><th>Book</th><th>Category</th><th>Available</th><th>Total</th><th>Status</th><th>Actions</th></tr>
            </thead>
            <tbody>
              {filtered.map(b => (
                <tr key={b.id}>
                  <td>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem' }}>
                      <div style={{ width: 38, height: 38, borderRadius: 10, background: '#dbeafe', display: 'flex', alignItems: 'center', justifyContent: 'center', flexShrink: 0 }}>
                        <BookOpen size={18} color="#2563eb" />
                      </div>
                      <div>
                        <div style={{ fontWeight: 600 }}>{b.title}</div>
                        <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{b.author}</div>
                      </div>
                    </div>
                  </td>
                  <td><span className="badge badge-info">{b.category}</span></td>
                  <td>
                    <div style={{ display: 'flex', alignItems: 'center', gap: 6 }}>
                      <div style={{ width: 60, height: 6, background: '#e2e8f0', borderRadius: 99, overflow: 'hidden' }}>
                        <div style={{ height: '100%', width: `${Math.round(b.available / b.total * 100)}%`, background: b.available > 0 ? '#10b981' : '#ef4444', borderRadius: 99 }} />
                      </div>
                      <span style={{ fontWeight: 700, color: b.available > 0 ? 'var(--success)' : 'var(--danger)' }}>{b.available}</span>
                    </div>
                  </td>
                  <td>{b.total}</td>
                  <td><span className={`badge ${b.status === 'Available' ? 'badge-success' : 'badge-danger'}`}>{b.status}</span></td>
                  <td>
                    <div style={{ display: 'flex', gap: '0.35rem' }}>
                      <button className="btn btn-primary btn-sm" onClick={() => checkout(b.id)} disabled={b.available === 0} id={`checkout-${b.id}`}>Checkout</button>
                      <button className="btn btn-secondary btn-sm" onClick={() => returnBook(b.id)} disabled={b.available >= b.total} id={`return-${b.id}`}>Return</button>
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
              <div className="modal-title">Add Book to Catalog</div>
              <button className="modal-close" onClick={() => setShowModal(false)}><X size={18} /></button>
            </div>
            <form onSubmit={handleAdd} id="book-form">
              <div className="form-group">
                <label className="form-label">Book Title *</label>
                <input className="form-control" required value={form.title} onChange={e => setF('title', e.target.value)} placeholder="Book Title" />
              </div>
              <div className="grid-2">
                <div className="form-group">
                  <label className="form-label">Author *</label>
                  <input className="form-control" required value={form.author} onChange={e => setF('author', e.target.value)} placeholder="Author Name" />
                </div>
                <div className="form-group">
                  <label className="form-label">Category</label>
                  <select className="form-control" value={form.category} onChange={e => setF('category', e.target.value)}>
                    {['CS', 'EC', 'ME', 'CV', 'Math', 'Physics'].map(c => <option key={c}>{c}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label">Total Copies</label>
                  <input type="number" className="form-control" min="1" value={form.total} onChange={e => setF('total', e.target.value)} />
                </div>
                <div className="form-group">
                  <label className="form-label">Available Copies</label>
                  <input type="number" className="form-control" min="0" value={form.available} onChange={e => setF('available', e.target.value)} />
                </div>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowModal(false)}>Cancel</button>
                <button type="submit" className="btn btn-primary">Add Book</button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}
