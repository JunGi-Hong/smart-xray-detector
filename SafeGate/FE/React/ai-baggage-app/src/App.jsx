import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { useState, useEffect } from 'react'
import Login from './components/Login'
import Register from './components/Register'
import Dashboard from './components/Dashboard'


function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)

  return (
    <BrowserRouter>
      <Routes>
        <Route path='/user/login' element={<Login />} />
        <Route path='/user/register' element={<Register />} />
        <Route path='/dashboard' element={<Dashboard />} />
        <Route path='/' element={<Navigate to='/user/login' />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App

