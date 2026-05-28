import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../auth/services/axiosConfig";
import { AuthContext } from "../auth/authContext";
import "./Dashboard.css";

export default function AdminDashboard() {
  const navigate = useNavigate();
  const { logout } = useContext(AuthContext);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadUsers = async () => {
      try {
        setLoading(true);
        const res = await API.get("/users/all");
        setUsers(Array.isArray(res.data) ? res.data : []);
        setError("");
      } catch (err) {
        setError(err.response?.data?.message || err.response?.data || "Failed to load users.");
      } finally {
        setLoading(false);
      }
    };

    loadUsers();
  }, []);

  const handleLogout = () => {
    logout();
    navigate("/login", { replace: true });
  };

  const handleDeleteUser = async (userId) => {
    if (!window.confirm("Delete this user? This action cannot be undone.")) {
      return;
    }

    try {
      await API.delete(`/users/${userId}`);
      setUsers((prevUsers) => prevUsers.filter((user) => (user.userId ?? user.user_id) !== userId));
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || "Failed to delete user.");
    }
  };

  return (
    <div className="dashboard-page admin-dashboard">
      <div className="top-nav">
        <div className="left">
          <span className="logo-text">TaskTide Admin</span>
        </div>
        <div className="right">
          <button type="button" className="btn-logout" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>

      <div className="admin-content">
        <h2>Admin Dashboard</h2>
        <p>Manage users and access the database view.</p>

        {loading ? (
          <p>Loading user list...</p>
        ) : error ? (
          <p className="error">{error}</p>
        ) : (
          <div className="admin-table-container">
            <table className="admin-table">
              <thead>
                <tr>
                  <th>User ID</th>
                  <th>Email</th>
                  <th>First Name</th>
                  <th>Last Name</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {users.map((user) => (
                  <tr key={user.userId ?? user.user_id}>
                    <td>{user.userId ?? user.user_id}</td>
                    <td>{user.email}</td>
                    <td>
                      <button
                        type="button"
                        className="link-button"
                        onClick={() =>
                          navigate(`/admin/users/${user.userId ?? user.user_id}/tasks`, {
                            state: { user },
                          })
                        }
                      >
                        {user.firstName || user.fname || user.email || "Unknown"}
                      </button>
                    </td>
                    <td>{user.lastName || user.lname || ""}</td>
                    <td>
                      {user.role?.roleName !== "ROLE_ADMIN" && user.role !== "ROLE_ADMIN" ? (
                        <button
                          type="button"
                          className="btn-delete-user"
                          onClick={() => handleDeleteUser(user.userId ?? user.user_id)}
                        >
                          Delete
                        </button>
                      ) : null}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
