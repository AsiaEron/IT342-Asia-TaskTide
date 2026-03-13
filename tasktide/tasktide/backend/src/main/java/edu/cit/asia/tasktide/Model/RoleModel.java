package edu.cit.asia.tasktide.Model;

    import java.util.List;

    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.OneToMany;

@Entity
public class RoleModel {

    @OneToMany(mappedBy = "role")
    private List<UserModel> users;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int role_id;
    private String role_name;

    public int getRole_id() {
        return role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
}
