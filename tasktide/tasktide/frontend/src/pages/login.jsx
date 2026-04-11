import React, { useState, useContext } from "react";
import API from "../api/axiosConfig";
import { AuthContext } from "../auth/authContext";
import { Link, useNavigate } from "react-router-dom";
import "../css/login.css";

function decodeTokenUserId(token) {
  try {
    const payload = JSON.parse(atob(token.split(".")[1]));
    return payload.userId ?? payload.id ?? (Number.isFinite(Number(payload.sub)) ? Number(payload.sub) : null);
  } catch {
    return null;
  }
}

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const res = await API.post("/users/login", { email, password });
      const token = res.data?.token ?? res.data;

      if (!token || token === "null" || token === "undefined") {
        setError("Invalid credentials");
        return;
      }

      login(token);

      const userId = res.data?.userId ?? decodeTokenUserId(token);
      if (userId) {
        localStorage.setItem("userId", String(userId));
      }

      navigate("/dashboard", { replace: true });
    } catch (err) {
      setError(
        err.response?.data?.message ||
          err.response?.data ||
          "Invalid credentials"
      );
    }
  };

  return (
    <div className="login-page">
      <div className="right-panel">
        <div className="login-container">
          <h2>Welcome Back</h2>
          <p className="subtitle">Sign in to continue to TaskTide</p>

          <form onSubmit={handleSubmit}>
            {error && <p className="error">{error}</p>}

            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />

            <button type="submit">Login</button>
          </form>

          <p className="register">
            Don&apos;t have an account? <Link to="/register">Register</Link>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;