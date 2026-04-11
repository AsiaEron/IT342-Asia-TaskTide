import React from "react";

const isCompletedStatus = (status) =>
  String(status || "").toLowerCase() === "completed";

export default function TaskCard({ task, onEdit, onDelete, onToggleComplete, isToggling = false }) {
  const id = task.id ?? task.task_id;
  const title = task.task_name ?? task.title ?? "";
  const description = task.description ?? task.category ?? "";
  const energyLevel = task.energy_level ?? task.difficulty ?? "";
  const status = task.status ?? (task.completed ? "COMPLETED" : "ACTIVE");
  const completed = task.completed ?? isCompletedStatus(status);

  return (
    <div className={`task-card ${completed ? "completed" : ""}`} data-id={id}>
      <div className="task-main-row">
        <div className="left">
          <div
            className={`circle ${completed ? "checked" : ""} ${isToggling ? "disabled" : ""}`}
            data-id={id}
            role="button"
            tabIndex={isToggling ? -1 : 0}
            aria-label={completed ? "Mark as active" : "Mark as completed"}
            aria-disabled={isToggling}
            onClick={() => {
              if (!isToggling) onToggleComplete(task);
            }}
            onKeyDown={(e) => {
              if (isToggling) return;
              if (e.key === "Enter" || e.key === " ") {
                e.preventDefault();
                onToggleComplete(task);
              }
            }}
          >
            {isToggling ? (
              <i className="fa fa-spinner fa-spin" />
            ) : (
              <i className="fa fa-check" style={{ display: completed ? "block" : "none" }} />
            )}
          </div>

          <div className="title-category">
            <span className={`task-title ${completed ? "crossed" : ""}`}>
              {title}
            </span>

            {description ? (
              <div className="tag" data-category={description}>
                {description}
              </div>
            ) : null}
          </div>
        </div>

        <div className="right">
          <span className="task-meta">{energyLevel}</span>
          <span className="task-meta">{status}</span>

          <button type="button" className="edit-btn" onClick={() => onEdit(task)}>
            <i className="fa fa-pen" />
          </button>

          <button type="button" className="delete-btn" onClick={() => onDelete(id)}>
            <i className="fa fa-trash" />
          </button>
        </div>
      </div>
    </div>
  );
}