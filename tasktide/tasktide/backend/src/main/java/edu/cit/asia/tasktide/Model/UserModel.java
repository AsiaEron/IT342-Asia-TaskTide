package edu.cit.asia.tasktide.Model;

    import java.util.Date;
    import java.util.List;

    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.JoinColumn;
    import jakarta.persistence.ManyToOne;
    import jakarta.persistence.OneToMany;

@Entity
public class UserModel {

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleModel role;
    @OneToMany(mappedBy = "user")
    private List<TaskModel> tasks;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;
    private String fname;
    private String lname;
    private String email;
    private String password;
    private Date date_joined;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate_joined() {
        return date_joined;
    }

    public void setDate_joined(Date date_joined) {
        this.date_joined = date_joined;
    }
}