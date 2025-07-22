import React , { useState } from "react";
import { Link } from "react-router-dom";

export default function Register() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    confirmPassword: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const validate = () => {
    const { email, password, confirmPassword } = formData;

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const passwordRegex =
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?#&])[A-Za-z\d@$!%*?#&]{8,}$/;

    if (!email || !password || !confirmPassword) {
      return "All fields are required.";
    }

    if (!emailRegex.test(email)) {
      return "Invalid email format.";
    }

    if (!passwordRegex.test(password)) {
      return "Password must be at least 8 characters, include uppercase, lowercase, number, and special character.";
    }

    if (password !== confirmPassword) {
      return "Passwords do not match.";
    }

    return null;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const validationError = validate();
    if (validationError) {
      setError(validationError);
    } else {
      setError("");
      alert("Registration successful!");
      console.log("Form data:", formData);
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-blue-50 p-4">
      <form
        onSubmit={handleSubmit}
        className="bg-white shadow-md p-8 rounded-xl w-full max-w-md"
      >
        <h2 className="text-2xl font-bold text-center text-blue-600 mb-6">
          Register
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
            className="w-full border px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-4">
          <label className="block mb-1 text-gray-700">Password</label>
          <input
            type="password"
            name="password"
            className="w-full border px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-6">
          <label className="block mb-1 text-gray-700">Re-type Password</label>
          <input
            type="password"
            name="confirmPassword"
            className="w-full border px-4 py-2 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-400"
            value={formData.confirmPassword}
            onChange={handleChange}
            required
          />
        </div>

        <button
          type="submit"
          className="w-full h-full bg-blue-400 text-white py-2 rounded-lg transition"
        >
          Register
        </button>
      </form>
      <div className="flex gap-2 h-full  mt-4">
      <p>Already have an account  ? </p>
      <Link to="/login" className="text-blue-600 hover:underline  " >Login</Link>
   </div>
    </div>
  );
}
