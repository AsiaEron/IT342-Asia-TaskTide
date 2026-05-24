import { Routes, Route } from "react-router-dom";
import Login from "./features/auth/Login";
import PrivateRoute from "./shared/utils/privateRoute";
import AdminRoute from "./shared/utils/AdminRoute";
import Register from "./features/auth/Register";
import AdminRegister from "./features/auth/AdminRegister";
import Dashboard from "./features/dashboard/Dashboard";
import AdminDashboard from "./features/dashboard/AdminDashboard";
import AdminUserTasks from "./features/dashboard/AdminUserTasks";

function App() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/register-admin" element={<AdminRegister />} />
      <Route path="/dashboard" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
      <Route path="/admin/dashboard" element={<AdminRoute><AdminDashboard /></AdminRoute>} />
      <Route
        path="/admin/users/:userId/tasks"
        element={<AdminRoute><AdminUserTasks /></AdminRoute>}
      />
      <Route path="*" element={<Login />} />
    </Routes>
  );
}

export default App;
