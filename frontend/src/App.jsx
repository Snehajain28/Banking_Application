import React,{ useState } from 'react'
import axios from 'axios'
import { Route, Routes } from "react-router"
import Register from './Component/Register';
import Login from './Component/Login';
import KYC from './Component/KYC';
import Page2 from './Component/Page';

function App() {
  
  
  return (
    <div className='h-[100vh] w-[100vw]'>
 <Routes>
  <Route path="/" element={<Register/>}/>
  <Route path="/register" element={<Register/>}/>
    <Route path="/login" element={<Login/>}/>
  <Route path="/kyc" element={<KYC/>}/>
   <Route path="/page" element={<Page2/>}/>
 </Routes>
   </div>
  )

}
export default App;
