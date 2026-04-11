import { useContext, useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api/axiosConfig";
import { AuthContext } from "../auth/authContext";
import TaskCard from "./components/taskCard";
import "../css/dashboard.css";

const initialForm = {
  task_name: "",
  description: "",
  energy_level: "Medium",
  status: "ACTIVE",
};

const isCompletedStatus = (status) =>
  String(status || "").toLowerCase() === "completed";

function normalizeTask(task) {
  const normalized = {
    ...task,
    id: task.task_id ?? task.taskId ?? task.id,
    task_name: task.task_name ?? "",
    description: task.description ?? "",
    energy_level: task.energy_level ?? "Medium",
    status: task.status ?? "ACTIVE",
    time_created: task.time_created ?? null,
    time_completed: task.time_completed ?? null,
  };

  // UI compatibility for existing TaskCard.jsx
  normalized.title = normalized.task_name;
  normalized.category = normalized.description;
  normalized.difficulty = normalized.energy_level;
  normalized.dueDate = "";
  normalized.priority = 0;
  normalized.completed = isCompletedStatus(normalized.status);

  return normalized;
}

function resolveUserId(userIdProp) {
  if (userIdProp !== undefined && userIdProp !== null && userIdProp !== "") {
    const parsedProp = Number(userIdProp);
    if (!Number.isNaN(parsedProp)) {
      return parsedProp;
    }
  }

  const storedUserId = localStorage.getItem("userId");
  if (storedUserId) {
    const parsedStored = Number(storedUserId);
    if (!Number.isNaN(parsedStored)) {
      return parsedStored;
    }
  }

  return null;
}

export default function Dashboard({ userId: userIdProp }) {
  const { logout } = useContext(AuthContext);
  const navigate = useNavigate();

  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [filters, setFilters] = useState({
    sort: "default",
    category: "",
    difficulty: "",
    status: "",
    energy_level: "",
  });

  const [showAddModal, setShowAddModal] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);
  const [editingTask, setEditingTask] = useState(null);
  const [form, setForm] = useState(initialForm);
  const [togglingTaskIds, setTogglingTaskIds] = useState(new Set());

  const userId = resolveUserId(userIdProp);

  useEffect(() => {
    const loadTasks = async () => {
      if (!userId) {
        setError("Missing user ID. Please log in again.");
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        const res = await API.get(`/api/tasks/user/${userId}`);
        setTasks((Array.isArray(res.data) ? res.data : []).map(normalizeTask));
        setError("");
      } catch (err) {
        setError(err.response?.data?.message || err.response?.data || "Failed to load tasks.");
      } finally {
        setLoading(false);
      }
    };

    loadTasks();
  }, [userId]);

  const activeCount = useMemo(() => tasks.filter((t) => !t.completed).length, [tasks]);

  const filteredTasks = useMemo(() => {
    let list = [...tasks];

    if (filters.status) list = list.filter((t) => t.status === filters.status);
    if (filters.energy_level) list = list.filter((t) => t.energy_level === filters.energy_level);

    return list;
  }, [tasks, filters]);

  const activeTasks = filteredTasks.filter((t) => !isCompletedStatus(t.status));
  const completedTasks = filteredTasks.filter((t) => isCompletedStatus(t.status));

  const openAddModal = () => {
    setEditingTask(null);
    setForm(initialForm);
    setShowAddModal(true);
  };

  const openEditModal = (task) => {
    setEditingTask(task);
    setForm({
      task_name: task.task_name ?? "",
      description: task.description ?? "",
      energy_level: task.energy_level ?? "Medium",
      status: task.status ?? "ACTIVE",
    });
    setShowAddModal(true);
  };

  const closeModals = () => {
    setShowAddModal(false);
    setEditingTask(null);
    setForm(initialForm);
  };

  const saveTask = async (e) => {
    e.preventDefault();

    if (!userId) {
      setError("Missing user ID. Please log in again.");
      return;
    }

    const nowIso = new Date().toISOString();
    const effectiveStatus = editingTask ? (editingTask.status ?? "ACTIVE") : form.status;

    const payload = {
      task_name: form.task_name,
      description: form.description,
      energy_level: form.energy_level,
      status: effectiveStatus,
      time_created: editingTask?.time_created ?? nowIso,
      time_completed: isCompletedStatus(effectiveStatus) ? (editingTask?.time_completed ?? nowIso) : null,
      user: { user_id: Number(userId) },
    };

    try {
      if (editingTask) {
        const res = await API.put(`/api/tasks/${editingTask.id}`, payload);
        const saved = normalizeTask(res.data);
        setTasks((prev) => prev.map((t) => (t.id === saved.id ? saved : t)));
      } else {
        const res = await API.post("/api/tasks", payload);
        const saved = normalizeTask(res.data);
        setTasks((prev) => [saved, ...prev]);
      }

      closeModals();
    } catch (err) {
      alert(err.response?.data?.message || err.response?.data || "Task save failed.");
    }
  };

  const deleteTask = async (taskId) => {
    if (!window.confirm("Delete this task?")) return;

    try {
      await API.delete(`/api/tasks/${taskId}`);
      setTasks((prev) => prev.filter((t) => t.id !== taskId));
    } catch (err) {
      alert(err.response?.data?.message || err.response?.data || "Task delete failed.");
    }
  };

  const toggleTaskComplete = async (task) => {
    const taskId = task.id ?? task.task_id;

    if (!taskId) {
      alert("Task ID is missing.");
      return;
    }

    if (!userId) {
      setError("Missing user ID. Please log in again.");
      return;
    }

    if (togglingTaskIds.has(taskId)) {
      return;
    }

    const nextStatus = isCompletedStatus(task.status) ? "ACTIVE" : "COMPLETED";
    const nowIso = new Date().toISOString();

    const payload = {
      task_name: task.task_name,
      description: task.description,
      energy_level: task.energy_level,
      status: nextStatus,
      time_created: task.time_created ?? nowIso,
      time_completed: nextStatus === "COMPLETED" ? (task.time_completed ?? nowIso) : null,
      user: { user_id: Number(userId) },
    };

    try {
      setTogglingTaskIds((prev) => {
        const next = new Set(prev);
        next.add(taskId);
        return next;
      });

      const res = await API.put(`/api/tasks/${taskId}`, payload);
      const updated = normalizeTask(res.data);
      setTasks((prev) => prev.map((t) => (t.id === updated.id ? updated : t)));
    } catch (err) {
      alert(err.response?.data?.message || err.response?.data || "Task update failed.");
    } finally {
      setTogglingTaskIds((prev) => {
        const next = new Set(prev);
        next.delete(taskId);
        return next;
      });
    }
  };

  const handleLogout = () => {
    localStorage.removeItem("userId");
    logout();
    navigate("/login", { replace: true });
  };

  if (loading) return <div className="page-container">Loading tasks...</div>;

  return (
    <div className="dashboard-page">
      <header className="top-nav">
        <div className="left">
          <span className="logo-text">TaskTide</span>
        </div>

        <div className="nav-center">
          <a href="#" className="nav-btn active">
            <i className="fa fa-list" /> Tasks
          </a>
        </div>

        <div className="right">
          <button type="button" className="btn-logout" onClick={() => setShowLogoutModal(true)}>
            Logout
          </button>
        </div>
      </header>

      <div className="page-container">
        <div className="header-row">
          <div>
            <h2>My Tasks</h2>
            <p className="subtext">
              <span id="activeTaskCount">{activeCount}</span> active tasks
            </p>
          </div>

          <button type="button" className="btn-primary" onClick={openAddModal}>
            <i className="fa fa-plus" /> Add new task
          </button>
        </div>

        <div className="filters">
          <select value={filters.status} onChange={(e) => setFilters((p) => ({ ...p, status: e.target.value }))}>
            <option value="">All Status</option>
            <option value="ACTIVE">Active</option>
            <option value="COMPLETED">Completed</option>
          </select>

          <select
            value={filters.energy_level}
            onChange={(e) => setFilters((p) => ({ ...p, energy_level: e.target.value }))}
          >
            <option value="">All Energy Levels</option>
            <option value="Low">Low</option>
            <option value="Medium">Medium</option>
            <option value="High">High</option>
          </select>
        </div>

        {error ? <p className="error-text">{error}</p> : null}

        <div className="task-list">
          <div id="active-tasks">
            {activeTasks.map((task) => (
              <TaskCard
                key={task.id}
                task={task}
                onEdit={openEditModal}
                onDelete={deleteTask}
                onToggleComplete={toggleTaskComplete}
                isToggling={togglingTaskIds.has(task.id)}
              />
            ))}
          </div>

          {completedTasks.length > 0 ? <h3 className="completed-title">Completed Tasks</h3> : null}

          <div id="completed-tasks">
            {completedTasks.map((task) => (
              <TaskCard
                key={task.id}
                task={task}
                onEdit={openEditModal}
                onDelete={deleteTask}
                onToggleComplete={toggleTaskComplete}
                isToggling={togglingTaskIds.has(task.id)}
              />
            ))}
          </div>
        </div>
      </div>

      {showAddModal && (
        <div className="modal" onClick={(e) => e.target.classList.contains("modal") && closeModals()}>
          <div className="modal-content">
            <span className="close" onClick={closeModals}>&times;</span>
            <h2>{editingTask ? "Edit Task" : "Add New Task"}</h2>

            <form onSubmit={saveTask}>
              <label>Task Name</label>
              <input
                type="text"
                value={form.task_name}
                required
                onChange={(e) => setForm((p) => ({ ...p, task_name: e.target.value }))}
              />

              <label>Description</label>
              <input
                type="text"
                value={form.description}
                onChange={(e) => setForm((p) => ({ ...p, description: e.target.value }))}
              />

              <label>Energy Level</label>
              <select
                value={form.energy_level}
                onChange={(e) => setForm((p) => ({ ...p, energy_level: e.target.value }))}
              >
                <option value="Low">Low</option>
                <option value="Medium">Medium</option>
                <option value="High">High</option>
              </select>

              {!editingTask && (
                <>
                  <label>Status</label>
                  <select
                    value={form.status}
                    onChange={(e) => setForm((p) => ({ ...p, status: e.target.value }))}
                  >
                    <option value="ACTIVE">Active</option>
                    <option value="COMPLETED">Completed</option>
                  </select>
                </>
              )}

              <button className="btn-primary" type="submit">
                {editingTask ? "Save Changes" : "Add Task"}
              </button>
            </form>
          </div>
        </div>
      )}

      {showLogoutModal && (
        <div className="modal" onClick={(e) => e.target.classList.contains("modal") && setShowLogoutModal(false)}>
          <div className="modal-content logout-box">
            <h2>Goodbye 👋</h2>
            <p>Are you sure you want to log out?</p>
            <button type="button" className="btn-danger" onClick={handleLogout}>Logout</button>
            <button type="button" className="btn-secondary" onClick={() => setShowLogoutModal(false)}>
              Stay logged in
            </button>
          </div>
        </div>
      )}
    </div>
  );
}