import { useCallback, useEffect, useMemo, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router-dom";
import API from "../auth/services/axiosConfig";
import TaskCard from "./components/TaskCard";
import "./Dashboard.css";

const isCompletedStatus = (status) => String(status || "").toLowerCase() === "completed";

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

  normalized.title = normalized.task_name;
  normalized.category = normalized.description;
  normalized.difficulty = normalized.energy_level;
  normalized.dueDate = "";
  normalized.priority = 0;
  normalized.completed = isCompletedStatus(normalized.status);

  return normalized;
}

export default function AdminUserTasks() {
  const navigate = useNavigate();
  const location = useLocation();
  const { userId } = useParams();

  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [togglingTaskIds, setTogglingTaskIds] = useState(new Set());

  const user = location.state?.user;
  const displayName =
    user?.firstName || user?.fname || "User" + (userId ? ` ${userId}` : "");
  const userEmail = user?.email || "";
  const numericUserId = Number(userId);

  const loadTasks = useCallback(async () => {
    if (!numericUserId || Number.isNaN(numericUserId)) {
      setError("Invalid user selected.");
      setLoading(false);
      return;
    }

    try {
      setLoading(true);
      const res = await API.get(`/api/tasks/user/${numericUserId}`);
      setTasks((Array.isArray(res.data) ? res.data : []).map(normalizeTask));
      setError("");
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || "Failed to load tasks.");
    } finally {
      setLoading(false);
    }
  }, [numericUserId]);

  useEffect(() => {
    loadTasks();
  }, [loadTasks]);

  const activeCount = useMemo(() => tasks.filter((task) => !task.completed).length, [tasks]);

  const filteredTasks = useMemo(() => {
    let list = [...tasks];
    return list;
  }, [tasks]);

  const activeTasks = filteredTasks.filter((task) => !isCompletedStatus(task.status));
  const completedTasks = filteredTasks.filter((task) => isCompletedStatus(task.status));

  const deleteTask = async (taskId) => {
    if (!window.confirm("Delete this task?")) return;

    try {
      await API.delete(`/api/tasks/${taskId}`);
      setTasks((prev) => prev.filter((task) => task.id !== taskId));
    } catch (err) {
      alert(err.response?.data?.message || err.response?.data || "Task delete failed.");
    }
  };


  if (loading) {
    return <div className="page-container">Loading user tasks...</div>;
  }

  return (
    <div className="dashboard-page">
      <header className="top-nav">
        <div className="left">
          <span className="logo-text">TaskTide Admin</span>
        </div>
        <div className="right">
          <button type="button" className="btn-secondary" onClick={() => navigate("/admin/dashboard")}>Back to users</button>
        </div>
      </header>

      <div className="page-container">
        <div className="header-row">
          <div>
            <h2>{displayName}'s Tasks</h2>
            {userEmail ? <p className="subtext">Email: {userEmail}</p> : null}
            <p className="subtext">Showing tasks for user ID {numericUserId}</p>
            <p className="subtext">{activeCount} active tasks</p>
          </div>
        </div>

        {error ? <p className="error-text">{error}</p> : null}

        <div className="filters">
          <span className="subtext">Delete tasks in this view that are inappropriate.</span>
        </div>

        <div className="task-list">
          <div id="active-tasks">
            {activeTasks.map((task) => (
              <TaskCard
                key={task.id}
                task={task}
                onDelete={deleteTask}
                isToggling={togglingTaskIds.has(task.id)}
                showEdit={false}
                showToggle={false}
              />
            ))}
          </div>

          {completedTasks.length > 0 ? <h3 className="completed-title">Completed Tasks</h3> : null}

          <div id="completed-tasks">
            {completedTasks.map((task) => (
              <TaskCard
                key={task.id}
                task={task}
                onDelete={deleteTask}
                isToggling={togglingTaskIds.has(task.id)}
                showEdit={false}
                showToggle={false}
              />
            ))}
          </div>
        </div>
      </div>

    </div>
  );
}
