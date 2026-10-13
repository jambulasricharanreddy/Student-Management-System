import { useState, useEffect } from 'react'
import { QRCodeSVG } from 'qrcode.react'
import { RefreshCw, CheckCircle, Clock, Wifi } from 'lucide-react'
import { courses } from '../data/mockData'

export default function QRAttendance() {
  const [selectedCourse, setSelectedCourse] = useState(courses[0].id)
  const [sessionActive, setSessionActive] = useState(false)
  const [timer, setTimer] = useState(0)
  const [scanned, setScanned] = useState([])
  const [qrData, setQrData] = useState('')

  const generateSession = () => {
    const token = `ssh-att-${selectedCourse}-${Date.now()}`
    setQrData(token)
    setSessionActive(true)
    setTimer(300)
    setScanned([])
  }

  useEffect(() => {
    if (!sessionActive || timer <= 0) return
    const t = setInterval(() => {
      setTimer(p => {
        if (p <= 1) { setSessionActive(false); return 0 }
        return p - 1
      })
    }, 1000)
    return () => clearInterval(t)
  }, [sessionActive, timer])

  // Simulate scan
  useEffect(() => {
    if (!sessionActive) return
    const names = ['Aarav Sharma', 'Priya Reddy', 'Ananya Singh', 'Arjun Gupta', 'Pooja Iyer']
    let idx = 0
    const iv = setInterval(() => {
      if (idx < names.length) {
        setScanned(p => [...p, { name: names[idx], time: new Date().toLocaleTimeString(), id: 'S-100' + (idx + 1) }])
        idx++
      } else clearInterval(iv)
    }, 2500)
    return () => clearInterval(iv)
  }, [sessionActive])

  const mins = String(Math.floor(timer / 60)).padStart(2, '0')
  const secs = String(timer % 60).padStart(2, '0')

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">QR Attendance</h1>
          <p className="page-subtitle">Generate a QR code for contactless attendance marking</p>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1.25rem' }}>
        {/* QR Generator */}
        <div className="card">
          <div className="section-title" style={{ marginBottom: '1.25rem' }}>Session Setup</div>
          <div className="form-group">
            <label className="form-label">Select Course</label>
            <select className="form-control" id="qr-course" value={selectedCourse} onChange={e => { setSelectedCourse(e.target.value); setSessionActive(false); setQrData('') }}>
              {courses.map(c => <option key={c.id} value={c.id}>{c.id} — {c.name}</option>)}
            </select>
          </div>

          <button
            className={`btn btn-primary btn-lg`}
            onClick={generateSession}
            id="generate-qr-btn"
            style={{ width: '100%', marginBottom: '1.5rem' }}
          >
            <RefreshCw size={17} />
            {sessionActive ? 'Regenerate QR' : 'Start QR Session'}
          </button>

          {qrData ? (
            <div className="qr-container">
              <div className="qr-code-box">
                <QRCodeSVG value={qrData} size={200} bgColor="#ffffff" fgColor="#0f172a" level="H" />
              </div>
              <div style={{ textAlign: 'center' }}>
                <div style={{ fontWeight: 700, fontSize: '0.95rem' }}>{courses.find(c => c.id === selectedCourse)?.name}</div>
                <div style={{ color: 'var(--text-muted)', fontSize: '0.82rem', marginTop: 4 }}>Scan with the university mobile app</div>
              </div>
              {sessionActive ? (
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', background: '#dcfce7', color: '#166534', padding: '0.6rem 1.2rem', borderRadius: 99, fontWeight: 700 }}>
                  <Wifi size={15} />
                  Session Active — {mins}:{secs} remaining
                </div>
              ) : (
                <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', background: '#fee2e2', color: '#991b1b', padding: '0.6rem 1.2rem', borderRadius: 99, fontWeight: 700 }}>
                  <Clock size={15} /> Session Expired
                </div>
              )}
            </div>
          ) : (
            <div className="empty-state">
              <div style={{ fontSize: 3, marginBottom: '0.75rem' }}>📲</div>
              <h3>No Active Session</h3>
              <p style={{ fontSize: '0.85rem' }}>Click "Start QR Session" to generate a code</p>
            </div>
          )}
        </div>

        {/* Scanned students */}
        <div className="card">
          <div className="section-header" style={{ marginBottom: '1rem' }}>
            <div className="section-title">Scanned Students</div>
            <span className="badge badge-success">{scanned.length} marked</span>
          </div>
          {scanned.length === 0 ? (
            <div className="empty-state" style={{ padding: '2rem' }}>
              <Clock size={36} style={{ opacity: 0.25, marginBottom: '0.5rem' }} />
              <h3>Waiting for scans…</h3>
              <p style={{ fontSize: '0.85rem' }}>Students will appear here as they scan the QR code</p>
            </div>
          ) : (
            <div>
              {scanned.map((s, i) => (
                <div key={i} style={{ display: 'flex', alignItems: 'center', gap: '0.85rem', padding: '0.75rem 0', borderBottom: '1px solid #f1f5f9', animation: 'modalEnter 0.3s ease' }}>
                  <CheckCircle size={20} color="#10b981" />
                  <div style={{ flex: 1 }}>
                    <div style={{ fontWeight: 700, fontSize: '0.9rem' }}>{s.name}</div>
                    <div style={{ fontSize: '0.75rem', color: 'var(--text-muted)' }}>{s.id}</div>
                  </div>
                  <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)', background: '#f1f5f9', padding: '2px 8px', borderRadius: 99 }}>{s.time}</div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
