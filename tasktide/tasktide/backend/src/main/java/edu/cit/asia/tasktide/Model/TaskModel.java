package edu.cit.asia.tasktide.Model;

    import java.util.Date;

    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;

@Entity
public class TaskModel {

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"tasks", "password", "role"})
    private UserModel user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int task_id;
    private String task_name;
    private String description;
    private String energy_level;
    private String status;
    private Date time_created;
    private Date time_completed;

    public int getTask_id() {
        return task_id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnergy_level() {
        return energy_level;
    }

    public void setEnergy_level(String energy_level) {
        this.energy_level = energy_level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getTime_created() {
        return time_created;
    }

    public void setTime_created(Date time_created) {
        this.time_created = time_created;
    }

    public Date getTime_completed() {
        return time_completed;
    }

    public void setTime_completed(Date time_completed) {
        this.time_completed = time_completed;
    }
}
