import React from "react";

const isCompletedStatus = (status) =>
  String(status || "").toLowerCase() === "completed";

export default function TaskCard({ task, onEdit, onDelete }) {
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
          <div className={`circle ${completed ? "checked" : ""}`} data-id={id}>
            <i className="fa fa-check" style={{ display: completed ? "block" : "none" }} />
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