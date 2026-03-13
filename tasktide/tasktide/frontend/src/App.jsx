import { useContext } from "react";
import { Routes, Route, useNavigate } from "react-router-dom";
import Login from "./pages/login";
import PrivateRoute from "./auth/privateRoute";
import Register from "./pages/register";
import { AuthContext } from "./auth/authContext";

function Dashboard() {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div style={{ padding: "24px" }}>
      <h1>Dashboard</h1>
      <button type="button" onClick={handleLogout}>Logout</button>
    </div>
  );
}

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route
        path="/dashboard"
        element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        }
      />
      <Route path="*" element={<Login />} />
    </Routes>
  );
}

export default App;
