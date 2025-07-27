import React, { useRef, useState } from 'react';
import axios from 'axios';
import { CloudArrowUpIcon } from '@heroicons/react/24/outline';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router-dom';

function Register() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    phone: '',
    dob: '',
    address: '',
    password: ''
  });

  const [aadhaarFile, setAadhaarFile] = useState(null);
  const [panFile, setPanFile] = useState(null);
  const [photoFile, setPhotoFile] = useState(null);

  // Refs for file inputs
  const aadhaarRef = useRef();
  const panRef = useRef();
  const photoRef = useRef();

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handleFileSelect = (e, setFile, acceptType, label) => {
    const file = e.target.files[0];
    if (!file) return;
    if (!acceptType.includes(file.type)) {
      toast.error(`${label} must be a ${acceptType.join(', ')}`);
    } else {
      setFile(file);
    }
  };

  const handleDrop = (e, setFile, acceptType, label) => {
    e.preventDefault();
    const droppedFile = e.dataTransfer.files[0];
    if (!droppedFile) return;
    if (!acceptType.includes(droppedFile.type)) {
      toast.error(`${label} must be a ${acceptType.join(', ')}`);
    } else {
      setFile(droppedFile);
    }
  };

  const renderDropzone = (label, file, setFile, acceptType, acceptText, inputRef, isImage = false) => (
    <div
      className="w-full h-44 flex flex-col items-center justify-center gap-2 border-2 border-dashed border-gray-400 hover:border-blue-500 rounded-md text-center transition bg-white shadow-sm cursor-pointer"
      onClick={() => inputRef.current.click()}
      onDrop={(e) => handleDrop(e, setFile, acceptType, label)}
      onDragOver={(e) => e.preventDefault()}
    >
      <input
        type="file"
        accept={acceptType.join(',')}
        style={{ display: 'none' }}
        ref={inputRef}
        onChange={(e) => handleFileSelect(e, setFile, acceptType, label)}
      />
      {file ? (
        isImage ? (
          <img src={URL.createObjectURL(file)} alt="Preview" className="h-24 w-24 object-cover rounded-full" />
        ) : (
          <p className="text-sm font-medium text-green-600">{file.name}</p>
        )
      ) : (
        <>
          <CloudArrowUpIcon className="h-8 w-8 text-blue-600" />
          <p className="text-gray-500 text-sm font-medium">
            Click or Drag & Drop {label} <br />
            <span className="text-xs text-gray-400">(Only {acceptText})</span>
          </p>
        </>
      )}
    </div>
  );

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!aadhaarFile || !panFile || !photoFile) {
      toast.error('Upload Aadhaar, PAN, and Profile Photo!');
      return;
    }
const data = new FormData();
data.append('name', formData.fullName); 
data.append('email', formData.email);
data.append('password', formData.password);
data.append('dob', formData.dob);
data.append('phone', formData.phone);
data.append('address', formData.address);
data.append('aadhar', aadhaarFile); 
data.append('pan', panFile);
data.append('photo', photoFile);

    try {
      const res = await axios.post(`${import.meta.env.VITE_API_URL}/api/register`, data, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });

      toast.success('Registered successfully!');
      localStorage.setItem('email', formData.email);
      localStorage.setItem('role', 'customer');
     window.location.reload();
    } catch (err) {
      toast.error('Registration failed. Try again!');
      console.log(err)
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 p-6">
      <form
        onSubmit={handleSubmit}
        className="w-full max-w-5xl bg-white p-10 rounded-xl shadow-md grid grid-cols-1 md:grid-cols-2 gap-6"
      >
        <h2 className="col-span-full text-3xl font-semibold text-center text-blue-600">Register</h2>

        <input type="text" name="fullName" placeholder="Full Name" required onChange={handleChange} value={formData.fullName} className="border rounded-md px-4 py-3 w-full" />
        <input type="email" name="email" placeholder="Email" required onChange={handleChange} value={formData.email} className="border rounded-md px-4 py-3 w-full" />

        <input type="text" name="phone" placeholder="Phone Number" required onChange={handleChange} value={formData.phone} className="border rounded-md px-4 py-3 w-full" />
        <input type="date" name="dob" required onChange={handleChange} value={formData.dob} className="border rounded-md px-4 py-3 w-full" />

        <input type="text" name="address" placeholder="Address" required onChange={handleChange} value={formData.address} className="border rounded-md px-4 py-3 col-span-full" />
        <input type="password" name="password" placeholder="Password" required onChange={handleChange} value={formData.password} className="border rounded-md px-4 py-3 col-span-full" />

        {/* Aadhaar and PAN Upload */}
        <div className="col-span-full grid grid-cols-1 md:grid-cols-2 gap-6">
          {renderDropzone('Aadhaar Card', aadhaarFile, setAadhaarFile, ['application/pdf'], 'PDF', aadhaarRef)}
          {renderDropzone('PAN Card', panFile, setPanFile, ['application/pdf'], 'PDF', panRef)}
        </div>

        {/* Profile Photo Upload */}
        <div className="col-span-full flex justify-center">
          <div className="w-full max-w-md">
            {renderDropzone('Profile Photo', photoFile, setPhotoFile, ['image/png', 'image/jpeg'], 'JPG/PNG', photoRef, true)}
          </div>
        </div>

        <button
          type="submit"
          className="col-span-full bg-blue-600 text-white py-3 rounded-md hover:bg-blue-700 transition font-semibold"
        >
          Submit
        </button>
      </form>
    </div>
  );
}

export default Register;
