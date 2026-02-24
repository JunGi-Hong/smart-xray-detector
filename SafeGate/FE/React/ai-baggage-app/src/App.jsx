import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { useState, useEffect } from 'react'
import Login from './components/Login'
import Register from './components/Register'
import Dashboard from './components/Dashboard'
import VerifyPassword from './components/VerifyPassword'
import EditProfile from './components/EditProfile'
import History from './components/History'
import EventDetail from './components/EventDetail'

import { UserContext } from './contexts/UserContext'

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)

  const user = {
    //user context prototype
    name: '홍길동',
    email: 'test@example.com',
    tel: '01012346578',
  }

  return (
    <UserContext.Provider value={user}>
      <BrowserRouter>
        <Routes>
          <Route path='/user/login' element={<Login />} />
          <Route path='/user/register' element={<Register />} />
          <Route path='/dashboard' element={<Dashboard />} />
          <Route path='/user/verify-password' element={<VerifyPassword />} />
          {/*use protected route for security*/}
          <Route path='/user/edit-profile' element={<EditProfile />} />
          <Route path='/history' element={<History />} />
          <Route path='/history/detail/:eventID' element={<EventDetail />} />
          <Route path='/' element={<Navigate to='/user/login' />} />
        </Routes>
      </BrowserRouter>
    </UserContext.Provider>
  )
}

export default App

