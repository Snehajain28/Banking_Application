import React, { useEffect, useState } from 'react';

import { Route, Routes, Navigate } from 'react-router-dom';
import Register from './Component/Register';
import Login from './Component/Login';
import CustomerDashboard from './Component/Dashboard/Dashboard';
import AdminPortal from './Component/Admin';
import { ToastContainer } from 'react-toastify';


function App() {


  const [email, setEmail] = useState(null);
  const [role, setRole] = useState(null);
  const [isLoggedIn, setIsLoggedIn] = useState(false);
 
 useEffect(() => {
    const storedEmail = localStorage.getItem('email');
    const storedRole = localStorage.getItem('role');
    setEmail(storedEmail);
    setRole(storedRole);
    setIsLoggedIn(true)
  }, []);
 
console.log(isLoggedIn);

  return (
    <div className="h-[100vh] w-[100vw]">
    
      <Routes>
     {!isLoggedIn && (
          <>
            <Route path="/" element={<Navigate to="/register" />} />
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<Login />} />
          </>
        )}

       
        {isLoggedIn && role === 'customer' && (
          <>
            <Route path="/" element={<Navigate to="/dashboard" />} />
            <Route path="/dashboard" element={<CustomerDashboard />} />
            <Route path="/register" element={<Navigate to="/dashboard" />} />
            <Route path="/login" element={<Navigate to="/dashboard" />} />
          </>
        )}

       
        {isLoggedIn && role === 'admin' && (
          <>
            <Route path="/" element={<Navigate to="/admin-portal" />} />
            <Route path="/admin-portal" element={<AdminPortal />} />
            <Route path="/register" element={<Navigate to="/admin-portal" />} />
            <Route path="/login" element={<Navigate to="/admin-portal" />} />
          </>
        )}

       
        <Route path="*" element={<Navigate to={isLoggedIn ? (role === 'admin' ? "/admin-portal" : "/dashboard") : "/login"} />} />
      </Routes>

      <ToastContainer />
    </div>
  );
}

export default App;
