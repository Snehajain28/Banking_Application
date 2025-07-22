import React, { useState } from 'react';

const KYC = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    dob: '',
    address: '',
    panNumber: '',
    aadhaarNumber: '',
  });

  const [panFile, setPanFile] = useState(null);
  const [aadhaarFile, setAadhaarFile] = useState(null);
  const [photoFile, setPhotoFile] = useState(null);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleFileDrop = (e, setter) => {
    e.preventDefault();
    const file = e.dataTransfer.files[0];
    setter(file);
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const data = new FormData();
    Object.keys(formData).forEach((key) => {
      data.append(key, formData[key]);
    });
    data.append('panFile', panFile);
    data.append('aadhaarFile', aadhaarFile);
    data.append('photoFile', photoFile);
    // fetch('/api/submit-kyc', { method: 'POST', body: data });
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
      <form onSubmit={handleSubmit} className="bg-white w-full max-w-4xl p-8 rounded-lg shadow-md grid grid-cols-1 md:grid-cols-2 gap-6">
        <input type="text" name="name" value={formData.name} onChange={handleInputChange} placeholder="Full Name" className="border p-2 rounded" />
        <input type="email" name="email" value={formData.email} onChange={handleInputChange} placeholder="Email" className="border p-2 rounded" />
        <input type="text" name="phone" value={formData.phone} onChange={handleInputChange} placeholder="Phone Number" className="border p-2 rounded" />
        <input type="date" name="dob" value={formData.dob} onChange={handleInputChange} className="border p-2 rounded" />
        <input type="text" name="address" value={formData.address} onChange={handleInputChange} placeholder="Address" className="border p-2 rounded col-span-1 md:col-span-2" />
        <input type="text" name="panNumber" value={formData.panNumber} onChange={handleInputChange} placeholder="PAN Number" className="border p-2 rounded" />
        <input type="text" name="aadhaarNumber" value={formData.aadhaarNumber} onChange={handleInputChange} placeholder="Aadhaar Number" className="border p-2 rounded" />

        <div onDrop={(e) => handleFileDrop(e, setPanFile)} onDragOver={handleDragOver} className="border-2 border-dashed border-gray-400 p-4 text-center rounded cursor-pointer">
          {panFile ? panFile.name : 'Drag & drop PAN card here'}
        </div>
        <div onDrop={(e) => handleFileDrop(e, setAadhaarFile)} onDragOver={handleDragOver} className="border-2 border-dashed border-gray-400 p-4 text-center rounded cursor-pointer">
          {aadhaarFile ? aadhaarFile.name : 'Drag & drop Aadhaar card here'}
        </div>
        <div onDrop={(e) => handleFileDrop(e, setPhotoFile)} onDragOver={handleDragOver} className="border-2 border-dashed border-gray-400 p-4 text-center rounded cursor-pointer col-span-1 md:col-span-2">
          {photoFile ? photoFile.name : 'Drag & drop your photo here'}
        </div>

        <button type="submit" className="bg-blue-600 text-white p-2 rounded col-span-1 md:col-span-2">Submit KYC</button>
      </form>
    </div>
  );
};

export default KYC;
