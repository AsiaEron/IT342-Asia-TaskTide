import { Navigate } from "react-router-dom";

const PrivateRoute = ({ children }) => {

  const token = localStorage.getItem("token");
  const hasValidToken = !!token && token !== "null" && token !== "undefined";

  return hasValidToken ? children : <Navigate to="/login" />;
};

export default PrivateRoute;