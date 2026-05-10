import { Routes, Route } from "react-router-dom";
import Login from "./features/auth/Login";
import PrivateRoute from "./shared/utils/privateRoute";
import Register from "./features/auth/Register";
import Dashboard from "./features/dashboard/Dashboard";

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
