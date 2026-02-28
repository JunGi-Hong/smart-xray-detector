import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useState, useEffect } from 'react'
import Login from './components/Login'
import Register from './components/Register'
import Dashboard from './components/Dashboard'
import VerifyPassword from './components/VerifyPassword'
import EditProfile from './components/EditProfile'
import History from './components/History'
import EventDetail from './components/EventDetail'
import KakaoCallback from './components/KakaoCallback'
import ProtectedRoute from './components/ProtectedRoute'


function App() {

  const [isAuthenticated, setIsAuthenticated] = useState(false)

  return (
    <BrowserRouter>
      <Routes>
        <Route path='/user/login' element={<Login setIsAuthenticated={setIsAuthenticated} />} />
        <Route path='/user/register' element={<Register />} />
        <Route path='/auth/kakao' element={<KakaoCallback setIsAuthenticated={setIsAuthenticated} />} />

        <Route element={<ProtectedRoute isAuthenticated={isAuthenticated} />}>
          <Route path='/dashboard' element={<Dashboard />} />
          <Route path='/user/verify-password' element={<VerifyPassword />} />
          {/*use protected route for security*/}
          <Route path='/user/edit-profile' element={<EditProfile />} />
          <Route path='/history' element={<History />} />
          <Route path='/history/detail/:eventID' element={<EventDetail />} />
        </Route>

        <Route path='/' element={<Navigate to='/user/login' />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App

