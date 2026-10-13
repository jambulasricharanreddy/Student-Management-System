import { useState } from 'react'
import { CreditCard, Search, AlertCircle, CheckCircle, Clock } from 'lucide-react'
import { fees as initFees } from '../data/mockData'
import { PieChart, Pie, Cell, Tooltip, ResponsiveContainer, Legend } from 'recharts'

export default function FeeManagement() {
  const [fees, setFees] = useState(initFees)
  const [search, setSearch] = useState('')
  const [filter, setFilter] = useState('All')

  const filtered = fees.filter(f => {
    const q = search.toLowerCase()
    const matchSearch = !q || f.student.toLowerCase().includes(q) || f.sId.toLowerCase().includes(q)
    const matchFilter = filter === 'All' || f.status === filter
    return matchSearch && matchFilter
  })

  const total = fees.reduce((s, f) => s + f.amount, 0)
  const collected = fees.reduce((s, f) => s + f.paid, 0)
  const pending = fees.reduce((s, f) => s + f.due, 0)

  const pieData = [
    { name: 'Paid', value: fees.filter(f => f.status === 'Paid').length },
    { name: 'Partial', value: fees.filter(f => f.status === 'Partial').length },
    { name: 'Unpaid', value: fees.filter(f => f.status === 'Unpaid').length },
  ]
  const COLORS = ['#10b981', '#f59e0b', '#ef4444']

  const markPaid = (id) => {
    setFees(prev => prev.map(f => f.id === id ? { ...f, paid: f.amount, due: 0, status: 'Paid', date: new Date().toISOString().split('T')[0] } : f))
  }

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Fee Management</h1>
          <p className="page-subtitle">Track and manage student fee payments</p>
        </div>
      </div>

      {/* Stats + Pie chart */}
      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr 1fr 260px', gap: '1.25rem', marginBottom: '1.5rem' }}>
        {[
          { label: 'Total Billed', val: '₹' + (total/100000).toFixed(1) + 'L', icon: CreditCard, color: '#2563eb', bg: '#dbeafe' },
          { label: 'Collected', val: '₹' + (collected/100000).toFixed(1) + 'L', icon: CheckCircle, color: '#10b981', bg: '#dcfce7' },
          { label: 'Pending', val: '₹' + (pending/100000).toFixed(1) + 'L', icon: AlertCircle, color: '#ef4444', bg: '#fee2e2' },
        ].map(s => (
          <div className="stat-card" key={s.label}>
            <div className="stat-icon" style={{ background: s.bg }}><s.icon size={22} color={s.color} /></div>
            <div className="stat-body">
              <div className="stat-label">{s.label}</div>
              <div className="stat-value" style={{ fontSize: '1.5rem' }}>{s.val}</div>
              <div style={{ marginTop: 6 }}>
                <div className="progress-bar"><div className="progress-fill" style={{ width: `${Math.round(collected/total*100)}%` }} /></div>
              </div>
            </div>
          </div>
        ))}
        <div className="card" style={{ padding: '0.75rem' }}>
          <ResponsiveContainer width="100%" height={130}>
            <PieChart>
              <Pie data={pieData} dataKey="value" cx="50%" cy="50%" innerRadius={30} outerRadius={55} paddingAngle={3}>
                {pieData.map((_, i) => <Cell key={i} fill={COLORS[i]} />)}
              </Pie>
              <Tooltip />
              <Legend iconType="circle" iconSize={8} wrapperStyle={{ fontSize: '0.75rem' }} />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>

      <div className="card">
        <div className="table-toolbar">
          <div className="table-search">
            <Search size={15} color="#94a3b8" />
            <input placeholder="Search by student or ID…" value={search} onChange={e => setSearch(e.target.value)} id="fee-search" />
          </div>
          <div style={{ display: 'flex', gap: '0.5rem' }}>
            {['All', 'Paid', 'Partial', 'Unpaid'].map(s => (
              <button key={s} className={`btn btn-sm ${filter === s ? 'btn-primary' : 'btn-secondary'}`} onClick={() => setFilter(s)}>{s}</button>
            ))}
          </div>
        </div>
        <div className="data-table-container">
          <table className="data-table">
            <thead>
              <tr><th>Student</th><th>Semester</th><th>Total Fee</th><th>Paid</th><th>Due</th><th>Status</th><th>Payment Date</th><th>Action</th></tr>
            </thead>
            <tbody>
              {filtered.map(f => (
                <tr key={f.id}>
                  <td>
                    <div style={{ fontWeight: 600 }}>{f.student}</div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{f.sId}</div>
                  </td>
                  <td>{f.semester}</td>
                  <td>₹{f.amount.toLocaleString()}</td>
                  <td style={{ color: 'var(--success)', fontWeight: 700 }}>₹{f.paid.toLocaleString()}</td>
                  <td style={{ color: f.due > 0 ? 'var(--danger)' : 'var(--text-muted)', fontWeight: f.due > 0 ? 700 : 400 }}>₹{f.due.toLocaleString()}</td>
                  <td>
                    <span className={`badge ${f.status === 'Paid' ? 'badge-success' : f.status === 'Partial' ? 'badge-warning' : 'badge-danger'}`}>
                      {f.status === 'Paid' ? <CheckCircle size={11} style={{ marginRight: 3 }} /> : f.status === 'Unpaid' ? <AlertCircle size={11} style={{ marginRight: 3 }} /> : <Clock size={11} style={{ marginRight: 3 }} />}
                      {f.status}
                    </span>
                  </td>
                  <td style={{ color: 'var(--text-muted)', fontSize: '0.82rem' }}>{f.date}</td>
                  <td>
                    {f.status !== 'Paid' && (
                      <button className="btn btn-success btn-sm" onClick={() => markPaid(f.id)} id={`mark-paid-${f.id}`}>Mark Paid</button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  )
}
