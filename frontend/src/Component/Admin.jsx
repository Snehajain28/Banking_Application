import React, { useEffect, useState } from 'react';
import axios from 'axios';

const AdminPortal = () => {
  const [kycList, setKycList] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/api/kyc/all')
      .then(response => setKycList(response.data))
      .catch(error => console.error('Error fetching KYC data:', error));
  }, []);

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <h1 className="text-3xl font-bold mb-6 text-center">Admin Portal</h1>

      {kycList.length === 0 ? (
        <p className="text-center text-gray-500">No KYC requests available.</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {kycList.map((user) => (
            <div key={user.id} className="bg-white p-4 rounded shadow">
              <p><strong>Name:</strong> {user.name}</p>
              <p><strong>Email:</strong> {user.email}</p>
              <p><strong>Phone:</strong> {user.phone}</p>
              <p><strong>PAN:</strong> {user.panNumber}</p>
              <p><strong>Aadhaar:</strong> {user.aadhaarNumber}</p>
              <p><strong>Status:</strong> <span className="text-blue-600">{user.status}</span></p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default AdminPortal;
