import React, { useState } from "react";
import "../css/register.css";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import API from "../api/axiosConfig";
import { useNavigate } from "react-router-dom";

function Register() {

  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: ""
  });

  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [showLogo, setShowLogo] = useState(true);

  const togglePassword = () => {
    setShowPassword(!showPassword);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {

      await API.post("/users/register", formData);

      alert("Account created successfully!");

      navigate("/login");

    } catch (err) {

      setError(
        err.response?.data?.message ||
        "Registration failed"
      );

    }
  };

  return (
    <div className="register-container">

      <div className="register-card">

        <div className="logo-container">
          {showLogo ? (
            <img
              src="/tasktide.jpg"
              alt="TaskTide Logo"
              onError={() => setShowLogo(false)}
            />
          ) : (
            <h3>TaskTide</h3>
          )}
        </div>

        <h2>Join TaskTide</h2>
        <p className="subtitle">
          Start working on tasks without feeling overwhelmed
        </p>

        <form onSubmit={handleSubmit}>

          <div className="input-group">

            <label>Email Address</label>

            <input
              type="email"
              name="email"
              placeholder="Email"
              required
              pattern="^[a-zA-Z0-9._%+-]+@(gmail\.com|yahoo\.com|outlook\.com|cit\.edu)$"
              title="Email must be @gmail.com, @yahoo.com, @outlook.com, or @cit.edu"
              value={formData.email}
              onChange={handleChange}
            />

          </div>

          <div className="input-group">

            <label>Password</label>

            <div className="password-container">

              <input
                type={showPassword ? "text" : "password"}
                name="password"
                placeholder="Password"
                required
                pattern="^(?=.*[A-Z])(?=.*[!@#$%^&*()_+{}\[\]:;<>,.?~\-]).+$"
                title="Password must contain at least 1 uppercase letter and 1 special character."
                value={formData.password}
                onChange={handleChange}
              />

              <span
                className="toggle-password"
                onClick={togglePassword}
              >
                {showPassword ? <FaEyeSlash /> : <FaEye />}
              </span>

            </div>

          </div>

          <button
            type="submit"
            className="create-btn"
          >
            Create Account
          </button>

          {error && <p className="error">{error}</p>}

        </form>

        <p className="login-link">
          Already have an account?{" "}
          <a href="/login">Login</a>
        </p>

      </div>

    </div>
  );
}

export default Register;