import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

export default function Login() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const validate = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (!formData.email || !formData.password) {
      return "Both fields are required.";
    }

    if (!emailRegex.test(formData.email)) {
      return "Invalid email format.";
    }

    return null;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const errorMsg = validate();
    if (errorMsg) {
      setError(errorMsg);
    } else {
      setError("");
      alert("Login successful!");
      console.log("Logging in:", formData);
    }
    navigate("/kyc");
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-blue-50 px-4">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-8 rounded-xl shadow-lg w-full max-w-md"
      >
        <h2 className="text-2xl font-bold text-center text-blue-600 mb-6">
          Login
        </h2>

        {error && (
          <div className="bg-red-100 text-red-700 p-2 mb-4 rounded text-sm">
            {error}
          </div>
        )}

        <div className="mb-4">
          <label className="block mb-1 text-gray-700">Email</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            className="w-full border px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Enter your email"
            required
          />
        </div>

        <div className="mb-6">
          <label className="block mb-1 text-gray-700">Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            className="w-full border px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            placeholder="Enter password"
            required
          />
        </div>

        <button
          type="submit"
          className="w-full bg-blue-600 hover:bg-blue-700 text-white py-2 rounded-lg transition"
        >
          Login
        </button>
      </form>
        <div className="flex gap-2 h-full  mt-4">
      <p>Don't have an account  ? </p>
      <Link to="/register" className="text-blue-600 hover:underline  " >Register</Link>
   </div>
    </div>
  );
}
