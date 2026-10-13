import { useState } from 'react'
import { GraduationCap, Eye, EyeOff, Lock, Mail } from 'lucide-react'

const DEMO_USERS = [
  { email: 'admin@university.edu', password: 'admin123', name: 'Dr. Rajesh Kumar', role: 'Administrator' },
  { email: 'faculty@university.edu', password: 'faculty123', name: 'Prof. Meena Iyer', role: 'Faculty' },
  { email: 'student@university.edu', password: 'student123', name: 'Aarav Sharma', role: 'Student' },
]

export default function LoginPage({ onLogin }) {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [showPass, setShowPass] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    if (!email || !password) { setError('Please enter your email and password.'); return }
    setLoading(true)
    await new Promise(r => setTimeout(r, 900))
    const user = DEMO_USERS.find(u => u.email === email && u.password === password)
    if (user) {
      onLogin(user)
    } else {
      setError('Invalid email or password. Try the demo credentials below.')
    }
    setLoading(false)
  }

  const fillDemo = (u) => {
    setEmail(u.email)
    setPassword(u.password)
    setError('')
  }

  return (
    <div className="login-page">
      <div className="login-bg-orb orb1" />
      <div className="login-bg-orb orb2" />

      <div className="login-card">
        {/* Logo */}
        <div className="login-logo">
          <div className="login-logo-icon">
            <GraduationCap size={26} color="#fff" />
          </div>
          <div>
            <div className="login-title">SmartHub</div>
            <div className="login-subtitle">University Portal</div>
          </div>
        </div>

        <h1 className="login-heading">Welcome back</h1>
        <p className="login-sub">Sign in to your account to continue</p>

        {error && <div className="login-error">{error}</div>}

        <form className="login-form" onSubmit={handleSubmit} id="login-form">
          <div className="form-group">
            <label className="form-label" htmlFor="login-email">Email Address</label>
            <div style={{ position: 'relative' }}>
              <Mail size={16} style={{ position: 'absolute', left: 12, top: '50%', transform: 'translateY(-50%)', color: 'rgba(255,255,255,0.35)' }} />
              <input
                id="login-email"
                type="email"
                className="form-control"
                style={{ paddingLeft: '2.4rem' }}
                placeholder="you@university.edu"
                value={email}
                onChange={e => setEmail(e.target.value)}
                autoComplete="email"
              />
            </div>
          </div>

          <div className="form-group">
            <label className="form-label" htmlFor="login-password">Password</label>
            <div style={{ position: 'relative' }}>
              <Lock size={16} style={{ position: 'absolute', left: 12, top: '50%', transform: 'translateY(-50%)', color: 'rgba(255,255,255,0.35)' }} />
              <input
                id="login-password"
                type={showPass ? 'text' : 'password'}
                className="form-control"
                style={{ paddingLeft: '2.4rem', paddingRight: '2.8rem' }}
                placeholder="••••••••"
                value={password}
                onChange={e => setPassword(e.target.value)}
                autoComplete="current-password"
              />
              <button
                type="button"
                onClick={() => setShowPass(s => !s)}
                style={{ position: 'absolute', right: 12, top: '50%', transform: 'translateY(-50%)', background: 'none', border: 'none', cursor: 'pointer', color: 'rgba(255,255,255,0.4)', display: 'flex' }}
                aria-label="Toggle password visibility"
              >
                {showPass ? <EyeOff size={16} /> : <Eye size={16} />}
              </button>
            </div>
          </div>

          <button type="submit" className="login-btn" disabled={loading} id="login-submit">
            {loading ? 'Signing in…' : 'Sign In'}
          </button>
        </form>

        {/* Demo credentials */}
        <div className="login-demo">
          <p>Demo Credentials (click to fill):</p>
          {DEMO_USERS.map(u => (
            <div
              key={u.email}
              onClick={() => fillDemo(u)}
              style={{ cursor: 'pointer', padding: '4px 0', borderBottom: '1px solid rgba(255,255,255,0.06)' }}
            >
              <code style={{ color: 'rgba(255,255,255,0.7)', fontSize: '0.78rem' }}>
                {u.role}: {u.email} / {u.password}
              </code>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
