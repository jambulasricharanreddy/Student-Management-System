import { useState } from 'react'
import { Settings as SettingsIcon, User, Bell, Lock, Palette, Globe, Save, Check } from 'lucide-react'

export default function Settings() {
  const [activeTab, setActiveTab] = useState('profile')
  const [saved, setSaved] = useState(false)
  const [profile, setProfile] = useState({
    name: 'Dr. Rajesh Kumar',
    email: 'admin@university.edu',
    phone: '9800000001',
    university: 'National Institute of Technology',
    dept: 'Computer Science',
    designation: 'Administrator',
    bio: 'Senior administrator with 15 years of experience in university management.'
  })
  const [notifs, setNotifs] = useState({
    emailAlerts: true, feeReminders: true, attendanceAlerts: true, eventUpdates: false, grievanceAlerts: true
  })
  const [theme, setTheme] = useState({ primaryColor: '#2563eb', fontSize: 'medium', compactMode: false })

  const handleSave = () => {
    setSaved(true)
    setTimeout(() => setSaved(false), 2000)
  }

  const setP = (k, v) => setProfile(p => ({ ...p, [k]: v }))
  const setN = (k) => setNotifs(p => ({ ...p, [k]: !p[k] }))

  const tabs = [
    { id: 'profile', label: 'Profile', icon: User },
    { id: 'notifications', label: 'Notifications', icon: Bell },
    { id: 'security', label: 'Security', icon: Lock },
    { id: 'appearance', label: 'Appearance', icon: Palette },
  ]

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Settings</h1>
          <p className="page-subtitle">Manage your account, preferences, and system settings</p>
        </div>
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: '220px 1fr', gap: '1.5rem', alignItems: 'start' }}>
        {/* Sidebar tabs */}
        <div className="card" style={{ padding: '0.75rem' }}>
          {tabs.map(t => (
            <button
              key={t.id}
              onClick={() => setActiveTab(t.id)}
              style={{
                display: 'flex', alignItems: 'center', gap: '0.65rem', width: '100%',
                padding: '0.65rem 0.85rem', border: 'none', borderRadius: 10, cursor: 'pointer',
                background: activeTab === t.id ? 'linear-gradient(135deg,#2563eb,#7c3aed)' : 'transparent',
                color: activeTab === t.id ? '#fff' : 'var(--text-muted)',
                fontWeight: 600, fontSize: '0.875rem', marginBottom: 2,
                transition: 'all 0.2s'
              }}
            >
              <t.icon size={17} /> {t.label}
            </button>
          ))}
        </div>

        {/* Content */}
        <div className="card">
          {activeTab === 'profile' && (
            <>
              <div style={{ fontFamily: 'var(--font-heading)', fontSize: '1.1rem', fontWeight: 700, marginBottom: '1.5rem' }}>Profile Settings</div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1.5rem', padding: '1rem', background: '#f8fafc', borderRadius: 12 }}>
                <div style={{ width: 70, height: 70, borderRadius: 18, background: 'linear-gradient(135deg,#2563eb,#7c3aed)', display: 'flex', alignItems: 'center', justifyContent: 'center', color: '#fff', fontSize: '1.8rem', fontWeight: 700, flexShrink: 0 }}>
                  {profile.name[0]}
                </div>
                <div>
                  <div style={{ fontWeight: 700, fontSize: '1.1rem' }}>{profile.name}</div>
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.85rem' }}>{profile.email}</div>
                  <div style={{ marginTop: 4 }}><span className="badge badge-primary">{profile.designation}</span></div>
                </div>
              </div>
              <div className="grid-2">
                {[['name', 'Full Name'], ['email', 'Email Address'], ['phone', 'Phone Number'], ['designation', 'Designation'], ['university', 'University Name'], ['dept', 'Department']].map(([k, label]) => (
                  <div className="form-group" key={k}>
                    <label className="form-label">{label}</label>
                    <input className="form-control" value={profile[k]} onChange={e => setP(k, e.target.value)} id={`setting-${k}`} />
                  </div>
                ))}
                <div className="form-group" style={{ gridColumn: '1 / -1' }}>
                  <label className="form-label">Bio</label>
                  <textarea className="form-control" rows={3} value={profile.bio} onChange={e => setP('bio', e.target.value)} style={{ resize: 'vertical' }} />
                </div>
              </div>
            </>
          )}

          {activeTab === 'notifications' && (
            <>
              <div style={{ fontFamily: 'var(--font-heading)', fontSize: '1.1rem', fontWeight: 700, marginBottom: '1.5rem' }}>Notification Preferences</div>
              {[
                { key: 'emailAlerts', label: 'Email Alerts', desc: 'Receive important notifications via email' },
                { key: 'feeReminders', label: 'Fee Payment Reminders', desc: 'Alerts for pending and overdue fee payments' },
                { key: 'attendanceAlerts', label: 'Attendance Alerts', desc: 'Notify when attendance falls below threshold' },
                { key: 'eventUpdates', label: 'Event Updates', desc: 'Get updates on campus events and activities' },
                { key: 'grievanceAlerts', label: 'Grievance Alerts', desc: 'Notifications for new and updated grievances' },
              ].map(n => (
                <div key={n.key} style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '1rem 0', borderBottom: '1px solid var(--border)' }}>
                  <div>
                    <div style={{ fontWeight: 600, fontSize: '0.9rem' }}>{n.label}</div>
                    <div style={{ color: 'var(--text-muted)', fontSize: '0.8rem', marginTop: 2 }}>{n.desc}</div>
                  </div>
                  <button
                    onClick={() => setN(n.key)}
                    id={`toggle-${n.key}`}
                    style={{
                      width: 48, height: 26, borderRadius: 9999, border: 'none', cursor: 'pointer',
                      background: notifs[n.key] ? 'linear-gradient(135deg,#2563eb,#7c3aed)' : '#e2e8f0',
                      position: 'relative', transition: 'all 0.3s', flexShrink: 0
                    }}
                  >
                    <div style={{
                      position: 'absolute', top: 3, left: notifs[n.key] ? 25 : 3,
                      width: 20, height: 20, borderRadius: '50%', background: '#fff',
                      transition: 'left 0.3s', boxShadow: '0 1px 4px rgba(0,0,0,0.2)'
                    }} />
                  </button>
                </div>
              ))}
            </>
          )}

          {activeTab === 'security' && (
            <>
              <div style={{ fontFamily: 'var(--font-heading)', fontSize: '1.1rem', fontWeight: 700, marginBottom: '1.5rem' }}>Security Settings</div>
              <div style={{ maxWidth: 440 }}>
                <div className="form-group">
                  <label className="form-label">Current Password</label>
                  <input type="password" className="form-control" placeholder="••••••••" id="current-password" />
                </div>
                <div className="form-group">
                  <label className="form-label">New Password</label>
                  <input type="password" className="form-control" placeholder="••••••••" id="new-password" />
                </div>
                <div className="form-group">
                  <label className="form-label">Confirm New Password</label>
                  <input type="password" className="form-control" placeholder="••••••••" id="confirm-password" />
                </div>
                <div style={{ padding: '1rem', background: '#f0f9ff', borderRadius: 10, border: '1px solid #bae6fd', fontSize: '0.82rem', color: '#0369a1', marginBottom: '1rem' }}>
                  <strong>Password requirements:</strong> Minimum 8 characters, at least one uppercase letter, one number, and one special character.
                </div>
                <button className="btn btn-primary">Update Password</button>
              </div>
            </>
          )}

          {activeTab === 'appearance' && (
            <>
              <div style={{ fontFamily: 'var(--font-heading)', fontSize: '1.1rem', fontWeight: 700, marginBottom: '1.5rem' }}>Appearance</div>
              <div className="form-group">
                <label className="form-label">Primary Color</label>
                <div style={{ display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
                  {['#2563eb', '#7c3aed', '#10b981', '#f59e0b', '#ef4444', '#0284c7'].map(c => (
                    <button
                      key={c}
                      onClick={() => setTheme(p => ({ ...p, primaryColor: c }))}
                      style={{
                        width: 36, height: 36, borderRadius: '50%', background: c, border: `3px solid ${theme.primaryColor === c ? '#0f172a' : 'transparent'}`,
                        cursor: 'pointer', transition: 'border 0.2s', flexShrink: 0
                      }}
                      id={`color-${c.replace('#','')}`}
                    />
                  ))}
                </div>
              </div>
              <div className="form-group">
                <label className="form-label">Font Size</label>
                <select className="form-control" value={theme.fontSize} onChange={e => setTheme(p => ({ ...p, fontSize: e.target.value }))} style={{ maxWidth: 220 }} id="font-size-select">
                  <option value="small">Small</option>
                  <option value="medium">Medium (Default)</option>
                  <option value="large">Large</option>
                </select>
              </div>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                <button
                  onClick={() => setTheme(p => ({ ...p, compactMode: !p.compactMode }))}
                  id="compact-toggle"
                  style={{
                    width: 48, height: 26, borderRadius: 9999, border: 'none', cursor: 'pointer',
                    background: theme.compactMode ? 'linear-gradient(135deg,#2563eb,#7c3aed)' : '#e2e8f0',
                    position: 'relative', transition: 'all 0.3s'
                  }}
                >
                  <div style={{ position: 'absolute', top: 3, left: theme.compactMode ? 25 : 3, width: 20, height: 20, borderRadius: '50%', background: '#fff', transition: 'left 0.3s', boxShadow: '0 1px 4px rgba(0,0,0,0.2)' }} />
                </button>
                <div>
                  <div style={{ fontWeight: 600, fontSize: '0.9rem' }}>Compact Mode</div>
                  <div style={{ color: 'var(--text-muted)', fontSize: '0.78rem' }}>Reduce spacing for more content on screen</div>
                </div>
              </div>
            </>
          )}

          <div style={{ marginTop: '1.5rem', paddingTop: '1.25rem', borderTop: '1px solid var(--border)', display: 'flex', gap: '0.75rem' }}>
            <button className={`btn ${saved ? 'btn-success' : 'btn-primary'}`} onClick={handleSave} id="save-settings-btn">
              {saved ? <><Check size={16} /> Saved!</> : <><Save size={16} /> Save Changes</>}
            </button>
            <button className="btn btn-secondary">Discard Changes</button>
          </div>
        </div>
      </div>
    </div>
  )
}
