import { timetable, subjectColors } from '../data/mockData'

const DAYS = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday']
const TIMES = ['9:00', '10:00', '11:00', '12:00', '14:00']
const TIME_LABELS = { '9:00': '9:00 AM', '10:00': '10:00 AM', '11:00': '11:00 AM', '12:00': '12:00 PM', '14:00': '2:00 PM' }

export default function Timetable() {
  const today = new Date().toLocaleDateString('en-US', { weekday: 'long' })

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Timetable</h1>
          <p className="page-subtitle">Weekly class schedule — Semester 5 (Oct–Dec 2026)</p>
        </div>
        <div style={{ display: 'flex', gap: '0.5rem', flexWrap: 'wrap' }}>
          {Object.entries(subjectColors).map(([sub, c]) => (
            <span key={sub} style={{ background: c.bg, color: c.color, padding: '3px 10px', borderRadius: 99, fontSize: '0.72rem', fontWeight: 700 }}>{sub}</span>
          ))}
        </div>
      </div>

      <div className="card" style={{ overflowX: 'auto' }}>
        <div style={{ minWidth: 700 }}>
          {/* Header row */}
          <div className="timetable-grid">
            <div className="tt-header">Time</div>
            {DAYS.map(day => (
              <div key={day} className="tt-header" style={{ background: day === today ? '#2563eb' : '#0f172a' }}>
                {day}
                {day === today && <div style={{ fontSize: '0.65rem', color: 'rgba(255,255,255,0.7)', marginTop: 2 }}>Today</div>}
              </div>
            ))}

            {TIMES.map(time => (
              <>
                <div key={'t-' + time} className="tt-cell tt-time">{TIME_LABELS[time]}</div>
                {DAYS.map(day => {
                  const slot = timetable[day]?.[time]
                  const col = slot?.sub && slot.sub !== '-' ? subjectColors[slot.sub] : null
                  return (
                    <div key={day + time} className="tt-cell" style={{ padding: '0.5rem', background: day === today ? '#f0f7ff' : '#fff' }}>
                      {slot && slot.sub !== '-' ? (
                        <div className="tt-subject" style={{ background: col?.bg || '#f1f5f9', color: col?.color || '#475569' }}>
                          <div style={{ fontWeight: 800, fontSize: '0.78rem' }}>{slot.sub}</div>
                          <div style={{ fontSize: '0.68rem', opacity: 0.75, marginTop: 1 }}>{slot.room}</div>
                          <div style={{ fontSize: '0.65rem', opacity: 0.65 }}>{slot.lecturer}</div>
                        </div>
                      ) : (
                        <div style={{ height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#cbd5e1', fontSize: '0.75rem' }}>—</div>
                      )}
                    </div>
                  )
                })}
              </>
            ))}
          </div>
        </div>
      </div>

      {/* Legend */}
      <div className="card" style={{ marginTop: '1.25rem' }}>
        <div className="section-title" style={{ marginBottom: '0.75rem' }}>Course Legend</div>
        <div style={{ display: 'flex', gap: '1rem', flexWrap: 'wrap' }}>
          {Object.entries(subjectColors).map(([sub, c]) => (
            <div key={sub} style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', padding: '0.4rem 0.75rem', background: c.bg, borderRadius: 8 }}>
              <div style={{ width: 10, height: 10, borderRadius: 3, background: c.color }} />
              <span style={{ fontWeight: 700, color: c.color, fontSize: '0.82rem' }}>{sub}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
