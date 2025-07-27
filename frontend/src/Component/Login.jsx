import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import axios from 'axios';

function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });

  const handleChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const data = new FormData();
data.append('email', formData.email);
data.append('password', formData.password);
let res ;
try {
 res=  await axios.post(`${import.meta.env.VITE_API_URL}/api/auth/login`, data, {
  withCredentials: true,
  headers: {
    "Content-Type": "multipart/form-data"
  }
});
  const { email, role } = res.data;

    // Store in localStorage or state
    localStorage.setItem("email", email);
    localStorage.setItem("role", role);

      toast.success('Login successful!');
    if (role === "admin") {
      navigate("/admin-portal");
    } else {
      navigate("/dashboard");
    }
     window.location.reload();
    } catch (err) {
      toast.error('Invalid credentials!');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 p-6">
      <form
        onSubmit={handleSubmit}
        className="w-full max-w-md bg-white p-10 rounded-xl shadow-md flex flex-col gap-6"
      >
        <h2 className=" text-3xl font-semibold text-center text-blue-600">Login</h2>

        <input
          type="email"
          name="email"
          placeholder="Email"
          required
          value={formData.email}
          onChange={handleChange}
          className="border rounded-md px-4 py-3 w-full"
        />

        <input
          type="password"
          name="password"
          placeholder="Password"
          required
          value={formData.password}
          onChange={handleChange}
          className="border rounded-md px-4 py-3 w-full"
        />

        <button
          type="submit"
          className="bg-blue-600 text-white py-3 rounded-md hover:bg-blue-700 transition font-semibold"
        >
          Login
        </button>

        <p className="text-center text-sm text-gray-600">
          Don't have an account?{' '}
          <span
            className="text-blue-600 font-medium hover:underline cursor-pointer"
            onClick={() => navigate('/register')}
          >
            Register here
          </span>
        </p>
      </form>
    </div>
  );
}

export default Login;
