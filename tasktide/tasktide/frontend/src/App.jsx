import { Routes, Route } from "react-router-dom";
import Login from "./pages/login";
import PrivateRoute from "./auth/privateRoute";
import Register from "./pages/register";
import Dashboard from "./pages/dashboard";

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
      <Route path="*" element={<Login />} />
    </Routes>
  );
}

export default App;
