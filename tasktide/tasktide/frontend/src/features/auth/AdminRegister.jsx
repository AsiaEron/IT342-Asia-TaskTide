import React, { useState } from "react";
import "./Auth.css";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import API from "../../shared/services/axiosConfig";
import { useNavigate } from "react-router-dom";

function AdminRegister() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
    fname: "",
    lname: ""
  });
  const [confirmPassword, setConfirmPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [verificationCode, setVerificationCode] = useState("");
  const [verificationError, setVerificationError] = useState("");
  const [isVerificationPending, setIsVerificationPending] = useState(false);
  const [serverMessage, setServerMessage] = useState("");
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

    if (formData.password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    try {
      const response = await API.post("/users/register-admin", formData);
      setIsVerificationPending(true);
      setServerMessage(response.data?.message || "A verification code has been sent to your email.");
      setError("");
      setVerificationError("");
    } catch (err) {
      setError(
        err.response?.data?.message ||
        err.response?.data ||
        "Admin registration failed"
      );
    }
  };

  const handleVerify = async (e) => {
    e.preventDefault();

    try {
      await API.post("/users/verify-email", {
        email: formData.email,
        verificationCode,
      });
      alert("Email verified successfully. You can now log in.");
      navigate("/login");
    } catch (err) {
      setVerificationError(
        err.response?.data ||
        "Verification failed. Please check your code."
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

        <h2>Admin Registration</h2>
        <p className="subtitle">
          Create an admin account to manage the TaskTide system.
        </p>

        <form onSubmit={isVerificationPending ? handleVerify : handleSubmit}>
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
              disabled={isVerificationPending}
            />
          </div>

          {!isVerificationPending ? (
            <>
              <div className="input-group">
                <label>First Name</label>
                <input
                  type="text"
                  name="fname"
                  placeholder="First name"
                  required
                  value={formData.fname}
                  onChange={handleChange}
                />
              </div>

              <div className="input-group">
                <label>Last Name</label>
                <input
                  type="text"
                  name="lname"
                  placeholder="Last name"
                  required
                  value={formData.lname}
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
                  <span className="toggle-password" onClick={togglePassword}>
                    {showPassword ? <FaEyeSlash /> : <FaEye />}
                  </span>
                </div>
              </div>

              <div className="input-group">
                <label>Confirm Password</label>
                <div className="password-container">
                  <input
                    type={showPassword ? "text" : "password"}
                    name="confirmPassword"
                    placeholder="Repeat password"
                    required
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                  <span className="toggle-password" onClick={togglePassword}>
                    {showPassword ? <FaEyeSlash /> : <FaEye />}
                  </span>
                </div>
              </div>

              <button type="submit" className="create-btn">
                Create Admin Account
              </button>
            </>
          ) : (
            <>
              <p className="success-message">{serverMessage}</p>
              <div className="input-group">
                <label>Verification Code</label>
                <input
                  type="text"
                  name="verificationCode"
                  placeholder="Enter verification code"
                  required
                  value={verificationCode}
                  onChange={(e) => setVerificationCode(e.target.value)}
                />
              </div>

              <button type="submit" className="create-btn">
                Verify Email
              </button>
            </>
          )}

          {error && <p className="error">{error}</p>}
          {verificationError && <p className="error">{verificationError}</p>}
        </form>

        <p className="login-link">
          Already have an account? <a href="/login">Login</a>
        </p>
      </div>
    </div>
  );
}

export default AdminRegister;
