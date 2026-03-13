import { createContext, useState } from "react";

export const AuthContext = createContext();

const isValidToken = (token) => {
  return !!token && token !== "null" && token !== "undefined";
};

export const AuthProvider = ({ children }) => {

  const initialToken = localStorage.getItem("token");
  const [user, setUser] = useState(isValidToken(initialToken) ? initialToken : null);

  const login = (token) => {
    if (!isValidToken(token)) {
      return;
    }
    localStorage.setItem("token", token);
    setUser(token);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};