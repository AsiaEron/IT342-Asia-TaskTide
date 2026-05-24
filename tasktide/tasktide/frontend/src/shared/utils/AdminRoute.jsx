import { Navigate } from "react-router-dom";

const AdminRoute = ({ children }) => {
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");
  const hasValidToken = !!token && token !== "null" && token !== "undefined";

  return hasValidToken && role === "ROLE_ADMIN" ? children : <Navigate to="/login" />;
};

export default AdminRoute;
